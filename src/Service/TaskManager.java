package Service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;

import static model.enums.Status.*;

public class TaskManager {
    private final HashMap<Integer, Task> dateBase = new HashMap<>();
    private int count = 0;

    public void saveTask(Task task) {
        if (task.getType().equals("Task") || task.getType().equals("Epic")) {
            task.setStatus(NEW);
            task.setId(count);
            dateBase.put(count, task);
            generateId();
        } else if (task.getType().equals("Subtask")) {
            Subtask subtask = (Subtask) task;
            int epicId = subtask.getEpicId();
            if (dateBase.containsKey(epicId)) {
                subtask.setStatus(NEW);
                subtask.setId(count);
                dateBase.put(count, subtask);
                Epic epic = (Epic) dateBase.get(epicId);
                epic.setStatus(checkSubtaskStatus(epic, NEW));
                HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
                subtasks.put(count, subtask);
                generateId();
                epic.setSubtasks(subtasks);
                updateTask(epicId, epic);
            } else {
                System.out.println("Несуществует задачи с веденым id");
                throw new IllegalArgumentException();
            }
        }
    }

    private Status checkSubtaskStatus(Epic epic, Status status) {
        HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
        int statusCount = 0;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus().equals(status)) {
                statusCount++;
            }
        }
        if (subtasks.size() == statusCount) {
            return status;
        } else {
            return IN_PROGRESS;
        }
    }

    public void removeTaskById(int id) {
        if (!dateBase.containsKey(id)) {
            System.out.println("Несуществует задачи с веденым id");
            throw new IllegalArgumentException();
        }
        String type = dateBase.get(id).getType();
        if (type.equals("Epic")) {
            Epic epic = (Epic) dateBase.get(id);
            for (Integer numberId : epic.getSubtasks().keySet()) {
                dateBase.remove(numberId);
            }
        }
        dateBase.remove(id);
    }

    public void removeAllTask() {
        dateBase.clear();
    }

    public void updateTask(int id, Task task) {
        if (!dateBase.containsKey(id)) {
            System.out.println("Несуществует задачи с веденым id");
            throw new IllegalArgumentException();
        }
        if (task.getType().equals("Task")) { //TODO Заменить все if && else if на switch
            dateBase.replace(id, task);
        } else if (task.getType().equals("Epic")) {
            Epic epic = (Epic) task;
            if (!epic.getSubtasks().isEmpty()) {
                if (checkSubtaskStatus(epic, DONE).equals(DONE)) {
                    epic.setStatus(DONE);
                } else {
                    epic.setStatus(checkSubtaskStatus(epic, epic.getStatus()));
                }
            }
            dateBase.replace(id, epic);
        } else if (task.getType().equals("Subtask")) {
            Subtask subtask = (Subtask) task;
            dateBase.replace(id, subtask);
            Epic epic = (Epic) dateBase.get(subtask.getEpicId());
            HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
            subtasks.replace(id, subtask);
            epic.setStatus(checkSubtaskStatus(epic, subtask.getStatus()));
            epic.setSubtasks(subtasks);
            dateBase.replace(epic.getId(), epic);
        }
    }

    public ArrayList<Task> taskList() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(dateBase.values());
        return allTasks;
    }

    public Task getById(int id) {
        if (dateBase.containsKey(id)) {
            return dateBase.get(id);
        } else {
            System.out.println("Несуществует задачи с веденым id");
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<Subtask> getAllSubtaskByEpicId(int id) {
        Epic epic = (Epic) getById(id);
        return new ArrayList<>(epic.getSubtasks().values());
    }

    private Integer generateId() {
        return count++;
    }
}