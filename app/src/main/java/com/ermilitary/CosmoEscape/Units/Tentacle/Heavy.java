package com.ermilitary.CosmoEscape.Units.Tentacle;

import android.util.Log;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Heavy extends TenUnit {
    Collider electro_bomb;
    Sprite shield_poly;
    boolean e_bomb;
    float e_time;
    float e_dps;

    public Heavy(GeoSystem gs){
        super(gs);

        max_tian = 2;
        tian = 0;

        setSphere(0.16f);
        setScale(0.14f, 0.17f, 1);
        cash = 9;
        health = max_health = 1200;
        shield = max_shield = 1000;
        energy = max_energy = 250;
        energy_regen = 50;

        max_m_speed = 0.2f;
        max_r_speed = 120f;

        vis_zone = 0.2f;

        shield_poly = new Sprite();
        shield_poly.setScale(sphere_collider+0.02f);
        shield_poly.position = position;
        delete = 0.3f;

        electro_bomb = new Collider();
        electro_bomb.setSphere(0.2f);
        electro_bomb.setScale(0.2f);
        e_bomb = false;
        e_time = 0;
    }

    @Override
    public void draw(GL10 gl) {
        // поиск каптора, если нет поиск врага
        // пр появлении врага в зоне атаки проводит атаку(огранич кол-во)
        if(death){ Death(gl); return; }
        if (pack.target_enemy != null) {
            // развидеть Х_x
            if (target != null && (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death)){
                target = null;
                if (e_bomb) e_bomb = false;
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
                if (energy >= 150 && Vector.Distance(position, target.position) - target.sphere_collider - electro_bomb.sphere_collider < 0) e_bomb = true;
                if (e_bomb && energy > 0) {
                    float dmg;
                    energy -= dmg = ActiveElement.delayTime / 1000 * 200 / 0.5f;
                    electro_bomb.setPosition(position);
                    electro_bomb.setRotation(((int)(energy/10))*120);
                    for (Unit unit : geoSistem.units)
                        if (!unit.death && unit.pack.tag == Pack.TAG_TIAN && electro_bomb.isCollision(unit)){
                            unit.dealDamage(dmg);
                            break;
                        }
                }
                else if (e_bomb) e_bomb = false;
                // уступай и не толкай сородичей своих
                notPush(pack.tag);
                if (!moving && !rotating) {
                    if (Vector.Distance(position, target.position) - target.sphere_collider - sphere_collider > 0f) {
                        moving = true;
                        rotating = true;
                        rot_to(target.position);
                        // если добрался до цели
                    }
                }
            }
        }
        super.draw(gl);
        if (shield > 0) shield_poly.draw(gl);
        if (e_bomb) electro_bomb.draw(gl);
    }
    private void e_atack(){
        for (Unit unit : geoSistem.units)
            if (!unit.death && unit.pack.tag == Pack.TAG_TIAN && isCollision(unit)){
                unit.dealDamage(e_dps * ActiveElement.delayTime / 1000);
                break;
            }
    }
    public void Death(GL10 gl) {
        float timer = 1 - delete/0.3f*1;
        explosion.setPosition(position.x+0.02f,position.y-0.01f,0);
        explosion.setScale(0.03f + 0.08f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.01f, position.y + 0.05f, 0);
        explosion.setScale(0.04f + 0.06f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.03f, position.y - 0.03f, 0);
        explosion.setScale(0.05f + 0.08f * timer);
        explosion.draw(gl);
        airblade.setPosition(position.x+ Vector.getDirection(rotation + 8).x*0.2f*timer,position.y+Vector.getDirection(rotation + 8).y*0.2f*timer,0);
        airblade.setRotation(rotation + 8);
        airblade.setScale(0.1f, 0.04f, 1);
        airblade.draw(gl);
        if (timer > 0.3f)
        airblade.setPosition(position.x+Vector.getDirection(rotation + 172).x*0.2f*(timer-0.3f),position.y+Vector.getDirection(rotation + 172).y*0.2f*(timer-0.3f),0);
        airblade.setRotation(rotation + 172);
        airblade.setScale(0.1f ,0.04f ,1);
        airblade.draw(gl);
        delete -= ActiveElement.delayTime / 1000;
    }

    @Override
    public void resLoad() {
        super.resLoad();
        setSprite(geoSistem.textures[40]);
        electro_bomb.setSprite(geoSistem.textures[20]);
        shield_poly.setSprite(geoSistem.textures[20]);
    }
}
