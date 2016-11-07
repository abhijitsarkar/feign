package org.abhijitsarkar.feign.api.domain

import java.net.URL

import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * @author Abhijit Sarkar
  */
case class Body(ignorableRequestProperties: Option[IgnorableRequestProperties] = None,
                raw: Option[String] = None,
                url: Option[String] = None,
                classpath: Option[String] = None) {
  val logger = LoggerFactory.getLogger(classOf[Body])

  def content: Option[String] = {
    def getUrlContent(s: String) = {
      Try {
        val u = new URL(s)
        val con = u.openConnection()
        con.setConnectTimeout(3000)
        con.setReadTimeout(3000)
        Source.fromInputStream(con.getInputStream, "UTF-8").mkString
      } match {
        case Success(x) => Some(x)
        case Failure(ex) => logger.error("Failed to read body from URL.", ex); None
      }
    }

    def getResourceContent(s: String) = {
      Try(getClass.getResourceAsStream(s)) match {
        case Success(x) => Some(Source.fromInputStream(x, "UTF-8").mkString)
        case Failure(ex) => logger.error("Failed to read body from classpath resource.", ex); None
      }
    }

    raw match {
      case x@Some(_) => x
      case _ => url.flatMap(getUrlContent) match {
        case y@Some(_) => y
        case _ => classpath.flatMap(getResourceContent) match {
          case z@Some(_) => z
          case _ => None
        }
      }
    }
  }
}
