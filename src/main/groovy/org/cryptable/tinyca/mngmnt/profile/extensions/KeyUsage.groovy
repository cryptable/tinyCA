package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonBuilder
import org.cryptable.tinyca.mngmnt.profile.IProfile
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper

/**
 * The KeyUsage configuration
 *
 * Created by davidtillemans on 30/12/15.
 */
class KeyUsage extends Extension implements IProfile {

    Integer fixed

    KeyUsage() {
        critical = true
    }

    /**
     * encode the configuration to a JSON string
     *
     * @return returns a JSON string
     */
    @Override
    String encode() {
        JsonBuilder builder = new JsonBuilder()
        Map mapJSON = [critical:critical]
        if (fixed != null) {
            def keyUsages = []
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.digitalSignature) {
                keyUsages.add("digitalSignature")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation) {
                keyUsages.add("nonRepudiation")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment) {
                keyUsages.add("keyEncipherment")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment) {
                keyUsages.add("dataEncipherment")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.keyAgreement) {
                keyUsages.add("keyAgreement")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.keyCertSign) {
                keyUsages.add("keyCertSign")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.cRLSign) {
                keyUsages.add("cRLSign")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.encipherOnly) {
                keyUsages.add("encipherOnly")
            }
            if (fixed & org.bouncycastle.asn1.x509.KeyUsage.decipherOnly) {
                keyUsages.add("decipherOnly")
            }
            mapJSON["fixed"]=keyUsages
        }
        builder(mapJSON)
        return builder.toString()

    }

    /**
     * decode a parsed JSON string to the KeyUsage object
     *
     * @param mapOptions a parsed JSON string
     */
    @Override
    void decode(Map mapOptions) {
        critical = mapOptions.critical ?: false
        if (mapOptions.fixed != null) {
            fixed = 0
            mapOptions.fixed.each({ value ->
                if (value == "digitalSignature") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.digitalSignature
                }
                if (value == "nonRepudiation") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation
                }
                if (value == "keyEncipherment") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment
                }
                if (value == "dataEncipherment") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment
                }
                if (value == "keyAgreement") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.keyAgreement
                }
                if (value == "keyCertSign") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.keyCertSign
                }
                if (value == "cRLSign") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.cRLSign
                }
                if (value == "encipherOnly") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.encipherOnly
                }
                if (value == "decipherOnly") {
                    fixed |= org.bouncycastle.asn1.x509.KeyUsage.decipherOnly
                }
            })
        }
        else {
            fixed = null
        }
    }

    /**
     * Validate the object
     */
    @Override
    void validate() {
        // No validation necessary
    }

    /**
     * Add the extension to the certificate
     *
     * @param cert certificate to which the extension must be added
     */
    @Override
    void addExtension(X509v3CertificateWrapper cert) {
        if (fixed != null) {
            org.bouncycastle.asn1.x509.KeyUsage keyUsage = new org.bouncycastle.asn1.x509.KeyUsage(fixed)
            cert.x509v3CertificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.keyUsage, critical, keyUsage)
        }
    }

}
