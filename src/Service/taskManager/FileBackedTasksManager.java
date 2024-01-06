package Service.taskManager;

import Service.historyManager.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.enums.Status;
import model.enums.Types;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends TaskManagerImpl {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public List<Task> taskList() {
        List<Task> tasks = super.taskList();
        save();
        return tasks;
    }

    @Override
    public Task getById(int id) {
        Task byId = super.getById(id);
        save();
        return byId;
    }

    @Override
    public List<Subtask> getAllSubtaskByEpicId(int id) {
        List<Subtask> allSubtaskByEpicId = super.getAllSubtaskByEpicId(id);
        save();
        return allSubtaskByEpicId;
    }

    protected HistoryManager getHistoryManager() {
        return super.getHistoryManager();

    }

    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String startLine = "id ,name, description, status, type, epic";
            bufferedWriter.write(startLine);
            bufferedWriter.newLine();
            for (Task task : dateBase.values()) {
                bufferedWriter.write(task.toString());
            }
            bufferedWriter.newLine();
            bufferedWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не можем сохранить файл " + file.getName(), e);
        }
    }

    static String historyToString(HistoryManager manager) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        List<Task> history = manager.getHistory();
        for (Task task : history) {
            counter++;
            if (counter == history.size() - 1) {
                builder.append(task.getId()).append(",");
            } else {
                builder.append(task.getId());
            }
        }
        return builder.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> tasksIds = new ArrayList<>();

        return tasksIds;
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0].trim());
        switch (Types.valueOf(split[4].trim())) {
            case TASK -> {
                return new Task(split[1].trim(),
                        split[2].trim(),
                        id,
                        Status.valueOf(split[3].trim()),
                        Types.valueOf(split[4].trim()));
            }
            case EPIC -> {
                return new Epic(split[1].trim(),
                        split[2].trim(),
                        id,
                        Status.valueOf(split[3].trim()),
                        Types.valueOf(split[4].trim()));
            }
            case SUBTASK -> {
                return new Subtask(split[1].trim(),
                        split[2].trim(),
                        id,
                        Status.valueOf(split[3].trim()),
                        Types.valueOf(split[4].trim()),
                        Integer.parseInt(split[5].trim()));
            }
        }
        return null;
    }

}
