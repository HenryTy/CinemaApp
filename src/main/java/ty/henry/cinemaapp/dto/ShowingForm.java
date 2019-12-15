package ty.henry.cinemaapp.dto;

import org.springframework.format.annotation.DateTimeFormat;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Movie;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowingForm {

    public interface DateValidation {

    }

    public interface TimeValidation {

    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Podaj datę z przyszłości", groups = {DateValidation.class})
    @NotNull(message = "Niepoprawna data", groups = {DateValidation.class})
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "Niepoprawna godzina", groups = {TimeValidation.class})
    private LocalTime time;

    private Movie movie;
    private Hall hall;
    private String hallName;

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
