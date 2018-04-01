package me.saferoute.saferouteapp.Model;

import java.util.Date;

/**
 * Created by Rafael on 17/03/2018.
 */

public class Usuario {

    private int id;
    private String email, senha;
    private char genero;
    private Date data_nasc;

    public Usuario(){}
    public Usuario(int id, String email, char genero, Date data_nasc) {
        this.id = id;
        this.email = email;
        this.genero = genero;
        this.data_nasc = data_nasc;
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

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public char getGenero() {
        return genero;
    }
    public void setGenero(char genero) {
        this.genero = genero;
    }

    public Date getData_nasc() {
        return data_nasc;
    }
    public void setData_nasc(Date data_nasc) {
        this.data_nasc = data_nasc;
    }
}
