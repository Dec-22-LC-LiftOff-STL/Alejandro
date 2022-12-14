package lifestyle.controllers;

import lifestyle.data.EventRepository;
import lifestyle.models.Event;
import lifestyle.models.EventTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public String displayEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/index";
    }

    @GetMapping("create")
    public String createEvent(Model model) {
        model.addAttribute("title", "Create history!");
        model.addAttribute(new Event());
        model.addAttribute("topics", EventTopic.values());
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

    @GetMapping("delete")
    public String deleteEvent(Model model) {
        model.addAttribute("title", "Remove Event");
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processDeleteEvent(@RequestParam(required = false) int[] eventIds) {

        if (eventIds != null) {
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }
        return "redirect:";
    }
}
