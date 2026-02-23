package com.revature.revado.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "completed", "subtaskList"})
public class Todo {
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Subtask> subtaskList = new ArrayList<>();

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Title must not be empty")
    @Column(nullable = false)
    private String title;

    private String description;

    @NotNull
    @Column(nullable = false)
    private boolean completed;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
