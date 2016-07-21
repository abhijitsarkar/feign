package name.abhijitsarkar.feign.core.model

import java.util.{List => JavaList, Map => JavaMap}

import scala.collection.JavaConverters._
import scala.collection.immutable.{List => ImmutableList, Map => ImmutableMap}


/**
  * @author Abhijit Sarkar
  */
class Queries extends AbstractIgnorableRequestProperties {
  var pairs: ImmutableMap[String, ImmutableList[String]] = _
  setPairs(null)

  def getPairs = pairs

  def setPairs(pairs: JavaMap[String, JavaList[String]]) {
    this.pairs = if (pairs == null) ImmutableMap()
    else pairs.asScala.mapValues(_.asScala.toList).toMap
  }

  override def toString = s"Queries($pairs)${super.toString}"
}
