package com.revature.revado.services;

import com.revature.revado.models.Subtask;
import com.revature.revado.models.Todo;
import com.revature.revado.repositories.SubtaskRepository;
import com.revature.revado.repositories.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubtaskService {

    private final TodoRepository todoRepository;
    private final SubtaskRepository subtaskRepository;

    public SubtaskService(TodoRepository todoRepository, SubtaskRepository subtaskRepository){
        this.todoRepository = todoRepository;
        this.subtaskRepository = subtaskRepository;
    }

    public void createSubtask(Subtask subtask, UUID todoid){
        Todo todo = todoRepository.findById(todoid).orElseThrow(() -> new RuntimeException("No todo found with that id"));
        subtask.setTodo(todo);
        subtaskRepository.save(subtask);
    }

    public List<Subtask> findSubtasksById(UUID todoid){
        return subtaskRepository.findAllByTodoId(todoid);
    }

}
