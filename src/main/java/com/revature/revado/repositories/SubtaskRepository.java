package com.revature.revado.repositories;

import com.revature.revado.models.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {

    List<Subtask> findAllByTodoId(UUID todoId);
}
