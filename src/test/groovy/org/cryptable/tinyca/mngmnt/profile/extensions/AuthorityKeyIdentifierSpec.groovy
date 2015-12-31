package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonSlurper
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
 * Test for the AuthorityKeyIdentifier
 *
 * Created by davidtillemans on 27/12/15.
 */
class AuthorityKeyIdentifierSpec extends Specification {

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

    void "encode AuthorityKeyIdentifier with valid values on true"() {
        given:"AuthorityKeyIdentifier with valid values"
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier(
                critical: false,
                issuerNameAndSerialNumber: true
        )
        when:"Encode the authorityKeyIdentifier"
        String json = authorityKeyIdentifier.encode()

        then:"Valid JSON value"
        assert json == '{"critical":false,"issuerNameAndSerialNumber":true}'
    }

    void "encode AuthorityKeyIdentifier with missing critical and issuerNameAndSerialNumber"() {
        given:"AuthorityKeyIdentifier with valid values"
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier()
        when:"Encode the authorityKeyIdentifier"
        String json = authorityKeyIdentifier.encode()

        then:"Valid JSON value"
        assert json == '{"critical":false,"issuerNameAndSerialNumber":false}'
    }

    void "decode AuthorityKeyIdentifier with valid values on true"() {
        given:"AuthorityKeyIdentifier JSON value"
        String json = '{"critical":false,"issuerNameAndSerialNumber":true}'

        when:"Decode the JSON value"
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier()
        authorityKeyIdentifier.decode((Map)jsonSlurper.parseText(json))

        then:"Valid AuthorityKeyIdentifier value"
        assert !authorityKeyIdentifier.critical
        assert authorityKeyIdentifier.issuerNameAndSerialNumber
    }

    void "decode AuthorityKeyIdentifier with valid values on false"() {
        given:"AuthorityKeyIdentifier JSON value"
        String json = '{"critical":true,"issuerNameAndSerialNumber":false}'

        when:"Decode the JSON value"
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier()
        authorityKeyIdentifier.decode((Map)jsonSlurper.parseText(json))

        then:"Valid AuthorityKeyIdentifier value"
        assert authorityKeyIdentifier.critical
        assert !authorityKeyIdentifier.issuerNameAndSerialNumber
    }

    void "decode AuthorityKeyIdentifier with valid values, but missing issuerNameAndSerialNumber and critical"() {
        given:"AuthorityKeyIdentifier JSON value"
        String json = '{}'

        when:"Decode the JSON value"
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier()
        authorityKeyIdentifier.decode((Map)jsonSlurper.parseText(json))

        then:"Valid AuthorityKeyIdentifier value"
        assert !authorityKeyIdentifier.critical
        assert !authorityKeyIdentifier.issuerNameAndSerialNumber
    }

    void "create encoded AuthorityKeyIdentifier with issuerNameAndSerialNumber"() {
        given: "An AuthorityKeyIdentifier"
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair()
        X509v3CertificateWrapper subjectX509 = new X509v3CertificateWrapper(caCertificate,
                BigInteger.valueOf(1000L),
                new Date(2010,01, 01, 00, 00, 00),
                new Date(2020,12, 31, 23, 59, 59),
                Locale.getDefault(),
                new X500Name("cn=Test User, ou=TinyCA, o=Cryptable, c=be"),
                new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.public.encoded)))
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier(
                critical: false,
                issuerNameAndSerialNumber: true
        )

        when:"Encoded to ASN1 extension and build certificate"
        authorityKeyIdentifier.addExtension(subjectX509)
        X509CertificateHolder subjectCert = subjectX509.x509v3CertificateBuilder.build(caSigner)

        then:"Verify authority key Identifier in certificate"
        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
                org.bouncycastle.asn1.x509.Extension.authorityKeyIdentifier)
        assert extension.critical == false
        assert extension.extnValue

        org.bouncycastle.asn1.x509.AuthorityKeyIdentifier authKeyId =
                new org.bouncycastle.asn1.x509.AuthorityKeyIdentifier(ASN1Sequence.getInstance(extension.extnValue.octets))
        assert authKeyId.authorityCertIssuer.toString() == "GeneralNames:\n    4: CN=CA,O=Cryptable,OU=TinyCA\n"
        assert authKeyId.authorityCertSerialNumber.equals(BigInteger.ONE)
        assert authKeyId.keyIdentifier.size() > 0

    }

    void "create encoded AuthorityKeyIdentifier without issuerNameAndSerialNumber"() {
        given: "An AuthorityKeyIdentifier"
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair()
        X509v3CertificateWrapper subjectX509 = new X509v3CertificateWrapper(caCertificate,
                BigInteger.valueOf(1000L),
                new Date(2010,01, 01, 00, 00, 00),
                new Date(2020,12, 31, 23, 59, 59),
                Locale.getDefault(),
                new X500Name("cn=Test User, ou=TinyCA, o=Cryptable, c=be"),
                new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.public.encoded)))
        AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier(
                critical: false,
                issuerNameAndSerialNumber: false
        )

        when:"Encoded to ASN1 extension and build certificate"
        authorityKeyIdentifier.addExtension(subjectX509)
        X509CertificateHolder subjectCert = subjectX509.x509v3CertificateBuilder.build(caSigner)

        then:"Verify authority key Identifier in certificate"
        org.bouncycastle.asn1.x509.Extension extension = subjectCert.getExtension(
                org.bouncycastle.asn1.x509.Extension.authorityKeyIdentifier)
        assert extension.critical == false
        assert extension.extnValue

        org.bouncycastle.asn1.x509.AuthorityKeyIdentifier authKeyId =
                new org.bouncycastle.asn1.x509.AuthorityKeyIdentifier(ASN1Sequence.getInstance(extension.extnValue.octets))
        assert !authKeyId.authorityCertIssuer
        assert !authKeyId.authorityCertSerialNumber
        assert authKeyId.keyIdentifier.size() > 0

    }
}
