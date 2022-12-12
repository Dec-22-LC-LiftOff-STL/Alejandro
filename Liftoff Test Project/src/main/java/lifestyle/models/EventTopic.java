package lifestyle.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public enum EventTopic {

        SLEEP("Sleep"),
        EXERCISE("Exercise"),
        DIET("Diet"),
        WORK("Work"),
        OTHER("Other");

    private final String name;

    EventTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

