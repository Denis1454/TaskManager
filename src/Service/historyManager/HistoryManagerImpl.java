package Service.historyManager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryManagerImpl implements HistoryManager {

    private final Map<Integer, Node<Task>> nods = new HashMap<>();
    Node<Task> last;
    Node<Task> first;

    @Override
    public List<Task> getHistory() {
        List<Task> task = new ArrayList<>();
        Node<Task> node = first;
        while (node != null) {
            task.add(node.getTask());
            node = node.getNext();
        }
        return task;
    }

    @Override
    public void addToHistory(Task task) {
        if (nods.containsKey(task.getId())) {
            remove(task.getId());
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override
    public void remove(Integer id) {
        Node<Task> node = nods.get(id);
        removeNode(node);
        nods.remove(id);
    }

    @Override
    public void removeAllHistory() {
        this.last = null;
        this.first = null;
        nods.clear();
    }

    private void linkLast(Task task) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.setNext(newNode);
        nods.put(task.getId(), newNode);
    }

    private void removeNode(Node<Task> node) {
        final Node<Task> next = node.getNext();
        final Node<Task> prev = node.getPrev();

        if (prev == null) {
            first = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }

        if (next == null) {
            last = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }
    }
}
