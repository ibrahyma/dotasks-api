package com.ibrahyma.dotasksapi.service;

import com.ibrahyma.dotasksapi.model.Task;
import com.ibrahyma.dotasksapi.repository.TaskRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Data
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public Iterable<Task> getTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTask(final int id) {
        return repository.findById(id);
    }

    public Task createTask(Task task) {
        if (task.getId() != null || task.getName() == null) {
            return null;
        }

        return repository.save(task);
    }

    public Task updateTask(Task task) {
        if (
            task == null
            || task.getName() == null
        ) {
            return null;
        }

        return repository.save(task);
    }

    public void deleteTask(final int id) {
        repository.deleteById(id);
    }

    public void clearTasks() {
        repository.deleteAll();
    }
}
