package name.abhijitsarkar.feign.core.service

import java.beans.Introspector
import java.lang.reflect.Method

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
                      .foreach(_.invoke(requestProperty, Option(ignore)))
                  }
              }
              case _ =>
            }
          }
      }
  }

  def getPropertyDescriptors(clazz: Class[_]) = Introspector.getBeanInfo(clazz).getPropertyDescriptors.toList

  def invokeGetter(getter: Method, requestProperty: Object) = {
    val ignore = Option(getter).flatMap(_.invoke(requestProperty).asInstanceOf[Option[Boolean]])

    logger.debug(s"Local getter returned: ${ignore}.")

    if (ignore == null) None else ignore
  }

  def invokeSuperGetter(superGetter: Method, feignProperties: FeignProperties) = {
    val ignore = Option(superGetter).flatMap(_.invoke(feignProperties).asInstanceOf[Option[Boolean]])

    logger.debug(s"Super getter returned: ${ignore}.")

    if (ignore == null) None else ignore
  }
}
