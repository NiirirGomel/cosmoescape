package com.ermilitary.CosmoEscape.Units.Tian;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Drop.Aggrieved;
import com.ermilitary.CosmoEscape.Units.Tentacle.Captor;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public abstract class TanUnit extends Unit {
    Sprite avatar_frame;
    Collider avatar;
    int ava_set;

    Sprite health_bar;
    Sprite shield_bar;
    Sprite energy_bar;
    Sprite explosion;
    Sprite airblade;


    public TanUnit(GeoSystem geoSystem){
        super(geoSystem);
        ava_set = 6;
        avatar_frame = new Sprite();
        avatar_frame.setScale(0.3f);
        avatar_frame.setPosition(Game.ratio - 0.35f, -0.65f, 0);
        avatar = new Collider();
        avatar.setScale(0.2f);
        avatar.setSphere(0.2f);
        avatar.setPosition(avatar_frame.position);

        health_bar = new Sprite();
        shield_bar = new Sprite();
        energy_bar = new Sprite();

        explosion = new Sprite();
        airblade = new Sprite();
    }

    @Override
    public void GUIdraw(GL10 gl) {
        avatar_frame.draw(gl);
        avatar.draw(gl);

        if(health > 0) {
            health_bar.setScale(health / max_health * 0.7f, 0.03f, 1);
            health_bar.setPosition(-0.7f + health_bar.scale.x, 0.9f, 0);
            health_bar.draw(gl);
        }
        if (shield > 0){
            shield_bar.setScale(shield / max_shield * 0.7f, 0.03f, 1);
            shield_bar.setPosition(-0.7f + shield_bar.scale.x, 0.9f, 0);
            shield_bar.draw(gl);
        }
        if (energy > 0) {
            energy_bar.setScale(energy / max_energy * 0.7f, 0.03f, 1);
            energy_bar.setPosition(-0.7f + energy_bar.scale.x, 0.8f, 0);
            energy_bar.draw(gl);
        }
    }

    @Override
    public int GUIinPut(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
                if(avatar.isTouchSphere(Vector.prepare_event(event)))
                    if (pack != null)
                        return setPack;
                    else
                        return next;
                else
                    return exit;
        }
        return next;
    }

    @Override
    public void draw(GL10 gl) {
        findTian();
        // ***Групповой уровень***
        if(pack != null && pack.target_enemy == null) {
            if (pack.mission == Pack.MISSION_NONE) {
                packFormation();
            }
            if (pack.mission == Pack.MISSION_ATTACK) {
                packFormation();
                // лети врагов своих уничтожать на поле ратном
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_to(Vector.getAngle(pack.target.position.minus(pack.position)));
                }
            }
            if (pack.mission == Pack.MISSION_HELP) {
                if (pack.target.tag != Pack.TAG_RESORCES && Vector.Distance(position, pack.target.position) - pack.target.vis_zone < vis_zone) {
                    // как к товарищам ты прийдешь так помоги им всеми силами своими
                    if (isSelect()) unselection();
                    if (geoSistem.getNav() == pack && pack.size == 1) geoSistem.setNav(pack.target);
                    setPack(pack.target);
                }
                packFormation();
                // лети на помошь товарищам своим
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_to(Vector.getAngle(pack.target.position.minus(pack.position)));
                }
            }
            if (pack.mission == Pack.MISSION_PARKING_BASE) {
                packFormation();
                 // не улетай далеко от дома своего
                if (!moving && !rotating && Vector.Distance(position, pack.target.position) > pack.target.squad_zone) {
                    rotating = true;
                    moving = true;
                    rot_to(pack.target.position);
                }
                // летай вокруг базы с отрядом своим
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_to(Vector.getAngle(pack.position.minus(pack.target.position)) - 90);
                }
            }
            if (pack.mission == Pack.MISSION_SCOUT) {
                packFormation();
                // на путь разведчика вынеси отряд свой
                if (!moving && !rotating && Vector.Distance(position, pack.target.position) < pack.target.pack_zone + 2 * pack.target.scout_size) {
                    rot_to(Vector.getAngle(pack.position.minus(pack.target.position)));
                    moving = true;
                    rotating = true;
                }
                // не улетай далеко от дома своего
                if (!moving && !rotating && Vector.Distance(position, pack.target.position) > pack.target.pack_zone + 2 * pack.target.scout_size + pack.pack_zone) {
                    rotating = true;
                    moving = true;
                    rot_to(pack.target.position);
                }
                // летай вокруг базы с отрядом своим
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_to(Vector.getAngle(pack.position.minus(pack.target.position)) + 90);
                }
            }
            // ******

            // ***Тактильный уровень***
            if (pack.mission == Pack.MISSION_BASE) {
                // не выходи за пределы отряда своего
                if (!moving && !rotating) {
                    if (Vector.Distance(position, pack.position) > pack.pack_zone) {
                        rotating = true;
                        moving = true;
                        rot_to(pack.position);
                    } else // уступай и не толкай сородичей своих
                        notPush();
                }
            }
            // ******
        }
        // ---движение---
        move();
        if (tian > need_tian){
            Base base = null;
            if (pack.mission == Pack.MISSION_PARKING_BASE && Vector.Distance(pack.target.position, position) < pack.target.squad_zone)
                base = (Base)pack.target;
            else if(pack.mission == Pack.MISSION_BASE && Vector.Distance(pack.position, position) < pack.squad_zone)
                base = (Base)pack;
            if (base != null && base.base_ship != this) {
                base.base_ship.addTian(tian - need_tian);
                tian = need_tian;
            }
        }
        super.draw(gl);
    }

    @Override
    public void Death() {
        if (!death) {
            Unit u_res;
            Pack p_res = new Pack(geoSistem, Pack.TAG_RESORCES);
            for (int i = 0; i < tian; i++) {
                u_res = new Aggrieved(geoSistem);
                u_res.setPosition(position);
                u_res.setPack(p_res);
                geoSistem.addUnitInDraw(u_res);
            }
            if(p_res.size > 0)geoSistem.addPackInDraw(p_res);
            setPack(null);
            super.Death();
        }

    }

    @Override
    public void resLoad() {
        avatar_frame.setSprite(geoSistem.textures[4]);
        avatar.setSprite(geoSistem.textures[ava_set]);

        health_bar.setSprite(geoSistem.textures[7]);
        shield_bar.setSprite(geoSistem.textures[8]);
        energy_bar.setSprite(geoSistem.textures[9]);

        explosion.setSprite(geoSistem.textures[34]);
        airblade.setSprite(geoSistem.textures[35]);
    }
    public void setAvatar(int avatar){
        ava_set = avatar;
        this.avatar.setSprite(geoSistem.textures[ava_set]);
    }
}
