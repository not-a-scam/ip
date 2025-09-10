package nomz.data.tasks;

import static nomz.common.Messages.MESSAGE_INVALID_TASK_INDEX;
import static nomz.common.Messages.MESSAGE_TASK_LIST_HEADER;

import java.util.ArrayList;

import nomz.data.exception.InvalidNomzArgumentException;

/**
 * Represents a list of tasks in Nomz.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a TaskList with an empty list of tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList with the specified list of tasks.
     *
     * @param tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task
     * @return
     */
    public Task add(Task task) {
        tasks.add(task);
        return task;
    }

    /**
     * Deletes a task from the list. Uses one based indexing.
     *
     * @param oneBasedIndex
     * @return
     * @throws InvalidNomzArgumentException
     */
    public Task delete(int oneBasedIndex) throws InvalidNomzArgumentException {
        int idx = oneBasedIndex - 1;
        boolean isValidIndex = idx >= 0 && idx < tasks.size();
        if (!isValidIndex) {
            throw new InvalidNomzArgumentException(MESSAGE_INVALID_TASK_INDEX);
        }
        return tasks.remove(idx);
    }

    /**
     * Retrieves a task from the list. Uses one based indexing.
     *
     * @param oneBasedIndex
     * @return
     * @throws InvalidNomzArgumentException
     */
    public Task get(int oneBasedIndex) throws InvalidNomzArgumentException {
        int idx = oneBasedIndex - 1;
        boolean isValidIndex = idx >= 0 && idx < tasks.size();
        if (!isValidIndex) {
            throw new InvalidNomzArgumentException(MESSAGE_INVALID_TASK_INDEX);
        }
        return tasks.get(idx);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return
     */
    public int size() {
        return tasks.size();
    }


    /**
     * Returns the list of tasks.
     *
     * @return
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns a string representation of the task list for display.
     *
     * @return
     */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder(MESSAGE_TASK_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            sb.append(i + 1).append(". ").append(task.toString()).append("\n");
        }
        return sb.toString();
    }

}
