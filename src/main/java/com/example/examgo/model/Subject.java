package com.example.examgo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subjects")
public class Subject {
    @Id
    private String id;
    private String name;
    private int questions;

    public Subject() {}
    public Subject(String id, String name, int questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuestions() { return questions; }
    public void setQuestions(int questions) { this.questions = questions; }
}
