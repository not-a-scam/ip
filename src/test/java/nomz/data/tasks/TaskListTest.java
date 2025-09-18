package nomz.data.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import nomz.data.exception.InvalidNomzArgumentException;


class TaskListTest {
    @Test
    void addTask_success() throws Exception {
        TaskList list = new TaskList();
        Todo todo = new Todo("read book", new ArrayList<>());
        list.add(todo);
        assertEquals(1, list.size());
        assertEquals(todo, list.get(1));
    }

    @Test
    void removeTask_success() throws Exception {
        TaskList list = new TaskList();
        Todo todo = new Todo("read book", new ArrayList<>());
        list.add(todo);
        list.delete(1);
        assertEquals(0, list.size());
    }

    @Test
    void getTask_outOfBounds_throwsException() {
        TaskList list = new TaskList();
        Exception e = assertThrows(InvalidNomzArgumentException.class, () -> list.get(0));
        assertNotNull(e);
    }

    @Test
    void size_emptyList_zero() {
        TaskList list = new TaskList();
        assertEquals(0, list.size());
    }

    @Test
    void addMultipleTasks_success() {
        TaskList list = new TaskList();
        for (int i = 0; i < 5; i++) {
            list.add(new Todo("task" + i, new ArrayList<>()));
        }
        assertEquals(5, list.size());
    }
}
