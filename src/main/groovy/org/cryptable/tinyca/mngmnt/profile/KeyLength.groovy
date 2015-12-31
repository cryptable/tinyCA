package org.cryptable.tinyca.mngmnt.profile

/**
 * We define fixed key lengths to prevent the usage of exotic or incompatible key lengths.
 * The key length will have a relation to the key generation algorithm.
 */
enum KeyLength {

    /**
     * RSA using 1024 bit
     */
    RSA1024("RSA1024"),

    /**
     * RSA using 2048 bit
     */
            RSA2048("RSA2048"),

    /**
     * RSA using 4096 bit
     */
            RSA4096("RSA4096"),

    /**
     * RSA using 8192 bit
     */
            RSA8192("RSA8192"),

    /**
     * RSA using 16384 bit. This will have a big performance impact.
     */
            RSA16384("RSA16384"),

    /**
     * Elliptic curve according to NIST secp192r1
     */
            secp192r1("secp192r1"),

    /**
     * Elliptic curve according to NIST secp224r1
     */
            secp224r1("secp224r1"),

    /**
     * Elliptic curve according to NIST secp256r1
     */
            secp256r1("secp256r1"),

    /**
     * Elliptic curve according to NIST secp384r1
     */
            secp384r1("secp384r1"),

    /**
     * Elliptic curve according to NIST secp521r1
     */
            secp521r1("secp521r1"),

    /**
     * Elliptic curve according to Russian government GostR3410-2001-CryptoPro-A
     */
            ECGOST_CRYPTOPR_A("ECGOST_CRYPTOPR_A"),

    /**
     * Elliptic curve according to Russian government GostR3410-2001-CryptoPro-B
     */
            ECGOST_CRYPTOPR_B("ECGOST_CRYPTOPR_B"),

    /**
     * Elliptic curve according to Russian government GostR3410-2001-CryptoPro-C
     */
            ECGOST_CRYPTOPR_C("ECGOST_CRYPTOPR_C"),

    /**
     * Elliptic curve according to Russian government GostR3410-2001-CryptoPro-XchA
     */
            ECGOST_CRYPTOPR_XchA("ECGOST_CRYPTOPR_XchA"),

    /**
     * Elliptic curve according to Russian government GostR3410-2001-CryptoPro-XchB
     */
            ECGOST_CRYPTOPR_XchB("ECGOST_CRYPTOPR_XchB")

    private final String value;

    KeyLength(String value) {
        this.value = value
    }

    String value() { return this.value }
}
