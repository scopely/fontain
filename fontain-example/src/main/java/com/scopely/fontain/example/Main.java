package com.scopely.fontain.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.scopely.fontain.Fontain;
import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;


public class Main extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fontain.init(this, "PTSans");
        Fontain.applyFontToViewHierarchy(getActionBarView(), Fontain.getFontFamily("MedievalSharp"), Weight.NORMAL.value, Width.NORMAL.value, Slope.NORMAL.value);
        setContentView(R.layout.activity_main);
    }

    public View getActionBarView() {
        Window window = getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
    }
}
