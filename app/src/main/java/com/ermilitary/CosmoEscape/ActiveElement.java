package com.ermilitary.CosmoEscape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.GameObject.Text;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 30.03.2015.
 */
public class ActiveElement {
    public static final int MENU = -1;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;
    public static final int LEVEL_5 = 5;
    public static final int LEVEL_6 = 6;
    public static Context context;

    // Используя константы выше позволяет переключать элементы родительскому классу
    public static boolean loading = false;
    public int nextElement = 0;
    public int changeElement = 0;

    public static long time;
    public static float delayTime;

    public static Text text = new Text();

    public static void printText(GL10 gl, Vector pos,float size, float r, float g, float b, String str) {
        text.print(gl,pos,size,r,g,b,str);
    }
    public void SayOne(float time, Unit unit, String say) {}
    // Загрузка текстуры по id
    public int res_to_tex(GL10 gl,int res_id){
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        int tex_id = textures[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV,GL10.GL_TEXTURE_ENV_MODE,GL10.GL_REPLACE);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), res_id);
        GLUtils.texImage2D(gl.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
        return tex_id;
    }

    public void resource_load(GL10 gl){
        text.setSprite(res_to_tex(gl,R.drawable.alfavit));
        time = SystemClock.uptimeMillis();
    }
    public void resource_free(GL10 gl){
        int T_i[] = {text.texture};
        gl.glDeleteTextures(1,T_i,0);
    }
    public void draw(GL10 gl){}
    public boolean inPut(MotionEvent event){return true;}

    // Функции переопределяются в родительском классе
    public void Draw(GL10 gl){
        if(nextElement != 0){
            resource_free(gl);
            changeElement = nextElement;
            return;
        }
        if(loading){
            resource_load(gl);
            loading = false;
        }
        delayTime = SystemClock.uptimeMillis() - time;
        time = SystemClock.uptimeMillis();
        draw(gl);
    }
    public boolean InPut(MotionEvent event){
        if(!loading) {
            inPut(event);
            if (nextElement != 0)
                loading = true;
        }
        return true;
    }
//    public void print(GL10 gl,String str){
//        text.print(gl,,0.5,1,1,1,"Привет мир");
//    }
}
