package com.ermilitary.CosmoEscape.Packs;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.GameObject;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Pack extends GameObject {
    // tags
    public static final int TAG_TIAN = 1;
    public static final int TAG_TENTACLE = 2;
    public static final int TAG_RESORCES = 3;
    // missions
    public static final int MISSION_BASE = 0;
    public static final int MISSION_NONE = 1;
    public static final int MISSION_PARKING_BASE = 2;
    public static final int MISSION_SCOUT = 3;
    public static final int MISSION_HELP = 4;
    public static final int MISSION_ATTACK = 5;
    public static final int MISSION_ESCAPE = 6;

    public boolean nav = false;

    public int scout_size = 0;

    public Collider emblem;

    public Vector direction;

    public int tag;
    public int mission;
    public Pack target;
    public Pack target_enemy;
    public Pack target_resource;

    GeoSystem geoSistem;
    public int size;
    public float pack_zone;
    public float vis_zone;
    public float squad_zone;
    public boolean bord_set;
    public float bor_u,bor_d,bor_l,bor_r;

    public boolean hide;

    public Pack(GeoSystem geoSystem, int tag){
        direction = new Vector(0,0,0);
        this.geoSistem = geoSystem;
        mission = MISSION_NONE;
        this.tag = tag;
        setPosition(0,0,0);
        size = 0;
        pack_zone = 0;
        squad_zone = 0;
        vis_zone = 0;
        bord_set = false;

        emblem = new Collider();
        emblem.setScale(0.1f);
        emblem.setSphere(0.1f);
        hide = true;
    }

    public void resLoad(){
        switch (tag) {
            case TAG_RESORCES:
            case TAG_TIAN: emblem.setSprite(geoSistem.textures[52]);
                break;
            case TAG_TENTACLE: emblem.setSprite(geoSistem.textures[53]);
                break;
        }
    }

    public void addOne(Unit unit){
        ++size;
        pack_zone += (unit.sphere_collider + 0.2*unit.sphere_collider) / (size*0.5);
    }
    public void dellOne(Unit unit){
        pack_zone -= (unit.sphere_collider + 0.2*unit.sphere_collider) / (size*0.5);
        --size;
    }

    public void draw(GL10 gl){
        // установка координат и зоны видимости отряда
        bord_set = false;
        vis_zone = pack_zone;
        for (Unit unit : geoSistem.units){
            if(unit.pack == this) {
                if (!bord_set) {
                    bor_u = bor_d = unit.position.y;
                    bor_l = bor_r = unit.position.x;
                    if (vis_zone < unit.vis_zone) vis_zone = unit.vis_zone;
                    bord_set = true;
                } else {
                    if (bor_u < unit.position.y) bor_u = unit.position.y;
                    else if (bor_d > unit.position.y) bor_d = unit.position.y;
                    if (bor_r < unit.position.x) bor_r = unit.position.x;
                    else if (bor_l > unit.position.x) bor_l = unit.position.x;
                    // установка зоны видимости отряда
                    if (vis_zone < unit.vis_zone) vis_zone = unit.vis_zone;
                }
            }
        }
        position.set(bor_l + (bor_r - bor_l)/2,bor_d + (bor_u - bor_d)/2,0);
        // в бою виз зона зависит от положения юнитов
        if ((target_enemy != null || tag == TAG_RESORCES) && (bor_r - bor_l > vis_zone || bor_u - bor_d > vis_zone)) vis_zone = (bor_r - bor_l > bor_u - bor_d) ? (bor_r - bor_l) : (bor_u - bor_d);

        if(tag == TAG_RESORCES){
            for (Pack res : geoSistem.packs)
                if (res.tag == TAG_RESORCES && Vector.Distance(position, res.position) - vis_zone - res.vis_zone < 0) {
                    for (Unit u : geoSistem.units)
                        if (!u.death && u.pack == res)u.setPack(this);
                }
            return;
        }
        // видеть ресурсоы
        if (target_enemy == null)
            for (Pack res : geoSistem.packs)
                if (res.tag == TAG_RESORCES && Vector.Distance(position, res.position) < vis_zone + res.vis_zone) {
                    res.hide = false;
                    if((mission == MISSION_HELP && target == res) || mission == MISSION_ESCAPE || mission == MISSION_NONE || mission == MISSION_PARKING_BASE) {
                        target_resource = res;
                        break;
                    }
                }
        // обнаружение врагов и начало боя
        for (Pack enemy : geoSistem.packs)
            if (enemy.tag != tag && enemy.tag != TAG_RESORCES && Vector.Distance(position, enemy.position) - vis_zone - enemy.vis_zone < 0) {
                enemy.hide = false;
                if (target_enemy == null) {
                    if ((mission == MISSION_ATTACK && (tag == TAG_TENTACLE || target == enemy)) || mission == MISSION_ESCAPE || mission == MISSION_SCOUT || mission == MISSION_NONE || (mission == MISSION_PARKING_BASE && Vector.Distance(target.position, position) < target.squad_zone)) {
                        target_enemy = enemy;
                        target_resource = null;
                    }
                    if (target_resource != null && mission == MISSION_PARKING_BASE) target_resource = null;
                }
            }
        // выход из боя ч_Ч
        if (target_enemy != null && (target_enemy.size == 0 || Vector.Distance(target_enemy.position,position) - target_enemy.vis_zone - vis_zone > 0)) target_enemy = null;
        // потеря ресурсов из виду
        if (target_resource != null && (target_resource.size == 0 || Vector.Distance(target_resource.position,position) > vis_zone + target_resource.vis_zone)) target_resource = null;

        // снятие тарджета
        if(target != null && target.size == 0) {
            if (mission == MISSION_SCOUT || mission == MISSION_PARKING_BASE){
                mission = MISSION_NONE;
                target = null;
            }
            if(mission == MISSION_ATTACK || mission == MISSION_HELP || mission == MISSION_ESCAPE){
                for (Pack p : geoSistem.packs)
                    if (p.tag == tag && p.mission == MISSION_BASE){
                        mission = MISSION_PARKING_BASE;
                        target = p;
                    }
                if (mission != MISSION_PARKING_BASE){
                    mission = MISSION_NONE;
                    target = null;
                }
            }
        }
        // ленивйы блок прощитывающий эскейп вражин
        if((tag == TAG_TENTACLE || mission == MISSION_HELP) && target != null) {
            int cur_max_tian = 0;
            int cur_tian = 0;
            for (Unit u : geoSistem.units)
                if (!u.death && u.pack == this) {
                    cur_max_tian += u.max_tian;
                    cur_tian += u.tian;
                }
            if (tag == TAG_TENTACLE && cur_tian * 3 > cur_max_tian) {
                mission = MISSION_ESCAPE;
            }
            if (mission == MISSION_HELP && cur_tian == cur_max_tian) {
                for (Pack p : geoSistem.packs)
                    if (p.mission == MISSION_BASE){
                        target = p;
                        mission = MISSION_PARKING_BASE;
                    }
            }
        }
    }
    public void GUIdraw(GL10 gl){

    }
    public boolean GUIinPut(MotionEvent event) {
        return false;
    }
}
