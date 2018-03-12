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
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.LinkedList;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class BaseShip extends TanUnit {
    Sprite gun_hiperlaser;
    Sprite gun_megabomb;

    public Laser hiperlaser;
    public Bullet megabomb;

    public Sprite gui_menuframe;
    public Collider gui_fighter;
    public Collider gui_assault;
    public Collider gui_medic;
    public Collider gui_scout;

    public Collider gui_megabomb;
    public Collider gui_hiperlaser;

    public int cash;
    //public int tian;
    boolean get_tian = false;

    float mb_prev_fire;
    float mb_fire_rate;

    boolean weapon_nav;
    public boolean mb_ready;
    public boolean hl_ready;
    Vector weapon_target;

    public BaseShip(GeoSystem gs){
        super(gs);
        health = max_health = 16000;
        energy = max_energy = 15000;
        energy_regen = 100;

        max_m_speed = 0;
        max_r_speed = 0;

        cash = 0;
        tian = 0;

        mb_prev_fire = 0;
        mb_fire_rate = 40;

        weapon_nav = false;
        mb_ready = false;
        weapon_target = null;

        setSphere(0.2f);
        setScale(0.18f, 0.2f, 1f);

        gun_hiperlaser = new Sprite();
        gun_hiperlaser.setScale(0.05f,0.06f,1);
        gun_megabomb = new Sprite();
        gun_megabomb.setScale(0.05f,0.07f,1);

        gui_menuframe = new Sprite();
        gui_menuframe.setScale(0.8f,1f,1f);
        gui_menuframe.setPosition(-Game.ratio+0.3f, 0, 0);
        gui_fighter = new Collider();
        gui_fighter.setScale(0.17f);
        gui_fighter.sphere_collider = 0.17f;
        gui_fighter.setPosition(-Game.ratio + 0.22f, 0.78f, 0);
        gui_assault = new Collider();
        gui_assault.setScale(0.17f);
        gui_assault.sphere_collider = 0.17f;
        gui_assault.setPosition(-Game.ratio + 0.33f, 0.45f, 0);
        gui_medic = new Collider();
        gui_medic.setScale(0.17f);
        gui_medic.sphere_collider = 0.17f;
        gui_medic.setPosition(-Game.ratio + 0.39f, 0.11f, 0);
        gui_scout = new Collider();
        gui_scout.setScale(0.17f);
        gui_scout.sphere_collider = 0.17f;
        gui_scout.setPosition(-Game.ratio + 0.34f, -0.23f, 0);

        gui_megabomb = new Collider();
        gui_megabomb.setScale(0.18f);
        gui_megabomb.sphere_collider = 0.18f;
        gui_megabomb.setPosition(Game.ratio - 0.6f, 0.78f, 0);
        gui_hiperlaser = new Collider();
        gui_hiperlaser.setScale(0.18f);
        gui_hiperlaser.sphere_collider = 0.18f;
        gui_hiperlaser.setPosition(Game.ratio - 0.20f, 0.78f, 0);

        hiperlaser = new Laser(geoSistem);
        megabomb = new Bullet(geoSistem);

        delete = 0.3f;
    }

    public void addTian(int n){
        if (n > 0) {
            tian += n;
            get_tian = true;
        }
    }
    public boolean getTian(){
        if (get_tian){
            get_tian = false;
            return true;
        } else return false;
    }
    @Override
    public void draw(GL10 gl) {
        if(death){ Death(gl); return; }
        super.draw(gl);
        gun_hiperlaser.setPosition(position.x - 0.025f, position.y + 0.11f, 0);
        gun_megabomb.setPosition(position.x + 0.11f, position.y - 0.13f, 0);
        // рисуем и удаляем снаряды
        hiperlaser.laser_draw(gl);
        megabomb.bullets_draw(gl);
        mb_prev_fire += ActiveElement.delayTime / 1000;
        gun_megabomb.draw(gl);
        gun_hiperlaser.draw(gl);
    }

    boolean check_bullets = true;
    public void Death(GL10 gl) {
        if(check_bullets) {
            float timer = 1 - delete / 0.3f * 1;
            explosion.setPosition(position.x + 0.04f, position.y - 0.03f, 0);
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
            airblade.setScale(0.2f, 0.05f, 1);
            airblade.draw(gl);
            if (timer > 0.3f) {
                airblade.setPosition(position.x + Vector.getDirection(rotation + 95).x * 0.3f * (timer - 0.3f), position.y + Vector.getDirection(rotation + 95).y * 0.3f * (timer - 0.3f), 0);
                airblade.setRotation(rotation + 95);
                airblade.setScale(0.2f, 0.05f, 1);
                airblade.draw(gl);
                airblade.setPosition(position.x + Vector.getDirection(rotation + 276).x * 0.3f * (timer - 0.3f), position.y + Vector.getDirection(rotation + 276).y * 0.3f * (timer - 0.3f), 0);
                airblade.setRotation(rotation + 276);
                airblade.setScale(0.2f, 0.05f, 1);
                airblade.draw(gl);
            }
            delete -= ActiveElement.delayTime / 1000;
            megabomb.bullets_draw(gl);
            if (delete <= 0) { check_bullets = false; delete = 1; }
        } else if(megabomb.next != null) {
            megabomb.bullets_draw(gl);
        } else delete = 0;

    }
    @Override
    public void GUIdraw(GL10 gl) {
        if (weapon_nav){
            for (Pack pack : geoSistem.packs) {
                if (pack.tag == Pack.TAG_TENTACLE && pack.target_enemy != geoSistem.getNav() && Vector.Distance(pack.position,geoSistem.getNav().position) > geoSistem.getNav().vis_zone) {
                    pack.emblem.setPosition(Vector.normalize(pack.position.minus(geoSistem.getNav().position)));
                    pack.emblem.position.x *= 0.8f;
                    pack.emblem.position.y *= 0.8f;
                    pack.emblem.draw(gl);
                }
            }
        } else {
            gui_megabomb.draw(gl);
            if (mb_fire_rate > mb_prev_fire)
                ActiveElement.printText(gl,new Vector(gui_megabomb.position.x - ((""+(int)(mb_fire_rate - mb_prev_fire)).length()-1)*0.06f - 0.09f, gui_megabomb.position.y,0), 0.06f,1,0,0,""+(int)(mb_fire_rate - mb_prev_fire));
            else
                ActiveElement.printText(gl, new Vector(gui_megabomb.position.x - 1.5f * 0.06f - 0.09f, gui_megabomb.position.y, 0), 0.05f, 0.5f, 1f, 0.5f, "100$");
            //mb_fire_rate < mb_prev_fire
            gui_hiperlaser.draw(gl);

            gui_menuframe.draw(gl);
            gui_fighter.draw(gl);
            ActiveElement.printText(gl, new Vector(gui_fighter.position.x + 0.2f, gui_fighter.position.y + 0.02f, 0), 0.04f, 0.5f, 1f, 0.5f, "11");
            ActiveElement.printText(gl, new Vector(gui_fighter.position.x + 0.2f, gui_fighter.position.y - 0.08f, 0), 0.04f, 1f, 1f, 0f, "1/3");
            gui_assault.draw(gl);
            ActiveElement.printText(gl, new Vector(gui_assault.position.x + 0.2f, gui_assault.position.y + 0.02f, 0), 0.04f, 0.5f, 1f, 0.5f, "16");
            ActiveElement.printText(gl, new Vector(gui_assault.position.x + 0.2f, gui_assault.position.y - 0.08f, 0), 0.04f, 1f, 1f, 0f, "2/6");
            gui_medic.draw(gl);
            ActiveElement.printText(gl, new Vector(gui_medic.position.x + 0.2f, gui_medic.position.y+0.02f, 0), 0.04f, 0.5f, 1f, 0.5f, "27");
            ActiveElement.printText(gl, new Vector(gui_medic.position.x + 0.2f, gui_medic.position.y - 0.08f, 0), 0.04f, 1f, 1f, 0f, "3/9");
            gui_scout.draw(gl);
            ActiveElement.printText(gl, new Vector(gui_scout.position.x + 0.2f, gui_scout.position.y+0.02f, 0), 0.04f, 0.5f, 1f, 0.5f, "7");
            ActiveElement.printText(gl, new Vector(gui_scout.position.x + 0.2f, gui_scout.position.y-0.08f, 0), 0.04f, 1f, 1f, 0f, "1/1");

            super.GUIdraw(gl);
        }
    }

    @Override
    public int GUIinPut(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (weapon_nav) {
                // навигация по отрядам что врагов завидели
                Vector touch = Vector.prepare_event(event);
                for (Pack pack : geoSistem.packs) {
                    if (pack.tag == Pack.TAG_TENTACLE && pack.target_enemy != geoSistem.getNav() && pack.emblem.isTouchSphere(touch)) {
                        geoSistem.setNav(pack);
                        return next;
                    }
                } // выбор вражины что тыкнул ты
                touch = geoSistem.cam_set_event(event);
                for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && unit.isTouchSphere(touch)) {
                        if(mb_ready) {
                            if (cash >= 100) {
                                megabomb.Shot(gun_megabomb.position, unit.position);
                                mb_prev_fire = 0;
                                cash -= 100;
                            }
                        } else if (hl_ready){
                            if (energy == max_energy) {
                                hiperlaser.Shot(gun_hiperlaser.position, unit.position, energy);
                                energy = 0;
                            }
                        }
                        //geoSistem.setNav(pack);
                        mb_ready = false;
                        hl_ready = false;
                        weapon_nav = false;
                        return exit;
                    }
                geoSistem.setNav(pack);
                weapon_nav = false;
                mb_ready = false;
                hl_ready = false;
                return next;
            } else {
                Unit new_unit = null;
                if (gui_megabomb.isTouchSphere(Vector.prepare_event(event))) {
                    if (!mb_ready && mb_fire_rate < mb_prev_fire && cash >= 100) {
                        weapon_nav = true;
                        mb_ready = true;
                        return next;
                    } else {
                        return next;
                    }
                } else if (gui_hiperlaser.isTouchSphere(Vector.prepare_event(event))) {
                    if (!hl_ready && energy == max_energy) {
                        weapon_nav = true;
                        hl_ready = true;
                        return next;
                    } else {
                        return next;
                    }
                } else if (gui_fighter.isTouchSphere(Vector.prepare_event(event))) {
                    if(cash >= 11 && tian >= 1) {
                        new_unit = new Fighter(geoSistem);
                        cash -= 11;
                        tian -= 1;
                    } else return next;
                } else if (gui_assault.isTouchSphere(Vector.prepare_event(event))) {
                    if(cash >= 16 && tian >= 2) {
                        new_unit = new Assault(geoSistem);
                        cash -= 16;
                        tian -= 2;
                    } else return next;
                } else if (gui_medic.isTouchSphere(Vector.prepare_event(event))) {
                        if(cash >= 27 && tian >= 3) {
                            new_unit = new Medic(geoSistem);
                            cash -= 27;
                            tian -= 3;
                        } else return next;
                } else if (gui_scout.isTouchSphere(Vector.prepare_event(event))) {
                            if(cash >= 7 && tian >= 1) {
                                Pack new_pack = new Pack(geoSistem,Pack.TAG_TIAN);
                                new_unit = new Scout(geoSistem);
                                new_unit.setPosition(position);
                                new_pack.target = pack;
                                new_pack.mission = Pack.MISSION_SCOUT;
                                new_unit.setPack(new_pack);
                                geoSistem.addUnit(new_unit);
                                geoSistem.addPack(new_pack);
                                cash -= 7;
                                tian -= 1;
                                return next;
                            } else return next;
                }
                if (new_unit != null) {
                    new_unit.setPosition(position);
                    new_unit.setPack(pack);
                    new_unit.resLoad();
                    geoSistem.addUnit(new_unit);
                    return next;
                }
            }
        }
        return super.GUIinPut(event);
    }

    @Override
    public void resLoad() {
        super.resLoad();
        setSprite(geoSistem.textures[14]);
        avatar.setSprite(geoSistem.textures[17]);

        gun_hiperlaser.setSprite(geoSistem.textures[15]);
        gun_megabomb.setSprite(geoSistem.textures[16]);

        gui_menuframe.setSprite(geoSistem.textures[21]);
        gui_fighter.setSprite(geoSistem.textures[22]);
        gui_assault.setSprite(geoSistem.textures[23]);
        gui_medic.setSprite(geoSistem.textures[24]);
        gui_scout.setSprite(geoSistem.textures[25]);

        gui_megabomb.setSprite(geoSistem.textures[26]);
        gui_hiperlaser.setSprite(geoSistem.textures[27]);

        hiperlaser.setSprite(geoSistem.textures[28]);

        megabomb.setSprite(geoSistem.textures[30]);
    }

    public class Bullet extends Collider{

        public float speed;
        Vector direction;
        float life_time;
        float start_time;
        float dmg1, dmg2, dmg3;
        public boolean death;
        float boom_time;

        Collider megabomb_explosion1;
        Collider megabomb_explosion2;
        Collider megabomb_explosion3;

        GeoSystem gs;

        Bullet next;

        public Bullet(GeoSystem gs){
            this.gs = gs;
            next = null;
        }

        public LinkedList<Unit> getHitsUnit(){
            LinkedList<Unit> u_list = null;
            Bullet cur = this;
            while (cur.next != null) {
                if (cur.next.death) {
                    if (u_list == null) u_list = new LinkedList<Unit>();
                    for (Unit unit : geoSistem.units)
                        if (!unit.death && cur.next.megabomb_explosion3.isCollision(unit)) u_list.add(unit);
                }
                cur = cur.next;
            }
            return u_list;
        }
        public void Shot(Vector position, Vector target){
            Bullet shot = new Bullet(gs);

            shot.megabomb_explosion1 = new Collider();
            shot.megabomb_explosion2 = new Collider();
            shot.megabomb_explosion3 = new Collider();

            shot.setPosition(position);
            shot.setSphere(0.04f);
            shot.setScale(0.08f, 0.1f, 1);
            shot.direction = Vector.normalize(target.minus(position));
            shot.setRotation(Vector.getAngle(shot.direction));
            shot.dmg1 = 1000;
            shot.dmg2 = 4000;
            shot.dmg3 = 10000;
            shot.speed = 1f;
            shot.life_time = 10;
            shot.start_time = 0;
            shot.boom_time = 0;
            shot.death = false;
            //shot.next = null; // список

            if(next != null)
                shot.next = next;

            next = shot;
            setSprite(geoSistem.textures[30]);
        }
        public void bullets_draw(GL10 gl) {
            Bullet cur = this;
            while (cur.next != null)
                if (cur.next.death) {
                    cur.next = cur.next.next;
                } else {
                    cur.next.draw(gl);
                    cur = cur.next;
                }
        }

        public void setSprite(Integer tex_id) {
            Bullet cur = next;
            while (cur != null){
                cur.texture = tex_id;
                cur.megabomb_explosion1.setSprite(geoSistem.textures[31]);
                cur.megabomb_explosion2.setSprite(geoSistem.textures[32]);
                cur.megabomb_explosion3.setSprite(geoSistem.textures[33]);
                cur = cur.next;
            }
        }

        @Override
        public void draw(GL10 gl) {
            if(!death) {
                // полет
                if (boom_time == 0) {
                    position.x += direction.x * speed * ActiveElement.delayTime / 1000;
                    position.y += direction.y * speed * ActiveElement.delayTime / 1000;
                    super.draw(gl);
                }
                // попадание
                if (life_time < (start_time += ActiveElement.delayTime / 1000)) {
                    boom_time += ActiveElement.delayTime / 1000;
                    megabomb_explosion1.setPosition(position);
                    megabomb_explosion2.setPosition(position);
                    megabomb_explosion3.setPosition(position);
                    megabomb_explosion3.setSphere(1f * 2);
                    megabomb_explosion2.setSphere(0.8f * 2);
                    megabomb_explosion1.setSphere(0.6f * 2);
                } else for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && unit.isCollision(this)){
                        boom_time += ActiveElement.delayTime / 1000;
                        megabomb_explosion1.setPosition(position);
                        megabomb_explosion2.setPosition(position);
                        megabomb_explosion3.setPosition(position);
                        megabomb_explosion3.setSphere(1f * 2);
                        megabomb_explosion2.setSphere(0.8f * 2);
                        megabomb_explosion1.setSphere(0.6f * 2);
                        //unit.dealDamage(dmg);
                        //death = true;
                        break;
                    }
                if (boom_time != 0 && (boom_time += ActiveElement.delayTime / 1000) < 0.4f){
                    megabomb_explosion3.setScale(boom_time / 0.4f * 2);
                    megabomb_explosion2.setScale(boom_time / 0.4f * 0.8f * 2);
                    megabomb_explosion1.setScale(boom_time / 0.4f * 0.6f * 2);

                    megabomb_explosion2.setRotation(boom_time / 0.4f * 360);
                    megabomb_explosion3.setRotation(boom_time / 0.4f * 270);

                    megabomb_explosion1.draw(gl);
                    megabomb_explosion3.draw(gl);
                    megabomb_explosion2.draw(gl);
                } else if (boom_time >= 0.4f) {
                    int cash = 0;
                    for (Unit unit : geoSistem.units) {
                        if (!unit.death/* && unit.pack.tag == Pack.TAG_TENTACLE*/) {
                            if (megabomb_explosion1.isCollision(unit)) cash += unit.dealDamage(dmg1);
                            if (megabomb_explosion2.isCollision(unit)) cash += unit.dealDamage(dmg2);
                            if (megabomb_explosion3.isCollision(unit)) cash += unit.dealDamage(dmg3);
                        }
                    }
                    if (cash > 0) for (Pack p : geoSistem.packs) if(p.mission == Pack.MISSION_BASE){ Base base = (Base)p; if (base.base_ship != null && !base.base_ship.death) base.base_ship.cash += cash;}
                    death = true;
                }
            }
        }
    }

    public class Laser extends Collider{

        Vector direction;
        Vector start_pos;
        float energy;
        public boolean death;
        Sprite hiperlaser_lightning;

        GeoSystem gs;

        Laser next;

        public Laser(GeoSystem gs){
            this.gs = gs;
            next = null;
        }

        public void Shot(Vector position, Vector target, float energy){
            Laser shot = new Laser(gs);

            shot.hiperlaser_lightning = new Sprite();

            //shot.setPosition(position);
            shot.start_pos = position;
            shot.setScale(0.2f, 10f, 1);
            shot.setBox(shot.scale);
            shot.direction = Vector.normalize(target.minus(position));
            shot.setRotation(Vector.getAngle(shot.direction));
            shot.setPosition(position.x + (shot.direction.x * shot.box_collider.y), position.y + (shot.direction.y * shot.box_collider.y), 0);
            shot.energy = energy;
            shot.death = false;
            //shot.next = null; // список

            if(next != null)
                shot.next = next;

            next = shot;
            setSprite(geoSistem.textures[28]);
        }
        public void laser_draw(GL10 gl) {
            Laser cur = this;
            while (cur.next != null)
                if (cur.next.death) {
                    cur.next = cur.next.next;
                } else {
                    cur.next.draw(gl);
                    cur = cur.next;
                }
        }

        public void setSprite(Integer tex_id) {
            Laser cur = next;
            while (cur != null){
                cur.texture = tex_id;
                cur.hiperlaser_lightning.setSprite(geoSistem.textures[29]);
                cur = cur.next;
            }
        }

        @Override
        public void draw(GL10 gl) {
            if (!death && energy > 0) {
                float dmg;
                energy -= dmg = ActiveElement.delayTime / 1000 * 1000 / 1f;
                // нанесение последовательного урона гиперлазером
                Unit u_atack = null;
                float u_dist = Float.MAX_VALUE;
                for (Unit unit : geoSistem.units) {
                    if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && isBoxTouchSphere(unit) && Vector.Distance(start_pos, unit.position) < u_dist){
                        u_dist = Vector.Distance(start_pos, unit.position);
                        u_atack = unit;
                    }
                }
                if (u_atack != null) cash += u_atack.dealDamage(dmg);
                super.draw(gl);
            } else death = true;
        }
    }
}
