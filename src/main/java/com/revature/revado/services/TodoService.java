package com.revature.revado.services;

import com.revature.revado.exceptions.ResourceNotFoundException;
import com.revature.revado.exceptions.UnauthorizedAccessException;
import com.revature.revado.models.Todo;
import com.revature.revado.models.User;
import com.revature.revado.repositories.TodoRepository;
import com.revature.revado.repositories.UserRepository;
import com.revature.revado.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.revature.revado.constants.ErrorMessages.TODO_NOT_FOUND;
import static com.revature.revado.constants.ErrorMessages.TODO_OWNERSHIP_REQUIRED;

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

    public void deleteTodo(UUID todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new ResourceNotFoundException(TODO_NOT_FOUND));
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!todo.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException(TODO_OWNERSHIP_REQUIRED);
        }
        todoRepository.delete(todo);
    }

    public List<Todo> fetchTodoByUser() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return todoRepository.findByUserId(userId);
    }

    public Todo getTodoById(UUID id) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Todo todo = todoRepository.findByUserIdAndId(userId, id);
        if(todo == null){
            throw new ResourceNotFoundException(TODO_NOT_FOUND);
        }
        return todoRepository.findByUserIdAndId(userId, id);
    }

    public List<Todo> fetchTodoCompletedByUser(Boolean completed) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return todoRepository.findByUserIdAndCompleted(userId, completed);
    }

    public void updateTodo(Todo updateTodo) {
        Todo existingTodo = todoRepository.findById(updateTodo.getId()).orElseThrow(() -> new ResourceNotFoundException(TODO_NOT_FOUND));

        UUID userId = SecurityUtils.getCurrentUserId();
        if (!existingTodo.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException(TODO_OWNERSHIP_REQUIRED);
        }

        existingTodo.setTitle(updateTodo.getTitle());
        existingTodo.setDescription(updateTodo.getDescription());
        existingTodo.setCompleted(updateTodo.isCompleted());

        todoRepository.save(existingTodo);
    }
}
