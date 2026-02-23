package com.revature.revado.services;

import com.revature.revado.exceptions.ResourceNotFoundException;
import com.revature.revado.exceptions.UnauthorizedAccessException;
import com.revature.revado.models.Subtask;
import com.revature.revado.models.Todo;
import com.revature.revado.repositories.SubtaskRepository;
import com.revature.revado.repositories.TodoRepository;
import com.revature.revado.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.revature.revado.constants.ErrorMessages.*;

@Service
public class SubtaskService {

    private final TodoRepository todoRepository;
    private final SubtaskRepository subtaskRepository;
    private final AiService aiService;

    public SubtaskService(TodoRepository todoRepository, SubtaskRepository subtaskRepository, AiService aiService) {
        this.todoRepository = todoRepository;
        this.subtaskRepository = subtaskRepository;
        this.aiService = aiService;
    }

    public void createSubtask(UUID todoId, Subtask subtask) {
        // 1. Find parent todo id
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new ResourceNotFoundException(TODO_NOT_FOUND));

        //2. Check todo ownership
        UUID todoOwnerId = SecurityUtils.getCurrentUserId();
        if (!todo.getUser().getId().equals(todoOwnerId)) {
            throw new UnauthorizedAccessException(TODO_OWNERSHIP_REQUIRED);
        }

        //3. Link and Save
        subtask.setTodo(todo);
        subtaskRepository.save(subtask);
    }

    public void deleteSubtask(UUID subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId).orElseThrow(() -> new ResourceNotFoundException(SUBTASK_NOT_FOUND));

        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!subtask.getTodo().getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException(TODO_OWNERSHIP_REQUIRED);
        }
        subtaskRepository.delete(subtask);
    }

    public void updateSubtask(Subtask subtask) {
        Subtask existingSubtask = subtaskRepository.findById(subtask.getId()).orElseThrow(() -> new ResourceNotFoundException(SUBTASK_NOT_FOUND));

        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!existingSubtask.getTodo().getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException(SUBTASK_OWNERSHIP_REQUIRED);
        }

        existingSubtask.setCompleted(subtask.getCompleted());
        existingSubtask.setTitle(subtask.getTitle());

        subtaskRepository.save(existingSubtask);
    }

    public List<Subtask> findSubtasksById(UUID todoId) {
        return subtaskRepository.findAllByTodoId(todoId);
    }

    public List<Subtask> generateAndSaveAiSubtasks(UUID todoId){
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new ResourceNotFoundException(TODO_NOT_FOUND));

        UUID todoOwnerId = SecurityUtils.getCurrentUserId();
        if (!todo.getUser().getId().equals(todoOwnerId)) {
            throw new UnauthorizedAccessException(TODO_OWNERSHIP_REQUIRED);
        }

        // 1. Call services to generate the subtasks
        List<String> generatedSubtasks = aiService.generateSubtasks(todo.getTitle(),todo.getDescription());

        // 2. Map and create each subtask base on the String list
        List<Subtask> newSubtasksList = generatedSubtasks.stream().map(title -> {
            Subtask newSubtask = new Subtask();
            newSubtask.setTitle(title);
            newSubtask.setCompleted(false);
            newSubtask.setTodo(todo);
            return subtaskRepository.save(newSubtask);
        }).toList();
        return newSubtasksList;

    }

}
