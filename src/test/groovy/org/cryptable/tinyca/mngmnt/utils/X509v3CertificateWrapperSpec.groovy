package org.cryptable.tinyca.mngmnt.utils

import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import spock.lang.Specification

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security

/**
 * Test specification for X509v3CertificateWrappe
 * Created by davidtillemans on 30/12/15.
 */
class X509v3CertificateWrapperSpec extends Specification{

    KeyPair keyPair
    X509CertificateHolder caCertificate

    void setup() {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(1024, SecureRandom.getInstance("SHA1PRNG"))
        keyPair = keyPairGenerator.generateKeyPair();
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.private);
        def name = new X500Name("cn=CA, o=Cryptable, ou=TinyCA")
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                name,
                new BigInteger(128, SecureRandom.getInstance("SHA1PRNG")),
                new Date(2010, 10, 22, 00, 00, 00), new Date(2030, 10, 22, 00, 00, 00),
                name,
                keyPair.public)
        caCertificate = x509v3CertificateBuilder.build(signer)
    }

    void cleanup() {

    }

    void "Expect to create a wrapper object"() {
        given:"Some Mocks to create the wrapper"
        SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(ASN1Sequence.getInstance(keyPair.public.encoded))

        when:"Create the wrapper"
        X509v3CertificateWrapper x509Wrapper = new X509v3CertificateWrapper(caCertificate,
                BigInteger.valueOf(1000L),
                new Date(2014, 10, 22, 00, 00, 00),
                new Date(2016, 10, 22, 00, 00, 00),
                Locale.getDefault(),
                new X500Name("cn=Test User"),
                publicKeyInfo)

        then:"Check the X509 wrapper values"
        assert x509Wrapper
        assert x509Wrapper.x509v3CertificateBuilder
        assert x509Wrapper.caCertificate == caCertificate
        assert x509Wrapper.subjectPublicKeyInfo == publicKeyInfo
    }
}
