import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public Task add(Task task) {
        tasks.add(task);
        return task;
    }

    public Task delete(int oneBasedIndex) throws InvalidNomzArgumentException {
        int idx = oneBasedIndex - 1;
        if (idx < 0 || idx >= tasks.size()) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_TASK_INDEX);
        }
        return tasks.remove(idx);
    }

    public Task get(int oneBasedIndex) throws InvalidNomzArgumentException {
        int idx = oneBasedIndex - 1;
        if (idx < 0 || idx >= tasks.size()) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_TASK_INDEX);
        }
        return tasks.get(idx);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public String toDisplayString() {
        StringBuilder sb = new StringBuilder(Messages.MESSAGE_TASK_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i).toString()).append("\n");
        }
        return sb.toString();
    }

}
