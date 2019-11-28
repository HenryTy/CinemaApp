package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ty.henry.cinemaapp.dto.MovieForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.logic.ShowingDateClassifier;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.service.MovieService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public String showMovies(Model model, @RequestParam(defaultValue = "") String search) {
        Iterable<Movie> movies = movieService.findMovies(search);
        model.addAttribute("movies", movies);
        model.addAttribute("search", search);
        return "movies";
    }

    @PostMapping("/search-movies")
    public String searchMovies(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String search = request.getParameter("search");
        redirectAttributes.addAttribute("search", search);
        return "redirect:/movies";
    }

    @GetMapping("/movie/{id}")
    public String showMoviePage(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findMovieById(id);
        ShowingDateClassifier classifier = new ShowingDateClassifier(movie.getShowings());
        model.addAttribute("movie", movie);
        model.addAttribute("classifier", classifier);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        model.addAttribute("dateFormatter", formatter);
        return "movie";
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
        return "redirect:/movie/{id}";
    }

    @DeleteMapping("/delete-movie/{id}")
    public String deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}
