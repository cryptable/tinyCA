package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonBuilder
import org.bouncycastle.cert.X509ExtensionUtils
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper
import org.cryptable.tinyca.mngmnt.profile.IProfile

/**
 * The Subject Key identifier profile setting
 * Created by davidtillemans on 27/12/15.
 */
class SubjectKeyIdentifier extends Extension implements IProfile {

    /**
     * true : The keyIdentifier is composed of the 160-bit SHA-1 hash of the
     *        value of the BIT STRING subjectPublicKey (excluding the tag,
     *        length, and number of unused bits).
     * false : The keyIdentifier is composed of a four-bit type field with
     *         the value 0100 followed by the least significant 60 bits of
     *         the SHA-1 hash of the value of the BIT STRING
     *         subjectPublicKey (excluding the tag, length, and number of
     *         unused bits).
     */
    Boolean sha1KeyIdentifier = true

    SubjectKeyIdentifier() {
        critical = false
    }

    String encode() {
        JsonBuilder builder = new JsonBuilder(this)
        return builder.toString()
    }

    void decode(Map mapAuthKeyId) {
        critical = mapAuthKeyId.critical ?: false
        sha1KeyIdentifier = mapAuthKeyId.sha1KeyIdentifier != null ? mapAuthKeyId.sha1KeyIdentifier : true
    }

    @Override
    void validate() {
        // Nothing to be validated
    }
/**
     * Add subject key identifier to the certificate builder
     *
     * @param cert certificate to add the extension to
     */
    @Override
    void addExtension(X509v3CertificateWrapper cert) {
        if (sha1KeyIdentifier) {
            cert.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.subjectKeyIdentifier,
                    critical,
                    X509v3CertificateWrapper.x509ExtensionUtils.createSubjectKeyIdentifier(cert.subjectPublicKeyInfo))

        }
        else {
            cert.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.subjectKeyIdentifier,
                    critical,
                    X509v3CertificateWrapper.x509ExtensionUtils.createTruncatedSubjectKeyIdentifier(cert.subjectPublicKeyInfo))
        }
    }
}
