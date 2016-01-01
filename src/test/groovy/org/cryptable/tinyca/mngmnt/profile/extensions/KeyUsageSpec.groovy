package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonSlurper
import org.bouncycastle.asn1.ASN1OctetString
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.DERBitString
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509CertificateHolder
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper
import spock.lang.Specification

import java.security.KeyPair

/**
 * Test for the Key usage extension
 *
 * Created by davidtillemans on 31/12/15.
 */
class KeyUsageSpec extends Specification implements TestCATrait {

    def jsonSlurper = new JsonSlurper()

    /**
     * Create a simple selfsigned CA
     *
     * @return
     */
    def setup() {
        caInit()
    }

    def cleanup() {

    }

    void "encode KeyUsage with all valid values"() {
        given:"KeyUsage with valid values"
        KeyUsage keyUsage = new KeyUsage(
                fixed: org.bouncycastle.asn1.x509.KeyUsage.digitalSignature |
                        org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation |
                        org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment |
                        org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment |
                        org.bouncycastle.asn1.x509.KeyUsage.keyAgreement |
                        org.bouncycastle.asn1.x509.KeyUsage.keyCertSign |
                        org.bouncycastle.asn1.x509.KeyUsage.cRLSign |
                        org.bouncycastle.asn1.x509.KeyUsage.encipherOnly |
                        org.bouncycastle.asn1.x509.KeyUsage.decipherOnly
        )

        when:"Encode the keyUsage"
        String json = keyUsage.encode()

        then:"Valid JSON value"
        assert json == '{"critical":true,"fixed":["digitalSignature","nonRepudiation","keyEncipherment","dataEncipherment",' +
                '"keyAgreement","keyCertSign","cRLSign","encipherOnly","decipherOnly"]}'
    }

    void "encode KeyUsage with each valid values"(Integer usage, String jsonRef) {
        given:"KeyUsage with valid values"
        KeyUsage keyUsage = new KeyUsage(
                fixed: usage
        )

        when:"Encode the keyUsage"
        String json = keyUsage.encode()

        then:"Valid JSON value"
        assert json == jsonRef

        where:
        usage                                                | jsonRef
        org.bouncycastle.asn1.x509.KeyUsage.digitalSignature | '{"critical":true,"fixed":["digitalSignature"]}'
        org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation   | '{"critical":true,"fixed":["nonRepudiation"]}'
        org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment  | '{"critical":true,"fixed":["keyEncipherment"]}'
        org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment | '{"critical":true,"fixed":["dataEncipherment"]}'
        org.bouncycastle.asn1.x509.KeyUsage.keyAgreement     | '{"critical":true,"fixed":["keyAgreement"]}'
        org.bouncycastle.asn1.x509.KeyUsage.keyCertSign      | '{"critical":true,"fixed":["keyCertSign"]}'
        org.bouncycastle.asn1.x509.KeyUsage.cRLSign          | '{"critical":true,"fixed":["cRLSign"]}'
        org.bouncycastle.asn1.x509.KeyUsage.encipherOnly     | '{"critical":true,"fixed":["encipherOnly"]}'
        org.bouncycastle.asn1.x509.KeyUsage.decipherOnly     | '{"critical":true,"fixed":["decipherOnly"]}'
    }

    void "encode KeyUsage with no fixed values"() {
        given:"KeyUsage with no fixed values"
        KeyUsage keyUsage = new KeyUsage(
                fixed:0
        )

        when:"Encode the keyUsage"
        String json = keyUsage.encode()

        then:"Valid JSON value"
        assert json == '{"critical":true,"fixed":[]}'
    }

    void "encode KeyUsage without fixed values and critical test"() {
        given:"KeyUsage without fixed values"
        KeyUsage keyUsage = new KeyUsage(critical:false)

        when:"Encode the keyUsage"
        String json = keyUsage.encode()

        then:"Valid JSON value"
        assert json == '{"critical":false}'
    }

