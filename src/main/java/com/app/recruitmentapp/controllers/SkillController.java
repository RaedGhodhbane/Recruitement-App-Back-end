package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Skill;
import com.app.recruitmentapp.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/skill")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping("/skills")
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        Optional<Skill> skill = skillService.getSkillById(id);
        return skill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{idCandidate}")
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill, @PathVariable Long idCandidate) {
        Skill savedSkill = skillService.saveSkill(skill,idCandidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSkill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        try {
            Skill updatedSkill = skillService.updateSkill(id, skill);
            return ResponseEntity.ok(updatedSkill);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long id) {
        try {
            skillService.deleteSkill(id);
            return ResponseEntity.ok("Skill deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
