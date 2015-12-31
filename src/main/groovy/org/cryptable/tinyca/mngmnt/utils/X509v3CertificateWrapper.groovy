package org.cryptable.tinyca.mngmnt.utils

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.X509ExtensionUtils
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils

/**
 * Wrapper class to be used by the profile settings implementation, because at subjectKeIdentifier I need access
 * to subject public key and at the issuerkeyIdentifier I need access to the ca PublicKey Identifier
 */
class X509v3CertificateWrapper {

    /**
     * Bouncycastle x509ExtensionUtils to create extension
     */
    static final X509ExtensionUtils x509ExtensionUtils = new JcaX509ExtensionUtils()

    /**
     * Certificate to de generated
     */
    final X509v3CertificateBuilder x509v3CertificateBuilder

    /**
     * public key of the generated certificate
     */
    final SubjectPublicKeyInfo subjectPublicKeyInfo

    /**
     * CA certificate which signs the certificate to be generated
     */
    final X509CertificateHolder caCertificate

    /**
     * Constructor based on the X509v3CertificateBuilder.
     *
     * @param caCertificate CA X509CertificateHolder
     * @param serial serial number for the certificate
     * @param notBefore not before date
     * @param notAfter not after date
     * @param dateLocale localization used for the date
     * @param subject subject name for the user
     * @param publicKeyInfo public Key Info of the subject
     */
    X509v3CertificateWrapper(X509CertificateHolder caCertificate, BigInteger serial, Date notBefore, Date notAfter, Locale dateLocale, X500Name subject, SubjectPublicKeyInfo publicKeyInfo) {

         this.x509v3CertificateBuilder = new X509v3CertificateBuilder(caCertificate.getSubject(),
                serial,
                notBefore,
                notAfter,
                dateLocale,
                subject,
                publicKeyInfo)

        this.subjectPublicKeyInfo = publicKeyInfo

        this.caCertificate = caCertificate
    }
}
