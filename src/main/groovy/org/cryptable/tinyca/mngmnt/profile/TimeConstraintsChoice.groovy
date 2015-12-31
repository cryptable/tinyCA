package org.cryptable.tinyca.mngmnt.profile

/**
 * Profile in JSOM format are just enums which have to be used through a correct way to
 * build up the profile in a JSON representation, see tests
 */
enum TimeConstraintsChoice {

    /**
     * Define a minimum and maximum date and time constraint
     */
    minMaxDateTime("MinMaxDateTime"),

    /**
     * Define a fixed period, which can be Years, Months, Weeks, Hours, Minutes, Seconds
     */
            fixedDateTimePeriod("FixedDateTimePeriod"),

    /**
     * Define a fixed begin and end date and time
     */
            fixedBeginEndDateTime("FixedBeginEndDateTime"),

    /**
     * Define a fixed period with a fixed begin and endtime
     */
            fixedDatePeriodFixedTime("FixedDatePeriodFixedTime")

    private final String value;

    TimeConstraintsChoice(String value) {
        this.value = value
    }

    String value() { return this.value }
}
