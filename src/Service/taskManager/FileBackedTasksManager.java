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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static model.enums.Status.NEW;
import static model.enums.Types.*;

public class FileBackedTasksManager extends TaskManagerImpl {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public List<Task> getAllTask() {
        List<Task> allTask = super.getAllTask();
        save();
        return allTask;
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
                bufferedWriter.newLine();
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
        String[] history = value.split(",");
        for (String id : history) {
            tasksIds.add(Integer.parseInt(id.trim()));
        }
        return tasksIds;
    }

    static Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0].trim());
        switch (Types.valueOf(split[4].trim())) {
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
            default -> {
                return new Task(split[1].trim(),
                        split[2].trim(),
                        id,
                        Status.valueOf(split[3].trim()),
                        Types.valueOf(split[4].trim()));
            }
        }
    }

    static FileBackedTasksManager loadFromFile(File file) throws ManagerLoadException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            List<String> lines = Files.readAllLines(Path.of(file.getAbsolutePath()));
            lines.remove(0);
            for (String line : lines) {
                if (!(line.isEmpty())) {
                    Task task = fromString(line);
                    fileBackedTasksManager.dateBase.put(task.getId(), task);
                    setId(task.getId());
                } else {
                    break;
                }
            }
            String lastLine = lines.get(lines.size() - 1);
            for (Integer id : historyFromString(lastLine)) {
                fileBackedTasksManager.historyManager.addToHistory(fileBackedTasksManager.dateBase.get(id));
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Не можем загрузить файл " + file.getName(), e);
        }
        return fileBackedTasksManager;
    }

    public static void main(String[] args) throws IOException, ManagerLoadException {
        Path src = Paths.get("src", "File.cvs");
        Path path;

        if (Files.exists(src)) {
            Files.delete(src);
            path = Files.createFile(src);
        } else {
            path = null;
        }
        FileBackedTasksManager manager = new FileBackedTasksManager(path.toFile());


        Task task = new Task("Денис", "Ремонт", NEW, TASK);

        manager.saveTask(task);

        manager.getById(task.getId());


        Task task1 = new Task("Аня", "Уборка", NEW, TASK);

        manager.saveTask(task1);

        manager.getById(task1.getId());


        Epic epic = new Epic("Катя", "Стирка", NEW, EPIC);
        manager.saveTask(epic);


        Subtask subtask = new Subtask("Дима", "Отпуск", NEW, SUBTASK, 2);

        Subtask subtask1 = new Subtask("Рома", "Отдых", NEW, SUBTASK, 2);
        manager.saveTask(subtask);

        manager.saveTask(subtask1);


        FileBackedTasksManager manager2 = loadFromFile(path.toFile());

        for (Task task3 : manager2.getHistoryManager().getHistory()) {

            System.out.println(task3);

        }

        System.out.println("------------------------");


        for (Task task4 : manager2.getAllTask()) {

            System.out.println(task4);
        }
    }
}