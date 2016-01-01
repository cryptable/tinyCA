package org.cryptable.tinyca.mngmnt.profile.extensions

import com.sun.xml.internal.ws.policy.PolicyException
import org.cryptable.tinyca.mngmnt.profile.ProfileException
import spock.lang.Specification

/**
 * Tests for the CertificatePolicies extension
 * certificatePolicies = [
 *{*      "policyIdentifier": "0.1.2.3.4.5",
 *      "policyQualifiers": [
 *{*         "policyQualifierId": "1.3.6.1.5.5.7.2.1 (id-qt-cps)" | "1.3.6.1.5.5.7.2.1 (id-qt-unotice)",
 *         "qualifier": {*             "cPSuri": "http://cps.cryptable.org"
 *}*         |            {*             "userNotice" : {*                 noticeRef: {*                     organization: "blablablabla" (1..200),
 *                     noticeNumbers: [1, 2, 9, 78, ...]
 *}*                 explicitText: "blablablabla" (1..200)
 *}*}*},
 *      ...
 *      ]
 *},
 *   ...
 * ]
 * ==> SIMPLIFY
 * certificatePolicies = [
 *{*      "policyIdentifier": "0.1.2.3.4.5",
 *      "policyQualifiers": [
 *{*          "cPSuri": "http://cps.cryptable.org"
 *         >>XOR<< (one or the other, not both)
 *          "userNotice.noticeRef.organization": "blablablabla" (1..200),  |
 *          "userNotice.noticeRef.noticeNumbers": [1, 2, 9, 78, ...]       +  OPTIONAL
 *          "userNotice.explicitText": "blablablabla" (1..200)             -> OPTIONAL
 *},
 *      ...
 *      ]
 *},
 *   ...
 * ]
 *
 * Created by davidtillemans on 1/01/16.
 */
class CertificatePoliciesSpec extends Specification implements TestCATrait {

    void setup() {

    }

    void cleanup() {

    }

    void "Create certificate policies JSON from valid values"() {
        given: "Certificate Policy with valid values"
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false)
        def arrayPolicyQualifiers1 = [
                [
                        "cPSuri": "http://cps.cryptable.org"
                ],
                [
                        "userNotice.noticeRef.organization" : "blablablabla",
                        "userNotice.noticeRef.noticeNumbers": [1, 2, 9, 78, 198],
                        "userNotice.explicitText"           : "bloblobloblo"
                ]
        ]
        def mapCertificatePolicy1 = ["policyIdentifier": "0.1.2.3.4.5",
                                     "policyQualifiers": arrayPolicyQualifiers1]
        certificatePolicies.addPolicy(mapCertificatePolicy1)
        def arrayPolicyQualifiers2 = [
                [
                        "cPSuri": "http://altcps.cryptable.org"
                ]
        ]
        def mapCertificatePolicy2 = ["policyIdentifier": "0.1.2.3.4.6",
                                     "policyQualifiers": arrayPolicyQualifiers2]
        certificatePolicies.addPolicy(mapCertificatePolicy2)

        when: "Encode the Certificate Policy"
        String json = certificatePolicies.encode()

