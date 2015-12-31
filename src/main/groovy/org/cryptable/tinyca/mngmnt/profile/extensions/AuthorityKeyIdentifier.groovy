package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonBuilder
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper
import org.cryptable.tinyca.mngmnt.profile.IProfile

/**
 * Settings for the Authority Key Identifier
 *
 * Created by davidtillemans on 27/12/15.
 */
class AuthorityKeyIdentifier extends Extension implements IProfile {

    Boolean issuerNameAndSerialNumber = false

    AuthorityKeyIdentifier() {
        critical = false
    }

    String encode() {
        JsonBuilder builder = new JsonBuilder(this)
        return builder.toString()
    }

    void decode(Map mapOptions) {
        critical = mapOptions.critical ?: false
        issuerNameAndSerialNumber = mapOptions.issuerNameAndSerialNumber ?: false
    }

    @Override
    void validate() {
        // Nothing to validate
    }

    void addExtension(X509v3CertificateWrapper cert) {
        if (issuerNameAndSerialNumber) {
            cert.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.authorityKeyIdentifier,
                    critical,
                    X509v3CertificateWrapper.x509ExtensionUtils.createAuthorityKeyIdentifier(cert.caCertificate.subjectPublicKeyInfo,
                        new GeneralNames(new GeneralName(GeneralName.directoryName, cert.caCertificate.subject)),
                        cert.caCertificate.serialNumber))
        }
        else {
            cert.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.authorityKeyIdentifier,
                    critical,
                    X509v3CertificateWrapper.x509ExtensionUtils.createAuthorityKeyIdentifier(cert.caCertificate.subjectPublicKeyInfo))
        }
    }

}
