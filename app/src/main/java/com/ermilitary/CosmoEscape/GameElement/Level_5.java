package com.ermilitary.CosmoEscape.GameElement;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.Level.Level;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Level_5 extends Level {
    Base base;
    Unit sov_ship;
    Pack sov_pack;
    Pack escort_pack;
    float  timer = 0;
    int h_time = 60;
    boolean msg_escort_kill = false;
//    boolean msg_bose_death = false;
//    boolean msg_bose_shield = false;
//    boolean msg_all_pack_death = false;
    Sprite mb_off;

    public Level_5(){
        packs.add(targetBase = base = new Base(this, Pack.TAG_TIAN));
        base.base_ship.cash = 1065;
        base.base_ship.tian = 8;

        mb_off = new Sprite();
        mb_off.setPosition(base.base_ship.gui_megabomb.position);
        mb_off.setScale(base.base_ship.gui_megabomb.scale);
    }
    @Override
    public void resource_load(GL10 gl) {
        loadBackground(gl, R.drawable.bg_level5);
        mb_off.setSprite(textures[3]);
        super.resource_load(gl);
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
            case 0: // *введение*
                if (dialog == 0)
                    setNav(sov_pack = (targetUnit = sov_ship = spawnSov(targetBase)).pack);
                Say(0,sov_ship,"Капитан. Разведка\nобнаружила конвой с тян\nнашей планеты (-_- )...");
                Say(1,sov_ship,"Вы обязаны их спасити,\nкапитан...");
                if (Say(2,sov_ship,"Вам пригодятся медики,\nони соберут тянок прямо\nво время боя...")){
                    setNav(targetBase);
                    nestStep();
                }
                break;
            case 1: // *спавн конвоя*
                if (Say(0,sov_ship,"А вот и конвой! Устройте\nим сюрприз\nкапитан (^_^!)...")){
                    setNav(escort_pack = spawnEnemy(330, 6, targetBase, Pack.MISSION_NONE, 4, 6, 4, 2, 0));
                    nestStep();
                }
                break;
            case 2: // ПОДСКАЗКИ
                // *появление поддержки*
                if ((timer += delayTime / 1000) > h_time) {
                    SayOne(0, sov_ship, "У врага подкрепление.\nКапитан, враги будут \nприбывать пока у них\nесть тян...");
                    setNav(spawnEnemy(_dft,6,escort_pack,Pack.MISSION_HELP,1,3,1,0,0));
                    timer = 0;
                }
                // уничтожение п до конвоя
                Pack del_pack;
                if ((del_pack = getDellPack()) != null && del_pack.tag == Pack.TAG_TENTACLE && del_pack.mission == Pack.MISSION_HELP) {
                    SayOne(0, sov_ship, "Коннвой остался без\nподдержки. Вы просто\nдьявол, капитан(^_^!)...");
                    setNav(del_pack);
                }
                // *уничтожение конвоя...*
                if (!msg_escort_kill && escort_pack.size == 0) {
                    SayOne(0, sov_ship, "Конвоя больше нет.\nОтличная работа!...");
                    msg_escort_kill = true;
                }
                // *спасение тян*
                if (targetBase.base_ship.getTian()) {
                    SayOne(0, sov_ship, "Ещё несколько тян\nспасены, капитан.\nСамое время отправить\nих в бой!...");
                    setNav(targetBase);
                }
                // *уничножен отряд тян*
                if ((del_pack = getDellPack()) != null && del_pack.tag == Pack.TAG_TIAN && del_pack.mission != Pack.MISSION_PARKING_BASE && del_pack.mission != Pack.MISSION_SCOUT){
                    SayOne(0,sov_ship,"Отряд уничтожен.\nВы просто ужасный\nкапитан (>_<!)...");
                    setNav(del_pack);
                }
                // *если мало тян то нападают на нас*
                int tian_tian = 0;
                int escort_tian = 0;
                for (Unit u : units)
                    if (!u.death && u.pack.tag == Pack.TAG_TIAN)
                        tian_tian += u.tian;
                    else if (!u.death && u.pack == escort_pack)
                        escort_tian += u.tian;
                if (tian_tian*4 < escort_tian) {
                    SayOne(0, sov_ship, "Мы обнаружены! Враг\nлетит к нам(>.<!)...");
                }
                // *победа*
                if (escort_pack.size == 0) nestStep();
                break;
            case 3: // победа
                Say(0,sov_ship,"Враг повержен и спасённые\nтян запомнят этот день\nкак День Свободы\nТян(^_^!)...");
                if (dialog == 1 && targetRadioUnit == null){
                    Victory(gl);
                }
                break;
        }
        // *мегабомба не работает*
        if(targetUnit == targetBase.base_ship) {
            gl.glColor4f(1, 1, 1, 0.5f);
            mb_off.draw(gl);
            gl.glColor4f(1, 1, 1, 1);
        }
        super.draw(gl);
    }

    @Override
    public boolean inPut(MotionEvent event) {
        if (targetUnit == targetBase.base_ship) {
            setMask(event,targetBase.base_ship.gui_megabomb);
        }
        return super.inPut(event);
    }
}
