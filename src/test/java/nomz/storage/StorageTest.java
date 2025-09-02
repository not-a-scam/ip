package nomz.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.data.tasks.Todo;

class StorageTest {
    private static final String TEST_FILE_PATH = "test_data/storage_test.txt";
    private Storage storage;

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        storage = new Storage(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        File parent = file.getParentFile();
        if (parent != null && parent.exists()) {
            parent.delete();
        }
    }

    @Test
    void appendAndLoad_singleTask_success() throws Exception {
        Task todo = new Todo("read book");
        storage.append(todo);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals(todo.savedString(), loaded.get(0).savedString());
    }

    @Test
    void saveAll_overwritesFile_success() throws Exception {
        Task todo1 = new Todo("task one");
        Task todo2 = new Todo("task two");
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(todo1);
        tasks.add(todo2);

        storage.saveAll(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(2, loaded.size());
        assertEquals(todo1.savedString(), loaded.get(0).savedString());
        assertEquals(todo2.savedString(), loaded.get(1).savedString());
    }

    @Test
    void load_emptyFile_returnsEmptyList() throws NomzException {
        ArrayList<Task> loaded = storage.load();
        assertTrue(loaded.isEmpty());
    }

    @Test
    void ensureFile_createsFileAndParentDirs() {
        File file = new File(TEST_FILE_PATH);
        assertTrue(file.exists());
        assertTrue(file.getParentFile().exists());
    }
}
