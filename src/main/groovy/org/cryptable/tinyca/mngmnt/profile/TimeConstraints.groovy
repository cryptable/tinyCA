package org.cryptable.tinyca.mngmnt.profile

import groovy.json.JsonBuilder

/**
 * TimeConstraints for the certificate generation
 */

class TimeConstraints implements IProfile {

    TimeConstraintsChoice timeConstraintsChoice

    Date beginTimestamp

    Date endTimestamp

    Integer periodYears

    Integer periodMonths

    Integer periodWeeks

    Integer periodDays

    Integer periodHours

    Integer periodMinutes

    Integer periodSeconds

    String encode() {
        validate()

        JsonBuilder builder = new JsonBuilder()
        switch (timeConstraintsChoice) {
            case TimeConstraintsChoice.fixedBeginEndDateTime:
            case TimeConstraintsChoice.minMaxDateTime:
                builder([
                        timeConstraintsChoice: timeConstraintsChoice.value(),
                        beginTimestamp: beginTimestamp.format("yyyy/MM/dd HH:mm:ss Z"),
                        endTimestamp: endTimestamp.format("yyyy/MM/dd HH:mm:ss Z"),
                ])
                break;
            case TimeConstraintsChoice.fixedDateTimePeriod:
                Map mapTimeConstraint = [ timeConstraintsChoice: timeConstraintsChoice.value() ]
                if (periodYears) mapTimeConstraint["periodYears"] = periodYears
                if (periodMonths) mapTimeConstraint["periodMonths"] = periodMonths
                if (periodWeeks) mapTimeConstraint["periodWeeks"] = periodWeeks
                if (periodDays) mapTimeConstraint["periodDays"] = periodDays
                if (periodHours) mapTimeConstraint["periodHours"] = periodHours
                if (periodMinutes) mapTimeConstraint["periodMinutes"] = periodMinutes
                if (periodSeconds) mapTimeConstraint["periodSeconds"] = periodSeconds
                builder(mapTimeConstraint)
                break;
            case TimeConstraintsChoice.fixedDatePeriodFixedTime:
                Map mapTimeConstraint = [ timeConstraintsChoice: timeConstraintsChoice.value() ]
                if (periodYears) mapTimeConstraint["periodYears"] = periodYears
                if (periodMonths) mapTimeConstraint["periodMonths"] = periodMonths
                if (periodWeeks) mapTimeConstraint["periodWeeks"] = periodWeeks
                if (periodDays) mapTimeConstraint["periodDays"] = periodDays
                mapTimeConstraint["beginTimestamp"] = beginTimestamp.format("HH:mm:ss Z")
                mapTimeConstraint["endTimestamp"] = endTimestamp.format("HH:mm:ss Z")
                builder(mapTimeConstraint)
                break;
        }
        builder.toString()
    }

    void decode(Map timeConstraints) {
        timeConstraintsChoice = timeConstraints.timeConstraintsChoice
        switch (timeConstraintsChoice) {
            case TimeConstraintsChoice.fixedBeginEndDateTime:
            case TimeConstraintsChoice.minMaxDateTime:
                if ((timeConstraints.beginTimestamp == null) || (timeConstraints.endTimestamp == null)) {
                    throw new ProfileException("The timeConstraints.endTimestamp or beginTimestamp can't be null")
                }
                beginTimestamp = Date.parse("yyyy/MM/dd HH:mm:ss Z", (String)timeConstraints.beginTimestamp)
                endTimestamp = Date.parse("yyyy/MM/dd HH:mm:ss Z", (String)timeConstraints.endTimestamp)
                break
            case TimeConstraintsChoice.fixedDateTimePeriod:
                periodYears = timeConstraints.periodYears ?: null
                periodMonths = timeConstraints.periodMonths ?: null
                periodWeeks = timeConstraints.periodWeeks ?: null
                periodDays = timeConstraints.periodDays ?: null
                periodHours = timeConstraints.periodHours ?: null
                periodMinutes = timeConstraints.periodMinutes ?: null
                periodSeconds = timeConstraints.periodSeconds ?: null
                break
            case TimeConstraintsChoice.fixedDatePeriodFixedTime:
                periodYears = timeConstraints.periodYears ?: null
                periodMonths = timeConstraints.periodMonths ?: null
                periodWeeks = timeConstraints.periodWeeks ?: null
                periodDays = timeConstraints.periodDays ?: null
                beginTimestamp = Date.parse("HH:mm:ss Z", (String)timeConstraints.beginTimestamp)
                endTimestamp = Date.parse("HH:mm:ss Z", (String)timeConstraints.endTimestamp)
                break
        }
        validate()
    }

    /**
     * Validate the time constraints
     *
     * @param timeConstraints is configuration settings of the time constraints
     */
    void validate() {
        switch (timeConstraintsChoice) {
            case TimeConstraintsChoice.fixedBeginEndDateTime:
            case TimeConstraintsChoice.minMaxDateTime:
                if ((beginTimestamp == null) || (endTimestamp == null)) {
                    throw new ProfileException("The timeConstraints.endTimestamp or beginTimestamp can't be null")
                }
                if (endTimestamp <= beginTimestamp) {
                    throw new ProfileException("The timeConstraints.endTimestamp can't be before the beginTimestamp")
                }
                break;
            case TimeConstraintsChoice.fixedDateTimePeriod:
                Integer period = periodYears ?: 0
                period += periodMonths ?: 0
                period += periodWeeks ?: 0
                period += periodDays ?: 0
                period += periodHours ?: 0
                period += periodMinutes ?: 0
                period += periodSeconds ?: 0
                if (period == 0) {
                    throw new ProfileException("The timeConstraints.periodXXXXX has added up to 0, so no period defined")
                }
                break;
            case TimeConstraintsChoice.fixedDatePeriodFixedTime:
                Integer period = periodYears ?: 0
                period += periodMonths ?: 0
                period += periodWeeks ?: 0
                period += periodDays ?: 0
                if (period == 0) {
                    throw new ProfileException("The timeConstraints.periodXXXXX has added up to 0, so no period was defined with a fixed time")
                }
        }
    }

}
