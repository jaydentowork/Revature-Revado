package com.revature.revado.controllers;

import com.revature.revado.models.Subtask;
import com.revature.revado.repositories.UserRepository;
import com.revature.revado.services.SubtaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subtask")
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService, UserRepository userRepository){
        this.subtaskService = subtaskService;
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<List<Subtask>> getSubtasks(@PathVariable UUID todoId){
        List<Subtask> subtaskList = subtaskService.findSubtasksById(todoId);
        return ResponseEntity.ok(subtaskList);
    }

    @PostMapping("/create/{todoId}")
    public ResponseEntity<Subtask> createSubtask(@PathVariable UUID todoId, @RequestBody Subtask subtask){
        subtaskService.createSubtask(todoId,subtask);
        return ResponseEntity.ok(subtask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subtask> updateSubtask(@PathVariable UUID id, @RequestBody Subtask subtask){
        subtask.setId(id);
        subtaskService.updateSubtask(subtask);
        return ResponseEntity.ok(subtask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable UUID id){
        subtaskService.deleteSubtask(id);
        return ResponseEntity.noContent().build();
    }


    }
