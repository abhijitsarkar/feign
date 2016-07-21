package name.abhijitsarkar.feign.core.model

import org.springframework.core.io.{ClassPathResource, UrlResource}
import org.springframework.util.StringUtils._

import scala.beans.BeanProperty
import scala.io.Source
import scala.util.{Success, Try}

/**
  * @author Abhijit Sarkar
  */
class Body extends AbstractIgnorableRequestProperties {
  @BeanProperty
  var raw: String = _
  @BeanProperty
  var url: String = _
  @BeanProperty
  var classpath: String = _

  def content = getContent

  def getContent: Option[String] = {
    validate()

    if (!isEmpty(raw)) Some(raw)
    else if (!isEmpty(url) || !isEmpty(classpath)) {
      val resource = List(url, classpath).filterNot(isEmpty _).head
      Try(new ClassPathResource(resource).getInputStream()) match {
        case Success(is) => Some(Source.fromInputStream(is).mkString)
        case _ => {
          Try(new UrlResource(resource).getInputStream()) match {
            case Success(is) => Some(Source.fromInputStream(is).mkString)
            case _ => None
          }
        }
      }
    } else None
  }

  private def validate() {
    val count = List(raw, url, classpath).filterNot(isEmpty).size

    assert(count <= 1, "Ambiguous request body declaration.")
  }

  override def toString = s"Body($raw, $url, $classpath)"
}
