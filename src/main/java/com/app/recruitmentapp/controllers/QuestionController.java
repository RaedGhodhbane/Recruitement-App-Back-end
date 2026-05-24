package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.QuestionDTO;
import com.app.recruitmentapp.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Questions", description = "Gestion des questions associées aux offres")
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Operation(summary = "Liste des questions", description = "Retourne toutes les questions")
    @GetMapping("/questions")
    public List<QuestionDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @Operation(summary = "Détail d'une question", description = "Retourne une question par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter une question", description = "Ajoute une question à une offre")
    @PostMapping("/{idOffer}")
    public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable Long idOffer) {
        QuestionDTO saved = questionService.saveQuestion(questionDTO, idOffer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une question", description = "Met à jour une question existante")
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        try {
            QuestionDTO updated = questionService.updateQuestion(id, questionDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Supprimer une question", description = "Supprime une question par son ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok("Question deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
