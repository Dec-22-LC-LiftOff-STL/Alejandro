package lifestyle.controllers;

import lifestyle.data.EventRepository;
import lifestyle.models.Event;
import lifestyle.models.User;
import lifestyle.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationController authenticationController;

    @GetMapping
    public String displayEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/index";
    }

    @GetMapping("create")
    public String createEvent(Model model) {
        model.addAttribute("title", "Create history!");
        model.addAttribute(new Event());
        return "events/create";
    }

    @PostMapping("create")
    public String processCreateEvent(@ModelAttribute @Valid Event newEvent, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Create history!");
            // TODO: Remove line below before project demo. Only used for testing.
            model.addAttribute("showErrors", errors.getAllErrors());
            return "events/create";
        }
        eventRepository.save(newEvent);
        return "redirect:";
    }

    // TODO: deleteEvent

    // TODO: processDeleteEvent
}
