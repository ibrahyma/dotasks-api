package com.ibrahyma.dotasksapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibrahyma.dotasksapi.enumeration.TaskPriority;
import com.ibrahyma.dotasksapi.enumeration.TaskState;
import com.ibrahyma.dotasksapi.model.Task;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    void testGetTasks() throws Exception {
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Faire les courses"))
            .andExpect(jsonPath("$[1].state").value(TaskState.IN_PROGRESS.name()))
            .andExpect(jsonPath("$[2].starred").value(true));
    }

    @Test
    @Order(2)
    void testGetUnknownTask() throws Exception {
        String notFoundId = "2442";

        mockMvc.perform(get("/tasks/{id}", notFoundId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    void testGetExistingTask() throws Exception {
        String existingId = "1";

        mockMvc.perform(get("/tasks/{id}", existingId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Faire les courses"))
            .andExpect(jsonPath("$.state").value(TaskState.TODO.name()))
            .andExpect(jsonPath("$.starred").value(false))
            .andExpect(jsonPath("$.description").isString())
            .andExpect(jsonPath("$.priority").value(TaskPriority.MEDIUM.name()))
            .andExpect(jsonPath("$.dueDate").doesNotExist())
            .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @Order(4)
    void testCreateBadTask() throws Exception {
        Task task = new Task();
        Date dueDate = new Date();
        long twoDaysAfterTimeValue = dueDate.getTime() + 172800000;

        dueDate.setTime(twoDaysAfterTimeValue);

        task.setState(TaskState.DONE);
        task.setStarred(true);
        task.setDescription("Exemple d'une tâche complétée");
        task.setPriority(TaskPriority.LOW);
        task.setDueDate(dueDate);
        task.setCreatedAt(new Date());

        String requestBody = objectMapper.writeValueAsString(task);

        mockMvc.perform(
            post("/tasks")
                .content(requestBody)
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void testCreateTask() throws Exception {
        Task task = new Task();
        Date dueDate = new Date();
        long twoDaysAfterTimeValue = dueDate.getTime() + 172800000;

        dueDate.setTime(twoDaysAfterTimeValue);

        task.setName("Une tâche finie");
        task.setState(TaskState.DONE);
        task.setStarred(true);
        task.setDescription("Exemple d'une tâche complétée");
        task.setPriority(TaskPriority.LOW);
        task.setDueDate(dueDate);
        task.setCreatedAt(new Date());

        String requestBody = objectMapper.writeValueAsString(task);

        mockMvc.perform(
            post("/tasks")
                .content(requestBody)
                .contentType("application/json")
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Une tâche finie"))
            .andExpect(jsonPath("$.state").value(TaskState.DONE.name()))
            .andExpect(jsonPath("$.starred").value(true))
            .andExpect(jsonPath("$.description").value("Exemple d'une tâche complétée"))
            .andExpect(jsonPath("$.priority").value(TaskPriority.LOW.name()))
            .andExpect(jsonPath("$.dueDate").exists())
            .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @Order(6)
    void testUpdateUnknownTask() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new Task());
        String notFoundId = "2442";

        mockMvc.perform(
            put("/tasks/{id}", notFoundId)
                    .contentType("application/json")
                    .content(requestBody)
        )
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void testUpdateBadTask() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new Task());
        String existingId = "2";

        mockMvc.perform(
            put("/tasks/{id}", existingId)
                .contentType("application/json")
                .content(requestBody)
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void testUpdateTask() throws Exception {
        Task newTask = new Task();
        Date newTaskDueDate = new Date();
        String existingTaskId = "2";

        newTaskDueDate.setTime(newTaskDueDate.getTime() - 100000000);
        newTask.setPriority(TaskPriority.HIGH);
        newTask.setName("Modifié");
        newTask.setDescription("");
        newTask.setDueDate(newTaskDueDate);

        String requestBody = objectMapper.writeValueAsString(newTask);

        mockMvc.perform(
            put("/tasks/{id}", existingTaskId)
                .contentType("application/json")
                .content(requestBody)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Modifié"))
            .andExpect(jsonPath("$.state").value(TaskState.TODO.name()))
            .andExpect(jsonPath("$.starred").value(false))
            .andExpect(jsonPath("$.description").isEmpty())
            .andExpect(jsonPath("$.priority").value(TaskPriority.HIGH.name()))
            .andExpect(jsonPath("$.dueDate").exists())
            .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @Order(9)
    void testDeleteTask() throws Exception {
        String anyTaskId = "3";
        mockMvc.perform(delete("/tasks/{id}", anyTaskId))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(10)
    void testClearTasks() throws Exception {
        mockMvc.perform(delete("/tasks"))
            .andExpect(status().isNoContent());
    }
}
