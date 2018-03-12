package com.ermilitary.CosmoEscape.Units.Tentacle;

import android.util.Log;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Tian.Assault;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Captor extends TenUnit {
    Collider tentacle;
    float t_speed;
    float t_state;
    boolean t_atack;
    public float t_dps;

    public Captor(GeoSystem gs){
        super(gs);
        setSphere(0.22f);

        max_tian = 6;
        tian = 0;

        setScale(0.2f,0.24f,1);
        cash = 12;
        health = max_health = 2700;

        max_m_speed = 0.05f;
        max_r_speed = 40f;
        vis_zone = 0.1f;

        tentacle = new Collider();
        tentacle.setSphere(0.1f);
        tentacle.setScale(0.05f, 0.1f, 0);
        delete = 0.3f;

        t_speed = 1;
        t_state = 0;
        t_atack = false;
        t_dps = 40;
    }

    @Override
    public void draw(GL10 gl) {
        // поиск целей
        // при попадании корабля в зону поражения атакует каждого тентакля
        if(death){ Death(gl); return; }

        if (pack.target_enemy != null) {
            // развидеть Х_x
            if (target != null && (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death)){
                target = null;
                if (t_atack) t_atack = false;
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
                // уступай и не толкай сородичей своих
                notPush(pack.tag);
                if (!moving && !rotating) {
                    if (Vector.Distance(position, target.position) - target.sphere_collider - sphere_collider > 0.08f) {
                        moving = true;
                        rotating = true;
                        rot_to(target.position);
                        // если добрался до цели
                    } else {
                        if(!t_atack) t_atack = true;
                    }
                }
            }
        } else if (t_atack) t_atack = false; // убрать)

        if (t_atack) {
            if ((t_state += t_speed * ActiveElement.delayTime / 1000) > 1) t_state = 1;
        } else {
            if ((t_state -= t_speed * ActiveElement.delayTime / 1000) < 0) t_state = 0;
        }

        if (t_state > 0){// щупальца
            float angle = 50/2;
            float length = 0.2f * t_state;
            tentacle.setPosition(position.x+ Vector.getDirection(rotation + 45).x*length,position.y+Vector.getDirection(rotation + 45).y*length,0);
            tentacle.setRotation(rotation + 45 + (float)Math.sin(Math.toRadians(ActiveElement.time % 1000 / 1000f * 360 + 90)) * angle);
            tentacle.draw(gl);t_atack();
            tentacle.setPosition(position.x+ Vector.getDirection(rotation + 90).x*length,position.y+Vector.getDirection(rotation + 90).y*length,0);
            tentacle.setRotation(rotation + 90 + (float)Math.sin(Math.toRadians((ActiveElement.time - 200) % 1000 / 1000f * 360 + 90)) * angle);
            tentacle.draw(gl);t_atack();
            tentacle.setPosition(position.x + Vector.getDirection(rotation + 135).x * length, position.y + Vector.getDirection(rotation + 135).y * length, 0);
            tentacle.setRotation(rotation + 135 + (float)Math.sin(Math.toRadians((ActiveElement.time - 400) % 1000 / 1000f * 360 + 90)) * angle);
            tentacle.draw(gl);t_atack();

            tentacle.setPosition(position.x+ Vector.getDirection(rotation + 225).x*length,position.y+Vector.getDirection(rotation + 225).y*length,0);
            tentacle.setRotation(rotation + 225 + (float)Math.sin(Math.toRadians((ActiveElement.time - 400) % 1000 / 1000f * 360+90)) * angle);
            tentacle.draw(gl);t_atack();
            tentacle.setPosition(position.x+ Vector.getDirection(rotation + 270).x*length,position.y+Vector.getDirection(rotation + 270).y*length,0);
            tentacle.setRotation(rotation + 270 + (float)Math.sin(Math.toRadians((ActiveElement.time - 200) % 1000 / 1000f * 360+90)) * angle);
            tentacle.draw(gl);t_atack();
            tentacle.setPosition(position.x+ Vector.getDirection(rotation + 315).x*length,position.y+Vector.getDirection(rotation + 315).y*length,0);
            tentacle.setRotation(rotation + 315 + (float)Math.sin(Math.toRadians(ActiveElement.time % 1000 / 1000f * 360+90)) * angle);
            tentacle.draw(gl);t_atack();
        }
        super.draw(gl);
    }

    private void t_atack(){
        for (Unit unit : geoSistem.units)
            if (!unit.death && unit.pack.tag == Pack.TAG_TIAN && tentacle.isCollision(unit)){
                unit.dealDamage(t_dps * ActiveElement.delayTime / 1000);
                break;
            }
    }

    public void Death(GL10 gl) {
        float timer = 1 - delete/0.3f*1;
        explosion.setPosition(position.x+0.04f,position.y-0.03f,0);
        explosion.setScale(0.06f + 0.1f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.03f, position.y + 0.08f, 0);
        explosion.setScale(0.05f + 0.08f * timer);
        explosion.draw(gl);
        explosion.setPosition(position.x - 0.05f, position.y - 0.05f, 0);
        explosion.setScale(0.06f + 0.1f * timer);
        explosion.draw(gl);
        airblade.setPosition(position.x + Vector.getDirection(rotation + 0).x * 0.3f * timer, position.y + Vector.getDirection(rotation + 0).y * 0.3f * timer, 0);
        airblade.setRotation(rotation + 0);
        airblade.setScale(0.2f, 0.05f, 1);
        airblade.draw(gl);
        airblade.setPosition(position.x + Vector.getDirection(rotation + 183).x * 0.3f * timer, position.y + Vector.getDirection(rotation + 183).y * 0.3f * timer, 0);
        airblade.setRotation(rotation + 180);
        airblade.setScale(0.2f ,0.05f ,1);
        airblade.draw(gl);
        if(timer > 0.3f) {
            airblade.setPosition(position.x + Vector.getDirection(rotation + 95).x * 0.3f * (timer-0.3f), position.y + Vector.getDirection(rotation + 95).y * 0.3f * (timer-0.3f), 0);
            airblade.setRotation(rotation + 95);
            airblade.setScale(0.2f, 0.05f, 1);
            airblade.draw(gl);
            airblade.setPosition(position.x + Vector.getDirection(rotation + 276).x * 0.3f * (timer-0.3f), position.y + Vector.getDirection(rotation + 276).y * 0.3f * (timer-0.3f), 0);
            airblade.setRotation(rotation + 276);
            airblade.setScale(0.2f, 0.05f, 1);
            airblade.draw(gl);
        }
        delete -= ActiveElement.delayTime / 1000;
    }
    @Override
    public void resLoad() {
        super.resLoad();
        setSprite(geoSistem.textures[38]);
        tentacle.setSprite(geoSistem.textures[44]);
    }
}
