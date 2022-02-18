package activitytracker;

import java.time.LocalDateTime;

public class Activity {

    private int id;
    private LocalDateTime startTime;
    private String description;
    private Type type;

    public Activity(int id, LocalDateTime startTime, String description, Type type) {
        this.id = id;
        this.startTime = startTime;
        this.description = description;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }
}
