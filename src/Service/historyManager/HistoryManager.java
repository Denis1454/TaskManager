package Service.historyManager;

import model.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();

    void addToHistory(Task task);

    void remove(Integer id);

    void removeAllHistory();

}
