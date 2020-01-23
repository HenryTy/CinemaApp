package ty.henry.cinemaapp.service;

import oracle.jdbc.OracleDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.dto.MovieForm;
import ty.henry.cinemaapp.dto.ShowingForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.MovieGenre;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.persistence.MovieRepository;
import ty.henry.cinemaapp.persistence.ShowingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowingRepository showingRepository;

    public Iterable<Movie> findMovies(String search) {
        if(search.equals("")) {
            return movieRepository.findAll();
        }
        else {
            return movieRepository.findAllByTitleContainsIgnoreCase(search);
        }
    }

    public Movie findMovieById(Integer id) throws EntityNotExistException {
        return movieRepository.findById(id).orElseThrow(EntityNotExistException::new);
    }

    public Movie addNewMovie(MovieForm movieForm) throws EntityAlreadyExistsException {
        if(movieRepository.existsByTitleAndProductionYear(movieForm.getTitle(),
                movieForm.getProductionYear())) {
            throw new EntityAlreadyExistsException();
        }
        Movie movie = new Movie();
        fillMovieWithFormData(movie, movieForm);
        return movieRepository.save(movie);
    }

    public Movie editMovie(Integer id, MovieForm movieForm) throws EntityNotExistException,
            EntityAlreadyExistsException {
        Movie movie = movieRepository.findById(id).orElseThrow(EntityNotExistException::new);
        if(movieRepository.existsByTitleAndProductionYearAndIdNot(movieForm.getTitle(),
                movieForm.getProductionYear(), id)) {
            throw new EntityAlreadyExistsException();
        }
        fillMovieWithFormData(movie, movieForm);
        return movieRepository.save(movie);
    }

    public boolean deleteMovie(Integer id) {
        try {
            movieRepository.deleteById(id);
        } catch (Exception ex) {
            Throwable rootCause = ex;
            while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
                rootCause = rootCause.getCause();
            }
            if(rootCause instanceof OracleDatabaseException &&
                    ((OracleDatabaseException) rootCause).getOracleErrorNumber() == 20000) {
                return false;
            }
            else {
                throw ex;
            }
        }
        return true;
    }

    public void addShowing(ShowingForm showingForm) {
        Movie movie = showingForm.getMovie();
        Hall hall = showingForm.getHall();
        LocalDate date = showingForm.getDate();
        LocalTime time = showingForm.getTime();
        Showing showing = new Showing(movie, hall, date.atTime(time));
        showingRepository.save(showing);
    }

    public List<Showing> findAllShowingsForMovie(Movie movie) {
        return showingRepository.findAllByMovie(movie, Sort.by("showingDate"));
    }

    public List<Showing> findFutureShowingsForMovie(Movie movie) {
        return showingRepository.findAllByMovieAndShowingDateAfter(movie, LocalDateTime.now(), Sort.by("showingDate"));
    }

    public List<Showing> findAllFutureShowings() {
        return showingRepository.findAllByShowingDateAfter(LocalDateTime.now(), Sort.by("showingDate"));
    }

    public boolean hasMovieFutureShowings(Movie movie) {
        return findFutureShowingsForMovie(movie).size() > 0;
    }

    private void fillMovieWithFormData(Movie movie, MovieForm movieForm) {
        movie.setTitle(movieForm.getTitle());
        movie.setProductionYear(movieForm.getProductionYear());
        movie.setLengthMinutes(movieForm.getLengthMinutes());
        movie.setGenre(MovieGenre.valueOf(movieForm.getGenre()));
        movie.setDirector(movieForm.getDirector());
        movie.setAllowedFromAge(movieForm.getAllowedFromAge());
    }
}
