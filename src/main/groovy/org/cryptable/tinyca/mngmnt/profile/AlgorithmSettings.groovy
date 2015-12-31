package org.cryptable.tinyca.mngmnt.profile

import groovy.json.JsonBuilder

/**
 * Default Algorithm settings for certificate generation
 * Algorithm settings
 */
class AlgorithmSettings implements IProfile {

    SignatureAlgorithm signatureAlgorithm

    KeyLength keyLength

    KeyAlgorithm keyAlgorithm

    String encode() {
        validate()

        JsonBuilder builder = new JsonBuilder()
        Map jsonMap = [
                "signatureAlgorithm": signatureAlgorithm.value()
        ]
        if (keyAlgorithm) {
            jsonMap["keyAlgorithm"] = keyAlgorithm.value()
            jsonMap["keyLength"] = keyLength.value()
        }

        builder(jsonMap)
        builder.toString()
    }

    void decode(Map mapOptions) {
        signatureAlgorithm = mapOptions.signatureAlgorithm
        keyLength = mapOptions.keyLength
        keyAlgorithm = mapOptions.keyAlgorithm

        validate()
    }

    /**
     * Validate the algorithm settings in the certificate profile
     */
    void validate() {
        if (!signatureAlgorithm) {
            throw new ProfileException("The algorithmSettings.signatureAlgorithm is null and is obligatory")
        }
        if (keyAlgorithm) {
            if (!keyLength) {
                throw new ProfileException("The algorithmSettings.keyLength is null," +
                        " while algorithmSettings.keyAlgorithm is set")
            }
        }
    }

}
