package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ty.henry.cinemaapp.dto.TicketForm;
import ty.henry.cinemaapp.model.*;
import ty.henry.cinemaapp.service.TicketService;
import ty.henry.cinemaapp.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes({"ticketForm", "showing"})
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping("/showing/{id}")
    public String showShowingPage(@PathVariable Long id, Model model, SelectShowingForm selectShowingForm,
                                  Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        Showing showing = ticketService.findShowingById(id);

        List<Showing> showingsAtTheSameDay;

        if(currentUser.getRole() == Role.ROLE_ADMIN) {
            showingsAtTheSameDay = ticketService.findShowingsForMovieAndDate(showing.getMovie(),
                    showing.getShowingDate().toLocalDate());
        }
        else {
            showingsAtTheSameDay = ticketService.findFutureShowingsForMovieAndDate(showing.getMovie(),
                    showing.getShowingDate().toLocalDate());
        }

        model.addAttribute("chosenShowing", showing);
        model.addAttribute("showings", showingsAtTheSameDay);
        return "showing";
    }

    @PostMapping("/buy-ticket")
    public String showBuyTicketPage(SelectShowingForm selectShowingForm, Model model,
                                    Principal principal) {
        Showing showing = ticketService.findShowingById(selectShowingForm.getSelectedShowing());
        model.addAttribute("showing", showing);

        User currentUser = userService.findUserByEmail(principal.getName());
        Hall showingHall = showing.getHall();

        TicketForm ticketForm = new TicketForm(showingHall.getRowCount(), showingHall.getSeatsInRow());
        ticketForm.setShowingId(showing.getId());
        ticketForm.setUserId(currentUser.getId());

        List<Reservation> showingReservations = ticketService.findReservationsForShowing(showing);
        for(Reservation r : showingReservations) {
            ticketForm.setReserved(r.getRowNumber(), r.getSeatInRow());
        }

        model.addAttribute("ticketForm", ticketForm);

        return "buyTicket";
    }

    @RequestMapping("/buy-ticket/{row}/{seat}")
    public String chooseSeat(@ModelAttribute("ticketForm") TicketForm ticketForm,
                             @PathVariable int row, @PathVariable int seat) {
        ticketForm.clickOnSeat(row, seat);
        return "buyTicket";
    }

    public static class SelectShowingForm {
        private long selectedShowing;

        public long getSelectedShowing() {
            return selectedShowing;
        }

        public void setSelectedShowing(long selectedShowing) {
            this.selectedShowing = selectedShowing;
        }
    }
}
