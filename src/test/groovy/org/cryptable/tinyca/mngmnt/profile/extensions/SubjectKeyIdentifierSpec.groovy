package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonSlurper
import org.bouncycastle.asn1.ASN1OctetString
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper
import spock.lang.Specification

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security

/**
 * Tests of the SubjectKeyIdentifier
 *
 * Created by davidtillemans on 27/12/15.
 */
class SubjectKeyIdentifierSpec extends Specification{

    def jsonSlurper = new JsonSlurper()

    X509CertificateHolder caCertificate

    ContentSigner caSigner

    KeyPairGenerator keyPairGenerator

    /**
     * Create a simple selfsigned CA
     *
     * @return
     */
    def setup() {
        Security.addProvider(new BouncyCastleProvider());
        keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024, SecureRandom.getInstance("SHA1PRNG"))
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        caSigner  = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.private);
        def name = new X500Name("cn=CA, o=Cryptable, ou=TinyCA")
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                name,
                new BigInteger(1),
                new Date(2010, 10, 22, 00, 00, 00), new Date(2030, 10, 22, 00, 00, 00),
                name,
                keyPair.public)
        caCertificate = x509v3CertificateBuilder.build(caSigner)

    }

    def cleanup() {

    }

    void "encode SubjectKeyIdentifier with valid values on true"() {
        given:"SubjectKeyIdentifier with valid values"
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier(
                critical: false,
                sha1KeyIdentifier: true
        )
        when:"Encode the subjectKeyIdentifier"
        String json = subjectKeyIdentifier.encode()

        then:"Valid JSON value"
        assert json == '{"sha1KeyIdentifier":true,"critical":false}'
    }

    void "encode SubjectKeyIdentifier with valid values on true, missing sha1KeyIdentifier"() {
        given:"SubjectKeyIdentifier with valid values"
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier(
                critical: true
        )
        when:"Encode the subjectKeyIdentifier"
        String json = subjectKeyIdentifier.encode()

        then:"Valid JSON value"
        assert json == '{"sha1KeyIdentifier":true,"critical":true}'
    }

    void "encode SubjectKeyIdentifier with valid values on false"() {
        given:"SubjectKeyIdentifier with valid values"
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier(
                critical: false,
                sha1KeyIdentifier: false
        )
        when:"Encode the subjectKeyIdentifier"
        String json = subjectKeyIdentifier.encode()

        then:"Valid JSON value"
        assert json == '{"sha1KeyIdentifier":false,"critical":false}'
    }

    void "decode SubjectKeyIdentifier with valid values on true"() {
        given:"AuthorityKeyIdentifier JSON value"
        String json = '{"sha1KeyIdentifier":true,"critical":false}'

        when:"Decode the JSON value"
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier()
        subjectKeyIdentifier.decode((Map)jsonSlurper.parseText(json))

        then:"Valid subjectKeyIdentifier value"
        assert !subjectKeyIdentifier.critical
        assert subjectKeyIdentifier.sha1KeyIdentifier
    }

    void "decode SubjectKeyIdentifier with valid values on false"() {
        given:"AuthorityKeyIdentifier JSON value"
        String json = '{"sha1KeyIdentifier":false,"critical":false}'

        when:"Decode the JSON value"
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier()
        subjectKeyIdentifier.decode((Map)jsonSlurper.parseText(json))

        then:"Valid subjectKeyIdentifier value"
        assert !subjectKeyIdentifier.critical
        assert !subjectKeyIdentifier.sha1KeyIdentifier
    }

    void "decode SubjectKeyIdentifier with valid values, but missing sha1KeyIdentifier"() {
        given:"AuthorityKeyIdentifier JSON value"
        String json = '{"critical":false}'

        when:"Decode the JSON value"
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier()
        subjectKeyIdentifier.decode((Map)jsonSlurper.parseText(json))

        then:"Valid subjectKeyIdentifier value"
        assert !subjectKeyIdentifier.critical
        assert subjectKeyIdentifier.sha1KeyIdentifier
    }

    void "create encoded SubjectKeyIdentifier with SHA1 algorithm"() {
        given: "An SubjectKeyIdentifier"
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair()
        X509v3CertificateWrapper subjectX509 = new X509v3CertificateWrapper(caCertificate,
                BigInteger.valueOf(1000L),
                new Date(2010,01, 01, 00, 00, 00),
                new Date(2020,12, 31, 23, 59, 59),
                Locale.getDefault(),
                new X500Name("cn=Test User, ou=TinyCA, o=Cryptable, c=be"),
                new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.public.encoded)))
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier(
                critical: false,
                sha1KeyIdentifier: true
        )

        when:"Encoded to ASN1 extension and build certificate"
        subjectKeyIdentifier.addExtension(subjectX509)
        X509CertificateHolder subjectCert = subjectX509.x509v3CertificateBuilder.build(caSigner)

        then:"Verify subject key Identifier in certificate"
        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
                org.bouncycastle.asn1.x509.Extension.subjectKeyIdentifier)
        assert extension.critical == false
        assert extension.extnValue

        org.bouncycastle.asn1.x509.SubjectKeyIdentifier subjectKeyId =
                new org.bouncycastle.asn1.x509.SubjectKeyIdentifier(ASN1OctetString.getInstance(extension.extnValue.octets))
        assert subjectKeyId.keyIdentifier.size() == 20

    }

    void "create encoded SubjectKeyIdentifier with truncated SHA1 algorithm"() {
        given: "An SubjectKeyIdentifier"
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair()
        X509v3CertificateWrapper subjectX509 = new X509v3CertificateWrapper(caCertificate,
                BigInteger.valueOf(1000L),
                new Date(2010,01, 01, 00, 00, 00),
                new Date(2020,12, 31, 23, 59, 59),
                Locale.getDefault(),
                new X500Name("cn=Test User, ou=TinyCA, o=Cryptable, c=be"),
                new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.public.encoded)))
        SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier(
                critical: false,
                sha1KeyIdentifier: false
        )

        when:"Encoded to ASN1 extension and build certificate"
        subjectKeyIdentifier.addExtension(subjectX509)
        X509CertificateHolder subjectCert = subjectX509.x509v3CertificateBuilder.build(caSigner)

        then:"Verify subject key Identifier in certificate"
        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
                org.bouncycastle.asn1.x509.Extension.subjectKeyIdentifier)
        assert extension.critical == false
        assert extension.extnValue

        org.bouncycastle.asn1.x509.SubjectKeyIdentifier subjectKeyId =
                new org.bouncycastle.asn1.x509.SubjectKeyIdentifier(ASN1OctetString.getInstance(extension.extnValue.octets))
        assert subjectKeyId.keyIdentifier.size() == 8

    }

}
