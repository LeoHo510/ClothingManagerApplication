package com.example.appmanager.Model;

public class User {
    int iduser;
    String password, firstname, lastname, address, phonenumber, email;

    public User(int iduser, String password, String firstname, String lastname, String address, String phonenumber, String email) {
        this.iduser = iduser;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
