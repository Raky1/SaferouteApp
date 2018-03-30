package me.saferoute.saferouteapp.DAO;

import me.saferoute.saferouteapp.Model.Usuario;

public class UsuarioDAO {

    private static final String URL_USER = "http://saferoute.me/php/Executor/mUsuarioExec.php";

    public boolean inserir(Usuario usuario) {
        String parametros = "email=" + usuario.getEmail() +
                "&senha=" + usuario.getSenha() +
                "&genero=" + usuario.getGenero() +
                "&data_nasc=" + usuario.getData_nasc().toString() +
                "&action=inserir";

        new RequestData().execute(URL_USER, parametros);

        return false;
    }
}
