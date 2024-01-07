import Service.taskManager.TaskManagerImpl;
import model.Epic;
import model.Subtask;
import model.Task;

import static model.enums.Status.NEW;
import static model.enums.Types.*;

public class Main {
    public static void main(String[] args) {

        TaskManagerImpl taskManager = new TaskManagerImpl();
        Task task = new Task("Денис", "Ремонт", NEW, TASK);
        taskManager.saveTask(task);

        Task task1 = new Task("Аня", "Уборка", NEW, TASK);
        taskManager.saveTask(task1);

        Epic epic = new Epic("Катя", "Стирка", NEW, EPIC);
        taskManager.saveTask(epic);

        Epic epic1 = new Epic("Сережа", "Работа", NEW, EPIC);
        taskManager.saveTask(epic1);

        Subtask subtask = new Subtask("Дима", "Отпуск", NEW, SUBTASK, 2);
        taskManager.saveTask(subtask);

        Subtask subtask1 = new Subtask("Рома", "Отдых", NEW, SUBTASK, 2);
        taskManager.saveTask(subtask1);

        Subtask subtask2 = new Subtask("Лена", "Прогулка", NEW, SUBTASK, 2);
        taskManager.saveTask(subtask2);

        taskManager.getById(2);
        taskManager.getById(3);
        taskManager.getById(4);
        taskManager.getById(2);
        taskManager.getById(3);
        taskManager.getById(4);
        //List<Task> history = taskManager.historyManager.getHistory();
        //for (Task task2 : history) {
        //    System.out.println(task2);
        //}

    }
}