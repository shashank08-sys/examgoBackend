package com.example.examgo.config;

import com.example.examgo.model.Subject;
import com.example.examgo.repository.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initSubjects(SubjectRepository subjectRepository) {
        return args -> {
            if (subjectRepository.count() == 0) {
                subjectRepository.save(new Subject("maths", "Mathematics", 1240));
                subjectRepository.save(new Subject("reasoning", "Reasoning", 980));
                subjectRepository.save(new Subject("gk", "General Knowledge", 1540));
                subjectRepository.save(new Subject("english", "English", 870));
                subjectRepository.save(new Subject("science", "General Science", 1100));
                subjectRepository.save(new Subject("computers", "Computer Awareness", 620));
            }
        };
    }
}
