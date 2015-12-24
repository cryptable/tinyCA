package org.cryptable.tinyca

/**
 * Certificate Signing Request table
 */
class CertificateSigningRequest {

    /**
     * Initial distinguished name of the subject, which can be different from the certificate itself
     * due to profile overrides
     */
    String subjectName

    /**
     * The binary value to of the certificate signing request
     */
    byte[] certificateSigningRequest

    static constraints = {
        subjectName nullable: false, blank: false
        certificateSigningRequest nullable: false
    }
}
