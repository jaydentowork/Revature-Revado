package com.revature.revado.controllers;

import com.revature.revado.models.Todo;
import com.revature.revado.services.TodoService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Todo>> get(@RequestParam(required = false) Boolean completed) {
        List<Todo> todoList = (completed == null) ? todoService.fetchTodoByUser() : todoService.fetchTodoCompletedByUser(completed);
        return ResponseEntity.ok(todoList);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Todo> getById(@PathVariable UUID id) {
        Todo todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    @PostMapping("/create")
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        todoService.createTodo(todo);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(required = true) UUID id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable UUID id, @RequestBody Todo todo) {
        todo.setId(id);
        todoService.updateTodo(todo);
        return ResponseEntity.ok(todo);
    }

}
