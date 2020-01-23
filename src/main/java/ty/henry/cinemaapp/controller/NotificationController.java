package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ty.henry.cinemaapp.dto.CancelShowingForm;
import ty.henry.cinemaapp.model.Notification;
import ty.henry.cinemaapp.model.Showing;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.service.NotificationService;
import ty.henry.cinemaapp.service.TicketService;
import ty.henry.cinemaapp.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    TicketService ticketService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @GetMapping("/cancel-showing/{showingId}")
    public String showCancelShowingPage(@PathVariable long showingId, Model model,
                                        CancelShowingForm cancelShowingForm) {
        Showing showing = ticketService.findShowingById(showingId);
        if(showing.getShowingDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("errorMessage", "Nie możesz odwołać seansu, który już się odbył");
        }
        model.addAttribute("showing", showing);
        return "cancelShowing";
    }

    @PostMapping("/cancel-showing/{showingId}")
    public String cancelShowing(@Valid CancelShowingForm cancelShowingForm,
                                BindingResult result, @PathVariable long showingId,
                                Model model) {
        Showing showing = ticketService.findShowingById(showingId);
        if(result.hasErrors()) {
            model.addAttribute("showing", showing);
            return "cancelShowing";
        } else {
            cancelShowingForm.setShowingId(showingId);
            notificationService.cancelShowing(cancelShowingForm);
            return "redirect:/movie/" + showing.getMovie().getId() + "?canceledShowing=true";
        }
    }

    @GetMapping("/notifications")
    public String showNotificationsPage(Principal principal, Model model) {
        User currentUser = userService.findUserByEmail(principal.getName());
        List<Notification> notifications = notificationService
                .findNotificationsForUser(currentUser);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @PostMapping("/mark-notification-as-read/{notId}")
    public String markNotificationAsRead(@PathVariable int notId, Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        notificationService.markNotificationAsRead(currentUser.getId(), notId);
        return "redirect:/notifications";
    }
}
