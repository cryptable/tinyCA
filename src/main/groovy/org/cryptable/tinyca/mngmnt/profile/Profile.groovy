package org.cryptable.tinyca.mngmnt.profile

import groovy.json.JsonSlurper

/**
 * Kind of date constraint
 * 1: minimum and maximum dates
 * 2: fixed time period (+years, +months, +weeks, +days, +hours, +minutes, +seconds)
 * 3: fixed beginDate and endData
 * 4: fixed date period (+years, +months, +weeks, +days) and fixed begin and end times (00.00.00)
 *
 * profile.timeConstraints.dateConstraint parameter
 */

/**
 * According to the use-cases
 * 1: Minimal begin Data and Time
 * 2: Not Used
 * 3: fixed begin date and time
 * 4: the time values are used as begin time
 *
 * profile.timeConstraints.beginTimestamp parameter
 */

/**
 * According to the use-cases
 * 1: Minimal end Data and Time
 * 2: Not Used
 * 3: fixed end date and time
 * 4: the time values are used as end time
 *
 * profile.timeConstraints.endTimestamp parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for years with current time as begin and end time
 * 3: Not used
 * 4: period definitio for years with fixed begin time and end time
 *
 * profile.timeConstraints.periodYears parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for months with current time as begin and end time
 * 3: Not used
 * 4: period definitio for months with fixed begin time and end time
 *
 * profile.timeConstraints.periodMonths parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for weeks with current time as begin and end time
 * 3: Not used
 * 4: period definition for weeks with fixed begin time and end time
 *
 * profile.timeConstraints.periodWeeks parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: Period definition for days with current time as begin and end time
 * 3: Not used
 * 4: Period definition for days with fixed begin time and end time
 *
 * profile.timeConstraints.periodDays parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for hours with current time as begin
 * 3: Not used
 * 4: Period definition for hours with fixed begin time and rounded upper end time
 *
 * profile.timeConstraints.periodHours paremeter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for years with current time as begin and end time
 * 3: Not used
 * 4: Period definition for minutes with fixed begin time and rounded upper end time
 *
 * profile.timeConstraints.periodMinutes parameter
 */

/**
 * According to the use-cases
 * 1: Not used
 * 2: period definition for years with current time as begin and end time
 * 3: Not used
 * 4: Period definition for seconds with fixed begin time and rounded upper end time
 *
 * profile.timeConstraints.periodSeconds parameter
 */

/**
 * Key generation algorithm:
 *
 * - ECDSA
 * - GOST3410
 * - RSA
 *
 * profile.certificate[x].algorithmSettings.keyAlgorithm parameter
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
 * profile.certificate[x].algorithmSettings.keyLength parameter
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
 * profile.certificate[x].algorithmSettings.signatureAlgorithm (obligatory)
 */

/**
 * Extensions of the certificate
 * These are the configuration of all the common extensions for a certificate
 * according to RFC5280
 *
 * - Authority Key Identifier
 *      profile.certificate[x].extensions.authorityKeyIndentifier
 * - Subject Key Identifier
 *      profile.certificate[x].extensions.subjectKeyIdentifier
 * - Key Usage
 *      profile.certificate[x].extensions.keyUsage
 * - Certificate Policies
 *      profile.certificate[x].extensions.certificatePolicies
 * - Policy Mappings
 *      profile.certificate[x].extensions.policyMapping
 * - Subject Alternative Name
 *      profile.certificate[x].extensions.subjectAlternativeName
 * - Issuer Alternative Name
 *      profile.certificate[x].extensions.issuerAlternativeName
 * - Subject Directory Attributes
 *      profile.certificate[x].extensions.subjectDirectoryAttributes
 * - Basic Constraints
 *      profile.certificate[x].extensions.basicConstraints
 * - Name Constraints
 *      profile.certificate[x].extensions.nameConstraints
 * - Policy Constraints
 *      profile.certificate[x].extensions.policyConstraints
 * - Extended Key Usage
 *      profile.certificate[x].extensions.extendedKeyUsage
 * - CRL Distribution Points
 *      profile.certificate[x].extensions.crlDistributionPoints
 * - Inhibit anyPolicy
 *      profile.certificate[x].extensions.inhibitAnyPolicy
 * - Freshest CRL
 *      profile.certificate[x].extensions.freshestCRL
 * - Authority Information Access
 *      profile.certificate[x].extensions.authorityInformationAccess
 * - Subject Information Access
 *      profile.certificate[x].extensions.subjectInformationAccess
 *
 */

/**
 * Handles Profile checking functions, templates and signature generation
 */
class Profile {

}