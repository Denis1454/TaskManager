package model;

import model.enums.Status;
import model.enums.Types;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, Types type, int epicId) {
        super(name, description, status, type);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, Types type, int epicId) {
        super(name, description, id, status, type);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return
                super.getId() +
                        ", " + super.getName() +
                        ", " + super.getDescription() +
                        ", " + super.getStatus() +
                        ", " + super.getType() +
                        ", " + epicId
                ;
    }
}
