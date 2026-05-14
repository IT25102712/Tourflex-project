package com.tourflex.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Column(unique = true, nullable = false)
    private String phone;

    private String address;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    // Getters and Setters
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public User() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}