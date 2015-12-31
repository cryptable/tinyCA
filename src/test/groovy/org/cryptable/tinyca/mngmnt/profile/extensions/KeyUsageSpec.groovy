package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonSlurper
import org.bouncycastle.asn1.x500.X500Name
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
 * Test for the Key usage extension
 *
 * Created by davidtillemans on 31/12/15.
 */
class KeyUsageSpec extends Specification {

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

}
