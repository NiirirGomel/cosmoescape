package com.ermilitary.CosmoEscape.Units.Tentacle;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Drop.Aggrieved;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public abstract class TenUnit extends Unit {
    Sprite avatar_frame;
    Sprite text_frame;
    Collider avatar;

    Sprite health_bar;
    Sprite shield_bar;
    Sprite energy_bar;
    Sprite explosion;
    Sprite airblade;

    public TenUnit(GeoSystem geoSystem){
        super(geoSystem);
        avatar_frame = new Sprite();
        avatar_frame.setScale(0.3f);
        avatar_frame.setPosition(Game.ratio - 0.35f, -0.65f, 0);
        text_frame = new Sprite();
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
            if (pack.mission == Pack.MISSION_ESCAPE) {
                packFormation();
                // улетаай
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_from(Vector.getAngle(pack.target.position.minus(pack.position)));
                }
                if (pack.target_resource == null && pack.target_enemy == null && Vector.Distance(pack.target.position, position) > (pack.target.pack_zone + 2f + 2 * pack.target.scout_size)) {
                    setPack(null);
                    super.Death();
                    delete = 0;
                }
            }
        }
        // ******

        // --движение--
        move();
        // ----
        moving = false;
        rotating = false;
        super.draw(gl);
    }
    public void only_draw(GL10 gl) {
        // --движение--
        move();
        // ----
        moving = false;
        rotating = false;
        super.draw(gl);
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
        if(event.getAction() == MotionEvent.ACTION_DOWN){
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
        text_frame.setSprite(geoSistem.textures[5]);
        avatar.setSprite(geoSistem.textures[36]);

        health_bar.setSprite(geoSistem.textures[7]);
        shield_bar.setSprite(geoSistem.textures[8]);
        energy_bar.setSprite(geoSistem.textures[9]);

        explosion.setSprite(geoSistem.textures[37]);
        airblade.setSprite(geoSistem.textures[35]);
    }
}
