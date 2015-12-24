package org.cryptable.tinyca.mngmnt

import groovy.json.JsonBuilder

/**
 * Profile in JSOM format
 */
enum TimeConstraintsChoice {

    minMaxDateTime("MinMaxDateTime"),

    fixedDateTimePeriod("FixedDateTimePeriod"),

    fixedBeginEndDateTime("FixedBeginEndDateTime"),

    fixedDatePeriodFixedTime("FixedDatePeriodFixedTime")

    private final String value;

    TimeConstraintsChoice(String value) {
        this.value = value
    }

    String value() { return this.value }
}


/**
 * Kind of date constraint
 * 1: minimum and maximum dates
 * 2: fixed time period (+years, +months, +weeks, +days, +hours, +minutes, +seconds)
 * 3: fixed beginDate and endData
 * 4: fixed date period (+years, +months, +weeks, +days) and fixed begin and end times (00.00.00)
 *
 * dateConstraint parameter
 */

/**
 * According to the use-cases
 * 1: Minimal begin Data and Time
 * 2: Not Used
 * 3: fixed begin date and time
 * 4: the time values are used as begin time
 *
 * beginTimestamp parameter
 */

/**
 * According to the use-cases
 * 1: Minimal end Data and Time
 * 2: Not Used
 * 3: fixed end date and time
 * 4: the time values are used as end time
 *
 * endTimestamp parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for years with current time as begin and end time
 * 3: Not used
 * 4: period definitio for years with fixed begin time and end time
 *
 * periodYears parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for months with current time as begin and end time
 * 3: Not used
 * 4: period definitio for months with fixed begin time and end time
 *
 * periodMonths parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for weeks with current time as begin and end time
 * 3: Not used
 * 4: period definition for weeks with fixed begin time and end time
 *
 * periodWeeks parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: Period definition for days with current time as begin and end time
 * 3: Not used
 * 4: Period definition for days with fixed begin time and end time
 *
 * periodDays parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for hours with current time as begin
 * 3: Not used
 * 4: Period definition for hours with fixed begin time and rounded upper end time
 *
 * periodHours paremeter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for years with current time as begin and end time
 * 3: Not used
 * 4: Period definition for minutes with fixed begin time and rounded upper end time
 *
 * periodMinutes parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for years with current time as begin and end time
 * 3: Not used
 * 4: Period definition for seconds with fixed begin time and rounded upper end time
 *
 * periodSecond
 */

enum KeyAlgorithm {

    ECDSA("ECDSA"),

    GOST3410("GOST3410"),

    RSA("RSA")

    private final String value;

    KeyAlgorithm(String value) {
        this.value = value
    }

    String value() { return this.value }
}

enum SignatureAlgorithm {

    SHA1withECDSA("SHA1withECDSA"),

    SHA256withECDSA("SHA256withECDSA"),

    SHA384withECDSA("SHA384withECDSA"),

    SHA512withECDSA("SHA512withECDSA"),

    GOST3411withGOST3410("GOST3411withGOST3410"),

    GOST3411withECGOST3410("GOST3411withECGOST3410"),

    SHA1withRSA("SHA1withRSA"),

    SHA256withRSA("SHA256withRSA"),

    SHA384withRSA("SHA384withRSA"),

    SHA512withRSA("SHA512withRSA"),

    RIPEMD128withRSA("RIPEMD128withRSA"),

    RIPEMD160withRSA("RIPEMD160withRSA"),

    RIPEMD256withRSA("RIPEMD256withRSA")

    private final String value;

    SignatureAlgorithm(String value) {
        this.value = value
    }

    String value() { return this.value }
}

enum KeyLength {

    RSA1024("1024"),

    RSA2048("2048"),

    RSA4096("4096"),

    RSA8192("8192"),

    RSA16384("16384"),

    secp192r1("secp192r1"),

    secp224r1("secp224r1"),

    secp256r1("secp256r1"),

    secp384r1("secp384r1"),

    secp521r1("secp521r1"),

    ECGOST_CRYPTOPR_A("GostR3410-2001-CryptoPro-A"),

    ECGOST_CRYPTOPR_B("GostR3410-2001-CryptoPro-B"),

    ECGOST_CRYPTOPR_C("GostR3410-2001-CryptoPro-C"),

    ECGOST_CRYPTOPR_XchA("GostR3410-2001-CryptoPro-XchA"),

    ECGOST_CRYPTOPR_XchB("GostR3410-2001-CryptoPro-XchB")

    private final String value;

    KeyLength(String value) {
        this.value = value
    }

    String value() { return this.value }
}

/**
 * Key generation algorithm:
 *
 * - ECDSA
 * - GOST3410
 * - RSA
 *
 * keyAlgorithm parameter
 */


/**
 * Hash generation algorithm for signature: SHA1, SHA256, SHA512
 * Must be in relation to the keyAlgorithm of the CA
 *
 * - SHA1withECDSA
 * - SHA256withECDSA
 * - SHA384withECDSA
 * - SHA512withECDSA
 * - GOST3411withGOST3410
 * - GOST3411withECGOST3410
 * - SHA1withRSA
 * - SHA256withRSA
 * - SHA384withRSA
 * - SHA512withRSA
 * - RIPEMD128withRSA
 * - RIPEMD160withRSA
 * - RIPEMD256withRSA
 *
 * signatureAlgorithm
 */

/**
 * Related to algorithm, you can define a key length
 *
 * recommended RSA key length
 *
 *   Minimum  | RSA      | Message
 *   Bits of  | Key Size | Digest
 *   Security |          | Algorithms
 *   ---------+----------+-----------------------------
 *   80       | 1024     | SHA1/SHA-256/SHA-384/SHA-512
 *   ---------+----------+-----------------------------
 *   112      | 2048     | SHA-256/SHA-384/SHA-512
 *   ---------+----------+-----------------------------
 *   128      | 4096     | SHA-256/SHA-384/SHA-512
 *   ---------+----------+-----------------------------
 *   192      | 8192     | SHA-384/SHA-512
 *   ---------+----------+-----------------------------
 *   256      | 16384    | SHA-512
 *   ---------+----------+-----------------------------
 *
 * recommended ECDSA curves for PKI certificate (rfc5480)
 *
 *   Minimum  | ECDSA    | Message    | Curves
 *   Bits of  | Key Size | Digest     |
 *   Security |          | Algorithms |
 *   ---------+----------+------------+-----------
 *   80       | 192      | SHA-256    | secp192r1
 *   ---------+----------+------------+-----------
 *   112      | 224      | SHA-256    | secp224r1
 *   ---------+----------+------------+-----------
 *   128      | 256      | SHA-256    | secp256r1
 *   ---------+----------+------------+-----------
 *   192      | 384      | SHA-384    | secp384r1
 *   ---------+----------+------------+-----------
 *   256      | 512      | SHA-512    | secp521r1
 *   ---------+----------+------------+-----------
 *
 *   GOST has a fixed key length
 *
 * keyLength parameter
 */
