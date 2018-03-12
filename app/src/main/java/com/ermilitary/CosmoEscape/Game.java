package com.ermilitary.CosmoEscape;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameElement.*;
import com.ermilitary.CosmoEscape.Level.Level;

import javax.microedition.khronos.opengles.GL10;
import java.io.*;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Game {
    public static float width;
    public static float height;
    public static float ratio;
    // сейв отчаяния
    public static int progress = 6;

    // Элемент игры который работает в данный момент
    ActiveElement activeElement;

    // При старте игры запускается меню
    Game(Context context){
        ActiveElement.context = context;
        activeElement = new StartMenu();
        Load();
    }

    public void Load(){
        try {
            InputStream inputStream = ActiveElement.context.openFileInput("saves");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                line = reader.readLine();
//                while ((line = reader.readLine()) != null) {
//
//                }
                inputStream.close();
                progress = Integer.parseInt(line);
            }
        } catch (Throwable t) {}
    }

    public void Save(){
        int n_progress = 0;
        Level cur_l = null;
        if(activeElement.getClass().getSimpleName().equals("Level_1")) {
            cur_l = (Level)activeElement;
            n_progress = 1;
        }
        if(activeElement.getClass().getSimpleName().equals("Level_2")) {
            cur_l = (Level)activeElement;
            n_progress = 2;
        }
        if(activeElement.getClass().getSimpleName().equals("Level_3")) {
            cur_l = (Level)activeElement;
            n_progress = 3;
        }
        if(activeElement.getClass().getSimpleName().equals("Level_4")) {
            cur_l = (Level)activeElement;
            n_progress = 4;
        }
        if(activeElement.getClass().getSimpleName().equals("Level_5")) {
            cur_l = (Level)activeElement;
            n_progress = 5;
        }
        if(activeElement.getClass().getSimpleName().equals("Level_6")) {
            cur_l = (Level)activeElement;
            n_progress = 6;
        }
        if(cur_l != null && cur_l.victory && progress < n_progress) {
            progress = n_progress;
            saveToFile();
        }
    }
    public void saveToFile(){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(ActiveElement.context.openFileOutput("saves", 0));
            osw.write(""+progress);
            osw.close();
        } catch (Throwable t) {}
    }

    // При изменении changeElement заменяет текущий элемент выбранным
    private void setActiveElement(int element){
            ActiveElement.loading = true;
            switch (element){
                case ActiveElement.MENU:
                    Save();
                    activeElement = new StartMenu();
                    break;
                case ActiveElement.LEVEL_1:
                    activeElement = new Level_1();
                    break;
                case ActiveElement.LEVEL_2:
                    activeElement = new Level_2();
                    break;
                case ActiveElement.LEVEL_3:
                    activeElement = new Level_3();
                    break;
                case ActiveElement.LEVEL_4:
                    activeElement = new Level_4();
                    break;
                case ActiveElement.LEVEL_5:
                    activeElement = new Level_5();
                    break;
                case ActiveElement.LEVEL_6:
                    activeElement = new Level_6();
                    break;
            }
        }

    public void resource_load(GL10 gl){
        activeElement.resource_load(gl);
    }

    // Отслеживает изменение changeElement и передает draw() элементу
    public void draw(GL10 gl) {
        activeElement.Draw(gl);
        if (activeElement.changeElement != 0)
            setActiveElement(activeElement.changeElement);
    }

    // Передает inPut() элементу
    public boolean touch(MotionEvent event){
            return activeElement.InPut(event);
        }
}
