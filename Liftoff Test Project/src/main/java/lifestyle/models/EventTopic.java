package lifestyle.models;

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
