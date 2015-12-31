package org.cryptable.tinyca.mngmnt.profile

/**
 * Signature algorithm is ofcourse in relation with the key algorithm of the signer
 */
enum SignatureAlgorithm {

    /**
     * SHA1 using ECDSA
     */
    SHA1withECDSA("SHA1withECDSA"),

    /**
     * SHA256 using ECDSA
     */
            SHA256withECDSA("SHA256withECDSA"),

    /**
     * SHA384 using ECDSA
     */
            SHA384withECDSA("SHA384withECDSA"),

    /**
     * SHA512 using ECDSA
     */
            SHA512withECDSA("SHA512withECDSA"),

    /**
     * GOST3411 using ECGOST3410
     */
            GOST3411withECGOST3410("GOST3411withECGOST3410"),

    /**
     * SHA1 using RSA
     */
            SHA1withRSA("SHA1withRSA"),

    /**
     * SHA256 using RSA
     */
            SHA256withRSA("SHA256withRSA"),

    /**
     * SHA384 using RSA
     */
            SHA384withRSA("SHA384withRSA"),

    /**
     * SHA512 using RSA
     */
            SHA512withRSA("SHA512withRSA"),

    /**
     * RIPEMD128 using RSA
     */
            RIPEMD128withRSA("RIPEMD128withRSA"),

    /**
     * RIPEMD160 using RSA
     */
            RIPEMD160withRSA("RIPEMD160withRSA"),

    /**
     * RIPEMD256 using RSA
     */
            RIPEMD256withRSA("RIPEMD256withRSA")

    private final String value;

    SignatureAlgorithm(String value) {
        this.value = value
    }

    String value() { return this.value }
}
