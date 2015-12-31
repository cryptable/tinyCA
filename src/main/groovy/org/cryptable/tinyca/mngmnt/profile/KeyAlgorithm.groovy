package org.cryptable.tinyca.mngmnt.profile

/**
 * Define the key algorithm used for generating key pairs
 */
enum KeyAlgorithm {

    /**
     * ECDSA is elliptic curve
     */
    ECDSA("ECDSA"),

    /**
     * GOST3410 is an elliptic curve according to Russian government
     */
            ECGOST3410("ECGOST3410"),

    /**
     * RSA is an common used key generation algorithm
     */
            RSA("RSA")

    private final String value;

    KeyAlgorithm(String value) {
        this.value = value
    }

    String value() { return this.value }
}
