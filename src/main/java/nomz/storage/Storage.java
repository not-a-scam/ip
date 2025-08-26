package nomz.storage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.parser.Parser;

public class Storage {
    private final File file;
    
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

    /** Load tasks from disk. */
    public ArrayList<Task> load() throws NomzException {
        ArrayList<Task> list = new ArrayList<>();
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                Task t = Parser.parseTaskFileContent(s.nextLine());
                list.add(t);
            }
        } catch (FileNotFoundException ignore) {}
        return list;
    }

    /** Append a newly added task */
    public void append(Task task) throws IOException {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(task.savedString() + "\n");
        }
    }

    /** Overwrite the file with the current list (for mark/unmark/delete). */
    public void saveAll(ArrayList<Task> tasks) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            for (Task t : tasks) {
                fw.write(t.savedString() + "\n");
            }
        }
    }
}
