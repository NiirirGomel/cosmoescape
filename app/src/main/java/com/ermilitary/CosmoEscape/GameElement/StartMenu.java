package com.ermilitary.CosmoEscape.GameElement;


import android.media.MediaPlayer;
import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.R;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class StartMenu extends ActiveElement {
    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    Vector first_move;
    Vector next_move;
    float distance_move = 0;

    int textures[] = new int[10];
    Sprite background = new Sprite();
    Sprite menu_ramka = new Sprite();
    Sprite menu_line = new Sprite();
    Sprite menu_text_ramka = new Sprite();
    Collider[] menu_options = new Collider[6];
    int target = 0;
    float anim_target = 0;
    int direction = 0;

    public StartMenu(){
        background.setScale(2.52f,1,1);
        menu_ramka.setScale(0.35f);
        menu_line.setScale(0.22f,0.025f,1);
        menu_text_ramka.setScale(1f,0.3f,1);
        for(int i = 0; i < 6; i++){
            menu_options[i] = new Collider();
            menu_options[i].setScale(0.32f);
            menu_options[i].sphere_collider = 0.32f;

        }
    }

    @Override
    public void resource_load(GL10 gl) {
        textures[0] = res_to_tex(gl,R.drawable.menu_bg);
        textures[1] = res_to_tex(gl,R.drawable.menu_level1);
        textures[2] = res_to_tex(gl,R.drawable.menu_level2);
        textures[3] = res_to_tex(gl,R.drawable.menu_level3);
        textures[4] = res_to_tex(gl,R.drawable.menu_level4);
        textures[5] = res_to_tex(gl,R.drawable.menu_level5);
        textures[6] = res_to_tex(gl,R.drawable.menu_level6);
        textures[7] = res_to_tex(gl,R.drawable.menu_ramka);
        textures[8] = res_to_tex(gl,R.drawable.menu_line);
        textures[9] = res_to_tex(gl,R.drawable.menu_text_ramka);

        background.setSprite(textures[0]);
        menu_ramka.setSprite(textures[7]);
        menu_line.setSprite(textures[8]);
        menu_text_ramka.setSprite(textures[9]);
        for(int i = 0; i < 6; i++)
            menu_options[i].setSprite(textures[i+1]);
        super.resource_load(gl);
    }

    public void resource_free(GL10 gl){
        gl.glDeleteTextures(10,textures,0);
    }

    public void draw(GL10 gl) {
        // выравнивание по тарджету
        if(distance_move == 0) {
            if (target * 1.1f > anim_target) direction = RIGHT;
            if (target * 1.1f < anim_target) direction = LEFT;
            anim_target += 3 * direction * delayTime / 1000;
            if ((direction == RIGHT && anim_target > target * 1.1f) || (direction == LEFT && anim_target < target * 1.1f)) {
                anim_target = target * 1.1f;
                direction = 0;
            }
        }

        background.setPosition(-anim_target * 0.3f + (3 * 0.3f) - 0.1f, 0, 0);
        background.draw(gl);

        if(menu_text_ramka.position.y < menu_options[target].position.y + 0.8f)
            menu_text_ramka.position.y += 3 * delayTime / 1000;
        else
            menu_text_ramka.position.y = menu_options[target].position.y + 0.8f;
        menu_text_ramka.setPosition(menu_options[target].position.x, menu_text_ramka.position.y, 0);

        float scale = menu_text_ramka.position.y/(menu_options[target].position.y + 0.8f);
        menu_text_ramka.setScale(1*scale,0.3f*scale,1*scale);
        menu_text_ramka.draw(gl);

        for(int i = 0; i < 6; i++) {
            menu_options[i].setPosition(i*1.1f - anim_target, -0.2f, 0);
            menu_ramka.setPosition(menu_options[i].position);
            menu_options[i].draw(gl);
            menu_ramka.draw(gl);
            if(i < 5){
                menu_line.setPosition(menu_options[i].position.x+0.55f,menu_options[i].position.y,0);
                menu_line.draw(gl);
            }
        }
    }

    @Override public boolean inPut(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                first_move = Vector.prepare_event(event);
                distance_move = 0;
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                next_move = Vector.prepare_event(event);
                if(distance_move < 0.05 && menu_options[target].isTouchSphere(first_move))
                    if (Game.progress >= target) nextElement = target+1; // переключение уровней
                distance_move = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                next_move = Vector.prepare_event(event);
                anim_target -= (next_move.x - first_move.x)*1.1f;
                if(anim_target < 0)
                    anim_target = 0;
                if(anim_target > 5*1.1f)
                    anim_target = 5*1.1f;
                distance_move += Vector.Distance(first_move, next_move);
                first_move.set(next_move);

                // выбот тарджета
                float ostatok = anim_target % 1.1f;
                if (ostatok > 0.55) {
                    if ((int) (anim_target / 1.1f) + 1 < 5*1.1f && target != (int) (anim_target / 1.1f) + 1) {
                        menu_text_ramka.position.y = 0;
                        target = (int) (anim_target / 1.1f) + 1;
                    }
                }
                else {
                    if (anim_target / 1.1f > 0 && target != (int) (anim_target / 1.1f)) {
                        menu_text_ramka.position.y = 0;
                        target = (int) (anim_target / 1.1f);
                    }
                }
                break;
        }
        return true;
    }
}
