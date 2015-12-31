package org.cryptable.tinyca.mngmnt.profile.extensions

import org.bouncycastle.cert.X509v3CertificateBuilder
import org.cryptable.tinyca.mngmnt.profile.IProfile
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper

/**
 * The KeyUsage configuration
 *
 * Created by davidtillemans on 30/12/15.
 */
class KeyUsage extends Extension implements IProfile {

    /**
     * encode the configuration to a JSON string
     *
     * @return returns a JSON string
     */
    @Override
    String encode() {
        return null
    }

    /**
     * decode a parsed JSON string to the KeyUsage object
     *
     * @param mapOptions a parsed JSON string
     */
    @Override
    void decode(Map mapOptions) {

    }

    /**
     * Validate the object
     */
    @Override
    void validate() {

    }

    /**
     * Add the extension to the certificate
     *
     * @param cert certificate to which the extension must be added
     */
    @Override
    void addExtension(X509v3CertificateWrapper cert) {

    }

}
