package com.revature.revado.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
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
@JsonPropertyOrder({"id", "title", "completed"})
public class Subtask {

    @Id
    @GeneratedValue
    private UUID id;

    @Nonnull
    @Column(nullable = false)
    private String title;

    @Nonnull
    @Column(nullable = false)
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    @JsonIgnore
    private Todo todo;
}
