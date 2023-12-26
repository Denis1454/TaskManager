package model;

import model.enums.Status;
import model.enums.Types;


import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer,Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description, Status status, Types type) {
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
