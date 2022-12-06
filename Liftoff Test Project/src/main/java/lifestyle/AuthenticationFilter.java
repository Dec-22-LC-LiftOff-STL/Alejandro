package lifestyle;

import lifestyle.controllers.AuthenticationController;
import lifestyle.models.User;
import lifestyle.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// Chapter 19.5 Filtering Requests in Spring.
public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;


    // Creating a whitelist of items NOT subject to restrictions - pages anyone can access regardless of login status.
    private static final List<String> whitelist = Arrays.asList("/login", "/register", "/logout", "/css");

    // method to check whether a given request is whitelisted.
    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    /* The preHandle() method below uses the whitelist field/methods above to allow user to access certain pages w/o
        being logged in, before moving on to prevent access to every other page on the app if a user is not logged in.*/
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        // Don't require sign-in for whitelisted pages as defined by whitelist field and isWhiteListed() method above.
        if (isWhitelisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        // Retrieves the user’s session object, which is contained in the request.
        HttpSession session = request.getSession();

        // Retrieves User object corresponding to given user. This will be null if user is not logged in.
        User user = authenticationController.getUserFromSession(session);

        // The user is logged in. (User object is non-null, so is logged in. Allow request to be handled as normal.)
        if (user != null) {
            return true;
        }

        // The user is NOT logged in. (User object is null, so we redirect the user to the login page.)
        response.sendRedirect("/login");
        return false;
    }



}
