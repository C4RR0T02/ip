package Carrot.Task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {

    @Test
    public void getAddPrint_validTaskName_returnsCorrectMessages(){
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15");
        assertEquals("New Deadline Alert: Complete CS2103T Task by 15-02-2025 00:00", deadline.getAddPrint());
    }

    @Test
    public void saveToString_completedTask_returnsCorrectMessages(){
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15");
        deadline.markCompleted();
        assertEquals("D|1|Complete CS2103T Task|2025-02-15 00:00", deadline.saveToString());
    }

    @Test
    public void saveToString_incompleteTask_returnsCorrectMessages(){
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15");
        assertEquals("D|0|Complete CS2103T Task|2025-02-15 00:00", deadline.saveToString());
    }

    @Test
    public void toString_validTaskNameNoTime_returnsCorrectString() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15");
        assertEquals("[D] [ ] Complete CS2103T Task (by: 15-02-2025 00:00)", deadline.toString());
    }

    @Test
    public void toString_validTaskNameValidDate_returnsCorrectString() {
        Deadline deadline = new Deadline("Complete CS2103T Task", "2025-02-15 13:00");
        assertEquals("[D] [ ] Complete CS2103T Task (by: 15-02-2025 13:00)", deadline.toString());
    }
}
