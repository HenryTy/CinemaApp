package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ty.henry.cinemaapp.dto.ShowingForm;
import ty.henry.cinemaapp.logic.ShowingTimeChecker;
import ty.henry.cinemaapp.logic.TimeRange;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.service.MovieService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/add-showing/{movieId}")
@SessionAttributes({"showingForm", "ranges"})
public class AddShowingController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowingTimeChecker showingTimeChecker;

    @RequestMapping
    public String showAddShowingPage(@PathVariable Integer movieId, ShowingForm showingForm, Model model) {
        Movie movie = movieService.findMovieById(movieId);
        showingForm.setMovie(movie);
        model.addAttribute("showingForm", showingForm);
        return "addShowing";
    }

    @PostMapping(params = "_step=1")
    public String generateShowingStartTimeRanges(@Validated({ShowingForm.DateValidation.class})
                                                     @ModelAttribute("showingForm") ShowingForm showingForm,
                                                 BindingResult result, Model model) {
        if(!result.hasErrors()) {
            Map<Hall, List<TimeRange>> correctRanges = showingTimeChecker
                    .getPossibleShowingStartTimeRanges(showingForm.getDate(), showingForm.getMovie());
            model.addAttribute("ranges", correctRanges);
        }
        return "addShowing";
    }

    @PostMapping(params = "_finish")
    public String addShowing(@Validated({ShowingForm.TimeValidation.class}) @ModelAttribute("showingForm")
                                         ShowingForm showingForm, BindingResult result,
                             @ModelAttribute("ranges") Map<Hall, List<TimeRange>> ranges, SessionStatus status) {
        if(!result.hasErrors()) {
            String chosenHallName = showingForm.getHallName();
            for (Hall hall : ranges.keySet()) {
                if (hall.getName().equals(chosenHallName)) {
                    showingForm.setHall(hall);
                }
            }

            Hall chosenHall = showingForm.getHall();

            LocalDate chosenDate = showingForm.getDate();
            LocalTime chosenTime = showingForm.getTime();
            if (showingTimeChecker.isTimeInRanges(chosenHall, chosenDate.atTime(chosenTime), ranges)) {
                movieService.addShowing(showingForm);
                status.setComplete();
                return "redirect:/movie/{movieId}";
            }
            result.rejectValue("time", "message.wrongShowingTime");
        }
        return "addShowing";
    }

    @RequestMapping(params = "_cancel")
    public String cancelProcess(SessionStatus status) {
        status.setComplete();
        return "redirect:/movie/{movieId}";
    }
}
