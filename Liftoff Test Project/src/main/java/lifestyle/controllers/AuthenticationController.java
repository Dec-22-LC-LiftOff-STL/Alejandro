package lifestyle.controllers;

import lifestyle.models.User;
import lifestyle.models.data.UserRepository;
import lifestyle.models.dto.LoginFormDTO;
import lifestyle.models.dto.RegisterFormDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;


    /* We need some utility methods for working with sessions. This code allows us to store and retrieve the login
    status of a user in a session. More specifically, a logged-in user’s user ID will be stored in their session.*/

    // creates a field 'user' for the key used to store user IDs.
    private static final String userSessionKey = "user";

    /* looks for data with the key 'user' in the user’s session. The HttpSession class handles the details of session
        creation and lookup for us, including generating unique session IDs and session cookies. These utility methods
        will allow our handlers to manage authentication. */
    public User getUserFromSession(HttpSession session) {

        // If no user ID is in the session, null is returned.
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        // or if there is no user with the given ID, null is returned.
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return null;
        }

        // If it finds one, it attempts to retrieve the corresponding User object from the database.
        return user.get();
    }

    // uses an HttpSession object (part of the standard javax.servlet.http package) to store key/value pair.
    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }


    // Before we can authenticate a user, we must have users in the application, so we need to render the registration form.
    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());  // default attribute name will be registerFormDTO (i.e. className)
        model.addAttribute("title", "Register");
        return "register";
    }


    /*   Define the handler method at the route /register that takes a valid RegisterFormDTO object,
         associated errors, and a Model. In addition, the method needs an HttpServletRequest object.
         This object represents the incoming request, and will be provided by Spring.  */
    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {
        // Return the user to the form if a validation errors occur.
        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }
        // Retrieve the user with the given username from the database.
        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        // If a user w/ given username already exists, register a custom error w/ errors object and return user to form.
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists.");
            model.addAttribute("title", "Register");
            return "register";
        }

        // Retrieve the two passwords submitted.
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();

        // If the passwords do not match, register a custom error and return user to form.
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match.");
            model.addAttribute("title", "Register");
            return "register";
        }

        /*  At this point, we know a user w/ given username does NOT already exist, and rest of form data is valid. */
        // we create a new user object
        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        // and store new user obj in the database
        userRepository.save(newUser);
        // and then create a new session for the user
        setUserInSession(request.getSession(), newUser);

        //  Finally, redirect the user to the home page.
        return "redirect:";
    }

    // Rendering the login form is similar to rendering the registration form
    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    //  Processing Login Form after user 'submits' it. Performs most of the same actions as processRegistrationForm above.
    //  HttpServletRequest object represents the incoming request, and will be provided by Spring.
    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }
        // Retrieves the User object with the given password from the database.
        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());
        // If no such user exists, register a custom error and return to the form.
        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }
        // Retrieves the submitted password from the form DTO.
        String password = loginFormDTO.getPassword();
        /*  If the password is incorrect, register a custom error and return to the form. Password verification uses
            the User.isMatchingPassword() method, which handles the details associated with checking hashed passwords. */
        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }
        // At this point, we know given user exists & submitted password is correct. So we create a new session for user.
        setUserInSession(request.getSession(), theUser);
        // Finally, redirect the user to the home page.
        return "redirect:";
    }

    /*  To log out, we simply invalidate the session associated with the given user. This removes all data from
        the session, so that when the user makes a subsequent request, they will be forced to log in again. */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }

}
