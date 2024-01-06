package Service.taskManager;

import Service.historyManager.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.enums.Status;
import model.enums.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.enums.Status.*;
import static util.Managers.getDefaultHistoryManager;

public class TaskManagerImpl implements TaskManager {
    final HistoryManager historyManager = getDefaultHistoryManager();
    final Map<Integer, Task> dateBase = new HashMap<>();
    private int count = 0;

    public static final String ERROR_NOT_FOUND_ID = "Несуществует задачи с веденым id";

    @Override
    public void saveTask(Task task) {
        switch (task.getType()) {
            case TASK, EPIC -> {
                task.setStatus(NEW);
                task.setId(count);
                dateBase.put(count, task);
                generateId();
            }
            case SUBTASK -> {
                Subtask subtask = (Subtask) task;
                int epicId = subtask.getEpicId();
                if (dateBase.containsKey(epicId)) {
                    subtask.setStatus(NEW);
                    subtask.setId(count);
                    dateBase.put(count, subtask);
                    Epic epic = (Epic) dateBase.get(epicId);
                    epic.setStatus(checkSubtaskStatusAndReturnStatus(epic, NEW));
                    HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
                    subtasks.put(count, subtask);
                    generateId();
                    epic.setSubtasks(subtasks);
                    updateTask(epicId, epic);
                } else {
                    throw new IllegalArgumentException(ERROR_NOT_FOUND_ID);
                }
            }
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (!dateBase.containsKey(id)) {
            throw new IllegalArgumentException(ERROR_NOT_FOUND_ID);
        }
        Types type = dateBase.get(id).getType();
        if (type.equals(Types.EPIC)) {
            Epic epic = (Epic) dateBase.get(id);
            for (Integer numberId : epic.getSubtasks().keySet()) {
                dateBase.remove(numberId);
            }
        }
        dateBase.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeAllTask() {
        historyManager.removeAllHistory();
        dateBase.clear();
    }

    @Override
    public void updateTask(int id, Task task) {
        if (!dateBase.containsKey(id)) {
            throw new IllegalArgumentException(ERROR_NOT_FOUND_ID);
        }
        switch (task.getType()) {
            case TASK -> dateBase.replace(id, task);
            case EPIC -> {
                Epic epic = (Epic) task;
                if (!epic.getSubtasks().isEmpty()) {
                    if (checkSubtaskStatusAndReturnStatus(epic, DONE).equals(DONE)) {
                        epic.setStatus(DONE);
                    } else {
                        epic.setStatus(checkSubtaskStatusAndReturnStatus(epic, epic.getStatus()));
                    }
                }
                dateBase.replace(id, epic);
            }
            case SUBTASK -> {
                Subtask subtask = (Subtask) task;
                dateBase.replace(id, subtask);
                Epic epic = (Epic) dateBase.get(subtask.getEpicId());
                HashMap<Integer, Subtask> subtasks = epic.getSubtasks();
                subtasks.replace(id, subtask);
                epic.setStatus(checkSubtaskStatusAndReturnStatus(epic, subtask.getStatus()));
                epic.setSubtasks(subtasks);
                dateBase.replace(epic.getId(), epic);
            }
        }
    }

    @Override
    public List<Task> taskList() {
        return new ArrayList<>(dateBase.values());
    }

    @Override
    public Task getById(int id) {
        if (dateBase.containsKey(id)) {
            Task task = dateBase.get(id);
            historyManager.addToHistory(task);
            return task;
        } else {
            throw new IllegalArgumentException(ERROR_NOT_FOUND_ID);
        }
    }

    @Override
    public List<Subtask> getAllSubtaskByEpicId(int id) {
        Epic epic = (Epic) getById(id);
        return new ArrayList<>(epic.getSubtasks().values());
    }

    private Status checkSubtaskStatusAndReturnStatus(Epic epic, Status status) {
        Map<Integer, Subtask> subtasks = epic.getSubtasks();
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

    private void generateId() {
        count++;
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }
}