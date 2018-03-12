package com.ermilitary.CosmoEscape.GameObject;

import android.graphics.Color;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 18.04.2015.
 */
public class Text extends Collider{
    float size;
    int length;
    int n;
    int cor_x;

    public void print(GL10 gl, Vector pos,float s, float r, float g, float b, String str){
        length = str.length();
        cor_x = 0;
        setPosition(pos);
        setScale(size = s);
        gl.glColor4f(r,g,b,1);
        for(n = 0; n < length; n++){
            //setTexCord(new Vector(0,0,0), new Vector(1/6f,1/7f,0));
            if(set_tex_pos(str.charAt(n))){
                position.x += size*1.5f;
                draw(gl);
            }
        }
        gl.glColor4f(1, 1, 1, 1);
    }
    public boolean set_tex_pos(char code){
        float max_x = 8;
        float max_y = 7;
        Vector ul = new Vector(0,0,0);
        Vector dr = new Vector(0,0,0);
        switch (code) {
            case '\n':
                position.y -= size*2; position.x -= (n-cor_x)*size*1.5f; cor_x += n - cor_x + 1;
                return false;
            case 'А':
            case 'а':
                ul.set(0, 0); dr.set(1/max_x,1/max_y);
                setTexCord(ul, dr);
                break;
            case 'Б':
            case 'б': ul.set(1/max_x,0); dr.set(2/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case 'В':
            case 'в': ul.set(2/max_x,0); dr.set(3/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case 'Г':
            case 'г': ul.set(3/max_x,0); dr.set(4/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case 'Д':
            case 'д': ul.set(4/max_x,0); dr.set(5/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case 'Е':
            case 'е': ul.set(5/max_x,0); dr.set(6/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ё':
            case 'ё': ul.set(0/max_x,1/max_y); dr.set(1/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ж':
            case 'ж': ul.set(1/max_x,1/max_y); dr.set(2/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case 'З':
            case 'з': ul.set(2/max_x,1/max_y); dr.set(3/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case 'И':
            case 'и': ul.set(3/max_x,1/max_y); dr.set(4/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case 'Й':
            case 'й': ul.set(4/max_x,1/max_y); dr.set(5/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case 'К':
            case 'к': ul.set(5/max_x,1/max_y); dr.set(6/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case 'Л':
            case 'л': ul.set(0/max_x,2/max_y); dr.set(1/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case 'М':
            case 'м': ul.set(1/max_x,2/max_y); dr.set(2/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case 'Н':
            case 'н': ul.set(2/max_x,2/max_y); dr.set(3/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case 'О':
            case 'о': ul.set(3/max_x,2/max_y); dr.set(4/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case 'П':
            case 'п': ul.set(4/max_x,2/max_y); dr.set(5/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case 'Р':
            case 'р': ul.set(5/max_x,2/max_y); dr.set(6/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case 'С':
            case 'с': ul.set(0/max_x,3/max_y); dr.set(1/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case 'Т':
            case 'т': ul.set(1/max_x,3/max_y); dr.set(2/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case 'У':
            case 'у': ul.set(2/max_x,3/max_y); dr.set(3/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ф':
            case 'ф': ul.set(3/max_x,3/max_y); dr.set(4/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case 'Х':
            case 'х': ul.set(4/max_x,3/max_y); dr.set(5/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ц':
            case 'ц': ul.set(5/max_x,3/max_y); dr.set(6/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ч':
            case 'ч': ul.set(0/max_x,4/max_y); dr.set(1/max_x,5/max_y);
                setTexCord(ul, dr);
                break;
            case 'Ш':
            case 'ш': ul.set(1/max_x,4/max_y); dr.set(2/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case 'Щ':
            case 'щ': ul.set(2/max_x,4/max_y); dr.set(3/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ь':
            case 'ь': ul.set(3/max_x,4/max_y); dr.set(4/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ъ':
            case 'ъ': ul.set(4/max_x,4/max_y); dr.set(5/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case 'Э':
            case 'э': ul.set(5/max_x,4/max_y); dr.set(6/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ю':
            case 'ю': ul.set(0/max_x,5/max_y); dr.set(1/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case 'Я':
            case 'я': ul.set(1/max_x,5/max_y); dr.set(2/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '0': ul.set(2/max_x,5/max_y); dr.set(3/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '1': ul.set(3/max_x,5/max_y); dr.set(4/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '2': ul.set(4/max_x,5/max_y); dr.set(5/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '3': ul.set(5/max_x,5/max_y); dr.set(6/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '4': ul.set(0/max_x,6/max_y); dr.set(1/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case '5': ul.set(1/max_x,6/max_y); dr.set(2/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case '6': ul.set(2/max_x,6/max_y); dr.set(3/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case '7': ul.set(3/max_x,6/max_y); dr.set(4/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case '8': ul.set(4/max_x,6/max_y); dr.set(5/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case '9': ul.set(5/max_x,6/max_y); dr.set(6/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case 'Ы':
            case 'ы': ul.set(6/max_x,0/max_y); dr.set(7/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case '.': ul.set(6/max_x,1/max_y); dr.set(7/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case ',': ul.set(6/max_x,2/max_y); dr.set(7/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case '-': ul.set(6/max_x,3/max_y); dr.set(7/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case '!': ul.set(6/max_x,4/max_y); dr.set(7/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case '?': ul.set(6/max_x,5/max_y); dr.set(7/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '+': ul.set(6/max_x,6/max_y); dr.set(7/max_x,7/max_y);
                setTexCord(ul,dr);
                break;
            case '_': ul.set(7/max_x,0/max_y); dr.set(8/max_x,1/max_y);
                setTexCord(ul,dr);
                break;
            case '$': ul.set(7/max_x,1/max_y); dr.set(8/max_x,2/max_y);
                setTexCord(ul,dr);
                break;
            case '^': ul.set(7/max_x,2/max_y); dr.set(8/max_x,3/max_y);
                setTexCord(ul,dr);
                break;
            case '(': ul.set(7/max_x,3/max_y); dr.set(8/max_x,4/max_y);
                setTexCord(ul,dr);
                break;
            case ')': ul.set(7/max_x,4/max_y); dr.set(8/max_x,5/max_y);
                setTexCord(ul,dr);
                break;
            case '<': ul.set(7/max_x,5/max_y); dr.set(8/max_x,6/max_y);
                setTexCord(ul,dr);
                break;
            case '>': ul.set(7/max_x,6/max_y); dr.set(8/max_x,7/max_y);
                setTexCord(ul,dr);
                break;

            default: ul.set(0,0); dr.set(0,0);
                setTexCord(ul,dr);
                break;
        }
        return true;
    }
}
