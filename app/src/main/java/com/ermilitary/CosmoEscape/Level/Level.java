package com.ermilitary.CosmoEscape.Level;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Packs.Squad;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Drop.Aggrieved;
import com.ermilitary.CosmoEscape.Units.Tentacle.*;
import com.ermilitary.CosmoEscape.Units.Tian.*;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Level extends GUI {
    public final static int _dft = -1;
    public final static int _infected_planet = 1;
    public final static int _octopus = 2;
    public boolean mask = false;
    public int dialog = 0;
    public int step = 0;
    float wait_time = 0;

    public boolean victory = false;
    public boolean defeat = false;

    public Sprite vic;
    public Sprite def;
    float aninTimer = 0;

    public Level(){
        super();
        vic = new Sprite();
        vic.setScale(3f, 1f, 1);
        vic.setPosition(0f, 0.25f, 0);
        def = new Sprite();
        def.setScale(3f, 1f, 1);
        def.setPosition(0f, 0.25f, 0);
    }

    @Override
    public void resource_load(GL10 gl) {
        super.resource_load(gl);
        vic.setSprite(res_to_tex(gl, R.drawable.gui_victory));
        def.setSprite(res_to_tex(gl, R.drawable.gui_defeat));
    }

    @Override
    public void resource_free(GL10 gl) {
        super.resource_free(gl);
        int tex_free[] = {vic.texture,def.texture};
        gl.glDeleteTextures(2,tex_free, 0);
    }

    public void Victory(GL10 gl){
        victory = true;
        super.draw(gl);
        if((aninTimer += delayTime / 1000) > 5) aninTimer = 5;
        vic.setScale(aninTimer/5*3/2f,aninTimer/5*1/2f,1);
        gl.glColor4f(1, 1, 1, aninTimer/5);
        vic.draw(gl);
        gl.glColor4f(1, 1, 1, 1);
        if(aninTimer == 5)
            nextElement = ActiveElement.MENU;
    }
    public void Defeat(GL10 gl){
        defeat = true;
        super.draw(gl);
        if((aninTimer += delayTime / 1000) > 5) aninTimer = 5;
        def.setScale(3-aninTimer/5*3/2f,1-aninTimer/5*1/2f,1);
        gl.glColor4f(1, 1, 1, aninTimer/5);
        def.draw(gl);
        gl.glColor4f(1, 1, 1, 1);
        if(aninTimer == 5)
            nextElement = ActiveElement.MENU;
    }
    public void fillMask(){ mask = true; }
    public void clearMask(){ mask = false; }
    public void setXMask(MotionEvent event, Collider zone){
        Vector touch = Vector.prepare_event(event);
        if (zone.sphere_collider > 0){
            if (zone.isTouchSphere(touch)) mask = false;
        } else {
            if (zone.isTouchBox(touch)) mask = false;
        }
    }
    public void setMask(MotionEvent event, Collider zone){
        Vector touch = Vector.prepare_event(event);
        if (zone.sphere_collider > 0){
            if (zone.isTouchSphere(touch)) mask = true;
        } else {
            if (zone.isTouchBox(touch)) mask = true;
        }
    }
    public void setGeoXMask(MotionEvent event, Collider zone){
        Vector touch = cam_set_event(event);
        if (zone.sphere_collider > 0){
            if (zone.isTouchSphere(touch)) mask = false;
        } else {
            if (zone.isTouchBox(touch)) mask = false;
        }
    }
    public void setGeoMask(MotionEvent event, Collider zone){
        Vector touch = cam_set_event(event);
        if (zone.sphere_collider > 0){
            if (zone.isTouchSphere(touch)) mask = true;
        } else {
            if (zone.isTouchBox(touch)) mask = true;
        }
    }
    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
    }

    @Override
    public boolean inPut(MotionEvent event) {
        if (!mask && !victory && !defeat) super.inPut(event);
        clearMask();
        return false;
    }
    public int findPackWTag(int dialog, int tag){
        if (this.dialog == dialog) {
            int find = 0;
            for (Pack p : packs)
                if (p.tag == tag) find++;
            return find;
        } else return -1;
    }
    public int findUWC(int dialog, String name){
        if (this.dialog == dialog) {
            int find = 0;
            for (Unit u : units)
                if (u.getClass().getSimpleName().equals(name)) find++;
            return find;
        } else return -1;
    }
    public boolean wait(float time){
        if ((wait_time += delayTime / 1000) < time){
            return false;
        } else {
            if (targetRadioUnit != null){
                targetRadioUnit.radio = "";
                return false;
            } else {
                wait_time = 0;
                return true;
            }
        }
    }
    public int findPackWMission(int dialog, int mission){
        if (this.dialog == dialog) {
            int find = 0;
            for (Pack p : packs)
                if (p.mission == mission) find++;
            return find;
        } else return -1;
    }
    public void startDialog(){ dialog = 0; }
    public void setDialog(int i){ dialog = i; }
    public boolean Say(int cur, Unit unit, String say){
        if (cur == dialog && targetRadioUnit == null) {
            unit.radio = say;
            targetRadioUnit = unit;
            dialog++;
            return true;
        } else return  false;
    }
    public void nestStep(){
        step++;
        startDialog();
    }
    public void setNav(Pack target){
        targetNav = target;
    }
    public Unit spawnSov(Pack base){
        Pack n_pack = new Pack(this, base.tag);
        n_pack.target = base;
        n_pack.mission = Pack.MISSION_SCOUT;

        Unit n_unit = new Scout(this);
        ((TanUnit)n_unit).setAvatar(17);
        n_unit.setPosition(base.position);
        n_unit.setPack(n_pack);
        n_unit.resLoad();
        addUnitInDraw(n_unit);
        addPackInDraw(n_pack);

        return n_unit;
    }
    public Pack spawnTian(float dir, float dist, Pack pack, int agg){
        Unit n_unit;
        Pack n_pack = new Pack(this, Pack.TAG_RESORCES);
        n_pack.mission = Pack.MISSION_NONE;
        // генерирует место спавна на рассто\нии зоны видимости
        Vector n_dir;
        float n_dist;
        if (dir < 0) {
            Random rnd = new Random(ActiveElement.time);
            n_dir = Vector.getDirection((float) rnd.nextInt(360));
        } else n_dir = Vector.getDirection(dir);
        if (dist < 0) n_dist =(pack.pack_zone + 2f + 2 * pack.scout_size);
        else n_dist = dist;

        Vector n_pos = new Vector(pack.position.x + n_dir.x*n_dist, pack.position.y + n_dir.y*n_dist, 0);

        for(int i = 0; i < agg; i++) {
            n_unit = new Aggrieved(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }
        n_pack.setPosition(n_pos);
        addPackInDraw(n_pack);

        return n_pack;
    }public Squad spawnShip(float dir, float dist, Pack pack, int mission, int fig, int ass,int med,int sco){
        Unit n_unit;
        Squad n_pack = new Squad(this, Pack.TAG_TIAN);
        // генерирует место спавна на рассто\нии зоны видимости
        Vector n_dir;
        float n_dist;
        if (dir < 0) {
            Random rnd = new Random(ActiveElement.time);
            n_dir = Vector.getDirection((float) rnd.nextInt(360));
        } else n_dir = Vector.getDirection(dir);
        if (dist < 0) n_dist =(pack.pack_zone + 2f + 2 * pack.scout_size);
        else n_dist = dist;

        Vector n_pos = new Vector(pack.position.x + n_dir.x*n_dist, pack.position.y + n_dir.y*n_dist, 0);

        for(int i = 0; i < fig; i++) {
            n_unit = new Fighter(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < ass; i++) {
            n_unit = new Assault(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < med; i++) {
            n_unit = new Medic(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < sco; i++) {
            n_unit = new Scout(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }
        n_pack.setPosition(n_pos);
        n_pack.mission = mission;
        n_pack.target = pack;
        addPackInDraw(n_pack);

        return n_pack;
    }
    public Pack addBose(Pack n_pack, int n_bose){
        Unit n_unit = null;
        switch (n_bose) {
            case 1: n_unit = new InfectedPlanet(this); break;
            case 2: n_unit = new Octopus(this); break;
        }
        if (n_unit != null) {
            n_unit.setPosition(n_pack.position);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }
        return n_pack;
    }
    public Pack spawnEnemy(float dir, float dist, Pack pack, int mission, int cap,int sol,int sni,int hea,int par){
        Unit n_unit;
        Pack n_pack = new Pack(this, Pack.TAG_TENTACLE);

        // генерирует место спавна на рассто\нии зоны видимости
        Vector n_dir;
        float n_dist;
        if (dir < 0) {
            Random rnd = new Random(ActiveElement.time);
            n_dir = Vector.getDirection((float) rnd.nextInt(360));
        } else n_dir = Vector.getDirection(dir);
        if (dist < 0) n_dist =(pack.pack_zone + 2f + 2 * pack.scout_size);
        else n_dist = dist;

        Vector n_pos = new Vector(pack.position.x + n_dir.x*n_dist, pack.position.y + n_dir.y*n_dist, 0);

        for(int i = 0; i < cap; i++) {
            n_unit = new Captor(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < sol; i++) {
            n_unit = new Solider(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < sni; i++) {
            n_unit = new Sniper(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < hea; i++) {
            n_unit = new Heavy(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }

        for(int i = 0; i < par; i++) {
            n_unit = new Parasite(this);
            n_unit.setPosition(n_pos);
            n_unit.setPack(n_pack);
            n_unit.resLoad();
            addUnitInDraw(n_unit);
        }
        n_pack.setPosition(n_pos);
        n_pack.mission = mission;
        n_pack.target = pack;
        addPackInDraw(n_pack);

        return n_pack;
    }
}
