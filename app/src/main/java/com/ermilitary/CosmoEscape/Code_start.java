package com.ermilitary.CosmoEscape;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Code_start extends Activity implements View.OnTouchListener{

    public GLSurfaceView glsv;
    public Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glsv = new GLSurfaceView(this);
        glsv.setEGLConfigChooser(false);
        game = new Game(this);
        glsv.setRenderer(new GameRenderer(game));
        setContentView(glsv);
        glsv.setOnTouchListener(this);
    }

    // Передает inPut() игре
    @Override public boolean onTouch(View view, MotionEvent event){
        return game.touch(event);
    }
    @Override public void onResume(){
        super.onResume();
        glsv.onResume();
        // Превращает меню в точки
        glsv.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }
    @Override public void onPause(){
        super.onPause();
        glsv.onPause();
    }
}
