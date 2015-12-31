package org.cryptable.tinyca.mngmnt

import groovy.time.TimeCategory
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.cryptable.tinyca.mngmnt.profile.KeyAlgorithm

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.spec.ECGenParameterSpec

/**
 * PKIService which performs the cryptographic operations
 */
class PKIService {

    private static final String PROVIDER = "BC"

    private static final Random RANDOM = SecureRandom.getInstance("SHA1PRNG")

    private static final Integer KEYLENGTH = 256

    private static final String KEY_ALGORITHM = "ECDSA"

    private static final String HASH_ALGORITHM = "SHA256"

    private static final String ALGORITHM_SPEC = "secp256r1"

    KeyPair integrityKeyPair

    Certificate integrityCertificate

    public PKIService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Create a Selfsigned key to initialisation services and keep the system
     * integer
     */
    void createIntegrityKey() {
        integrityCertificate = null;
        integrityKeyPair = null;
        Date beginDate = new Date()
        beginDate.clearTime()
        Date endDate
        String signAlgo = HASH_ALGORITHM + "with" + KEY_ALGORITHM
        use (TimeCategory) {
            endDate = beginDate + 30.years
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, PROVIDER);
        if (KEY_ALGORITHM == "EC") {
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(ALGORITHM_SPEC);
            keyPairGenerator.initialize(ecGenParameterSpec, RANDOM)
        }
        else {
            keyPairGenerator.initialize(KEYLENGTH, RANDOM)
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ContentSigner signer = new JcaContentSignerBuilder(signAlgo).setProvider(PROVIDER).build(keyPair.private);
        def name = new X500Name("cn=IntegrityKey, o=Cryptable, ou=TinyCA")
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(
                name,
                new BigInteger(128, RANDOM),
                beginDate, endDate,
                name,
                keyPair.public)
        X509CertificateHolder x509CertificateHolder = x509v3CertificateBuilder.build(signer)
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509")
        integrityCertificate = certificateFactory.generateCertificate(new ByteArrayInputStream(x509CertificateHolder.encoded))
        integrityKeyPair = keyPair
    }

    /**
     * Return the integrity certificate
     */
    Certificate getIntegrityCertificate() {
        this.integrityCertificate
    }

    /**
     * Return the integrity key pair
     */
    KeyPair getIntegrityKeyPair() {
        this.integrityKeyPair
    }

    KeyPair generateRSAKeyPair(def profile) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
        keyPairGenerator.initialize(profile.algorithmSettings.keyLength.toInteger(), RANDOM)
        keyPairGenerator.generateKeyPair();
    }

    KeyPair generateECDSAKeyPair(def profile) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", PROVIDER);
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(profile.algorithmSettings.keyLength);
        keyPairGenerator.initialize(ecGenParameterSpec, RANDOM)
        keyPairGenerator.generateKeyPair();
    }

    KeyPair generateGOSTKeyPair(def profile) {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECGOST3410", PROVIDER);
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(profile.algorithmSettings.keyLength);
        keyPairGenerator.initialize(ecGenParameterSpec, RANDOM)
        keyPairGenerator.generateKeyPair();
    }

    KeyPair generateKeyPair(def profile) {
        if (profile.algorithmSettings.keyAlgorithm == KeyAlgorithm.RSA) {
            generateRSAKeyPair(profile)
        }
        else if (profile.algorithmSettings.keyAlgorithm == KeyAlgorithm.ECDSA) {
            generateECDSAKeyPair(profile)
        }
        else if (profile.algorithmSettings.keyAlgorithm == KeyAlgorithm.GOST3410) {
            generateGOSTKeyPair(profile)
        }
        else {
            throw new PKIServiceException("Unknown key generation algorthim" + profile.algorithmSettings.keyAlgorithm)
        }
    }
}