        then: "Valid JSON value"
        assert json == '{"certificatePolicies":[' +
                '{"policyIdentifier":"0.1.2.3.4.5",' +
                '"policyQualifiers":[' +
                '{"cPSuri":"http://cps.cryptable.org"},' +
                '{' +
                '"userNotice.noticeRef.organization":"blablablabla",' +
                '"userNotice.noticeRef.noticeNumbers":[1,2,9,78,198],' +
                '"userNotice.explicitText":"bloblobloblo"' +
                '}' +
                ']' +
                '},' +
                '{"policyIdentifier":"0.1.2.3.4.6",' +
                '"policyQualifiers":[' +
                '{"cPSuri":"http://altcps.cryptable.org"}' +
                ']' +
                '}' +
                '],"critical":false}'

    }

    void "Create certificate policies JSON from valid values with optional userNotice"() {
        given: "Certificate Policy with valid values"
        def arrayPolicyQualifiers1 = [
                [
                        "cPSuri": "http://cps.cryptable.org"
                ],
                [
                        "userNotice.noticeRef.organization" : "blablablabla",
                        "userNotice.noticeRef.noticeNumbers": [1, 2, 9, 78, 198],
                ]
        ]
        def mapCertificatePolicy1 = ["policyIdentifier": "0.1.2.3.4.5",
                                     "policyQualifiers": arrayPolicyQualifiers1]
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [mapCertificatePolicy1])

        when: "Encode the Certificate Policy"
        String json = certificatePolicies.encode()

        then: "Valid JSON value"
        assert json == '{"certificatePolicies":[' +
                '{"policyIdentifier":"0.1.2.3.4.5",' +
                '"policyQualifiers":[' +
                '{"cPSuri":"http://cps.cryptable.org"},' +
                '{' +
                '"userNotice.noticeRef.organization":"blablablabla",' +
                '"userNotice.noticeRef.noticeNumbers":[1,2,9,78,198]' +
                '}' +
                ']' +
                '}' +
                '],"critical":false}'

    }

    void "Create certificate policies JSON from valid values with optional explicitText"() {
        given: "Certificate Policy with valid values"
        def arrayPolicyQualifiers1 = [
                [
                        "cPSuri": "http://cps.cryptable.org"
                ],
                [
                        "userNotice.explicitText": "bloblobloblo"
                ]
        ]
        def mapCertificatePolicy1 = ["policyIdentifier": "0.1.2.3.4.5",
                                     "policyQualifiers": arrayPolicyQualifiers1]
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [mapCertificatePolicy1])

        when: "Encode the Certificate Policy"
        String json = certificatePolicies.encode()

        then: "Valid JSON value"
        assert json == '{"certificatePolicies":[' +
                '{"policyIdentifier":"0.1.2.3.4.5",' +
                '"policyQualifiers":[' +
                '{"cPSuri":"http://cps.cryptable.org"},' +
                '{' +
                '"userNotice.explicitText":"bloblobloblo"' +
                '}' +
                ']' +
                '}' +
                '],"critical":false}'

    }

    void "Create certificate policies JSON from valid values without policyQualifiers"() {
        given: "Certificate Policy with valid values"
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [["policyIdentifier": "0.1.2.3.4.5"], ["policyIdentifier": "0.1.2.3.4.6"]])

        when: "Encode the Certificate Policy"
        String json = certificatePolicies.encode()

        then: "Valid JSON value"
        assert json == '{"certificatePolicies":[' +
                '{"policyIdentifier":"0.1.2.3.4.5"},' +
                '{"policyIdentifier":"0.1.2.3.4.6"}' +
                '],"critical":false}'

    }

    void "Create certificate policies JSON from invalid values, both cpsUri and userNotice"() {
        given: "Certificate Policy with valid values"
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [["policyIdentifier": "0.1.2.3.4.5",
                                       "policyQualifiers": [[
                                                                    "cPSuri"                            : "http://cps.cryptable.org",
                                                                    "userNotice.noticeRef.organization" : "blablablabla",
                                                                    "userNotice.noticeRef.noticeNumbers": [1, 2, 9, 78, 198],
                                                                    "userNotice.explicitText"           : "bloblobloblo"
                                                            ]]]])

        when: "Encode the Certificate Policy"
        certificatePolicies.encode()

        then: "Throw PolicyException"
        ProfileException e = thrown()
        assert e.message == "Both cSPuri and userNotice defined in certificatePolicies with policyIdentifier [0.1.2.3.4.5], which is not allowed"
    }

    void "Create certificate policies JSON from invalid values, userNotice.noticeRef.noticeNumbers missing"() {
        given: "Certificate Policy with valid values"
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [["policyIdentifier": "0.1.2.3.4.5",
                                       "policyQualifiers": [[
                                                                    "userNotice.noticeRef.organization": "blablablabla",
                                                                    "userNotice.explicitText"          : "bloblobloblo"
                                                            ]]]])

        when: "Encode the Certificate Policy"
        certificatePolicies.encode()

        then: "Throw PolicyException"
        ProfileException e = thrown()
        assert e.message == "In the userNotice of the certificatePolicies with policyIdentifier [0.1.2.3.4.5] are the noticeRef.noticeNumbers or userNotice.noticeRef.organization missing, which are obliged when userNotice.noticeRef is used"
    }

    void "Create certificate policies JSON from invalid values, userNotice.noticeRef.organization missing"() {
        given: "Certificate Policy with valid values"
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [["policyIdentifier": "0.1.2.3.4.6",
                                       "policyQualifiers": [[
                                                                    "userNotice.noticeRef.noticeNumbers": [1, 2, 9, 78, 198]
                                                            ]]]])

        when: "Encode the Certificate Policy"
        String json = certificatePolicies.encode()

        then: "Throw PolicyException"
        ProfileException e = thrown()
        assert e.message == "In the userNotice of the certificatePolicies with policyIdentifier [0.1.2.3.4.6] are the noticeRef.noticeNumbers or userNotice.noticeRef.organization missing, which are obliged when userNotice.noticeRef is used"
    }

    void "Create certificate policies JSON from invalid values, invalid OID for policyIdentifier"() {
        given: "Certificate Policy with valid values"
        CertificatePolicies certificatePolicies = new CertificatePolicies(critical: false,
                certificatePolicies: [["policyIdentifier": "0.1.2.3.4.5.",
                                       "policyQualifiers": [[
                                                                    "userNotice.noticeRef.organization" : "blablablabla",
                                                                    "userNotice.noticeRef.noticeNumbers": [1, 2, 9, 78, 198]
                                                            ]]]])

        when: "Encode the Certificate Policy"
        String json = certificatePolicies.encode()

        then: "Throw PolicyException"
        ProfileException e = thrown()
        assert e.message == "Invalid OID format for [0.1.2.3.4.5.]"
    }
}
