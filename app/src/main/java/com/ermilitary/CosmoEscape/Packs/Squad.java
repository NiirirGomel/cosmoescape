package com.ermilitary.CosmoEscape.Packs;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Squad extends Pack {
    static final int GUI_NONE = 0;
    static final int GUI_HELP = 1;
    static final int GUI_ATTACK = 2;
    Sprite line;
    Collider gui_emblem;
    public Collider gui_atack;
    public Collider gui_help;
    public Collider gui_disband;
    public Collider gui_escape;
    Collider gui_scout;
    int gui_option;
    float otmena_timer;

    public Squad(GeoSystem geoSystem, int tag){
        super(geoSystem, tag);
        gui_option = GUI_NONE;
        gui_emblem = new Collider();
        gui_emblem.setScale(0.2f);
        gui_emblem.setSphere(0.2f);
        float icon_size = 0.15f;
        //line = new Sprite();
        gui_atack = new Collider();
        gui_atack.setScale(icon_size);
        gui_atack.setSphere(icon_size);
        gui_help = new Collider();
        gui_help.setScale(icon_size);
        gui_help.setSphere(icon_size);
        gui_disband = new Collider();
        gui_disband.setScale(icon_size);
        gui_disband.setSphere(icon_size);
        gui_escape = new Collider();
        gui_escape.setScale(icon_size);
        gui_escape.setSphere(icon_size);
        gui_scout = new Collider();
        gui_scout.setScale(icon_size);
        gui_scout.setSphere(icon_size);

        //gui_emblem.setPosition(Game.ratio - 0.35f, -0.65f, 0);
        //gui_scout.setPosition(Game.ratio - 0.95f,-0.75f,0);
        gui_atack.setPosition(-Game.ratio + 0.22f, 0.75f,0);
        gui_help.setPosition(-Game.ratio + 0.22f, 0.40f,0);
        gui_escape.setPosition(-Game.ratio + 0.22f, 0.05f,0);
        gui_disband.setPosition(gui_escape.position);

    }
    public void resLoad(){
        super.resLoad();
//        line.setSprite(geoSistem.textures[54]);
        gui_emblem.setSprite(emblem.texture);
        gui_atack.setSprite(geoSistem.textures[55]);
        gui_help.setSprite(geoSistem.textures[56]);
        gui_disband.setSprite(geoSistem.textures[57]);
        gui_escape.setSprite(geoSistem.textures[58]);
        gui_scout.setSprite(geoSistem.textures[59]);
    }

    @Override
    public void draw(GL10 gl) {
        // выбор эмблемы
        if (mission == MISSION_ATTACK)
            emblem.setSprite(gui_atack.texture);
        else if (mission == MISSION_HELP)
            emblem.setSprite(gui_help.texture);
        else if (mission == MISSION_SCOUT)
            emblem.setSprite(gui_scout.texture);
        else if (mission == MISSION_PARKING_BASE)
            emblem.setSprite(gui_disband.texture);

        super.draw(gl);
    }

    public void GUIdraw(GL10 gl){
        String find;
        float text_size = 0.04f;
        //line.draw(gl);
        //gui_emblem.draw(gl);
        // *атака*
        gui_atack.draw(gl);
        if (mission == MISSION_ATTACK && gui_option != GUI_ATTACK){
            find = "Выполнение";
            //ActiveElement.printText(gl, new Vector(gui_atack.position.x - find.length() * text_size * 1.5f - 0.2f, gui_atack.position.y, 0), text_size, 0.5f, 0.5f, 1f, find);
            ActiveElement.printText(gl, new Vector(gui_atack.position.x + 0.16f, gui_atack.position.y+0.05f, 0), text_size, 0.5f, 0.5f, 1f, find);
        }
        // *помощь*
        gui_help.draw(gl);
        if (mission == MISSION_HELP && gui_option != GUI_HELP){
            find = "Выполнение";
            //ActiveElement.printText(gl, new Vector(gui_help.position.x - find.length()*text_size*1.5f - 0.2f,gui_help.position.y,0),text_size,0.5f,0.5f,1f,find);
            ActiveElement.printText(gl, new Vector(gui_help.position.x + 0.16f,gui_help.position.y+0.05f,0),text_size,0.5f,0.5f,1f,find);
        }
        // *база/отсткпить*
        if (mission == MISSION_PARKING_BASE)
            gui_disband.draw(gl);
             else
            gui_escape.draw(gl);
        //gui_scout.draw(gl);

        if (gui_option == GUI_HELP) {
            find = "Поиск союзника";
            if (geoSistem.getNav() != this) {
                //ActiveElement.printText(gl, new Vector(gui_help.position.x - find.length()*text_size*1.5f - 0.2f,gui_help.position.y,0),text_size,0.5f,0.5f,1f,find);
                ActiveElement.printText(gl, new Vector(gui_help.position.x + 0.16f,gui_help.position.y+0.05f,0),text_size,0.5f,0.5f,1f,find);

                // навигация
                for (Pack pack : geoSistem.packs) {
                    if (((pack.tag == tag && pack != target && pack.mission != MISSION_BASE && pack.mission != Pack.MISSION_SCOUT) || (pack.tag == TAG_RESORCES)) && pack != this && pack != geoSistem.getNav() && Vector.Distance(pack.position, geoSistem.getCam().position) > 1/geoSistem.getCam().scale.x) {
                        pack.emblem.setPosition(Vector.normalize(pack.position.minus(geoSistem.getCam().position)));
                        pack.emblem.position.x *= 0.8f;
                        pack.emblem.position.y *= 0.8f;
                        pack.emblem.draw(gl);
                    }
                }
            } else if ((otmena_timer += ActiveElement.delayTime / 1000) < 1f){
                find = "Отмена";
                //ActiveElement.printText(gl, new Vector(gui_help.position.x - find.length()*text_size*1.5f - 0.2f,gui_help.position.y,0),text_size,0.5f,0.5f,1f,find);
                ActiveElement.printText(gl, new Vector(gui_help.position.x + 0.16f,gui_help.position.y+0.05f,0),text_size,0.5f,0.5f,1f,find);
            } else {
                gui_option = GUI_NONE;
                nav = false;
            }
        }
        if (gui_option == GUI_ATTACK) {
            find = "Поиск врага";
            if (geoSistem.getNav() != this) {
                //ActiveElement.printText(gl, new Vector(gui_atack.position.x - find.length()*text_size*1.5f - 0.2f,gui_atack.position.y,0),text_size,0.5f,0.5f,1f,find);
                ActiveElement.printText(gl, new Vector(gui_atack.position.x + 0.16f,gui_atack.position.y+0.05f,0),text_size,0.5f,0.5f,1f,find);

                // навигация
                for (Pack pack : geoSistem.packs) {
                    if (pack.tag == Pack.TAG_TENTACLE && pack != target && pack != geoSistem.getNav() && Vector.Distance(pack.position, geoSistem.getCam().position) > 1/geoSistem.getCam().scale.x) {
                        pack.emblem.setPosition(Vector.normalize(pack.position.minus(geoSistem.getCam().position)));
                        pack.emblem.position.x *= 0.8f;
                        pack.emblem.position.y *= 0.8f;
                        pack.emblem.draw(gl);
                    }
                }
            } else if ((otmena_timer += ActiveElement.delayTime / 1000) < 1f){
                find = "Отмена";
                //ActiveElement.printText(gl, new Vector(gui_atack.position.x - find.length()*text_size*1.5f - 0.2f,gui_atack.position.y,0),text_size,0.5f,0.5f,1f,find);
                ActiveElement.printText(gl, new Vector(gui_atack.position.x + 0.16f,gui_atack.position.y+0.05f,0),text_size,0.5f,0.5f,1f,find);
            } else {
                gui_option = GUI_NONE;
                nav = false;
            }
        }
    }

    public boolean GUIinPut(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            // в атаке интерфейс вкючи свой навигации для
            if(gui_option == GUI_ATTACK) {
                // навигация по отрядам что врагов завидели
                Vector touch = Vector.prepare_event(event);
                for (Pack pack : geoSistem.packs) {
                    if (pack.tag == Pack.TAG_TENTACLE && pack != target && pack != geoSistem.getNav() && Vector.Distance(pack.position, geoSistem.getCam().position) > 1/geoSistem.getCam().scale.x && pack.emblem.isTouchSphere(touch)) {
                        geoSistem.setNav(pack);
                        return true;
                    }
                } // выбор вражины что тыкнул ты
                touch = geoSistem.cam_set_event(event);
                for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && unit.pack != target && unit.isTouchSphere(touch)) {
                        target_enemy = null;
                        target_resource = null;
                        mission = MISSION_ATTACK;
                        target = unit.pack;
                        geoSistem.setNav(this);
                        gui_option = GUI_NONE;
                        nav = false;
//                        for (Unit u : geoSistem.units)
//                            if (!u.death && u.pack == this) {
//                                geoSistem.SayOne(0f,u,"Приступаю к уничтожению\nврага...");
//                                break;
//                            }
                        return true;
                    }
                otmena_timer = 0;
                geoSistem.setNav(this);
                //gui_option = GUI_NONE;
                //nav = false;
            }
            // помагая товарищам интерфейс вкючи свой навигации для
            if(gui_option == GUI_HELP) {
                // навигация по отрядам своим
                Vector touch = Vector.prepare_event(event);
                for (Pack pack : geoSistem.packs) {
                    if (((pack.tag == tag && pack.mission != MISSION_BASE && pack.mission != Pack.MISSION_SCOUT && pack.mission != MISSION_HELP) || (pack.tag == TAG_RESORCES)) && pack != this && pack != geoSistem.getNav() && Vector.Distance(pack.position, geoSistem.getCam().position) > 1/geoSistem.getCam().scale.x && pack.emblem.isTouchSphere(touch)) {
                        geoSistem.setNav(pack);
                        return true;
                    }
                } // выбор корабля что тыкнул ты
                touch = geoSistem.cam_set_event(event);
                for (Unit unit : geoSistem.units)
                    if (!unit.death && ((unit.pack.tag == tag && unit.pack != this && unit.pack.mission != MISSION_BASE && unit.pack.mission != Pack.MISSION_SCOUT && unit.pack.mission != MISSION_HELP) || (unit.pack.tag == TAG_RESORCES)) && unit.isTouchSphere(touch)) {
                        target_enemy = null;
                        target_resource = null;
                        mission = MISSION_HELP;
                        target = unit.pack;
                        geoSistem.setNav(this);
                        gui_option = GUI_NONE;
                        nav = false;
//                        for (Unit u : geoSistem.units)
//                            if (!u.death && u.pack == this) {
//                                geoSistem.SayOne(0f,u,"Вылетаею на помощь\nуказанному отряяду...");
//                                break;
//                            }
                        return true;
                    }
                otmena_timer = 0;
                geoSistem.setNav(this);
                //gui_option = GUI_NONE;
                //nav = false;
            }
            // гуи интерфейс твой
            if (gui_atack.isTouchSphere(Vector.prepare_event(event))) {
                gui_option = GUI_ATTACK;
                geoSistem.setNav(null);
                nav = true;
                return true;
            } else if (gui_help.isTouchSphere(Vector.prepare_event(event))) {
                gui_option = GUI_HELP;
                geoSistem.setNav(null);
                nav = true;
                return true;
            } else if (mission != MISSION_PARKING_BASE && gui_escape.isTouchSphere(Vector.prepare_event(event))) {
                target_enemy = null;
                target_resource = null;
                mission = MISSION_PARKING_BASE;
                for(Pack p : geoSistem.packs)
                    if (p.tag == tag && p.mission == MISSION_BASE)
                        target = p;
                return true;
            } else if (mission == MISSION_PARKING_BASE && gui_disband.isTouchSphere(Vector.prepare_event(event))) {
                geoSistem.setNav(target);
                for(Unit u : geoSistem.units)
                    if (u.pack == this) {
                        u.setPack(target);
                        u.unselection();
                    }
            }
//            else if (gui_scout.isTouchSphere(Vector.prepare_event(event))) {
//                // если в отряде есть не разведчики то не разрешает патрулировать
//                for (Unit unit : geoSistem.units)
//                    if (!unit.death && unit.pack == this && !unit.getClass().getSimpleName().equals("Scout"))
//                        return true;
//                // ищет базу для патрулирования
//                for(Pack p : geoSistem.packs)
//                    if (p.tag == tag && p.mission == MISSION_BASE){
//                        target_enemy = null;
//                        target_resource = null;
//                        mission = MISSION_SCOUT;
//                        target = p;
//                        break;
//                    }
//                return true;
//            }
            return false;
        }
        return true;
    }
}
