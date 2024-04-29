package com.ibrahyma.dotasksapi.repository;

import com.ibrahyma.dotasksapi.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
}
