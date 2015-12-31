package org.cryptable.tinyca.mngmnt.profile

/**
 * Interface to be implemented for the profile
 *
 * Created by davidtillemans on 30/12/15.
 */
interface IProfile {

    /**
     * implement the enconding to JSON value from profile parameters
     */
    String encode()

    /**
     * implement the decoding of the JSON value to profile parameters
     */
    void decode(Map mapOptions)

    /**
     * The validation of the settings must be implemented
     *
     * @return
     */
    void validate()

}