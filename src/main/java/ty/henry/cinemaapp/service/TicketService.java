package ty.henry.cinemaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.persistence.ShowingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private ShowingRepository showingRepository;

    public List<Showing> findShowingsForMovieAndDate(Movie movie, LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59);
        return showingRepository.findAllByMovieAndShowingDateBetween(movie, from, to, Sort.by("showingDate"));
    }

    public List<Showing> findFutureShowingsForMovieAndDate(Movie movie, LocalDate date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59);

        if(now.isAfter(from)) {
            from = now;
        }
        return showingRepository.findAllByMovieAndShowingDateBetween(movie, from, to, Sort.by("showingDate"));
    }

    public Showing findShowingById(Long id) {
        return showingRepository.findById(id).orElseThrow(EntityNotExistException::new);
    }
}
