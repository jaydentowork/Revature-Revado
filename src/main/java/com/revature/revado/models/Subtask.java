package com.revature.revado.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subtask {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;
}