    void "decode KeyUsage with all fixed values"() {
        given:"KeyUsage JSON value with all fixed values"
        String json = '{"critical":true,"fixed":["digitalSignature","nonRepudiation","keyEncipherment","dataEncipherment",' +
                                                  '"keyAgreement","keyCertSign","cRLSign","encipherOnly","decipherOnly"]}'
        when:"Decode the JSON value"
        KeyUsage keyUsage = new KeyUsage()
        keyUsage.decode((Map)jsonSlurper.parseText(json))

        then:"Valid key usage value"
        assert keyUsage.critical
        assert keyUsage.fixed
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.digitalSignature) == org.bouncycastle.asn1.x509.KeyUsage.digitalSignature
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation) == org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment) == org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment) == org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.keyAgreement) == org.bouncycastle.asn1.x509.KeyUsage.keyAgreement
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.keyCertSign) == org.bouncycastle.asn1.x509.KeyUsage.keyCertSign
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.cRLSign) == org.bouncycastle.asn1.x509.KeyUsage.cRLSign
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.encipherOnly) == org.bouncycastle.asn1.x509.KeyUsage.encipherOnly
        assert (keyUsage.fixed & org.bouncycastle.asn1.x509.KeyUsage.decipherOnly) == org.bouncycastle.asn1.x509.KeyUsage.decipherOnly
    }

    void "decode KeyUsage with one fixed values"(String value, Integer ref) {
        given:"KeyUsage JSON value with one fixed values"
        String json = '{"critical":true,"fixed":["' + value + '"]}'

        when:"Decode the JSON value"
        KeyUsage keyUsage = new KeyUsage()
        keyUsage.decode((Map)jsonSlurper.parseText(json))

        then:"Valid key usage value"
        assert keyUsage.critical
        assert keyUsage.fixed
        assert (keyUsage.fixed & ref) == ref

        where:
        value              | ref
        "digitalSignature" | org.bouncycastle.asn1.x509.KeyUsage.digitalSignature
        "nonRepudiation"   | org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation
        "keyEncipherment"  | org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment
        "dataEncipherment" | org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment
        "keyAgreement"     | org.bouncycastle.asn1.x509.KeyUsage.keyAgreement
        "keyCertSign"      | org.bouncycastle.asn1.x509.KeyUsage.keyCertSign
        "cRLSign"          | org.bouncycastle.asn1.x509.KeyUsage.cRLSign
        "encipherOnly"     | org.bouncycastle.asn1.x509.KeyUsage.encipherOnly
        "decipherOnly"     | org.bouncycastle.asn1.x509.KeyUsage.decipherOnly

    }

    void "decode KeyUsage with no fixed values"() {
        given:"KeyUsage JSON value with no fixed values"
        String json = '{"critical":true,"fixed":[]}'

        when:"Decode the JSON value"
        KeyUsage keyUsage = new KeyUsage()
        keyUsage.decode((Map)jsonSlurper.parseText(json))

        then:"Valid key usage value"
        assert keyUsage.critical
        assert keyUsage.fixed == 0
    }

    void "decode KeyUsage with no fixed value at all"() {
        given:"KeyUsage JSON value with no fixed value at all"
        String json = '{"critical":false}'

        when:"Decode the JSON value"
        KeyUsage keyUsage = new KeyUsage()
        keyUsage.decode((Map)jsonSlurper.parseText(json))

        then:"Valid key usage value"
        assert !keyUsage.critical
        assert keyUsage.fixed == null
    }

    void "create encoded KeyUsage with all usages"() {
        given: "An KeyUsage"
        X509v3CertificateWrapper x509v3CertificateWrapper = createTestCertificate()
        KeyUsage keyUsage = new KeyUsage(
                critical: true,
                fixed: org.bouncycastle.asn1.x509.KeyUsage.digitalSignature |
                        org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation |
                        org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment |
                        org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment |
                        org.bouncycastle.asn1.x509.KeyUsage.keyAgreement |
                        org.bouncycastle.asn1.x509.KeyUsage.keyCertSign |
                        org.bouncycastle.asn1.x509.KeyUsage.cRLSign |
                        org.bouncycastle.asn1.x509.KeyUsage.encipherOnly |
                        org.bouncycastle.asn1.x509.KeyUsage.decipherOnly
        )

        when:"Encoded to ASN1 extension and build certificate"
        keyUsage.addExtension(x509v3CertificateWrapper)
        X509CertificateHolder subjectCert = x509v3CertificateWrapper.x509v3CertificateBuilder.build(caSigner)

        then:"Verify key usage in certificate"
        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
                org.bouncycastle.asn1.x509.Extension.keyUsage)
        assert extension
        assert extension.critical
        org.bouncycastle.asn1.x509.KeyUsage keyUsageRef = org.bouncycastle.asn1.x509.KeyUsage.fromExtensions(
                subjectCert.extensions
        )
        assert keyUsageRef
        assert keyUsageRef.hasUsages(org.bouncycastle.asn1.x509.KeyUsage.digitalSignature |
                org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation |
                org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment |
                org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment |
                org.bouncycastle.asn1.x509.KeyUsage.keyAgreement |
                org.bouncycastle.asn1.x509.KeyUsage.keyCertSign |
                org.bouncycastle.asn1.x509.KeyUsage.cRLSign |
                org.bouncycastle.asn1.x509.KeyUsage.encipherOnly |
                org.bouncycastle.asn1.x509.KeyUsage.decipherOnly)
    }

    /**
     * Remove test, because Bouncycastle doesn't allow to
     * overwrite and extension, only to add extensions
     */
