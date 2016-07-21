package name.abhijitsarkar.feign.core.model

import org.scalatest.{FlatSpec, Matchers}

/**
  * @author Abhijit Sarkar
  */
class BodySpec extends FlatSpec with Matchers {
  "body" should "get content when raw is set" in {
    val body = new Body
    body.setRaw("body")

    body.content shouldBe Some("body")
  }

  it should "get content when url is set" in {
    val body = new Body
    body.setUrl(getClass().getResource("/body.txt").toString)

    body.content shouldBe Some("body")
  }
}
