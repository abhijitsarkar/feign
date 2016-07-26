/*
 * Copyright (c) 2016, the original author or authors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * A copy of the GNU General Public License accompanies this software,
 * and is also available at http://www.gnu.org/licenses.
 */

package name.abhijitsarkar.feign.core.service

import java.beans.Introspector
import java.lang.reflect.Method
import java.lang.{Boolean => JavaBoolean}

import name.abhijitsarkar.feign.core.model.{AbstractIgnorableRequestProperties, FeignProperties, RequestProperties}
import org.slf4j.LoggerFactory

/**
  * @author Abhijit Sarkar
  */
class IgnorablePropertiesMerger {
  val logger = LoggerFactory.getLogger(classOf[IgnorablePropertiesMerger])

  def merge(requestProperties: RequestProperties, feignProperties: FeignProperties) {
    val globalPd = getPropertyDescriptors(classOf[AbstractIgnorableRequestProperties])

    List(requestProperties.path,
      requestProperties.method,
      requestProperties.queries,
      requestProperties.headers,
      requestProperties.body
    )
      .foreach { requestProperty =>
        logger.debug(s"Introspecting request property: ${requestProperty}.")
        val pd = getPropertyDescriptors(requestProperty.getClass)

        pd
          .zip(Stream.continually(globalPd))
          .flatMap(x => {
            val z = x._2
              .find(y => y.getName != "class" && y.getName == x._1.getName)

            List((Option(x._1), z))
          })
          .foreach {
            _ match {
              case (Some(local), Some(global)) => {
                val getter = local.getReadMethod
                val superGetter = global.getReadMethod

                Option(getter)
                  .flatMap(invokeGetter(_, requestProperty))
                  .orElse(invokeSuperGetter(superGetter, feignProperties))
                  .foreach { ignore =>
                    val setter = local.getWriteMethod

                    logger.debug(s"Setting ${local.getName} to $ignore.")

                    Option(setter)
                      .foreach(_.invoke(requestProperty, ignore))
                  }
              }
              case _ =>
            }
          }
      }
  }

  def getPropertyDescriptors(clazz: Class[_]) = Introspector.getBeanInfo(clazz).getPropertyDescriptors.toList

  def invokeGetter(getter: Method, requestProperty: Object) = {
    val ignore = Option(getter).flatMap(x => {
      val g = x.invoke(requestProperty).asInstanceOf[JavaBoolean]

      if (g == null) None else Some(g)
    })

    logger.debug(s"Local getter returned: ${ignore}.")

    ignore
  }

  def invokeSuperGetter(superGetter: Method, feignProperties: FeignProperties) = {
    val ignore = Option(superGetter).flatMap(x => {
      val g = x.invoke(feignProperties).asInstanceOf[JavaBoolean]

      if (g == null) None else Some(g)
    })

    logger.debug(s"Super getter returned: ${ignore}.")

    ignore
  }
}
