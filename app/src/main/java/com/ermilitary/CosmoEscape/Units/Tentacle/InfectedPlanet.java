package com.ermilitary.CosmoEscape.Units.Tentacle;

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
public class InfectedPlanet extends TenUnit {
    Sprite shield_poly;
    Collider tentacle;
    Collider hand;

    float dps;

    public InfectedPlanet(GeoSystem gs){
        super(gs);

        max_tian = 0;
        tian = 0;

        setSphere(0.9f);
        setScale(0.9f, 0.9f, 1);
        cash = 9;
        health = max_health = 27000;
        shield = max_shield = 10000;
        //energy = max_energy = 250;
        //energy_regen = 50;
        dps = 5000f;

        max_m_speed = 0.04f;
        max_r_speed = 120f;
        vis_zone = 1.2f;

        delete = 0.3f;

        shield_poly = new Sprite();
        shield_poly.setScale(sphere_collider+0.04f);
        shield_poly.position = position;
        tentacle = new Collider();
        hand = new Collider();
    }

    @Override
    public void draw(GL10 gl) {
        if(death){ Death(gl); return; }
        if (pack.target_enemy != null) {
            // развидеть Х_x
            if (target != null && (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death)){
                target = null;
            }

            // видеть 0_0
            float cur_dist = Float.MAX_VALUE;
            for (Unit unit : geoSistem.units)
                if (!unit.death && unit.pack == pack.target_enemy && Vector.Distance(position, unit.position) - unit.sphere_collider < vis_zone) {
                    if (Vector.Distance(position, unit.position) - unit.sphere_collider < cur_dist){
                        cur_dist = Vector.Distance(position, unit.position) - unit.sphere_collider;
                        target = unit;
                    }
                }
            // в стан врага лети ты
            if (!moving && !rotating) {
                rotating = true;
                moving = true;
                rot_to(pack.target.position);
            }
        } else {
            // в стан врага лети ты
            if (!moving && !rotating) {
                rotating = true;
                moving = true;
                rot_to(pack.target.position);
            }
        }
        for (Unit u : geoSistem.units) {
            if (!u.death && u.pack.tag == Pack.TAG_TIAN && Vector.Distance(position, u.position) - u.sphere_collider - sphere_collider <= 0) {
                u.dealDamage(dps * ActiveElement.delayTime / 1000);
            }
        }
        only_draw(gl); if (shield > 0) shield_poly.draw(gl);
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
        setSprite(geoSistem.textures[46]);
        shield_poly.setSprite(geoSistem.textures[20]);
        tentacle.setSprite(geoSistem.textures[47]);
        hand.setSprite(geoSistem.textures[48]);
    }
}
