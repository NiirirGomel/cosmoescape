package com.ermilitary.CosmoEscape.Units.Tentacle;

import android.util.Log;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Parasite extends TenUnit {
    Collider laser;
    boolean laser_shot;

    public Parasite(GeoSystem gs){
        super(gs);

        max_tian = 0;
        tian = 0;

        setSphere(0.1f);
        setScale(0.06f,0.1f,1);
        cash = 2;
        health = max_health = 600;
        energy = max_energy = 100;
        energy_regen = 20;

        max_m_speed = 0.3f;
        max_r_speed = 170f;


        vis_zone = 0.2f;

        laser = new Collider();
        delete = 0.3f;
    }

    @Override
    public void draw(GL10 gl) {
        // поиск союзного корабля не паразита
        // если у хозяина есть тарджет то подлетает и атакует его лазером
        // сразу улетает
        // если хозяина нет то сам атакует и убегает, но теряет скорость и маневреность
        if(death){ Death(gl); return; }

        // не толкаеться
        notPush();
        // развидеть Х_x
        if(target != null && target.death) {
            if (laser_shot) laser_shot = false;
            target = null;
        }
        // находит папку
        if (target == null) {
            // видеть 0_0
            float cur_dist = Float.MAX_VALUE;
            Unit xTarget = null;
            for (Unit unit : geoSistem.units)
                if (!unit.death && unit.pack == pack && !unit.getClass().getSimpleName().equals("Parasite")) {
                    if (Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < cur_dist){
                        cur_dist = Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider;
                        xTarget = unit;
                    }
                }
            if (xTarget != null) { // летим к новому батьке
                if (Vector.Distance(position, xTarget.position) - sphere_collider - xTarget.sphere_collider < vis_zone)
                    target = xTarget;
                else {
                    if (!moving) {
                        moving = true;
                        rotating = true;
                        rot_to(xTarget.position);
                    }
                }
            } else if (!moving) { // улетаем к чертовой бабуфке
                for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack.tag == Pack.TAG_TIAN && Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < unit.vis_zone) {
                        moving = true;
                        rotating = true;
                        rot_from(unit.position);
                        break;
                    }
            }
        }

        // если есть союзник даёт бонусы, иначе не дает
        if (target == null) {
            max_m_speed = 0.08f;
            max_r_speed = 60f;
        } else {
            max_m_speed = 0.5f;
            max_r_speed = 360f;
        }
        // не толкаеться
        notPush();
        if (pack.target_enemy != null){
            if (target != null && target.target != null) {
                // если может то стреляет лазером
                // выстрел лазером
                if (energy == max_energy && !target.target.death)
                    if (Vector.Distance(position, target.target.position) - target.target.sphere_collider < vis_zone)
                        laser_shot = true;
                    else if (!moving) {
                        moving = true;
                        rotating = true;
                        rot_to(target.target.position);
                    }
                if (laser_shot && energy > 0) {
                    float dmg;
                    energy -= dmg = ActiveElement.delayTime / 1000 * max_energy / 0.2f;
                    target.target.dealDamage(dmg);
                    laser.setPosition(position.x + (target.target.position.x - position.x) / 2, position.y + (target.target.position.y - position.y) / 2, 0);
                    laser.setScale(0.02f, Vector.Distance(position, target.target.position) / 2, 1);
                    laser.setRotation(Vector.getAngle(target.target.position.minus(position)));
                    laser.draw(gl);
                } else if (laser_shot) laser_shot = false;
            }

            // ***побег если враг близко***
            if (!laser_shot && !moving) {
                for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack.tag == Pack.TAG_TIAN && Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < unit.vis_zone) {
                        moving = true;
                        rotating = true;
                        rot_from(unit.position);
                        break;
                    }
            }
            // ***

        }
        // не уходит далеко от папки
        if (target != null && !moving && Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > 0.08f) {
            moving = true;
            rotating = true;
            rot_to(target.position);
        }

        super.draw(gl);
    }

    public void Death(GL10 gl) {
        float timer = 1 - delete/0.3f*1;
        explosion.setPosition(position.x+0.02f,position.y-0.01f,0);
        explosion.setScale(0.02f + 0.05f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.01f, position.y + 0.05f, 0);
        explosion.setScale(0.01f + 0.03f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.03f, position.y - 0.03f, 0);
        explosion.setScale(0.02f + 0.05f * timer);
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
        setSprite(geoSistem.textures[42]);
        laser.setSprite(geoSistem.textures[18]);
    }
}
