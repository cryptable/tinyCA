package org.cryptable.tinyca.mngmnt.profile

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Test of the AlgorithmSettings class
 *
 * Created by davidtillemans on 27/12/15.
 */
class AlgorithmSettingsSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Test for a valid algorithm settings encode"() {
        given: "valid Algorithm settings"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings(
                signatureAlgorithm: SignatureAlgorithm.SHA256withRSA,
                keyLength: KeyLength.RSA2048,
                keyAlgorithm: KeyAlgorithm.RSA
        )

        when: "Algorithm settings encode to JSON"
        String json = algorithmSettings.encode()

        then: "No exception is thrown and valid JSON string"
        notThrown(ProfileException)
        assert json == '{"signatureAlgorithm":"SHA256withRSA","keyAlgorithm":"RSA","keyLength":"RSA2048"}'
    }

    void "Test for a minimal valid algorithm settings encode"() {
        given: "valid minimal Algorithm settings"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings(
                signatureAlgorithm: SignatureAlgorithm.SHA256withRSA
        )

        when: "Algorithm settings encode to JSON"
        String json = algorithmSettings.encode()

        then: "No exception is thrown and valid minimal JSON string"
        assert json == '{"signatureAlgorithm":"SHA256withRSA"}'
    }

    void "Test for an invalid algorithm settings, missing algorthimSetting encode"() {
        given: "missing signatureAlgorithm in Algorithm settings"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings(
                keyLength: KeyLength.ECGOST_CRYPTOPR_A
        )

        when: "Algorithm settings encode to JSON"
        algorithmSettings.encode()

        then: "Exception is thrown with null value explication"
        final ProfileException e = thrown()
        assert e.message == "The algorithmSettings.signatureAlgorithm is null and is obligatory"
    }

    void "Test for an invalid algorithm settings, missing keyLength"() {
        given: "invalid Algorithm settings, missing keyLength"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings(
                signatureAlgorithm: SignatureAlgorithm.SHA256withRSA,
                keyAlgorithm: KeyAlgorithm.RSA
        )

        when: "Algorithm settings encode to JSON"
        String json = algorithmSettings.encode()

        then: "Exception is thrown with keyLength"
        final ProfileException e = thrown()
        assert e.message == "The algorithmSettings.keyLength is null, while algorithmSettings.keyAlgorithm is set"
    }

    void "Test for a valid decode to AlgorithmSettings using signature Algorithm"(String strSignAlgo, SignatureAlgorithm signAlgo) {
        given: "Valid AlgorithmSettings JSON value"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{"signatureAlgorithm":"' + strSignAlgo + '"}')

        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "Exception is thrown with keyLength"
        assert algorithmSettings.signatureAlgorithm == signAlgo

        where:
        strSignAlgo              | signAlgo
        "SHA1withRSA"            | SignatureAlgorithm.SHA1withRSA
        "SHA256withRSA"          | SignatureAlgorithm.SHA256withRSA
        "SHA384withRSA"          | SignatureAlgorithm.SHA384withRSA
        "SHA512withRSA"          | SignatureAlgorithm.SHA512withRSA
        "RIPEMD128withRSA"       | SignatureAlgorithm.RIPEMD128withRSA
        "RIPEMD160withRSA"       | SignatureAlgorithm.RIPEMD160withRSA
        "RIPEMD256withRSA"       | SignatureAlgorithm.RIPEMD256withRSA
        "SHA1withECDSA"          | SignatureAlgorithm.SHA1withECDSA
        "SHA256withECDSA"        | SignatureAlgorithm.SHA256withECDSA
        "SHA384withECDSA"        | SignatureAlgorithm.SHA384withECDSA
        "SHA512withECDSA"        | SignatureAlgorithm.SHA512withECDSA
        "GOST3411withECGOST3410" | SignatureAlgorithm.GOST3411withECGOST3410
    }

    void "Test for a valid decode to AlgorithmSettings using keyAlogrithm and KeyLength"(String strKeyAlgo, String strKeyLength, KeyAlgorithm keyAlgo, KeyLength keyLength) {
        given: "Valid AlgorithmSettings JSON value"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{"signatureAlgorithm":"SHA512withRSA", ' +
                '"keyAlgorithm":"' + strKeyAlgo + '",' +
                '"keyLength":"' + strKeyLength +'"}')

        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "Exception is thrown with keyLength"
        assert algorithmSettings.keyAlgorithm == keyAlgo
        assert algorithmSettings.keyLength == keyLength

        where:
        strKeyAlgo   | strKeyLength           | keyAlgo                 | keyLength
        "RSA"        | "RSA1024"              | KeyAlgorithm.RSA        | KeyLength.RSA1024
        "RSA"        | "RSA2048"              | KeyAlgorithm.RSA        | KeyLength.RSA2048
        "RSA"        | "RSA4096"              | KeyAlgorithm.RSA        | KeyLength.RSA4096
        "RSA"        | "RSA8192"              | KeyAlgorithm.RSA        | KeyLength.RSA8192
        "RSA"        | "RSA16384"             | KeyAlgorithm.RSA        | KeyLength.RSA16384
        "ECDSA"      | "secp192r1"            | KeyAlgorithm.ECDSA      | KeyLength.secp192r1
        "ECDSA"      | "secp224r1"            | KeyAlgorithm.ECDSA      | KeyLength.secp224r1
        "ECDSA"      | "secp256r1"            | KeyAlgorithm.ECDSA      | KeyLength.secp256r1
        "ECDSA"      | "secp384r1"            | KeyAlgorithm.ECDSA      | KeyLength.secp384r1
        "ECDSA"      | "secp521r1"            | KeyAlgorithm.ECDSA      | KeyLength.secp521r1
        "ECGOST3410" | "ECGOST_CRYPTOPR_A"    | KeyAlgorithm.ECGOST3410 | KeyLength.ECGOST_CRYPTOPR_A
        "ECGOST3410" | "ECGOST_CRYPTOPR_B"    | KeyAlgorithm.ECGOST3410 | KeyLength.ECGOST_CRYPTOPR_B
        "ECGOST3410" | "ECGOST_CRYPTOPR_C"    | KeyAlgorithm.ECGOST3410 | KeyLength.ECGOST_CRYPTOPR_C
        "ECGOST3410" | "ECGOST_CRYPTOPR_XchA" | KeyAlgorithm.ECGOST3410 | KeyLength.ECGOST_CRYPTOPR_XchA
        "ECGOST3410" | "ECGOST_CRYPTOPR_XchB" | KeyAlgorithm.ECGOST3410 | KeyLength.ECGOST_CRYPTOPR_XchB
    }

    void "Test for a invalid decode to AlgorithmSettings, missing signatureAlgorithm"() {
        given: "Valid JSON, but missong signature Algorithm"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{' +
                '"keyAlgorithm":"RSA",' +
                '"keyLength":"RSA2048"}')
        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "throws ProfileException with valid explanation"
        ProfileException e = thrown()
        assert e.message == "The algorithmSettings.signatureAlgorithm is null and is obligatory"

    }

    void "Test for a invalid decode to AlgorithmSettings, missing keyLength"() {
        given: "Valid JSON, but missong signature Algorithm"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{"signatureAlgorithm":"SHA512withRSA",' +
                '"keyAlgorithm":"RSA"}')
        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "throws ProfileException with valid explanation"
        ProfileException e = thrown()
        assert e.message == "The algorithmSettings.keyLength is null, while algorithmSettings.keyAlgorithm is set"

    }

    void "Test for a invalid decode to AlgorithmSettings, unknown signing algorithm"() {
        given: "Valid JSON, but unknown signature Algorithm"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{"signatureAlgorithm":"blabla",' +
                '"keyAlgorithm":"RSA",' +
                '"keyLength":"RSA1024"}')
        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "throws IllegalArgumentException with valid explanation"
        IllegalArgumentException e = thrown()
        assert e.message == "No enum constant org.cryptable.tinyca.mngmnt.profile.SignatureAlgorithm.blabla"

    }

    void "Test for a invalid decode to AlgorithmSettings, unknown key algorithm"() {
        given: "Valid JSON, but unknown key Algorithm"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{"signatureAlgorithm":"SHA512withRSA",' +
                '"keyAlgorithm":"blabla",' +
                '"keyLength":"RSA1024"}')
        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "throws IllegalArgumentException with valid explanation"
        IllegalArgumentException e = thrown()
        assert e.message == "No enum constant org.cryptable.tinyca.mngmnt.profile.KeyAlgorithm.blabla"

    }

    void "Test for a invalid decode to AlgorithmSettings, unknown key length"() {
        given: "Valid JSON, but unknown key length"
        AlgorithmSettings algorithmSettings = new AlgorithmSettings()
        JsonSlurper jsonSlurper = new JsonSlurper()
        def jsonValue = jsonSlurper.parseText('{"signatureAlgorithm":"SHA512withRSA",' +
                '"keyAlgorithm":"RSA",' +
                '"keyLength":"blabla"}')
        when: "Algorithm settings decode to AlgorithmSettings"
        algorithmSettings.decode(jsonValue)

        then: "throws IllegalArgumentException with valid explanation"
        IllegalArgumentException e = thrown()
        assert e.message == "No enum constant org.cryptable.tinyca.mngmnt.profile.KeyLength.blabla"

    }
}
