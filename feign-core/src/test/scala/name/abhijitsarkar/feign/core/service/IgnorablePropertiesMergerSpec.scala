package name.abhijitsarkar.feign.core.service

import name.abhijitsarkar.feign.core.model.{FeignProperties, Queries, RequestProperties}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class IgnorablePropertiesMergerSpec extends FlatSpec with Matchers with BeforeAndAfter {
  val feignProperties = new FeignProperties
  val propertiesMerger = new IgnorablePropertiesMerger
  var requestProperties: RequestProperties = _
  var queries: Queries = _

  before {
    feignProperties.postConstruct
    requestProperties = new RequestProperties()
    queries = new Queries
  }

  "properties merger" should "copy from global if local is null" in {
    requestProperties.queries = queries

    propertiesMerger.merge(requestProperties, feignProperties)

    queries.ignoreEmpty shouldEqual feignProperties.ignoreEmpty
    queries.ignoreUnknown shouldEqual feignProperties.ignoreUnknown
    queries.ignoreCase shouldEqual feignProperties.ignoreCase
  }

  it should "keep local properties if defined" in {
    queries.ignoreEmpty = feignProperties.ignoreEmpty.map(!_)
    queries.ignoreUnknown = feignProperties.ignoreUnknown.map(!_)
    queries.ignoreCase = feignProperties.ignoreCase.map(!_)
    requestProperties.queries = queries

    propertiesMerger.merge(requestProperties, feignProperties)

    queries.ignoreEmpty shouldNot equal(feignProperties.ignoreEmpty)
    queries.ignoreUnknown shouldNot equal(feignProperties.ignoreUnknown)
    queries.ignoreCase shouldNot equal(feignProperties.ignoreCase)
  }
}
