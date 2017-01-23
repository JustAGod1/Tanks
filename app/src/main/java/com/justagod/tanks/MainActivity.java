package com.justagod.tanks;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.justagod.tanks.Rendering.WorldRenderer;
import com.justagod.tanks.WorldProviding.HUD;
import com.justagod.tanks.WorldProviding.World;

public class MainActivity extends AppCompatActivity {

    public static MainActivity activity;
    private GLSurfaceView mSurface;
    private GLSurfaceView.Renderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        World.generateNewWorld();



        mSurface = new GLSurfaceView(MainActivity.this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                HUD.instance.onTouchEvent(event);
                return super.onTouchEvent(event);

            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mRenderer = new WorldRenderer(getMetrics().widthPixels, getMetrics().heightPixels);

        mSurface.setRenderer(mRenderer);

        setContentView(mSurface);


    }

    private DisplayMetrics getMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurface.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurface.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
