package Service.historyManager;

public class Node<Task> {

    private Task task;
    private Node<Task> next;
    private Node<Task> prev;

    Node(Node<Task> prev, Task element, Node<Task> next) {
        this.task = element;
        this.next = next;
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }
}
