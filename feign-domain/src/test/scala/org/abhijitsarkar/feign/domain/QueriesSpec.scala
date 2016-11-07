package org.abhijitsarkar.feign.domain

import QueriesYamlProtocol._
import net.jcazevedo.moultingyaml._
import org.abhijitsarkar.feign.api.domain.Queries
import org.specs2.mutable.Specification

/**
  * @author Abhijit Sarkar
  */
class QueriesSpec extends Specification {
  "Queries" should {

    "parse when pairs are not specified" in {
      val source =
        """ignoreUnknown: false
          |ignoreEmpty: false
          |ignoreCase: true""".stripMargin

      val yamlAst = source.parseYaml

      val queries = yamlAst.convertTo[Queries]

      queries.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual Some(false)
      queries.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual Some(false)
      queries.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)

      queries.pairs must be empty
    }

    "parse when ignoreUnknown and pairs are not specified" in {
      val source =
        """ignoreEmpty: false
          |ignoreCase: true""".stripMargin

      val yamlAst = source.parseYaml

      val queries = yamlAst.convertTo[Queries]

      queries.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual None
      queries.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual Some(false)
      queries.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual Some(true)

      queries.pairs must be empty
    }

    "parse when only pairs are specified" in {
      val source =
        """pairs:
          |  i: ["1", "2", "3"]
          |  j: ["4", "5", "6"]""".stripMargin

      val yamlAst = source.parseYaml

      val queries = yamlAst.convertTo[Queries]

      queries.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual None
      queries.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual None
      queries.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual None

      queries.pairs must havePairs("i" -> Seq("1", "2", "3"), "j" -> Seq("4", "5", "6"))
    }

    "parse when nothing is specified" in {
      val source = ""

      val yamlAst = source.parseYaml

      val queries = yamlAst.convertTo[Queries]

      queries.ignorableRequestProperties.flatMap(_.ignoreUnknown) mustEqual None
      queries.ignorableRequestProperties.flatMap(_.ignoreEmpty) mustEqual None
      queries.ignorableRequestProperties.flatMap(_.ignoreCase) mustEqual None

      queries.pairs must be empty
    }
  }
}
