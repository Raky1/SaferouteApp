package me.saferoute.saferouteapp.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CacheData {

    /**
     * Email
     */
    public static boolean SaveEmail(String email, Context context) {
        Log.d("INFO", "Save Email");

        try {
            File file = new File(context.getCacheDir(), "data1");

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(email);
            bw.close();
            fw.close();

            return true;
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            return false;
        }
    }

    public static String GetEmail(Context context) {
        Log.d("INFO", "Load Email");
        try{
            File file = new File(context.getCacheDir(), "data1");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String email = br.readLine();
            if(email.equals(""))
                return "";
            return email;

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            return "";
        }
    }

    /**
     * location
     */
    public static boolean SaveLastLocation(LatLng latLng, Context context) {
        Log.d("INFO", "Save Location");
        try {

            File file = new File(context.getCacheDir(), "data2");

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(latLng.latitude+";"+latLng.longitude);
            bw.close();
            fw.close();

            return true;
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            return false;
        }
    }

    public static LatLng GetLastLocation(Context context) {
        Log.d("INFO", "Load Location");
        try{
            File file = new File(context.getCacheDir(), "data2");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String ll = br.readLine();
            if(ll.equals(""))
                return null;

            String[] llSplit = ll.toString().split(";");
            double lat = Double.parseDouble(llSplit[0]);
            double lon = Double.parseDouble(llSplit[1]);

            LatLng latLng = new LatLng(lat, lon);

            return latLng;

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            return null;
        }
    }

    /**
     * Zoom
     */
    public static boolean SaveLastZoom(float zoom, Context context) {
        Log.d("INFO", "Save Zoom");
        try {
            File file = new File(context.getCacheDir(), "data3");

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Float.toString(zoom));
            bw.close();
            fw.close();

            return true;
        } catch (IOException e) {
            Log.d("ERROR", e.getMessage());
            return false;
        }
    }

    public static float GetLastZoom(Context context) {
        Log.d("INFO", "Load Zoom");
        try{
            File file = new File(context.getCacheDir(), "data3");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String zoom = br.readLine();

            if(zoom.equals(""))
                return 0;

            return Float.parseFloat(zoom.toString());

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            return 0;
        }
    }
}
