package model;

import model.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer,Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description, Status status, String type) {
        super(name, description, status, type);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer,Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                "Status=" + super.getStatus() +
                '}';
    }
}
