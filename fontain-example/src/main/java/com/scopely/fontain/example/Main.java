package com.scopely.fontain.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;


public class Main extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fontain.init(this, R.string.default_font_family);
        Fontain.applyFontToViewHierarchy(getActionBarView(), Fontain.getFontFamily("MedievalSharp"), Weight.NORMAL, Width.NORMAL, Slope.NORMAL);
        setContentView(R.layout.activity_main);

        ViewGroup container = (ViewGroup) findViewById(R.id.textViewContainer);
        rotateFonts(container);
    }

    private void rotateFonts(final ViewGroup container) {
        final String[] fontFamilies = new String[]{"MedievalSharp", "LS", "PTSans"};
        final int[] index = new int[]{0};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Fontain.applyFontFamilyToViewHierarchy(container, Fontain.getFontFamily(fontFamilies[index[0]]));
                index[0]++;
                if (index[0] > 2){
                    index[0] = 0;
                }
                container.postDelayed(this, 3000);
            }
        };
        runnable.run();
    }

    public View getActionBarView() {
        Window window = getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
    }
}
