package name.abhijitsarkar.feign.core.model

import java.util.{Map => JavaMap}

import scala.collection.JavaConverters._
import scala.collection.immutable.{Map => ImmutableMap}

/**
  * @author Abhijit Sarkar
  */
class Headers extends AbstractIgnorableRequestProperties {
  var pairs: ImmutableMap[String, String] = _

  setPairs(null)

  def getPairs = pairs

  def setPairs(pairs: JavaMap[String, String]) {
    this.pairs = if (pairs == null) ImmutableMap()
    else pairs.asScala.toMap
  }

  override def toString = s"Headers($pairs)${super.toString}"
}
