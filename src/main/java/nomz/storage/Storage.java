package nomz.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.parser.Parser;

/**
 * Handles storage and retrieval of tasks.
 */
public class Storage {
    private final File file;
    private boolean hasLoadError = false;

    /**
     * Creates a Storage object to manage task storage.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Storage(String filePath) {
        this.file = new File(filePath);
        ensureFile();
    }

    private void ensureFile() {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create storage file", e);
        }
        assert file.exists() : "File must exist";
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A list of tasks loaded from the file.
     * @throws NomzException If the file content is invalid.
     */
    public ArrayList<Task> load() throws NomzException {
        assert file.exists() : "Storage file should exist before loading";

        ArrayList<Task> list = new ArrayList<>();
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                addTaskToList(list, s);
            }
        } catch (FileNotFoundException ignore) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * Parses a line from the file and adds the corresponding task to the list.
     *
     * @param list The list to add the task to.
     * @param s    The scanner reading the file.
     */
    private void addTaskToList(ArrayList<Task> list, Scanner s) {
        String rawTask = s.nextLine().trim();

        if (rawTask.isEmpty()) {
            return;
        }

        try {
            Task task = Parser.parseTaskFileContent(rawTask);
            list.add(task);
        } catch (NomzException e) {
            this.hasLoadError = true;
        }
    }

    /**
     * Checks if there was an error during the last load operation.
     *
     * @return true if there was a load error, false otherwise.
     */
    public boolean hasLoadError() {
        return hasLoadError;
    }

    /**
     * Appends a new task to the storage file.
     *
     * @param task The task to append.
     * @throws IOException If an I/O error occurs.
     */
    public void append(Task task) throws IOException {
        assert task != null : "Task should not be null";
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(task.toSavedString() + "\n");
        }
    }

    /**
     * Saves all tasks to the storage file.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If an I/O error occurs.
     */
    public void saveAll(ArrayList<Task> tasks) throws IOException {
        assert tasks != null : "Tasks should not be null";
        try (FileWriter fw = new FileWriter(file)) {
            for (Task t : tasks) {
                fw.write(t.toSavedString() + "\n");
            }
        }
    }
}
