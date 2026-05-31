package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.dto.QuestionDTO;
import com.app.recruitmentapp.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/{idOffer}")
    public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionDTO questionDTO, @PathVariable Long idOffer) {
        QuestionDTO saved = questionService.saveQuestion(questionDTO, idOffer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Modifier une question", description = "Met à jour une question existante")
    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/{id}")
    public QuestionDTO updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        return questionService.updateQuestion(id, questionDTO);
    }

    @Operation(summary = "Supprimer une question", description = "Supprime une question par son ID")
    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }
}
