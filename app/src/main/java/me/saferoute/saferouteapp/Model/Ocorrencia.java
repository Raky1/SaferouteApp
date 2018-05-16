package me.saferoute.saferouteapp.Model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rafael on 17/03/2018.
 */

public class Ocorrencia implements Serializable, ClusterItem {

    private int id;
    private double latitude, longitude;
    private String complemento;
    private Date data;
    private Time hora;
    private boolean dinheiro, celular, veiculo, cartao, carteira, bolsa, bicicleta, documentos, outros;
    private boolean boletim, agrecao;
    private boolean visible;

    public Ocorrencia() {
        visible = true;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dia = format.format(data);

        format = new SimpleDateFormat("HH:mm");
        String tempo = format.format(hora);
        String info = dia +"*"+tempo+"*" +
                (dinheiro ? "1" : "0") + "*" +
                (celular ? "1" : "0") + "*" +
                (veiculo ? "1" : "0") + "*" +
                (cartao ? "1" : "0") + "*" +
                (carteira ? "1" : "0") + "*" +
                (bolsa ? "1" : "0") + "*" +
                (bicicleta ? "1" : "0") + "*" +
                (documentos ? "1" : "0") + "*" +
                (outros ? "1" : "0") + "*";


        int numeroHora = Integer.parseInt((tempo.split(":"))[0]);
        if(numeroHora > 5 && numeroHora < 18)
            info+= "0";
        else
            info+= "1";

        return info;
    }

    public void setFromJSON(JSONObject jsonOcor) {
        try {
            id = jsonOcor.getInt("id");
            latitude =jsonOcor.getDouble("latitude");
            longitude = jsonOcor.getDouble("longitude");

            dinheiro = jsonOcor.getString("dinheiro").equals("1");
            celular = jsonOcor.getString("celular").equals("1");
            veiculo = jsonOcor.getString("veiculo").equals("1");
            cartao = jsonOcor.getString("cartao").equals("1");
            carteira = jsonOcor.getString("carteira").equals("1");
            bolsa = jsonOcor.getString("bolsa").equals("1");
            bicicleta = jsonOcor.getString("bicicleta").equals("1");
            documentos = jsonOcor.getString("documentos").equals("1");
            outros = jsonOcor.getString("outros").equals("1");


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            data = new java.sql.Date(format.parse(jsonOcor.getString("dia")).getTime());

            format = new SimpleDateFormat("HH:mm");
            hora = new java.sql.Time(format.parse(jsonOcor.getString("hora")).getTime());

            boletim = jsonOcor.getString("boletim").equals("1");
            agrecao = jsonOcor.getString("agrecao").equals("1");

            complemento = jsonOcor.getString("complemento");

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    //pertences
    public boolean isDinheiro() { return dinheiro; }
    public void setDinheiro(boolean dinheiro) { this.dinheiro = dinheiro; }

    public boolean isCelular() { return celular; }
    public void setCelular(boolean celular) { this.celular = celular; }

    public boolean isVeiculo() { return veiculo; }
    public void setVeiculo(boolean veiculo) { this.veiculo = veiculo; }

    public boolean isCartao() { return cartao; }
    public void setCartao(boolean cartao) { this.cartao = cartao; }

    public boolean isCarteira() { return carteira; }
    public void setCarteira(boolean carteira) { this.carteira = carteira; }

    public boolean isBolsa() { return bolsa; }
    public void setBolsa(boolean bolsa) {  this.bolsa = bolsa; }

    public boolean isBicicleta() { return bicicleta; }
    public void setBicicleta(boolean bicicleta) { this.bicicleta = bicicleta; }

    public boolean isDocumentos() { return documentos; }
    public void setDocumentos(boolean documentos) { this.documentos = documentos; }

    public boolean isOutros() { return outros; }
    public void setOutros(boolean outros) { this.outros = outros; }

    //-----

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Time getHora() { return hora; }
    public void setHora(Time hora) { this.hora = hora; }

    public boolean isBoletim() { return boletim; }
    public void setBoletim(boolean boletim) { this.boletim = boletim; }

    public boolean isAgrecao() { return agrecao; }
    public void setAgrecao(boolean agrecao) { this.agrecao = agrecao; }


    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
