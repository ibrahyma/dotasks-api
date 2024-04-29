package com.ibrahyma.dotasksapi.model;

import com.ibrahyma.dotasksapi.enumeration.TaskPriority;
import com.ibrahyma.dotasksapi.enumeration.TaskState;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private TaskState state = TaskState.TODO;

    private boolean starred = false;

    @Nullable
    private String description;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Basic(optional = false)
    private TaskPriority priority = TaskPriority.NONE;

    @Nullable
    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "created_at")
    private Date createdAt = new Date();
}
