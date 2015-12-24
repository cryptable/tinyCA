package org.cryptable.tinyca

/**
 * CertificateOwner: the owner the certificates
 * It contains a unique name and multiple certificates and contact details of the owner
 */
class CertificateOwner {

    /**
     * Unique name to identify the owner of the certificate
     */
    String name;

    /**
     * The principal e-mail address of the owner of the certificate to contact him
     */
    String principalEmail;

    /**
     * The principale phone number to call the owner in an emergency time
     */
    String principalPhone;

    static constraints = {
        name unique: true, nullable: false, maxSize: 256

        principalEmail email: true, nullable: false

        principalPhone nullable: true

        certificate nullable: true

        certificateSigningRequest nullable: true

    }

    static hasMany = [ certificate: Certificate, certificateSigningRequest: CertificateSigningRequest ]
}
