package ui1.raullozano.bigfootball.common.utils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

public class Time {

    private static final Map<Integer, String> LongWeekDay = new HashMap<>() {{
        put(1, "lunes");
        put(2, "martes");
        put(3, "miércoles");
        put(4, "jueves");
        put(5, "viernes");
        put(6, "sábado");
        put(7, "domingo");
    }};

    private static final Map<Integer, String> ShortWeekDay = new HashMap<>() {{
        put(1, "L");
        put(2, "M");
        put(3, "X");
        put(4, "J");
        put(5, "V");
        put(6, "S");
        put(7, "D");
    }};

    private static final Map<Integer, String> LongMonthMap = new HashMap<>() {{
        put(1, "enero");
        put(2, "febrero");
        put(3, "marzo");
        put(4, "abril");
        put(5, "mayo");
        put(6, "junio");
        put(7, "julio");
        put(8, "agosto");
        put(9, "septiembre");
        put(10, "octubre");
        put(11, "noviembre");
        put(12, "diciembre");
    }};

    /* INSTANT OF ----------------------------------------------------------------------------------------------------*/

    public static Instant currentInstant() {
        return getInstantOf(getCanaryZonedDateTimeOf());
    }

    public static Instant getInstantOf(int year, int month, int day, int hour, int minute, int second) {
        return getInstantOf(getLocalDateTimeOf(year, month, day, hour, minute, second));
    }

    public static Instant getInstantOf(int year, int month, int day, int hour, int minute) {
        return getInstantOf(year, month, day, hour, minute, 0);
    }

    public static Instant getInstantOf(int year, int month, int day, int hour) {
        return getInstantOf(year, month, day, hour, 0);
    }

    public static Instant getInstantOf(int year, int month, int day) {
        return getInstantOf(year, month, day, 0);
    }

    public static Instant getInstantOf(int year, int month) {
        return getInstantOf(year, month, 1);
    }

    public static Instant getInstantOfWeeks(int year, int week) {
        if(week <= 0) return null;
        week = yearOf(nextInstant(truncateTo(getInstantOf(year), Scale.W), Scale.D, 3)) == year ? week - 1 : week;
        return nextInstant(truncateTo(getInstantOf(year), Scale.W), Scale.W, week);
    }

    public static Instant getInstantOf(int year) {
        if(year < 0) return null;
        return getInstantOf(year, 1);
    }

    public static Instant getInstantOfZoneToUtc(Instant instant, String zone) {
        int offsetHours = (int) getInstantDiff(instant, getInstantOfUtcToZone(instant, zone), Scale.H);
        return previousInstant(instant, Scale.H, offsetHours);
    }

    public static Instant getInstantOfUtcToZone(Instant instant, String zone) {
        return getInstantOf(instant.atZone(ZoneId.of(zone)));
    }

    /* COMPONENT OF --------------------------------------------------------------------------------------------------*/

    public static int minuteOf(Instant instant) {
        return getLocalDateTimeOf(instant).getMinute();
    }

    public static int hourOf(Instant instant) {
        return getLocalDateTimeOf(instant).getHour();
    }

    public static int weekDayOf(Instant instant) {
        return getLocalDateTimeOf(instant).getDayOfWeek().getValue();
    }

    public static int monthDayOf(Instant instant) {
        return getLocalDateTimeOf(instant).getDayOfMonth();
    }

