package com.ermilitary.CosmoEscape.GameElement;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Level.Level;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Level_4 extends Level {
    Base base;
    Unit sov_ship;
    Pack sov_pack;
    Unit bose_ship;
    Pack bose_pack;
    boolean msg_bose_death = false;
    boolean msg_bose_shield = false;
    boolean msg_all_pack_death = false;

    public Level_4(){
        packs.add(targetBase = base = new Base(this, Pack.TAG_TIAN));
        base.base_ship.cash = 1065;
        base.base_ship.tian = 8;
    }
    @Override
    public void draw(GL10 gl) {
        // ***внепошаговые ситуации***
        // поражение
        if(base.base_ship.death) {
            if (!defeat) {
                Random random = new Random();
                int option = random.nextInt(4);
                String msg = "";
                switch (option) {
                    case 0:msg = "Поздравляю, из за тебя\nмы все поуши в\nтентаклях!..."; break;
                    case 1:msg = "Когда меня будут\nмучать тентакли\nя буду молиться\nчто бы твоих тентаклей\nбыло больше чем моих..."; break;
                    case 2:msg = "Надеюсь теперь тебя\nбудут мучить не\nтолько тентакли,\nно и совесть..."; break;
                    case 3:msg = "В каждом есть что то\nхорошее, но не в тебе.\nВ тебе нет ничего\nхорошего..."; break;
                    case 4:msg = "Как думаете капитан,\nесли мы отдадим им вас,\nто они нас отпустят?..."; break;
                }
                SayOne(0, sov_ship, msg);
            }
            Defeat(gl);
            return;
        } else
            // смерть советника
            if(sov_ship != null && sov_ship.death) {
                if (base.base_ship.cash >= 7 && base.base_ship.tian >= 1) {
                    base.base_ship.cash -= 7;
                    base.base_ship.tian -= 1;
                }
                sov_ship = spawnSov(base);
                sov_pack = sov_ship.pack;
                setNav(sov_pack);
                Random random = new Random();
                int option = random.nextInt(6);
                String msg = "";
                switch (option){
                    case 0:msg = "Каждый раз когда выносят\nразведчика в мире\nпогибает\nодин котёнок =(..."; break;
                    case 1:msg = "Я опять умерла.\nТы меня совсем\nне ценишь! =(..."; break;
                    case 2:msg = "Почему ты заставляешь\nдевушек умирать? -_-..."; break;
                    case 3:msg = "После смерти кому то\nрай, а кому то телепорт\nна базу и по новой..."; break;
                    case 4:msg = "Если я ещё раз умру то\nобьявлю тебе байкот!..."; break;
                    case 6:msg = "Если бы после каждой\nсмерти мне дарили по\nцветку, то я бы уже\nдавно сплела себе гроб..."; break;
                }
                SayOne(4,sov_ship,msg);
            }
        // ---Сюжет---
        switch (step) {
            case 0: // введение
                if (dialog == 0)
                    setNav(sov_pack = (targetUnit = sov_ship = spawnSov(targetBase)).pack);
                Say(0,sov_ship,"Проснитесь капитан.\nРадары засекли ещё\nодну планету...");
                if (Say(1,sov_ship,"Только до вторжения\nеё не было... Сейчас\nмы сможем показать\nкартинку...")){
                    nestStep();
                }
                break;
            case 1: // *спавн врагов по линии и их настройка
                if (Say(0,sov_ship,"Присвятая тян, это\nвторженец(>о<!)...")){
                    spawnEnemy(250,5,targetBase,Pack.MISSION_ATTACK,1,2,0,1,0);
                    spawnEnemy(250,8,targetBase,Pack.MISSION_ATTACK,1,0,4,0,0);
                    spawnEnemy(250,11,targetBase,Pack.MISSION_ATTACK,1,3,0,0,0);
                    setNav(bose_pack = addBose(spawnEnemy(250,12,targetBase,Pack.MISSION_ATTACK,0,0,0,0,20),_infected_planet));
                }
                Say(1,sov_ship,"Капитан, нам\nприйдётся сражаться\nс планетой, а\nвокруг неё\nцелая армия!...");
                if (Say(2,sov_ship,"Может попрбуете вначале\nизбавиться от помех, а\nпотом ударить чем нибудь\nтяжелым этой громадине\nпо голове?...")){
                    // поиск корабля боса
                    for (Unit u : units)
                        if (u.getClass().getSimpleName().equals("InfectedPlanet")) {
                            bose_ship = u;
                            break;
                        }
                    nestStep();
                }
                break;
            case 2: // ПОДСКАЗКИ
                // *мегабомба не задела боса*
                LinkedList<Unit> hits_unit;
                if (bose_ship != null && !bose_ship.death && (hits_unit = targetBase.base_ship.megabomb.getHitsUnit()) != null){
                    boolean mb_hit_bose = false;
                    for (Unit u : hits_unit)
                        if (u == bose_ship){
                            mb_hit_bose = true;
                            break;
                        }
                    if (!mb_hit_bose)
                        SayOne(0,sov_ship,"Мегабомба не попала в\nэтого монстра. Наши шансы\nвыжить уменьшились!\nСпасибо вам,\nкапитан (-_-#)...");
                }
                // *сбилли щит боса*
                if (bose_ship != null && bose_ship.shield <= 0 && !msg_bose_shield){
                    SayOne(0,sov_ship,"Вот живучий гад(>_<!).\nМы лишь снесли ему щит.\nНужно его добить\nкапитан...");
                    setNav(bose_pack);
                    msg_bose_shield = true;
                }
                // *бос убит*
                if (bose_ship != null && bose_ship.death && !msg_bose_death) {
                    SayOne(0, sov_ship, "Вы планету уничтожили!\nКапитан, я вас\nбоюсь (0_о!) ...");
                    setNav(bose_pack);
                    msg_bose_death = true;
                }
                // первый выстрел гиперлазера если не все отряды врага уничтожены

//                // гиперлазер не задел боса(единожды)
//                if (findPackWTag(2,Pack.TAG_TENTACLE) == 0 && Say(2,sov_ship,"Эти овощи уже не знают\nс какой стороны к нам\nподойти, опять справа\nатакуют -_-...")){
//                    setNav(spawnEnemy(260,-1,targetBase,Pack.MISSION_ATTACK,0,3,0,0,0));
//                    nestStep();
//                }
//                // гиперлазер попал по босу
//                if (findPackWTag(2,Pack.TAG_TENTACLE) == 0 && Say(2,sov_ship,"Эти овощи уже не знают\nс какой стороны к нам\nподойти, опять справа\nатакуют -_-...")){
//                    setNav(spawnEnemy(260,-1,targetBase,Pack.MISSION_ATTACK,0,3,0,0,0));
//                    nestStep();
//                }
                // *уничножен отряд тян*
                Pack d_pack;
                if ((d_pack = getDellPack()) != null && d_pack.tag == Pack.TAG_TIAN && d_pack.mission != Pack.MISSION_PARKING_BASE && d_pack.mission != Pack.MISSION_SCOUT){
                    SayOne(0,sov_ship,"Несём потери капитан!\nЗапомните - не мы еда,\nа они(-_-!)...");
                    setNav(d_pack);
                }
                // *уничтожены все стаи*
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 1 && bose_pack.size > 0 && !msg_all_pack_death) {
                    SayOne(0, sov_ship, "Отлично! Враги теперь\nпойдут разве что на\nсувениры!...");
                    msg_all_pack_death = true;
                }
                // *победа*
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 0) nestStep();
                break;
            case 3: // победа
                if (Say(0,sov_ship,"Враг полностью\nизничтожен. Тян\nпразднуют, танцуют,\nи поют!")){
                    setNav(targetBase);
                }
                Say(1,sov_ship,"Вы бы лопнули\nсо смеху от этого\nзрелища(^_^)...");
                if (dialog == 2 && targetRadioUnit == null){
                    Victory(gl);
                    return;
                }
                break;
        }
        super.draw(gl);
    }

    @Override
    public void resource_load(GL10 gl) {
        loadBackground(gl, R.drawable.bg_level4);
        super.resource_load(gl);
    }

}
