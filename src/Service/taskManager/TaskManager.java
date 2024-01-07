package Service.taskManager;

import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    List<Task> getAllTask();
    void saveTask(Task task) throws IOException;

    void removeTaskById(int id) throws IOException;

    void removeAllTask() throws IOException;

    void updateTask(int id, Task task) throws IOException;

    List<Task> taskList() throws IOException;

    Task getById(int id) throws IOException;

    List<Subtask> getAllSubtaskByEpicId(int id) throws IOException;

}
