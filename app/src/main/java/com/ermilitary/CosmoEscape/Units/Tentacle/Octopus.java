package com.ermilitary.CosmoEscape.Units.Tentacle;

import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Octopus extends TenUnit {
    Collider tentacle;
    Collider hand;

    public Octopus(GeoSystem gs){
        super(gs);

        max_tian = 0;
        tian = 0;

        setSphere(0.1f);
        setScale(0.08f,0.1f,1);
        health = max_health = 27000;

        max_m_speed = 0.3f;
        max_r_speed = 170f;

        vis_zone = 0.4f;

        tentacle = new Collider();
        hand = new Collider();
    }

    @Override
    public void draw(GL10 gl) {
        // ии =)
        // 1 - крутится вокруг тарджета пака
        // 2 - затускает по таймеру паки на тарджет пака
        // 3 - если нет паков сам наподает
        // 4 - если враг близко и есть энергия ебашит лазерами
        // 5 - а если нет лазеров пиздит всех щупальцами
        // 6 - отбивает мегабомбу
        only_draw(gl);
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
        setSprite(geoSistem.textures[49]);
        tentacle.setSprite(geoSistem.textures[50]);
        hand.setSprite(geoSistem.textures[51]);
    }
}
