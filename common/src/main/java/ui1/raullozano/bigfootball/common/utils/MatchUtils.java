package ui1.raullozano.bigfootball.common.utils;

public class MatchUtils {

    public static int minuteOf(String minute) {
        if(minute.contains("+")) {
            return Integer.parseInt(minute.split("\\+")[0]) + Integer.parseInt(minute.split("\\+")[1]);
        } else {
            return Integer.parseInt(minute);
        }
    }
}
