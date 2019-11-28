package ty.henry.cinemaapp.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.persistence.HallRepository;
import ty.henry.cinemaapp.persistence.ShowingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ShowingTimeChecker {

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private ShowingRepository showingRepository;

    public Map<Hall, List<TimeRange>> getPossibleShowingStartTimeRanges(LocalDate date, Movie movie) {
        Iterable<Hall> halls = hallRepository.findAll();
        Map<Hall, List<TimeRange>> hallToRanges = new TreeMap<>();
        for(Hall hall : halls) {
            List<TimeRange> timeRanges = getStartTimeRangesForHall(hall, date, movie);
            if(timeRanges.size() > 0) {
                hallToRanges.put(hall, timeRanges);
            }
        }
        return hallToRanges;
    }

    private List<TimeRange> getStartTimeRangesForHall(Hall hall, LocalDate date, Movie movie) {
        LocalDateTime timeLimit = date.plusDays(1).atStartOfDay().plusMinutes(movie.getLengthMinutes() - 1);
        List<Showing> showingsOnDate = showingRepository.findAllByHallAndShowingDateBetween(hall,
                date.atStartOfDay(), timeLimit, Sort.by("showingDate"));

        List<TimeRange> timeRanges = new ArrayList<>();
        LocalDateTime currentStart = date.atStartOfDay();

        for(Showing showing : showingsOnDate) {
            TimeRange beforeShowingRange = new TimeRange(currentStart, showing.getShowingDate());
            tryToFitMovieInRange(beforeShowingRange, timeRanges, movie);

            TimeRange showingRange = new TimeRange(beforeShowingRange.getEndTime(), showing.getMovie().getLengthMinutes());
            currentStart = showingRange.getEndTime();
        }

        TimeRange lastRange = new TimeRange(currentStart, timeLimit);
        tryToFitMovieInRange(lastRange, timeRanges, movie);

        return timeRanges;
    }

    private void tryToFitMovieInRange(TimeRange range, List<TimeRange> timeRanges, Movie movie) {
        if(range.getLengthMinutes() >= movie.getLengthMinutes()) {
            LocalDateTime resultRangeEnd = range.getEndTime().minusMinutes(movie.getLengthMinutes());
            TimeRange resultRange = new TimeRange(range.getStartTime(), resultRangeEnd);
            timeRanges.add(resultRange);
        }
    }
}
