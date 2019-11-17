package ty.henry.cinemaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ty.henry.cinemaapp.dto.MovieForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.error.EntityNotExistException;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.persistence.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Iterable<Movie> findAllMovies() {
        return movieRepository.findAll();
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

    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }

    private void fillMovieWithFormData(Movie movie, MovieForm movieForm) {
        movie.setTitle(movieForm.getTitle());
        movie.setProductionYear(movieForm.getProductionYear());
        movie.setLengthMinutes(movieForm.getLengthMinutes());
        movie.setGenre(movieForm.getGenre());
        movie.setDirector(movieForm.getDirector());
        movie.setAllowedFromAge(movieForm.getAllowedFromAge());
    }
}
