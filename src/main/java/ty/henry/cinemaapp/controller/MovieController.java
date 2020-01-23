package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ty.henry.cinemaapp.dto.MovieForm;
import ty.henry.cinemaapp.dto.RatingForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.logic.ShowingClassifier;
import ty.henry.cinemaapp.logic.ShowingDateClassifier;
import ty.henry.cinemaapp.logic.TwoLevelShowingClassifier;
import ty.henry.cinemaapp.model.*;
import ty.henry.cinemaapp.service.MovieService;
import ty.henry.cinemaapp.service.RatingService;
import ty.henry.cinemaapp.service.TicketService;
import ty.henry.cinemaapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private RatingService ratingService;

    @GetMapping("/movies")
    public String showMovies(Model model, @RequestParam(defaultValue = "") String search) {
        Iterable<Movie> movies = movieService.findMovies(search);
        model.addAttribute("movies", movies);
        model.addAttribute("search", search);

        List<Showing> futureShowings = movieService.findAllFutureShowings();
        TwoLevelShowingClassifier<Movie, LocalDate> movieDateShowingClassifier =
                new TwoLevelShowingClassifier<>(futureShowings, ShowingClassifier.BY_MOVIE_CLASSIFICATION_FUNCTION,
                        ShowingClassifier.BY_DATE_CLASSIFICATION_FUNCTION,
                        null, null);
        model.addAttribute("movieDateShowingClassifier", movieDateShowingClassifier);
        return "movies";
    }

    @PostMapping("/search-movies")
    public String searchMovies(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String search = request.getParameter("search");
        redirectAttributes.addAttribute("search", search);
        return "redirect:/movies";
    }

    @GetMapping("/movie/{id}")
    public String showMoviePage(@PathVariable Integer id, Model model, Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        Movie movie = movieService.findMovieById(id);
        List<Showing> showingsToClassify;

        if(currentUser.getRole() == Role.ROLE_ADMIN) {
            showingsToClassify = movieService.findAllShowingsForMovie(movie);
        }
        else {
            showingsToClassify = movieService.findFutureShowingsForMovie(movie);
            if(ticketService.hasUserTicketForMovie(currentUser, movie)) {
                RatingForm ratingForm = new RatingForm();
                Rating rating = ratingService.findRatingByUserAndMovie(currentUser, movie);
                int ratingValue = rating == null ? 0 : rating.getValue();
                ratingForm.setRatingValue(ratingValue);
                model.addAttribute("ratingForm", ratingForm);
            }
        }
        ShowingDateClassifier classifier = new ShowingDateClassifier(showingsToClassify);
        model.addAttribute("movie", movie);
        model.addAttribute("classifier", classifier);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        model.addAttribute("dateFormatter", formatter);

        int ratingCount = ratingService.getRatingCountForMovie(movie);
        model.addAttribute("ratingCount", ratingCount);
        if(ratingCount > 0) {
            float ratingAvg = ratingService.getAvgRatingForMovie(movie);
            model.addAttribute("ratingAvg", String.format("%.1f", ratingAvg));
        }
        return "movie";
    }

    @GetMapping("/add-movie")
    public String showAddMoviePage(MovieForm movieForm, Model model) {
        model.addAttribute("genres", MovieGenre.values());
        movieForm.setGenre(MovieGenre.ACTION.name());
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
        return "redirect:/movies?added";
    }

    @GetMapping("/edit-movie/{id}")
    public String showEditMoviePage(MovieForm movieForm, @PathVariable Integer id, Model model) {
        Movie movie = movieService.findMovieById(id);
        if(movieService.hasMovieFutureShowings(movie)) {
            return "redirect:/movie/{id}?modificationError";
        }

        model.addAttribute("genres", MovieGenre.values());
        movieForm.setTitle(movie.getTitle());
        movieForm.setProductionYear(movie.getProductionYear());
        movieForm.setLengthMinutes(movie.getLengthMinutes());
        movieForm.setGenre(movie.getGenre().name());
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
        return "redirect:/movie/{id}?updated";
    }

    @DeleteMapping("/delete-movie/{id}")
    public String deleteMovie(@PathVariable Integer id) {
        boolean deleteSuccess = movieService.deleteMovie(id);
        if(deleteSuccess) {
            return "redirect:/movies?deleted";
        } else {
            return "redirect:/movie/{id}?modificationError";
        }
    }

    @PostMapping("/rate-movie/{movieId}")
    public String rateMovie(RatingForm ratingForm, @PathVariable Integer movieId,
                            Principal principal) {
        String redirectToMovie = "redirect:/movie/" + movieId;
        if(ratingForm.getRatingValue() == 0) {
            return redirectToMovie + "?wrongRating=true";
        }
        User currentUser = userService.findUserByEmail(principal.getName());
        Movie movie = movieService.findMovieById(movieId);
        boolean firstRate = ratingService.rateMovie(currentUser, movie, ratingForm);
        if(firstRate) {
            return redirectToMovie + "?rated=true";
        }
        return redirectToMovie;
    }
}
