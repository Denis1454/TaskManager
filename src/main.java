import Service.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

import static model.enums.Status.*;

public class main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("Денис", "Ремонт", NEW, "Task");
        taskManager.saveTask(task);

        Task task1 = new Task("Аня", "Уборка", NEW, "Task");
        taskManager.saveTask(task1);

        Epic epic = new Epic("Катя", "Стирка", NEW, "Epic");
        taskManager.saveTask(epic);

        Epic epic1 = new Epic("Сережа", "Работа", NEW, "Epic");
        taskManager.saveTask(epic1);

        Subtask subtask = new Subtask("Дима", "Отпуск", NEW, "Subtask", 2);
        taskManager.saveTask(subtask);

        Subtask subtask1 = new Subtask("Рома", "Отдых", NEW, "Subtask", 2);
        taskManager.saveTask(subtask1);

        Subtask subtask2 = new Subtask("Лена", "Прогулка", NEW, "Subtask", epic1.getId());
        taskManager.saveTask(subtask2);

        System.out.println(taskManager.taskList());
        Task task2 = new Task("Денис", "Ремонт", IN_PROGRESS, "Task");
        taskManager.updateTask(task.getId(), task2);
        System.out.println(taskManager.getById(task.getId()));
        System.out.println(epic);
        Subtask subtask3 = new Subtask("Рома", "Отдых", IN_PROGRESS, "Subtask", 2);
        taskManager.updateTask(subtask.getId(), subtask3);
        System.out.println(epic);

        Subtask subtask4 = new Subtask("Лена", "Прогулка", DONE, "Subtask", epic1.getId());
        taskManager.updateTask(subtask2.getId(), subtask4);
        System.out.println(epic1);

        System.out.println(taskManager.getAllSubtaskByEpicId(2));

        taskManager.removeTaskById(2);
        System.out.println(taskManager.taskList());
    }
}