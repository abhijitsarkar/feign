package org.abhijitsarkar.feign.domain

import PathYamlProtocol._
import net.jcazevedo.moultingyaml._
import org.abhijitsarkar.feign.api.domain.Path
import org.specs2.mutable.Specification

/**
  * @author Abhijit Sarkar
  */
class PathSpec extends Specification {
  "Path" should {

    "parse all fields" in {
      val source =
        """uri: /feign/abc
          |ignoreCase: true""".stripMargin

      val yamlAst = source.parseYaml

      val path = yamlAst.convertTo[Path]

      path.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)
      path.uri mustEqual Some("/feign/abc")
    }

    "parse when ignoreCase is not specified" in {
      val source = "uri: /feign/abc"

      val yamlAst = source.parseYaml

      val path = yamlAst.convertTo[Path]

      path.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual None
      path.uri mustEqual Some("/feign/abc")
    }

    "parse when uri is not specified" in {
      val source = "ignoreCase: true"

      val yamlAst = source.parseYaml

      val path = yamlAst.convertTo[Path]

      path.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)
      path.uri mustEqual None
    }

    "parse when nothing is specified" in {
      val source = ""

      val yamlAst = source.parseYaml

      val path = yamlAst.convertTo[Path]

      path.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual None
      path.uri mustEqual None
    }
  }
}
