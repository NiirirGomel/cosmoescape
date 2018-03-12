package com.ermilitary.CosmoEscape.Packs;

import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Units.Tian.BaseShip;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Base extends Pack {
    public BaseShip base_ship;

    public Base(GeoSystem geoSystem, int tag){
        super(geoSystem, tag);
        mission = MISSION_BASE;
        base_ship = new BaseShip(geoSystem);
        base_ship.setPack(this);
        geoSystem.addUnit(base_ship);
    }

    @Override
    public void draw(GL10 gl) {
       // super.draw(gl);
        // установка зоны видимости отряда
        vis_zone = squad_zone = pack_zone;
        for (Pack p : geoSistem.packs)
            if(p.tag == tag && p.mission == MISSION_PARKING_BASE)
                squad_zone += p.pack_zone;
        vis_zone += (squad_zone - vis_zone) / 2;

        // обнаружение врагов и начало боя
        for (Pack enemy : geoSistem.packs)
            if (enemy.tag == TAG_TENTACLE && Vector.Distance(position, enemy.position) - vis_zone - enemy.vis_zone < 0) {
                if (target_enemy == null) target_enemy = enemy;
                enemy.hide = false;
            }
        // выход из боя ч_Ч
        if (target_enemy != null)
            if (target_enemy.size == 0) target_enemy = null;
            else if (mission == MISSION_BASE && Vector.Distance(position, target_enemy.position) - vis_zone - target_enemy.vis_zone > 0) target_enemy = null;

        scout_size = 0;
        for (Unit u : geoSistem.units)
            if(u.getClass().getSimpleName().equals("Scout") && !u.death && u.pack.target == this)
                scout_size++;
    }

    public void resLoad(){
        super.resLoad();
    }
}
