package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ty.henry.cinemaapp.dto.MovieForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.service.MovieService;

import javax.validation.Valid;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public String showMovies(Model model) {
        Iterable<Movie> movies = movieService.findAllMovies();
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/add-movie")
    public String showAddMoviePage(MovieForm movieForm) {
        return "addMovie";
    }

    @PostMapping("/add-movie")
    public String addMovie(@Valid MovieForm movieForm, BindingResult result) {
        if(!result.hasErrors()) {
            try {
                movieService.addNewMovie(movieForm);
            } catch (EntityAlreadyExistsException ex) {
                result.rejectValue("title", "message.movieExistsError");
            }
        }
        if(result.hasErrors()) {
            return "addMovie";
        }
        return "redirect:/movies";
    }

    @GetMapping("/edit-movie/{id}")
    public String showEditMoviePage(MovieForm movieForm, @PathVariable Integer id) {
        Movie movie = movieService.findMovieById(id);
        movieForm.setTitle(movie.getTitle());
        movieForm.setProductionYear(movie.getProductionYear());
        movieForm.setLengthMinutes(movie.getLengthMinutes());
        movieForm.setGenre(movie.getGenre());
        movieForm.setDirector(movie.getDirector());
        movieForm.setAllowedFromAge(movie.getAllowedFromAge());
        return "editMovie";
    }

    @PostMapping("/edit-movie/{id}")
    public String editMovie(@Valid MovieForm movieForm, BindingResult result, @PathVariable Integer id) {
        if(!result.hasErrors()) {
            try {
                movieService.editMovie(id, movieForm);
            } catch (EntityAlreadyExistsException ex) {
                result.rejectValue("title", "message.movieExistsError");
            }
        }
        if(result.hasErrors()) {
            return "editMovie";
        }
        return "redirect:/movies";
    }

    @DeleteMapping("/movie/{id}")
    public String deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}
