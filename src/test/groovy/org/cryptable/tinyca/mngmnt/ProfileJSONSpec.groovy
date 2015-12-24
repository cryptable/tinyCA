package org.cryptable.tinyca.mngmnt

import groovy.json.JsonBuilder
import groovy.time.TimeCategory
import spock.lang.Specification

/**
 * Tests for the ProfileJSON
 */
class ProfileJSONSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test a JSON creation of a profile"() {

        given:"the fields of Date constraints"
        def builder = new JsonBuilder()
        def startDate = new Date().copyWith(year: 2014,
                month: Calendar.JANUARY,
                dayOfMonth: 1,
                hourOfDay: 0,
                minute: 0,
                second: 0)
        def endDate
        use (TimeCategory) {
            endDate = startDate + 10.years
        }
        def profile = builder.profile {
            timeConstraint {
                dateConstraint TimeConstraintsChoice.fixedBeginEndDateTime
                beginTimestamp startDate
                endTimestamp endDate
            }
            algorithmSettings {
                keyAlgorithm KeyAlgorithm.ECDSA
                keyLength KeyLength.secp256r1
                signatureAlgorithm SignatureAlgorithm.SHA256withECDSA
            }
        }

        when:"JSON is created"
        String JSONvalue = builder.toString()

        then:"JSON should be valid"
        JSONvalue.toString() == '{"profile":{"timeConstraint":{"dateConstraint":"fixedBeginEndDateTime","beginTimestamp":"2013-12-31T23:00:00+0000","endTimestamp":"2023-12-31T23:00:00+0000"},"algorithmSettings":{"keyAlgorithm":"ECDSA","keyLength":"secp256r1","signatureAlgorithm":"SHA256withECDSA"}}}'
    }

}
