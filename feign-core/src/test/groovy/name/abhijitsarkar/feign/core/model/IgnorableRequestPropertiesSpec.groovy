package name.abhijitsarkar.feign.core.model

import spock.lang.Specification
import spock.lang.Unroll


/**
 * @author Abhijit Sarkar
 */
class IgnorableRequestPropertiesSpec extends Specification {
    private static class TestIgnorableRequestProperties extends IgnorableRequestProperties {
    }

    @Unroll
    def "sets #ignoreProperty"() {
        setup:
        def testUnit = new TestIgnorableRequestProperties()

        expect:
        !values.collect {
            testUnit."$ignoreProperty" = it
            testUnit."$ignoreProperty" == it
        }.contains(false)

        where:
        ignoreProperty   | _
        'ignoreUnknown'  | _
        'ignoreCase'     | _
        'ignoreEmpty'    | _

        values << [null, Boolean.TRUE, Boolean.FALSE]
    }
}
