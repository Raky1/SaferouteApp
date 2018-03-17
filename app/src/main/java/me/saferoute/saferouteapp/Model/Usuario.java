package me.saferoute.saferouteapp.Model;

/**
 * Created by Rafael on 17/03/2018.
 */

public class Usuario {

    private int id;
    private String email;
    private char genero;
    private int idade;


    public Usuario(int id, String email, char genero, int idade) {
        this.id = id;
        this.email = email;
        this.genero = genero;
        this.idade = idade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}
