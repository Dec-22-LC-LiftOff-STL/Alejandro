package lifestyle.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity {

    /* Our validation annotations on User are very lenient. This is okay, however, because we will validate user input
    used to make User objects using a DTO with more restrictive validation. */

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    /* While our class needs one of these encoder objects below, it does not need to be an instance variable. We’ll
    make it static, so it can be shared by all User objects. And final so it can have contents modified but object
    itself cannot be deleted. */
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Need empty constructor to create an entity.
    public User() {}

    // parameterized constructor to create a user instance with a username and password.
    public User(String username, String password) {
        this.username = username;
//        this.pwHash = password;  // assign more readable variable (password) to the pwHash field in param list.
        this.pwHash = encoder.encode(password);  // replaced line above to use encoder to create a hash from given password.
    }

    // Only need getter for username.
    public String getUsername() {
        return username;
    }

    /* Our User objects should also be responsible for determining if a given password is a match for the hash stored
        by the object. We can do this by adding a method to our User class that uses the encoder.matches() method.
        bcrypt internally uses a technique called salting, which requires additional steps before comparison.
        These additional steps are carried out by encoder.matches(). */
    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

}