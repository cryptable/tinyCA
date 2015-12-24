package org.cryptable.tinyca

/**
 * Profile model which contains a signed profile definition in a signed JSON datamodel
 */
class Profile {

    /**
     * name of the profile
     */
    String name

    /**
     * Creation or modification date of the profile, latest profile is the current profile and only used profile.
     * So a profile can be changed without losing the link of a previous profile.
     */
    Date creationDate

    /**
     * Signed JSON representation of the profiles. The profiles are signed using a SelfSigned Integrity Key of tinyCA.
     */
    String protectedProfileStream

    static constraints = {
        name nullable: false
        creationDate nullable: false
        protectedProfileStream nullable: false
    }

    static hasMany = [ certificates: Certificate ]
}