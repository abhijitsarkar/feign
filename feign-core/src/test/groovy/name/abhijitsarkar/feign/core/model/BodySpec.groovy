package name.abhijitsarkar.feign.core.model

import spock.lang.Specification

/**
 * @author Abhijit Sarkar
 */
class BodySpec extends Specification {
    def body

    def "gets content when raw is set"() {
        setup:
        body = new Body()
        body.raw = 'body'

        expect:
        body.content == 'body'
    }

    def "gets content when url is set"() {
        setup:
        body = new Body()
        body.url = getClass().getResource('/body.txt')

        expect:
        body.content == 'body'
    }
}