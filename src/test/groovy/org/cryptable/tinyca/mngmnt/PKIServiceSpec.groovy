package org.cryptable.tinyca.mngmnt

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Created by david on 20/12/15.
 */
class PKIServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test generation of the integrity key"() {

        expect:"test the initialisation of the integrity key"
        PKIService pkiService = new PKIService()
        pkiService.createIntegrityKey()
        pkiService.integrityCertificate.verify(pkiService.integrityKeyPair.public)
    }

    void "test JWT with the integrity key"() {
        given: "Some JSON token"
        String json = '{name:"david", password:"secret"}'
        PKIService pkiService = new PKIService()
        when: "Create JSON Signature"
        String signedJSON = pkiService.signJSON(json)
        then: "We have a signed JSON object"
        assert signedJSON == ''
    }
}
