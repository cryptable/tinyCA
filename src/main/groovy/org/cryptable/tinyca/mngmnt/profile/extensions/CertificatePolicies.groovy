package org.cryptable.tinyca.mngmnt.profile.extensions

import groovy.json.JsonBuilder
import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.cryptable.tinyca.mngmnt.profile.IProfile
import org.cryptable.tinyca.mngmnt.profile.ProfileException
import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper

/**
 * Definition of the Certificate Policies extension:
 * see RFC 5280
 * Created by davidtillemans on 1/01/16.
 */
class CertificatePolicies extends Extension implements IProfile{

    ArrayList certificatePolicies = []

    void addPolicy(Map policy) {
        certificatePolicies.add(policy)
    }

    @Override
    String encode() {
        validate()
        JsonBuilder builder = new JsonBuilder(this)
        builder
    }

    @Override
    void decode(Map mapOptions) {

    }

    @Override
    void validate() {
        certificatePolicies.each({ policy ->
            try {
                ASN1ObjectIdentifier asn1ObjectIdentifier = new ASN1ObjectIdentifier(policy["policyIdentifier"])
            } catch (IllegalArgumentException e) {
                throw new ProfileException("Invalid OID format for [" + policy["policyIdentifier"] + "]", e.cause)
            }
            ArrayList policyQualifiers = policy["policyQualifiers"]
            if (policyQualifiers) {
                policyQualifiers.each({ policyQualifier ->
                    if (policyQualifier["cPSuri"] && (policyQualifier["userNotice.noticeRef.organization"] ||
                            policyQualifier["userNotice.noticeRef.noticeNumbers"] ||
                            policyQualifier["userNotice.noticeRef.explicitText"])) {
                        throw new ProfileException("Both cSPuri and userNotice defined in certificatePolicies with " +
                                "policyIdentifier [" + policy["policyIdentifier"] +"], which is not allowed")
                    }
                    if (policyQualifier["userNotice.noticeRef.organization"] || policyQualifier["userNotice.noticeRef.noticeNumbers"]) {
                        if (!(policyQualifier["userNotice.noticeRef.organization"] && policyQualifier["userNotice.noticeRef.noticeNumbers"])) {
                            throw new ProfileException("In the userNotice of the certificatePolicies with policyIdentifier [" + policy["policyIdentifier"] + "] " +
                                    "are the noticeRef.noticeNumbers or userNotice.noticeRef.organization missing, which are obliged when userNotice.noticeRef is used")
                        }
                    }

                })
            }
        })
    }

    @Override
    void addExtension(X509v3CertificateWrapper cert) {

    }

}
