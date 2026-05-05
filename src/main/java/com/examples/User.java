package com.examples;

public class User {
    private int id;
    private String name;
    private String email;

    // Конструктор для створення нового користувача (без id)
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Конструктор для зчитування з БД (з id)
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Геттери та сеттери
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}