package org.abhijitsarkar.feign.service

import scala.reflect.runtime.{universe => ru}

/**
  * @author Abhijit Sarkar
  */
// Exists only as a reference for Scala reflection
class IgnorablePropertiesMerger {
  //  type ReflectivePair = (ru.Mirror, ru.Symbol)
  //
  //  private def getProperty(propertyClass: Class[_], containedInClass: Class[_]): Option[ReflectivePair] = {
  //    val runtimeMirror = ru.runtimeMirror(containedInClass.getClassLoader)
  //    val classSymbol = runtimeMirror.classSymbol(containedInClass)
  //
  //    classSymbol.toType.members
  //      .filter { x =>
  //        x.isMethod && !x.isSynthetic match {
  //          case true => {
  //            val rt = x.asMethod.returnType
  //            val rtName = rt.typeSymbol.fullName
  //            rtName == propertyClass.getName ||
  //              (rtName == classOf[Option[_]].getName &&
  //                rt.typeArgs.map(_.toString).contains(propertyClass.getName))
  //          }
  //          case _ => false
  //        }
  //      }
  //      .map(x => {
  //        Logger.debug(s"Found ignorable property: ${x}");
  //        x
  //      })
  //      .map((runtimeMirror, _))
  //      .headOption
  //  }
  //
  //  private val globalIgnorableProperty = getProperty(classOf[IgnorableRequestProperties], classOf[FeignProperties])
  //
  //  val peek = (clazz: Class[_]) => {
  //    Logger.debug(s"Introspecting class: ${clazz}.")
  //
  //    clazz
  //  }
  //
  //  private val localIgnorableProperty: Seq[Option[ReflectivePair]] = Seq(
  //    classOf[Path],
  //    classOf[Method],
  //    classOf[Queries],
  //    classOf[Headers],
  //    classOf[Body]
  //  )
  //    .map(peek)
  //    .map(getProperty(_, classOf[IgnorableRequestProperties]))

  //  def merge(requestProperties: RequestProperties, feignProperties: FeignProperties) {
  //    Seq(
  //      requestProperties.path.ignorableRequestProperties,
  //      requestProperties.method.ignorableRequestProperties,
  //      requestProperties.queries.ignorableRequestProperties,
  //      requestProperties.headers.ignorableRequestProperties,
  //      requestProperties.body.ignorableRequestProperties
  //    )
  //      .zip(Stream.continually(Some(feignProperties.ignorableRequestProperties)))
  //      .foreach {
  //        _ match {
  //          case (Some(local), Some(global)) => {
  //            local.merge(global)
  //          }
  //          case (None, Some(global)) => {
  //
  //          }
  //          case _ =>
  //        }
  //      }


  //    localIgnorableProperty.zip(Stream.continually(globalIgnorableProperty))
  //      .foreach {
  //        _ match {
  //          case (Some(local), Some(global)) => {
  //            Logger.debug(s"Merging ${local._2} with ${global._2}.")
  //
  //            val localValue = getFieldValue(local, requestProperties).asInstanceOf[Option[IgnorableRequestProperties]]
  //            val globalValue = getFieldValue(local, feignProperties).asInstanceOf[Option[IgnorableRequestProperties]]
  //
  //            if (!localValue.isDefined) {
  //              Logger.debug("Local value not defined, applying from global value.")
  //
  //              setFieldValue(local, requestProperties, globalValue)
  //            } else {
  //              Logger.debug("Local value defined, merging individual fields.")
  //              setFieldValue(local, requestProperties, localValue.get.merge(globalValue.get))
  //            }
  //          }
  //          case _ =>
  //        }
  //      }
  //  }
  //
  //  private def getFieldValue(r: ReflectivePair, instance: Any) = {
  //    val instanceMirror = r._1.reflect(instance)
  //    val fieldMirror = instanceMirror.reflectField(r._2.asTerm)
  //
  //    fieldMirror.get
  //  }
  //
  //  private def setFieldValue(r: ReflectivePair, instance: Any, value: Any) = {
  //    val instanceMirror = r._1.reflect(instance)
  //    val fieldMirror = instanceMirror.reflectField(r._2.asTerm)
  //
  //    fieldMirror.set(value)
  //  }
}
