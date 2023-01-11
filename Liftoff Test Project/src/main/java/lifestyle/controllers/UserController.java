package lifestyle.controllers;

import lifestyle.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private AuthenticationController authenticationController;

    @GetMapping("")
    public String displayProfile(Model model, HttpServletRequest request) {
        User theUser = authenticationController.getUserFromSession(request.getSession());
        model.addAttribute("name", theUser.getUsername());
        return "index";
    }
}
