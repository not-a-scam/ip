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
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A list of tasks loaded from the file.
     * @throws NomzException If the file content is invalid.
     */
    public ArrayList<Task> load() throws NomzException {
        try (Scanner s = new Scanner(file)) {
            ArrayList<Task> list = new ArrayList<>();

            while (s.hasNextLine()) {
                String rawTask = s.nextLine();
                Task task = Parser.parseTaskFileContent(rawTask);
                list.add(task);
            }

            return list;
        } catch (FileNotFoundException ignore) {
            return new ArrayList<Task>();
        }
    }

    /**
     * Appends a new task to the storage file.
     *
     * @param task The task to append.
     * @throws IOException If an I/O error occurs.
     */
    public void append(Task task) throws IOException {
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
        try (FileWriter fw = new FileWriter(file)) {
            for (Task t : tasks) {
                fw.write(t.toSavedString() + "\n");
            }
        }
    }
}
