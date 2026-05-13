package com.example.examgo.controller;

import com.example.examgo.model.Subject;
import com.example.examgo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public List<Subject> getSubjects() {
        System.out.println("Fetching subjects from database...");
        return subjectRepository.findAll();
    }
}
