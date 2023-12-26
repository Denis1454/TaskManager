package util;

import Service.historyManager.HistoryManager;
import Service.historyManager.HistoryManagerImpl;
import Service.taskManager.TaskManager;
import Service.taskManager.TaskManagerImpl;

public class Managers {
    private static final HistoryManager historyManager = new HistoryManagerImpl();
    private static final TaskManager taskManager = new TaskManagerImpl();

    public static TaskManager getDefaultTaskManager() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistoryManager() {
        return historyManager;
    }
}
