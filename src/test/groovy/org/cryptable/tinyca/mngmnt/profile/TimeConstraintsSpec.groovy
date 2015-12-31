package org.cryptable.tinyca.mngmnt.profile

import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import spock.lang.Specification

/**
 * Test of the TimeConstraints
 * Created by davidtillemans on 27/12/15.
 */
class TimeConstraintsSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Test a valid TimeConstraint encode with vaild fixed begin date and end date"() {
        given: "valid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedBeginEndDateTime,
                beginTimestamp: startDate,
                endTimestamp: endDate
        )

        when: "TimeConstraint settings encode to JSON"
        String json = timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        assert json == '{"timeConstraintsChoice":"FixedBeginEndDateTime",' +
                '"beginTimestamp":"2014/01/01 00:00:00 +0100",' +
                '"endTimestamp":"2024/01/01 00:00:00 +0100"}'
    }

    void "Test a valid TimeConstraint encode with valid min begin date and max end date"() {
        given: "valid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.minMaxDateTime,
                beginTimestamp: startDate,
                endTimestamp: endDate
        )

        when: "TimeConstraint settings encode to JSON"
        String json = timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        assert json == '{"timeConstraintsChoice":"MinMaxDateTime",' +
                '"beginTimestamp":"2014/01/01 00:00:00 +0100",' +
                '"endTimestamp":"2024/01/01 00:00:00 +0100"}'
    }

    void "Test a valid TimeConstraint encode with valid period "(String kindOfPeriod, Integer value) {
        given: "valid TimeConstraint settings"
        Map args = ["timeConstraintsChoice":TimeConstraintsChoice.fixedDateTimePeriod ,
                    "${kindOfPeriod}": value]
        TimeConstraints timeConstraints = new TimeConstraints(args)

        when: "TimeConstraint settings encode to JSON"
        String json = timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        assert json == '{"timeConstraintsChoice":"FixedDateTimePeriod",' +
                '"' + kindOfPeriod + '":' + value + '}'

        where:
        kindOfPeriod    | value
        "periodYears"   |   1
        "periodMonths"  |   2
        "periodWeeks"   |   3
        "periodDays"    |   4
        "periodHours"   |   5
        "periodMinutes" |   6
        "periodSeconds" |   7
    }

    void "Test a valid TimeConstraint encode with valid period and begin- and endtime"(String kindOfPeriod, Integer value) {
        given: "valid TimeConstraint settings"
        def startDate = new Date().copyWith(hourOfDay: 0,
                minute: 0,
                second: 0)
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 2.hours + 10.minutes
        }
        Map args = ["timeConstraintsChoice":TimeConstraintsChoice.fixedDatePeriodFixedTime ,
                    "${kindOfPeriod}": value,
                    "beginTimestamp": startDate,
                    "endTimestamp": endDate]
        TimeConstraints timeConstraints = new TimeConstraints(args)

        when: "TimeConstraint settings encode to JSON"
        String json = timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        assert json == '{"timeConstraintsChoice":"FixedDatePeriodFixedTime",' +
                '"' + kindOfPeriod + '":' + value + ',' +
                '"beginTimestamp":"00:00:00 +0100",' +
                '"endTimestamp":"02:10:00 +0100"}'

        where:
        kindOfPeriod    | value
        "periodYears"   |   1
        "periodMonths"  |   2
        "periodWeeks"   |   3
        "periodDays"    |   4
    }

    void "Test a invalid TimeConstraint fixed begin date and end date, missing begin date"() {
        given: "invalid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedBeginEndDateTime,
                endTimestamp: endDate
        )

        when: "TimeConstraint settings encode to JSON"
        timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.endTimestamp or beginTimestamp can't be null"
    }

    void "Test a invalid TimeConstraint min max begin date and end date, missing end date"() {
        given: "invalid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedBeginEndDateTime,
                beginTimestamp: startDate
        )

        when: "TimeConstraint settings encode to JSON"
        timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.endTimestamp or beginTimestamp can't be null"
    }

    void "Test a invalid TimeConstraint fixed begin date and end date, end date before begin date"() {
        given: "invalid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedBeginEndDateTime,
                beginTimestamp: endDate,
                endTimestamp: startDate
        )

        when: "TimeConstraint settings encode to JSON"
        timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.endTimestamp can't be before the beginTimestamp"
    }

    void "Test a invalid TimeConstraint min/max begin date and end date, end date equal begin date"() {
        given: "invalid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedBeginEndDateTime,
                beginTimestamp: startDate,
                endTimestamp: startDate
        )

        when: "TimeConstraint settings encode to JSON"
        timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.endTimestamp can't be before the beginTimestamp"
    }

    void "Test a invalid TimeConstraint fixed period begin date and end date, no periods"() {
        given: "invalid TimeConstraint settings"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z", "2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedDateTimePeriod,
                beginTimestamp: startDate,
                endTimestamp: startDate
        )

        when: "TimeConstraint settings encode to JSON"
        timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.periodXXXXX has added up to 0, so no period defined"
    }

    void "Test a invalid TimeConstraint fixed period with begin date and end date, no periods"() {
        given: "invalid TimeConstraint settings"
        def startDate = new Date().copyWith(hourOfDay: 0,
                minute: 0,
                second: 0)
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 2.hours + 10.minutes
        }
        TimeConstraints timeConstraints = new TimeConstraints(
                timeConstraintsChoice: TimeConstraintsChoice.fixedDatePeriodFixedTime,
                beginTimestamp: startDate,
                endTimestamp: startDate
        )

        when: "TimeConstraint settings encode to JSON"
        timeConstraints.encode()

        then: "No exception is thrown and valid JSON string"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.periodXXXXX has added up to 0, so no period was defined with a fixed time"
    }

    void "Test a valid TimeConstraint decode with begin date and end date"() {
        given: "valid TimeConstraint JSON"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z","2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.fixedBeginEndDateTime + '",' +
                        '"beginTimestamp":"' + startDate.format("yyyy/MM/dd HH:mm:ss Z") + '",' +
                        '"endTimestamp":"' + endDate.format("yyyy/MM/dd HH:mm:ss Z") + '"}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "No exception is thrown and valid values object"
        assert timeConstraints.timeConstraintsChoice == TimeConstraintsChoice.fixedBeginEndDateTime
        assert timeConstraints.beginTimestamp == startDate
        assert timeConstraints.endTimestamp == endDate
    }

    void "Test a valid TimeConstraint decode with min and max date"() {
        given: "valid TimeConstraint JSON"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z","2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.minMaxDateTime + '",' +
                '"beginTimestamp":"' + startDate.format("yyyy/MM/dd HH:mm:ss Z") + '",' +
                '"endTimestamp":"' + endDate.format("yyyy/MM/dd HH:mm:ss Z") + '"}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "No exception is thrown and valid values object"
        assert timeConstraints.timeConstraintsChoice == TimeConstraintsChoice.minMaxDateTime
        assert timeConstraints.beginTimestamp == startDate
        assert timeConstraints.endTimestamp == endDate
    }

    void "Test a valid TimeConstraint decode with fixed period"(String kindOfPeriod, Integer period) {
        given: "valid TimeConstraint JSON"
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.fixedDateTimePeriod + '",' +
                '"' + kindOfPeriod + '":' + period + '}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "No exception is thrown and valid values object"
        assert timeConstraints.timeConstraintsChoice == TimeConstraintsChoice.fixedDateTimePeriod
        assert timeConstraints["${kindOfPeriod}"] == period

        where:
        kindOfPeriod    | period
        "periodYears"   |   1
        "periodMonths"  |   2
        "periodWeeks"   |   3
        "periodDays"    |   4
        "periodHours"   |   5
        "periodMinutes" |   6
        "periodSeconds" |   7
    }

    void "Test a valid TimeConstraint decode with fixed period with begin and endtime"(String kindOfPeriod, Integer period) {
        given: "valid TimeConstraint JSON"
        def startDate = Date.parse("HH:mm:ss Z","00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 2.hours + 10.minutes
        }
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.fixedDatePeriodFixedTime + '",' +
                '"beginTimestamp":"' + startDate.format("HH:mm:ss Z") + '",' +
                '"endTimestamp":"' + endDate.format("HH:mm:ss Z") + '",' +
                '"' + kindOfPeriod + '":' + period + '}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "No exception is thrown and valid values object"
        assert timeConstraints.timeConstraintsChoice == TimeConstraintsChoice.fixedDatePeriodFixedTime
        assert timeConstraints["${kindOfPeriod}"] == period
        assert timeConstraints.beginTimestamp == startDate
        assert timeConstraints.endTimestamp == endDate

        where:
        kindOfPeriod    | period
        "periodYears"   |   1
        "periodMonths"  |   2
        "periodWeeks"   |   3
        "periodDays"    |   4
    }

    void "Test a invalid TimeConstraint decode with begin date and end date, missing begin date"() {
        given: "invalid TimeConstraint JSON"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z","2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.fixedBeginEndDateTime + '",' +
                '"endTimestamp":"' + endDate.format("yyyy/MM/dd HH:mm:ss Z") + '"}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "ProfileException is thrown"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.endTimestamp or beginTimestamp can't be null"
    }

    void "Test a invalid TimeConstraint decode with min and max date, missing endDate"() {
        given: "valid TimeConstraint JSON"
        def startDate = Date.parse("yyyy/MM/dd HH:mm:ss Z","2014/01/01 00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 10.years
        }
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.minMaxDateTime + '",' +
                '"beginTimestamp":"' + startDate.format("yyyy/MM/dd HH:mm:ss Z") + '"}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "ProfileException is thrown"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.endTimestamp or beginTimestamp can't be null"
    }

    void "Test a invalid TimeConstraint decode with fixed period added to 0"() {
        given: "valid TimeConstraint JSON"
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.fixedDateTimePeriod + '",' +
                '"periodYears":0,' +
                '"periodMonths":0,' +
                '"periodWeeks":0,' +
                '"periodDays":0,' +
                '"periodHours":0,' +
                '"periodMinutes":0,' +
                '"periodSeconds":0}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "ProfileException is thrown"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.periodXXXXX has added up to 0, so no period defined"
    }

    void "Test a invalid TimeConstraint decode with fixed period and times, period added to 0"() {
        given: "valid TimeConstraint JSON"
        def startDate = Date.parse("HH:mm:ss Z","00:00:00 +0100")
        Date endDate = null
        use(TimeCategory) {
            endDate = startDate + 2.hours + 10.minutes
        }
        def jsonValue = new JsonSlurper().parseText('{"timeConstraintsChoice":"' + TimeConstraintsChoice.fixedDatePeriodFixedTime + '",' +
                '"beginTimestamp":"' + startDate.format("HH:mm:ss Z") + '",' +
                '"endTimestamp":"' + endDate.format("HH:mm:ss Z") + '",' +
                '"periodYears":0,' +
                '"periodMonths":0,' +
                '"periodWeeks":0,' +
                '"periodDays":0}')

        when: "TimeConstraint settings decode"
        TimeConstraints timeConstraints = new TimeConstraints()
        timeConstraints.decode((Map)jsonValue)

        then: "ProfileException is thrown"
        ProfileException e = thrown()
        assert e.message == "The timeConstraints.periodXXXXX has added up to 0, so no period was defined with a fixed time"
    }

}
