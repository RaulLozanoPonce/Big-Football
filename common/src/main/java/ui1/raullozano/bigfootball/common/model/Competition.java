package ui1.raullozano.bigfootball.common.model;

import ui1.raullozano.bigfootball.common.utils.Time;

import java.time.Instant;

import static ui1.raullozano.bigfootball.common.utils.Time.monthDayOf;
import static ui1.raullozano.bigfootball.common.utils.Time.monthOf;

public class Competition {

    private final String id;
    private final String startSeason;
    private final String folderName;

    public Competition(String id, String startSeason, String folderName) {
        this.id = id;
        this.startSeason = startSeason;
        this.folderName = folderName;
    }

    public String id() {
        return id;
    }

    public String folderName() {
        return folderName;
    }

    public int yearOf(Instant instant) {
        int year = Time.yearOf(instant);
        int month = monthOf(instant);

        int monthStart = Integer.parseInt(startSeason.split("-")[1]);

        if(month < monthStart) {
            return year - 1;
        } else if(month > monthStart) {
            return year;
        } else {
            int day = monthDayOf(instant);
            int dayStart = Integer.parseInt(startSeason.split("-")[1]);
            if(day < dayStart) {
                return year - 1;
            } else {
                return year;
            }
        }
    }
}
