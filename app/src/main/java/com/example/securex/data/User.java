package com.example.securex.data;

public class User {

    private String Username;
    private String Email;
    private String Color;
    private String Pin;
    private String Password;
    private int Size;

    public User(String Username, String Email, String Color, String Pin, int Size, String Password) {
        this.Username=Username;
        this.Color=Color;
        this.Email=Email;
        this.Color=Color;
        this.Pin=Pin;
        this.Password=Password;
        this.Size=Size;
    }

    public String getUsername() {
        return Username;
    }

    public String getEmail() {
        return Email;
    }

    public String getColor() {
        return Color;
    }

    public String getPin() {
        return Pin;
    }

    public String getPassword() {
        return Password;
    }

    public int getSize() {
        return Size;
    }


}