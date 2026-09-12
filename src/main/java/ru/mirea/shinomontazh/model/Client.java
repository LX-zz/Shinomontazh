package ru.mirea.shinomontazh.model;

public class Client{
    private int id;
    private String fullName;
    private String phone; 
    private String email;

    public Client(int id, String fullName, String phone, String email){
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }
    public int getId(){
        return id;
    }
    public String getFullName(){
        return fullName;   
        }
    public String getPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }
}