package org.cryptable.tinyca

/**
 * The domain class of the certificate
 */
class Certificate {

    /**
     * The distinguished name of the subjet
     */
    String subjectName

    /**
     * The distinguished name of the issuer of the certificate
     */
    String issuerName

    /**
     * The common name of the subject for search purposes
     */
    String commonName

    /**
     * The serial number of the certificate
     */
    String serialNumber

    /**
     * The begin date of the certificate
     */
    Date beginDate

    /**
     * The end date of the certificate
     */
    Date endDate

    /**
     * The date when the certificate was issued (created)
     */
    Date issuingDate

    /**
     * Binary blob of the certificate
     */
    byte[] certificate

    /**
     * Encrypted Binary blob of the private key
     */
    byte[] privateKey

    static constraints = {
        subjectName nullable: false, blank: false
        issuerName nullable: false, blank: false
        commonName nullable: true, blank: true
        serialNumber nullable: false, blank: false, matches: "[0-9]+", size : 32
        certificate nullable: false
        privateKey nullable: true
        certificates nullable: true
        revocation nullable: true
    }

    static belongsTo = [ Certificate, CertificateOwner, CertificateSigningRequest, Profile ]
    static hasMany = [ certificates: Certificate ]
    static hasOne = [ revocation: Revocation ]
}
