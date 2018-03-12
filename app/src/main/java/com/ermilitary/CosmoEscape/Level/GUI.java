package com.ermilitary.CosmoEscape.Level;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Game;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Packs.Squad;
import com.ermilitary.CosmoEscape.Units.Tentacle.*;
import com.ermilitary.CosmoEscape.Units.Tian.*;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class GUI extends Camera {
    public Unit targetUnit;
    public Pack targetPack;
    public Unit targetRadioUnit;
    // для синхронизации потоков ин и драв
    public boolean targetUnit_off = false;
    public boolean targetPack_off = false;
    public boolean targetRadioUnit_off = false;
    Squad nPack;
    public Base targetBase;
    public Collider text_frame;

    //gui elements
    Collider select_zone;
    Vector select_first_touch;
    Sprite tian;
    Sprite cash;
    Collider button[] = new Collider[3]; // выбор, подтверждение, отмена

    String radio_prev;
    float say_time = 0;
    float say_endtime = 0;

    public GUI(){
        text_frame = new Collider();
        select_zone = null;
        for(int i = 0; i < 3; i++) {
            button[i] = new Collider();
            button[i].setScale(0.2f);
            button[i].setSphere(0.2f);
        }
        button[0].setPosition(Game.ratio - 0.4f, -0.65f, 0);
        button[1].setPosition(Game.ratio - 0.9f, -0.65f, 0);
        button[2].setPosition(Game.ratio - 0.4f,-0.65f,0);

        tian = new Sprite();
        tian.setScale(0.075f,0.1f,1);
        tian.setPosition(0.2f - Game.ratio,-0.55f,0);
        cash = new Sprite();
        cash.setScale(0.075f, 0.1f, 1);
        cash.setPosition(0.2f - Game.ratio, -0.8f, 0);

        text_frame.setScale(0.8f,0.25f,1f);
        text_frame.setBox(0.8f, 0.25f);
        text_frame.setPosition(0.2f, -0.65f, 1f);
        radio_prev = "";

    }
    public boolean SayOneSoft(float time, Unit unit, String say) {
        if (targetRadioUnit == null){
            SayOne(time,unit,say);
            return true;
        }
        return false;
    }
    public void SayOne(float time, Unit unit, String say) {
        say_time = 0;
        say_endtime = time;
        unit.radio = say;
        targetRadioUnit = unit;
    }

    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
        // очистка убитых и потеряных
        if(targetUnit != null && (targetUnit.death || targetUnit_off)) { targetUnit = null; targetUnit_off = false; }
        if(targetPack != null && targetPack.size == 0) targetPack = null;
        if(targetRadioUnit != null && targetRadioUnit.radio.length() <= 0) { targetRadioUnit = null; say_time = 0; say_endtime = 0; }

        // таймер временных сообщений
        if (say_time < say_endtime){
            say_time += delayTime / 1000;
        } else if (say_endtime != 0) {
            targetRadioUnit = null;
            say_endtime = 0;
            say_time = 0;
        }

        if(targetRadioUnit != null){
            targetRadioUnit.GUIdraw(gl);
            text_frame.draw(gl);
            ActiveElement.printText(
                    gl,
                    new Vector(text_frame.position.x - text_frame.scale.x + 0.1f, text_frame.position.y + text_frame.scale.y - 0.07f,0),
                    0.04f,
                    0,0,0,
                    targetRadioUnit.radio);

        } else if(targetUnit != null){
            targetUnit.GUIdraw(gl);
            if (targetUnit.radio.length() > 0){
                text_frame.draw(gl);
                ActiveElement.printText(
                        gl,
                        new Vector(text_frame.position.x - text_frame.scale.x + 0.1f, text_frame.position.y + text_frame.scale.y - 0.07f,0),
                        0.04f,
                        0,0,0,
                        targetUnit.radio);
            }
        } else if(targetPack != null && targetPack.tag == Pack.TAG_TIAN && targetPack != targetBase) targetPack.GUIdraw(gl);

        // зона выделения
        if (select_zone != null) {
            Collider nsz = select_zone;
            gl.glColor4f(0.2f, 0.2f, 1, 0.3f);
// рассинхра?
            nsz.draw(gl);
            gl.glColor4f(1, 1, 1, 1);
        }
         // навигация по отрядам
         if (targetUnit == null && targetRadioUnit  == null && !(targetPack != null && targetPack.nav)) {
             for (Pack pack : packs) {
                 if (pack.tag != Pack.TAG_RESORCES && pack.mission != Pack.MISSION_SCOUT && pack != getNav() && Vector.Distance(pack.position, getCam().position) > 1/getCam().scale.x) {
                     pack.emblem.setPosition(Vector.normalize(pack.position.minus(getCam().position)));
                     pack.emblem.position.x *= 0.8f;
                     pack.emblem.position.y *= 0.8f;
                     pack.emblem.draw(gl);
                 }
             }
        }
        // показатели кэша и тян базы
        if(targetBase != null && targetBase.size != 0) {
            tian.draw(gl);
            text.print(gl, new Vector(tian.position.x + 0.15f, tian.position.y, 0), 0.05f, 1f, 1f, 0f, "" + targetBase.base_ship.tian);
            cash.draw(gl);
            text.print(gl, new Vector(cash.position.x + 0.15f, cash.position.y, 0), 0.05f, 0.5f, 1f, 0.5f, "" + targetBase.base_ship.cash);
        }
    }

    @Override
    public boolean inPut(MotionEvent event) {
        // *радейка в первую очередь
        if(targetRadioUnit != null){
            if (event.getAction() == MotionEvent.ACTION_DOWN && text_frame.isTouchBox(Vector.prepare_event(event))){
                targetRadioUnit.radio = "";
                return true;
            } else if (targetRadioUnit.GUIinPut(event) == Unit.exit) {
                targetRadioUnit.radio = "";
            } else if (select_zone == null) return true;
        // **юнит во вторую
        } else if (targetUnit != null) {
            // радейка юнита
            if (!targetUnit.radio.isEmpty() && event.getAction() == MotionEvent.ACTION_DOWN && text_frame.isTouchBox(Vector.prepare_event(event))) {
                targetUnit.radio = "";
                return true;
            // ГУИ и снятие юнита с тарджета
            } else if (targetUnit.GUIinPut(event) == Unit.exit) {
                targetUnit.unselection();
                // выделение
                if (targetPack != null) {
                    for (Unit unit : units)
                        if (unit.pack == targetPack) unit.selection();
                    setNav(targetPack);
                }
                targetUnit_off = true;
                //targetUnit = null;
            } else if (select_zone == null) return true;
        // ***отряд в третию
        } else if (targetPack != null && targetPack != targetBase && targetPack.GUIinPut(event)) if (select_zone == null) return true;

        // ****выбор ручками в четвертую
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // ***навигация по отрядам***
                Vector touch = Vector.prepare_event(event);
                if (targetUnit == null && targetRadioUnit == null && !(targetPack != null && targetPack.nav)) {
                    for (Pack pack : packs)
                        if (pack.tag != Pack.TAG_RESORCES && pack.mission != Pack.MISSION_SCOUT && pack != targetNav && Vector.Distance(pack.position, targetNav.position) > targetNav.vis_zone && pack.emblem.isTouchSphere(touch)) {
                            setNav(pack);
                            if (pack.tag == Pack.TAG_TIAN) {
                                targetPack = pack;
                                if (pack.mission == Pack.MISSION_BASE) {
                                    for (Unit unit : units) unit.unselection();
                                    targetBase.base_ship.selection();
                                    targetUnit = targetBase.base_ship; targetUnit_off = false;
                                } else {
                                    // выделение
                                    for (Unit unit : units)
                                        if (unit.pack == targetPack) unit.selection();
                                        else unit.unselection();
                                }
                            } else {
                                if (targetUnit != null) targetUnit_off = true;
                                targetPack = null;
                                for (Unit unit : units) unit.unselection();
                            }
                            return true;
                        }
                } // ***обработка нажатий на корабли***
                touch = cam_set_event(event);
                for (Unit unit : units)
                    if (!unit.death && unit.pack.mission != Pack.MISSION_SCOUT && unit.isTouchSphere(touch)) {
                        // выбор юнита в паке
                        if (unit.pack.tag == Pack.TAG_TIAN) {
                            if (unit == targetBase.base_ship) {
                                for (Unit u : units) u.unselection();
                                targetBase.base_ship.selection();
                                targetPack = targetBase;
                                targetUnit = targetBase.base_ship; targetUnit_off = false;
                                setNav(targetBase);
                                return true;
                            } else if (unit.pack.mission == Pack.MISSION_BASE) {
                                //targetUnit = null;
                                if (targetUnit != null) targetUnit_off = true;
                                nPack = new Squad(this, targetBase.tag);
                                nPack.target = targetBase;
                                nPack.mission = Pack.MISSION_PARKING_BASE;
                                for (Unit u : units)
                                    if (!u.death && u.pack.mission == Pack.MISSION_BASE && u != targetBase.base_ship) {
                                        u.setPack(nPack);
                                        u.selection();
//                                                if(unit.pack.mission != Pack.MISSION_BASE)
//                                                    unit.radio = "Подтверждаю вступление\nв отрядддд.";
                                    } else u.unselection();
                                addPack(nPack);
                                targetPack = nPack;
                                setNav(nPack);
                                return true;
                            } else if (targetPack != null && unit.pack == targetPack) {
                                targetUnit = unit; targetUnit_off = false;
                                for (Unit u : units)
                                    if (u == unit) u.selection();
                                    else u.unselection();
                                return true;
                            } else {
                                //targetUnit = null;
                                if (targetUnit != null) targetUnit_off = true;
                                targetPack = unit.pack;
                                setNav(targetPack);
                                for (Unit u : units)
                                    if (u.pack == targetPack) u.selection();
                                    else u.unselection();
                                return true;
                            }
                        } else if (unit.pack.tag == Pack.TAG_TENTACLE || unit.pack.tag == Pack.TAG_RESORCES) {
                            targetUnit = unit; targetUnit_off = false;
                            setNav(unit.pack);
                            for (Unit u : units)
                                if (u == unit) unit.selection();
                                else u.unselection();
                            return true;
                        }
                    }
                // снятие тарджета с юнита и подготовка к выделению нового отряда
                if (targetUnit != null && targetUnit.pack.tag == Pack.TAG_TIAN){
                    //targetUnit = null;
                    targetUnit_off = true;
                    for (Unit u : units)
                        if (u.pack == targetPack) u.selection();
                        else u.unselection();
                }
                // начать выделение
                select_zone = new Collider();
                select_zone.setSprite(0);
                select_zone.setBox(0);
                select_zone.setScale(0);
                select_first_touch = new Vector(Vector.prepare_event(event));
                select_zone.setPosition(select_first_touch);
                break;
            case MotionEvent.ACTION_MOVE:
                // выделение зоны
                if (select_zone != null) {
                    Vector t = new Vector(Vector.prepare_event(event));
                    Vector box = new Vector(select_zone.position.minus(t));
                    box.set(Math.abs(box.x), Math.abs(box.y), 0);
                    select_zone.setBox(box);
                    select_zone.setScale(box);
                    select_zone.setPosition((t.x - select_first_touch.x) / 2 + select_first_touch.x, (t.y - select_first_touch.y) / 2 + select_first_touch.y, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                // завершение выделения и создание отряда
                if (select_zone != null) {
                    // новый отряд
                    nPack = new Squad(this, targetBase.tag);
                    nPack.mission = Pack.MISSION_PARKING_BASE;
                    nPack.target = targetBase;
                    for (Unit unit : units)
                        if (!unit.death && unit.pack.tag == targetBase.tag && unit != targetBase.base_ship && unit.pack.mission != Pack.MISSION_SCOUT && select_zone.isTouchBox(cam_set_vec(unit.position)))
                            unit.setPack(nPack);
                    if (nPack.size > 0) {
                        for (Unit unit : units) if (unit.pack == nPack) unit.selection(); else unit.unselection();
                        targetPack = nPack;
                        addPack(nPack);
                        setNav(nPack);
                    } else {

                    }
                    nPack = null;
                    select_zone = null;
                }
                break;
        }
        return true;
    }

    @Override
    public void resource_load(GL10 gl) {
        super.resource_load(gl);
        text_frame.setSprite(textures[5]);
        button[0].setSprite(textures[1]);// выбор
        button[1].setSprite(textures[2]);// подтверждение
        button[2].setSprite(textures[3]);// отмена

        tian.setSprite(textures[60]);
        cash.setSprite(textures[61]);
    }

    static int test_num = -1;
    float test_time = 1;

    public void testTenSpawn(Vector touch){
        Unit unit;
        Pack pack;
        test_time -= delayTime / 1000;
        if (test_time < 0) {
            test_time = 1;
            pack = new Pack(this, Pack.TAG_TENTACLE);

            unit = new Captor(this);
            unit.setPosition(touch);
            unit.setPack(pack);
            unit.resLoad();
            addUnit(unit);

            unit = new Parasite(this);
            unit.setPosition(touch);
            unit.setPack(pack);
            unit.resLoad();
            addUnit(unit);

            unit = new Sniper(this);
            unit.setPosition(touch);
            unit.setPack(pack);
            unit.resLoad();
            addUnit(unit);

            unit = new Solider(this);
            unit.setPosition(touch);
            unit.setPack(pack);
            unit.resLoad();
            addUnit(unit);
//
            unit = new Heavy(this);
            unit.setPosition(touch);
            unit.setPack(pack);
            unit.resLoad();
            addUnit(unit);

            addPack(pack);
        }
    }
    public void testTianSpawn(Vector touch){
        Unit ship;
        switch(++test_num){
            case 0:
                ship = new Fighter(this);
                ship.setPosition(touch);
                ship.setPack(targetBase);
                addUnit(ship);
                break;
            case 1:
                ship = new Assault(this);
                ship.setPosition(touch);
                ship.setPack(targetBase);
                addUnit(ship);
                break;
            case 2:
                ship = new Medic(this);
                ship.setPosition(touch);
                ship.setPack(targetBase);
                addUnit(ship);
                break;
            case 3:
                ship = new Scout(this);
                ship.setPosition(touch);
                ship.setPack(targetBase);
                addUnit(ship);
                break;
            case 4:
                test_num = 0;
                break;
        }

    }

}
