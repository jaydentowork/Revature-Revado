package com.revature.revado.services;

import com.revature.revado.models.Todo;
import com.revature.revado.models.User;
import com.revature.revado.repositories.TodoRepository;
import com.revature.revado.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepostiroy){
        this.todoRepository = todoRepository;
        this.userRepository = userRepostiroy;
    }

    public void createTodo(Todo todo, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        todo.setUser(user);
        todoRepository.save(todo);
    }

    public List<Todo> fetchTodoByUser(UUID userId){
       return todoRepository.findByUserId(userId);
    }
}
