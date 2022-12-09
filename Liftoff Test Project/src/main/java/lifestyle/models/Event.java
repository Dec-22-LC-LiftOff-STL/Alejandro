package lifestyle.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Event extends AbstractEntity {

    @NotNull(message = "You gotta enter something. Try harder!")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters. Make it count!")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters.")
    private String description;

    @ManyToOne
    private User user;

    @DateTimeFormat
    private LocalDate date = LocalDate.now();

    public Event() {
    }

    public Event(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
