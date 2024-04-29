package com.ibrahyma.dotasksapi.controller;

import com.ibrahyma.dotasksapi.model.Task;
import com.ibrahyma.dotasksapi.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class TaskController {
    @Autowired
    private TaskService service;

    @GetMapping("/")
    public String hello() {
        return "Hello WORLD DoTasks API !";
    }

    @GetMapping("/tasks")
    public Iterable<Task> getTasks() {
        return service.getTasks();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTask(@PathVariable final int id) {
        Optional<Task> task = service.getTask(id);

//        if (task.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(task.get());
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = service.createTask(task);

        if (createdTask == null) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable final int id, @RequestBody Task task) {
        Optional<Task> supposedTask = service.getTask(id);

        if (supposedTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task updatedTask = service.updateTask(task);

        if (updatedTask == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable final int id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<Void> clearTasks() {
        service.clearTasks();
        return ResponseEntity.noContent().build();
    }
}
