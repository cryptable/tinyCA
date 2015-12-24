package org.cryptable.tinyca

/**
 * The Revocation model
 */
class Revocation {

    /**
     * The existing revocation reasons
     */
    public enum Reason {
        unspecified(0),
        keyCompromise(1),
        CACompromise(2),
        affiliationChanged(3),
        superseded(4),
        cessationOfOperation(5),
        certificateHold(6),
        removeFromCRL(8),
        privilegeWithdrawn(9),
        aACompromise(10)

        private final int value;

        Reason(int value) {
            this.value = value
        }

        int value() { return this.value }
    }

    /**
     * Date of the revocation
     */
    Date date

    /**
     * Date of the suspected invalidity
     */
    Date invalidityDate

    /**
     * Reason of the revocation
     */
    Reason reason

    static constraints = {
        date nullable: false
        invalidityDate nullable: false
        reason nullable: false
        certificate nullable: false
    }

    static belongsTo = [certificate : Certificate]
}
