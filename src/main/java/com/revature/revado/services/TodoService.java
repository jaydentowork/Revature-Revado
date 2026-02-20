package com.revature.revado.services;

import com.revature.revado.models.Todo;
import com.revature.revado.models.User;
import com.revature.revado.repositories.TodoRepository;
import com.revature.revado.repositories.UserRepository;
import com.revature.revado.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepostiroy) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepostiroy;
    }

    public void createTodo(Todo todo) {
        UUID userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.getReferenceById(userId);
        todo.setUser(user);
        todoRepository.save(todo);
    }

    public List<Todo> fetchTodoByUser() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return todoRepository.findByUserId(userId);
    }

    public List<Todo> fetchTodoCompletedByUser(Boolean completed) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return todoRepository.findByUserIdAndCompleted(userId, completed);
    }

    public void updateTodo(Todo updateTodo) {
        Todo existingTodo = todoRepository.findById(updateTodo.getId()).orElseThrow(() -> new RuntimeException("Todo not found"));

        UUID userId = SecurityUtils.getCurrentUserId();
        if (!existingTodo.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this Todo");
        }

        existingTodo.setTitle(updateTodo.getTitle());
        existingTodo.setDescription(updateTodo.getDescription());
        existingTodo.setCompleted(updateTodo.isCompleted());

        todoRepository.save(existingTodo);
    }
}
