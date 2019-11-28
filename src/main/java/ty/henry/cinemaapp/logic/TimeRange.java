package ty.henry.cinemaapp.logic;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeRange {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeRange(LocalDateTime startTime, int lengthMinutes) {
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(lengthMinutes);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getLengthMinutes() {
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }
}
