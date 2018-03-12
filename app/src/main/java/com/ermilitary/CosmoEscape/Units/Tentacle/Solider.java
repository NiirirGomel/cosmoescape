package com.ermilitary.CosmoEscape.Units.Tentacle;

import android.os.Debug;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.LinkedList;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Solider extends TenUnit {
    float slash_timer;
    float cooldown;
    boolean slash;
    LinkedList<Unit> enemy;
    float dmg;
    boolean slash_atack;

    public Solider(GeoSystem gs){
        super(gs);

        max_tian = 2;
        tian = 0;

        setSphere(0.11f);
        setScale(0.07f,0.13f,1);
        cash = 5;
        health = max_health = 1300;

        max_m_speed = 0.22f;
        max_r_speed = 180f;

        vis_zone = 0.3f;

        dmg = 100;
        slash = false;
        slash_atack = false;
        slash_timer = 0;
        cooldown = 0;
        enemy = new LinkedList<Unit>();

        delete = 0.3f;
    }

    @Override
    public void draw(GL10 gl) {
        // поиск врага
        // если есть тарджет ускоряеться и меняе манеру движения
        // долетая до врага атакует его
        // в откате держит дистанцию до цели и теряет ускорение
        if(death){ Death(gl); return; }
        max_r_speed = 180f;
        if (slash && (slash_timer += ActiveElement.delayTime / 1000) < 0.3f) {
            airblade.setPosition(position.x+ Vector.getDirection(rotation + 330).x*0.13f,position.y+Vector.getDirection(rotation + 330).y*0.13f,0);
            airblade.setRotation(rotation + 313);
            airblade.setScale(0.08f, 0.005f, 1);
            airblade.draw(gl);
            airblade.setPosition(position.x+ Vector.getDirection(rotation + 30).x*0.13f,position.y+Vector.getDirection(rotation + 30).y*0.13f,0);
            airblade.setRotation(rotation + 47);
            airblade.setScale(0.08f, 0.01f, 1);
            airblade.draw(gl);
            moving = true;
            for (Unit unit : geoSistem.units){
                if (!unit.death && unit.pack.tag == Pack.TAG_TIAN && isCollision(unit)){
                    boolean add = true;
                    for (Unit e : enemy)
                        if (unit == e) add = false;
                    if (add) {
                        unit.dealDamage(dmg);
                        enemy.add(unit);
                    }
                }
            }
        } else {
            if (slash) {
                slash_timer = 0;
                enemy.clear();
                m_speed = 0.8f;
                max_m_speed = 0.22f;
                slash = false;
            }
            if (pack.target_enemy != null) {
                // развидеть Х_x
                if (target != null && (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death)) {
                    target = null;
                }

                // видеть 0_0
                float cur_dist = Float.MAX_VALUE;
                for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack == pack.target_enemy && Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < vis_zone) {
                        if (Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < cur_dist){
                            cur_dist = Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider;
                            target = unit;
                        }
                    }
                // *поиск вражины*
                if (target == null) {
                    // уступай и не толкай сородичей своих
                    notPush();
                    // в стан врага лети ты
                    if (!moving && !rotating) {
                        rotating = true;
                        moving = true;
                        rot_to(pack.target_enemy.position);
                    }
                }
                // *

                // тарджет есть
                else {
                    // не толкатцо! ч_ч
                    notPush();
                    if (!moving && !rotating) {
                        max_r_speed = 300f;
                        if (Vector.Distance(position, target.position) - target.sphere_collider > vis_zone / 2) {
                            moving = true;
                            rotating = true;
                            rot_to(target.position);
                            // если добрался до цели
                        } else {
                            float razn = Vector.getAngle(target.position.minus(position));
                            float rot_a;
                            if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
                            float angle;
                            if ((angle = rot_a - razn) < 0) angle += 360;  // от а до б справа
                            if (angle > 5f && angle < 360 - 5f) { // радиус атаки 10 градусов
                                rotating = true;
                                rot_to(target.position);
                            } else {
                                // начать атаку
                                slash = true;
                                m_speed = 2;
                                max_m_speed = 2;
                                cooldown = 0;
                            }
                        }
                    }
                }
            }
        }
        super.draw(gl);
    }

    public void Death(GL10 gl) {
        float timer = 1 - delete/0.3f*1;
        explosion.setPosition(position.x+0.02f,position.y-0.01f,0);
        explosion.setScale(0.03f + 0.06f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.01f, position.y + 0.05f, 0);
        explosion.setScale(0.02f + 0.04f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.03f, position.y - 0.03f, 0);
        explosion.setScale(0.03f + 0.06f * timer);
        explosion.draw(gl);
        airblade.setPosition(position.x+Vector.getDirection(rotation + 8).x*0.2f*timer,position.y+Vector.getDirection(rotation + 8).y*0.2f*timer,0);
        airblade.setRotation(rotation + 8);
        airblade.setScale(0.1f, 0.03f, 1);
        airblade.draw(gl);
        airblade.setPosition(position.x+Vector.getDirection(rotation + 172).x*0.2f*timer,position.y+Vector.getDirection(rotation + 172).y*0.2f*timer,0);
        airblade.setRotation(rotation + 172);
        airblade.setScale(0.1f ,0.03f ,1);
        airblade.draw(gl);
        delete -= ActiveElement.delayTime / 1000;
    }

    @Override
    public void resLoad() {
        super.resLoad();
        setSprite(geoSistem.textures[39]);
    }
}
