package ty.henry.cinemaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ty.henry.cinemaapp.dto.HallForm;
import ty.henry.cinemaapp.error.EntityAlreadyExistsException;
import ty.henry.cinemaapp.model.Hall;
import ty.henry.cinemaapp.service.HallService;

import javax.validation.Valid;

@Controller
public class HallController {

    @Autowired
    private HallService hallService;

    @RequestMapping("/halls")
    public String showHalls(Model model) {
        Iterable<Hall> halls = hallService.findAllHalls();
        model.addAttribute("halls", halls);
        return "halls";
    }

    @GetMapping("/add-hall")
    public String showAddHallPage(HallForm hallForm) {
        return "addHall";
    }

    @PostMapping("/add-hall")
    public String addHall(@Valid HallForm hallForm, BindingResult result) {
        if(!result.hasErrors()) {
            try {
                hallService.addNewHall(hallForm);
            } catch (EntityAlreadyExistsException ex) {
                result.rejectValue("name", "message.hallExistsError");
            }
        }
        if(result.hasErrors()) {
            return "addHall";
        }
        return "redirect:/halls";
    }

    @GetMapping("/edit-hall/{id}")
    public String showEditHallPage(HallForm hallForm, @PathVariable Integer id) {
        Hall hall = hallService.findHallById(id);
        hallForm.setName(hall.getName());
        hallForm.setRowCount(hall.getRowCount());
        hallForm.setSeatsInRow(hall.getSeatsInRow());
        return "editHall";
    }

    @PostMapping("/edit-hall/{id}")
    public String editHall(@Valid HallForm hallForm, BindingResult result, @PathVariable Integer id) {
        if(!result.hasErrors()) {
            try {
                hallService.editHall(id, hallForm);
            } catch (EntityAlreadyExistsException ex) {
                result.rejectValue("name", "message.hallExistsError");
            }
        }
        if(result.hasErrors()) {
            return "editHall";
        }
        return "redirect:/halls";
    }

    @DeleteMapping("/hall/{id}")
    public String deleteHall(@PathVariable Integer id) {
        hallService.deleteHall(id);
        return "redirect:/halls";
    }
}