//    void "create encoded KeyUsage, but overwrite key usage"() {
//        given: "An Certificate with Keyusage"
//        X509v3CertificateWrapper x509v3CertificateWrapper = createTestCertificate()
//        org.bouncycastle.asn1.x509.KeyUsage predefKeyUsage = new org.bouncycastle.asn1.x509.KeyUsage(org.bouncycastle.asn1.x509.KeyUsage.keyCertSign |
//                org.bouncycastle.asn1.x509.KeyUsage.cRLSign |
//                org.bouncycastle.asn1.x509.KeyUsage.encipherOnly |
//                org.bouncycastle.asn1.x509.KeyUsage.decipherOnly)
//        x509v3CertificateWrapper.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage,
//                true,
//                predefKeyUsage)
//        KeyUsage keyUsage = new KeyUsage(
//                critical: false,
//                fixed: org.bouncycastle.asn1.x509.KeyUsage.digitalSignature |
//                        org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation |
//                        org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment |
//                        org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment
//        )
//
//        when:"Encoded to ASN1 extension and build certificate"
//        keyUsage.addExtension(x509v3CertificateWrapper)
//        X509CertificateHolder subjectCert = x509v3CertificateWrapper.x509v3CertificateBuilder.build(caSigner)
//
//        then:"Verify key usage in certificate"
//        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
//                org.bouncycastle.asn1.x509.Extension.keyUsage)
//        assert extension
//        assert extension.critical
//        org.bouncycastle.asn1.x509.KeyUsage keyUsageRef = org.bouncycastle.asn1.x509.KeyUsage.fromExtensions(
//                subjectCert.extensions
//        )
//        assert keyUsageRef
//        assert keyUsageRef.hasUsages(org.bouncycastle.asn1.x509.KeyUsage.digitalSignature |
//                org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation |
//                org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment |
//                org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment
//        )
//    }

    void "create encoded KeyUsage, but no key usage defined"() {
        given: "a Certificate and Key usage"
        X509v3CertificateWrapper x509v3CertificateWrapper = createTestCertificate()
        KeyUsage keyUsage = new KeyUsage(
                critical: false
        )

        when:"Encoded to ASN1 extension and build certificate"
        keyUsage.addExtension(x509v3CertificateWrapper)
        X509CertificateHolder subjectCert = x509v3CertificateWrapper.x509v3CertificateBuilder.build(caSigner)

        then:"Verify no key usage in certificate"
        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
                org.bouncycastle.asn1.x509.Extension.keyUsage)
        assert extension == null
    }

    /**
     * Remove test, because Bouncycastle doesn't allow to
     * remove an extension, only to add extensions
     */
//    void "create encoded KeyUsage, but remove key usage"() {
//        given: "An SubjectKeyIdentifier"
//        X509v3CertificateWrapper x509v3CertificateWrapper = createTestCertificate()
//        org.bouncycastle.asn1.x509.KeyUsage predefKeyUsage = new org.bouncycastle.asn1.x509.KeyUsage(org.bouncycastle.asn1.x509.KeyUsage.keyCertSign |
//                org.bouncycastle.asn1.x509.KeyUsage.cRLSign |
//                org.bouncycastle.asn1.x509.KeyUsage.encipherOnly |
//                org.bouncycastle.asn1.x509.KeyUsage.decipherOnly)
//        x509v3CertificateWrapper.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage,
//                true,
//                predefKeyUsage)
//        KeyUsage keyUsage = new KeyUsage(
//                critical: false
//        )
//
//        when:"Encoded to ASN1 extension and build certificate"
//        keyUsage.addExtension(x509v3CertificateWrapper)
//        X509CertificateHolder subjectCert = x509v3CertificateWrapper.x509v3CertificateBuilder.build(caSigner)
//
//        then:"Verify subject key Identifier in certificate"
//        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
//                org.bouncycastle.asn1.x509.Extension.keyUsage)
//        assert extension == null
//    }

}
