package com.revature.revado.repositories;

import com.revature.revado.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {

    List<Todo> findByUserId(UUID userId);
    Todo findByUserIdAndId(UUID userId,UUID id);
    List<Todo> findByUserIdAndCompleted(UUID userId, boolean completed);

}
