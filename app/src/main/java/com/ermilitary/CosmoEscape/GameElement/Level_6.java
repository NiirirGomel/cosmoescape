package com.ermilitary.CosmoEscape.GameElement;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
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
public class Level_6 extends Level {
    Base base;
    Unit sov_ship;
    Pack sov_pack;
    Pack oct_pack;
    float  timer = 0;
    int h_time = 60;
//    boolean msg_bose_death = false;
//    boolean msg_bose_shield = false;
//    boolean msg_all_pack_death = false;

    public Level_6(){
        packs.add(targetBase = base = new Base(this, Pack.TAG_TIAN));
        base.base_ship.cash = 1065;
        base.base_ship.tian = 8;
    }

    @Override
    public void resource_load(GL10 gl) {
        loadBackground(gl, R.drawable.bg_level6);
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
                Say(0,sov_ship,"Капитан. Мы почти\nвылетели из нашей\nзвёздной системы!...");
                if (Say(1,sov_ship,"Скоро нам нечего\nбудет бояться,  мы\nнайдём себе новый\nдом и будем жить там\nкак и раньше...")){
                    setNav(targetBase);
                    nestStep();
                }
                break;
            case 1: // спавн
                if (Say(0,sov_ship,"Только не это, здесь враги\nи им нет конца!...")){
                    setNav(oct_pack = addBose(spawnEnemy(230, _dft, targetBase, Pack.MISSION_ATTACK, 0, 0, 0, 0, 0), _octopus));
//                    spawnEnemy(320, 0.9, oct_pack, Pack.MISSION_SLAVE, 1, 0, 3, 0, 0);
                    spawnEnemy(315, 0.6f, oct_pack, Pack.MISSION_NONE, 1, 2, 0, 1, 0);
                    spawnEnemy(310, 0.3f, oct_pack, Pack.MISSION_NONE, 0, 1, 2, 1, 0);
                    spawnEnemy(130, 0.3f, oct_pack, Pack.MISSION_NONE, 1, 0, 3, 0, 0);
                    spawnEnemy(125, 0.6f, oct_pack, Pack.MISSION_NONE, 1, 2, 0, 0, 0);
                    spawnEnemy(120, 0.9f, oct_pack, Pack.MISSION_NONE, 0, 4, 0, 1, 0);
                }
                Say(1,sov_ship,"Они не хотят нас отпускать?\nПокажем им как больно\nмогут укусить тян(^_^?)...");
                if (Say(2,sov_ship,"Похоже этот осьминог\nими управляет, враги не\nбудут наподать пока он\nим не прикажет...")){
                    nestStep();
                }
                break;
            case 2: // ПОДСКАЗКИ
                // *уничножен отряд тян*
                Pack del_pack;
                if ((del_pack = getDellPack()) != null && del_pack.tag == Pack.TAG_TIAN && del_pack.mission != Pack.MISSION_PARKING_BASE && del_pack.mission != Pack.MISSION_SCOUT){
                    SayOne(0, sov_ship, "Если мы продолжим\nтерять тян то проиграем.\nКапитан, мы верим\nв вас(>.<!)...");
                    setNav(del_pack);
                }
                // кус наподает
                if (false){
                    SayOne(0, sov_ship, "Осьминог собираеться\nатаковать!...");
                }
                // кус отправил отряд
                if (false){
                    SayOne(0, sov_ship, "Осьминог направил\nна нас стаю. Отбейте\nнаподение капитан(>_<!)...");
                }
                // победа
                if (oct_pack.size == 0) nestStep();
                break;
            case 3: // победа
                Say(0,sov_ship,"Вы уничтожили их\nвсех. Я восхищаюсь\nвами капитан (^///^!)...");
                if (dialog == 1 && targetRadioUnit == null){
                    Victory(gl);
                    return;
                }
                break;
        }
        super.draw(gl);
    }
}
