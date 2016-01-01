package org.cryptable.tinyca.mngmnt.profile.extensions

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

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security

/**
 * Created by davidtillemans on 1/01/16.
 */
trait TestCATrait {

    X509CertificateHolder caCertificate

    ContentSigner caSigner

    KeyPairGenerator keyPairGenerator

    /**
     * Create a simple selfsigned CA
     *
     * @return
     */
    def caInit() {
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

    X509v3CertificateWrapper createTestCertificate() {
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair()
        X509v3CertificateWrapper subjectX509 = new X509v3CertificateWrapper(caCertificate,
                BigInteger.valueOf(1000L),
                new Date(2010,01, 01, 00, 00, 00),
                new Date(2020,12, 31, 23, 59, 59),
                Locale.getDefault(),
                new X500Name("cn=Test User, ou=TinyCA, o=Cryptable, c=be"),
                new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.public.encoded)))
        subjectX509
    }

}