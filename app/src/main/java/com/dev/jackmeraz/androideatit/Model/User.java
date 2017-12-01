package com.dev.jackmeraz.androideatit.Model;

/**
 * Created by jacobo.meraz on 17/11/2017.
 */

public class User {

    private String Name;
    private String Password;
    private String Telefono;

    public User() {
    }

    public User(String name, String password) {
        Name = name;
        Password = password;

    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
