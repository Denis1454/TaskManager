package Service.taskManager;

import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    void saveTask(Task task);

    void removeTaskById(int id);

    void removeAllTask();

    void updateTask(int id, Task task);

    List<Task> taskList();

    Task getById(int id);

    List<Subtask> getAllSubtaskByEpicId(int id);

}
