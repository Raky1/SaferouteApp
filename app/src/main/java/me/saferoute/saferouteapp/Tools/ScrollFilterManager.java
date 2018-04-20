package me.saferoute.saferouteapp.Tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.saferoute.saferouteapp.R;

public class ScrollFilterManager {

    Context context;

    private boolean[] pertences = {true, true, true, true, true, true, true, true, true};

    public ScrollFilterManager(Context context) {
        this.context = context;
        configScroll();
    }

    private void configScroll() {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private boolean changeStatePertence(ImageView img, boolean state) {
        state = !state;
        ColorMatrix matrix = new ColorMatrix();

        if(state) {
            matrix.setSaturation(1);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
        } else {
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
        }

        return state;
    }
}
