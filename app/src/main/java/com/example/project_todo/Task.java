package com.example.project_todo;

import java.io.Serializable;

/**
 * Represents a single task with a title, priority, and scheduled time.
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 1L; // Recommended for Serializable

    private String title;
    private String priority;
    private long time;

    // Constructor
    public Task(String title, String priority, long time) {
        this.title = title;
        this.priority = priority;
        this.time = time;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public long getTime() {
        return time;
    }

    // Setters (optional)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setTime(long time) {
        this.time = time;
    }

    // Optional: toString for logging or debugging
    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", priority='" + priority + '\'' +
                ", time=" + time +
                '}';
    }
}