    public static int yearWeekOf(Instant instant) {
        return getLocalDateTimeOf(instant).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    public static int monthOf(Instant instant) {
        return getLocalDateTimeOf(instant).getMonth().getValue();
    }

    public static int yearOf(Instant instant) {
        return getLocalDateTimeOf(instant).getYear();
    }

    /* TRUNCATE ------------------------------------------------------------------------------------------------------*/

    public static Instant truncateTo(Instant instant, Scale scale) {

        if(scale.equals(Scale.H)) return instant.truncatedTo(ChronoUnit.HOURS);
        if(scale.equals(Scale.D)) return instant.truncatedTo(ChronoUnit.DAYS);
        if(scale.equals(Scale.W)) return truncateToWeek(instant);
        if(scale.equals(Scale.M)) return truncateToMonth(instant);
        if(scale.equals(Scale.Y)) return truncateToYear(instant);
        return instant;
    }

    /* OFFSET INSTANT ------------------------------------------------------------------------------------------------*/

    public static Instant previousInstant(Instant instant, Scale scale) {
        return previousInstant(instant, scale, 1);
    }

    public static Instant previousInstant(Instant instant, Scale scale, int n) {

        if(scale.equals(Scale.H)) return instant.minus(n, ChronoUnit.HOURS);
        if(scale.equals(Scale.D)) return instant.minus(n, ChronoUnit.DAYS);
        if(scale.equals(Scale.W)) return instant.minus(7L * n, ChronoUnit.DAYS);
        if(scale.equals(Scale.M)) return minusMonths(instant, n);
        if(scale.equals(Scale.Y)) return minusYears(instant, n);
        return instant;
    }

    public static Instant nextInstant(Instant instant, Scale scale) {
        return nextInstant(instant, scale, 1);
    }

    public static Instant nextInstant(Instant instant, Scale scale, int n) {

        if(scale.equals(Scale.H)) return instant.plus(n, ChronoUnit.HOURS);
        if(scale.equals(Scale.D)) return instant.plus(n, ChronoUnit.DAYS);
        if(scale.equals(Scale.W)) return instant.plus(7L * n, ChronoUnit.DAYS);
        if(scale.equals(Scale.M)) return plusMonths(instant, n);
        if(scale.equals(Scale.Y)) return plusYears(instant, n);
        return instant;
    }

    /*-------------------------------------------------------------------------------------------------------*/

    public static long getInstantDiff(Instant instant1, Instant instant2, Scale scale) {

        if(instant1 == null || instant2 == null) return 0;

        long diffInSeconds = Math.abs(instant2.getEpochSecond() - instant1.getEpochSecond());
        if(diffInSeconds == 0) return 0;

        switch (scale) {
            case H:
                return (long) (diffInSeconds/60.0f/60.0f);
            case D:
                return (long) (diffInSeconds/60.0f/60.0f/24.0f);
            case W:
                return (long) (diffInSeconds/60.0f/60.0f/24.0f/7.0f);
            case M:
                return (long) (diffInSeconds/60.0f/60.0f/24.0f/30.0f);
            case Y:
                return (long) (diffInSeconds/60.0f/60.0f/24.0f/365.0f);
        }

        return 0;
    }

    public static boolean instantIsInRange(Instant instant, Instant from, Instant to) {
        return !instant.isBefore(from) && instant.isBefore(to);
    }

    public static List<Instant> getInstantsBetween(Instant from, Instant to, Scale scale) {

        List<Instant> instants = new ArrayList<>();

        Instant currentInstant = Instant.ofEpochSecond(truncateTo(from, scale).getEpochSecond());
        while(currentInstant.isBefore(to)) {
            instants.add(currentInstant);
            currentInstant = nextInstant(currentInstant, scale);
        }

        return instants;
    }

    /*-------------------------------------------------------------------------------------------------------*/

    public static String yearStyled(int year) {
        if(year < 0 || year > 9999) return null;
        return timeComponentStyled(year, 4);
    }

    public static String monthStyled(int month) {
        if(month <= 0 || month > 12) return null;
        return timeComponentStyled(month, 2);
    }

    public static String monthLongStyled(int month) {
        return LongMonthMap.get(month);
    }

    public static String dayStyled(int day) {
        if(day <= 0 || day > 31) return null;
        return timeComponentStyled(day, 2);
    }

    public static String longWeekDayMonthStyled(int weekDay) {
        return LongWeekDay.get(weekDay);
    }

    public static String shortWeekDayMonthStyled(int weekDay) {
        return ShortWeekDay.get(weekDay);
    }

    public static String hourStyled(int hour) {
        if(hour < 0 || hour > 23) return null;
        return timeComponentStyled(hour, 2);
    }

    public static String minuteStyled(int minute) {
        if(minute < 0 || minute > 59) return null;
        return timeComponentStyled(minute, 2);
    }

    public static String shortDateStyled(Instant instant) {
        return dayStyled(monthDayOf(instant)) + "/" + monthStyled(monthOf(instant)) + "/" + yearStyled(yearOf(instant)) +
                " a las " + hourStyled(hourOf(instant)) + ":" + minuteStyled(minuteOf(instant));
    }

    public static String longDateStyled(Instant instant) {
        return dayStyled(monthDayOf(instant)) + " de " + monthLongStyled(monthOf(instant)) + " de " + yearStyled(yearOf(instant)) +
                " a las " + hourStyled(hourOf(instant)) + ":" + minuteStyled(minuteOf(instant));
    }

    /*-------------------------------------------------------------------------------------------------------*/

    private static Instant getInstantOf(ZonedDateTime zdt) {
        return getInstantOf(zdt.getYear(), zdt.getMonth().getValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond());
    }

    private static Instant getInstantOf(LocalDateTime localDateTime) {
        if(localDateTime == null) return null;
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    private static ZonedDateTime getCanaryZonedDateTimeOf() {
        return ZonedDateTime.now(ZoneId.of("Atlantic/Canary"));
    }

    private static LocalDateTime getLocalDateTimeOf(Instant instant) {
        return LocalDateTime.of(getYearOf(instant), getMonthOf(instant), getDayOf(instant), getHourOf(instant), getMinuteOf(instant));
    }

    private static LocalDateTime getLocalDateTimeOf(int year, int month, int day, int hour, int minute, int second) {
        try {
            return LocalDateTime.of(year, month, day, hour, minute, second);
        } catch (Exception e) {
            return null;
        }
    }

    private static int getYearOf(Instant instant) {
        return getComponentOf(instant, ChronoUnit.YEARS);
    }

    private static int getMonthOf(Instant instant) {
        return getComponentOf(instant, ChronoUnit.MONTHS);
    }

    private static int getDayOf(Instant instant) {
        return getComponentOf(instant, ChronoUnit.DAYS);
    }

    private static int getHourOf(Instant instant) {
        return getComponentOf(instant, ChronoUnit.HOURS);
    }

    private static int getMinuteOf(Instant instant) {
        return getComponentOf(instant, ChronoUnit.MINUTES);
    }

    private static int getComponentOf(Instant instant, ChronoUnit unit) {

        String ret = "-1";

        if(unit.equals(ChronoUnit.YEARS)) ret = instant.toString().split("[TZ]")[0].split("-")[0];
        if(unit.equals(ChronoUnit.MONTHS)) ret = instant.toString().split("[TZ]")[0].split("-")[1];
        if(unit.equals(ChronoUnit.DAYS)) ret = instant.toString().split("[TZ]")[0].split("-")[2];
        if(unit.equals(ChronoUnit.HOURS)) ret = instant.toString().split("[TZ]")[1].split(":")[0];
        if(unit.equals(ChronoUnit.MINUTES)) ret = instant.toString().split("[TZ]")[1].split(":")[1];

        return Integer.parseInt(ret);
    }

    private static String timeComponentStyled(int component, int nDigits) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nDigits - 1; i++) {
            sb.append('0');
        }
        sb.append(component);

        String timeComponentStyled = sb.toString();
        return timeComponentStyled.substring(timeComponentStyled.length() - nDigits);
    }

