package me.saferoute.saferouteapp.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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
    private String tipo, complemento, pertences;
    private Date data;
    private Time hora;
    private boolean boletim, agrecao;

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dia = format.format(data);

        format = new SimpleDateFormat("HH:mm");
        String tempo = format.format(hora);
        return this.id + ": " + this.tipo + " [" + dia +"] ["+tempo+"]";
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

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

    public String getPertences() { return pertences; }
    public void setPertences(String pertences) { this.pertences = pertences; }

}
