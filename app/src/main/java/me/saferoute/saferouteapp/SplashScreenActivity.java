package me.saferoute.saferouteapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity{

    private static int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Verifica conectividade com internet
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Sem Conexão com a Internet!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.show();
                } else {

                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

        ImageView splash = (ImageView) findViewById(R.id.imgSplash);
        ObjectAnimator animatorT = ObjectAnimator.ofFloat(splash, View.ALPHA, 0.0f, 1.0f);
        ObjectAnimator animatorUp = ObjectAnimator.ofFloat(splash, "translationY", 50f);
        animatorT.setDuration(SPLASH_TIME_OUT);
        animatorUp.setDuration(SPLASH_TIME_OUT);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorT, animatorUp);
        animatorSet.start();

    }
}