    private static Instant truncateToWeek(Instant instant) {

        Instant dayInstant = truncateTo(instant, Scale.D);
        int dayOfWeek = weekDayOf(dayInstant);
        return previousInstant(dayInstant, Scale.D, dayOfWeek - 1);
    }

    private static Instant truncateToMonth(Instant instant) {
        return getInstantOf(yearOf(instant), monthOf(instant));
    }

    private static Instant truncateToYear(Instant instant) {
        return getInstantOf(yearOf(instant));
    }

    private static Instant minusMonths(Instant instant, int nMonth) {
        return offsetMonth(instant, -nMonth);
    }

    private static Instant plusMonths(Instant instant, int nMonth) {
        return offsetMonth(instant, nMonth);
    }

    private static Instant offsetMonth(Instant instant, int nMonth) {
        int day = monthDayOf(instant);
        int month = monthOf(instant) + nMonth;
        int year = yearOf(instant);

        while(month < 1) {
            month += 12;
            year--;
        }

        while(month > 12) {
            month -= 12;
            year++;
        }

        day = adjustedDay(day, month, year);

        return getInstantOf(year, month, day, hourOf(instant), minuteOf(instant));
    }

    private static Instant minusYears(Instant instant, int nYears) {
        return offsetYear(instant, -nYears);
    }

    private static Instant plusYears(Instant instant, int nYears) {
        return offsetYear(instant, nYears);
    }

    private static Instant offsetYear(Instant instant, int nYears) {
        int day = monthDayOf(instant);
        int year = yearOf(instant) + nYears;

        day = adjustedDay(day, monthOf(instant), year);

        return getInstantOf(year, monthOf(instant), day, hourOf(instant), minuteOf(instant));
    }

    private static int adjustedDay(int day, int month, int year) {
        switch (month) {
            case 2:
                return Math.min(day, getFebruaryDaysOf(year));
            case 4:
            case 6:
            case 9:
            case 11:
                return Math.min(day, 30);
            default:
                return day;
        }
    }

    private static int getFebruaryDaysOf(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0) ? 29 : 28;
    }

    public static String singularScale(Scale scale) {

        if(scale == null) return null;
        switch (scale) {
            case H:
                return "Hora";
            case D:
                return "Día";
            case W:
                return "Semana";
            case M:
                return "Mes";
            case Y:
                return "Año";
        }
        return null;
    }

    public static String pluralScale(Scale scale) {

        if(scale == null) return null;
        switch (scale) {
            case H:
                return "Horas";
            case D:
                return "Días";
            case W:
                return "Semanas";
            case M:
                return "Meses";
            case Y:
                return "Años";
        }
        return null;
    }

    /*--------------------------------------------------------------------------------------------*/

    public enum Scale {
        H,
        D,
        W,
        M,
        Y
    }
}