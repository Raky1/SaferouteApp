package me.saferoute.saferouteapp.Tools;

import android.content.Context;
import android.widget.Toast;

public class Validacao {

    public static boolean CheckEmail(String email) {
        if(!email.isEmpty())
            if(email.contains("@"))
                return true;

        return false;
    }

    public static boolean CheckSenha(String senha) {
        if(!senha.isEmpty())
            if(senha.length() >= 6)
                return true;

        return false;
    }
}
