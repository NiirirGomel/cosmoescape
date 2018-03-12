package com.ermilitary.CosmoEscape.Units.Tian;

import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Medic extends TanUnit {
    float heal_distance;
    public Medic(GeoSystem gs){
        super(gs);
        setSphere(0.1f);

        max_tian = 9;
        tian = need_tian = 3;

        setScale(0.08f,0.1f,1);
        health = max_health = 1300;

        max_m_speed = 0.18f;
        max_r_speed = 170f;

        vis_zone = 0.4f;
        delete = 0.3f;

        heal_distance = 0.25f;
    }

    private void Heal(Unit unit){
        if ((unit.health += ActiveElement.delayTime / 1000 * 20) > unit.max_health) unit.health = unit.max_health;
    }

    @Override
    public void draw(GL10 gl) {
        if(death){ Death(gl); return; }
        // уст скорость корабля
        max_m_speed = 0.18f;
        max_r_speed = 170f;
        for (Unit unit : geoSistem.units) {
            if (!unit.death && unit != this && unit.pack == pack && unit.health < unit.max_health) {
                max_m_speed = 0.32f;
                max_r_speed = 170f;
                break;
            }
        }
        // медим все что по близости
        for (Unit unit : geoSistem.units) {
            if (!unit.death && unit != this && unit.pack == pack && unit.health < unit.max_health && Vector.Distance(unit.position, position) - unit.sphere_collider < heal_distance)
                Heal(unit);
        }

        // в бою
        if (pack.target_enemy != null) {
            // собирание потерпевших тян
            if (!moving) medFindTian();
            // соблюдаем формацию
            if (!moving) packFormation();
            // убегаем от монстров
            if (!moving) {
                for (Unit unit : geoSistem.units) {
                    if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && Vector.Distance(unit.position, position) - unit.sphere_collider < vis_zone) {
                        moving = true;
                        rotating = true;
                        rot_from(unit.position);
                    }
                }
            }
        } else {
        // вне боя
            // не толкатьсо
            if (!moving) notPush();
            // подлетаем к пострадавшим
            if (!moving) {
                for (Unit unit : geoSistem.units) {
                    if (!unit.death && unit != this && unit.pack == pack && unit.health < unit.max_health) {
                        if (Vector.Distance(unit.position, position) - unit.sphere_collider > heal_distance) {
                            moving = true;
                            rotating = true;
                            rot_to(unit.position);
                            break;
                        }
                    }
                }
            }
        }
        super.draw(gl);
    }

    public void medFindTian(){
        // развидеть Х_x
        //if (target != null && (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death))
        //    target = null;
        if (target != null && (Vector.Distance(pack.position, target.position) - target.sphere_collider > pack.vis_zone || target.death))
            target = null;

        // видеть 0_0
        float cur_dist = Float.MAX_VALUE;
        for (Unit unit : geoSistem.units)
            if (!unit.death && unit.pack.tag == Pack.TAG_RESORCES && Vector.Distance(pack.position, unit.position) - unit.sphere_collider < pack.vis_zone) {
                if (Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < cur_dist){
                    cur_dist = Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider;
                    target = unit;
                }
            }
        // *поиск ресурса*
        if (target != null) { // тарджет есть
            if (tian < max_tian) {
                if (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > 0.05f) {
                    moving = true;
                    rotating = true;
                    rot_to(target.position);
                } else { // если добрался до цели
                    pickUp(target);
                }
            }
        }
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
        airblade.setPosition(position.x+ Vector.getDirection(rotation + 8).x*0.2f*timer,position.y+Vector.getDirection(rotation + 8).y*0.2f*timer,0);
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
        setSprite(geoSistem.textures[12]);
    }
}
