package org.cryptable.tinyca.mngmnt.profile.extensions

import org.cryptable.tinyca.mngmnt.utils.X509v3CertificateWrapper

/**
 * Abstract class for the extensions
 *
 * Created by davidtillemans on 27/12/15.
 */
abstract class Extension {

    boolean critical

    abstract void addExtension(X509v3CertificateWrapper cert)

}
