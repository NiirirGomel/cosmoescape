package com.ermilitary.CosmoEscape.GameElement;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Level.Level;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Packs.Squad;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Level_3 extends Level {
    Base base;
    Unit sov_ship;
    Pack sov_pack;

    public Level_3(){
        packs.add(targetBase = base = new Base(this, Pack.TAG_TIAN));
        base.base_ship.cash = 65;
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
                    case 1:msg = "Когда меня будут мучать\nтентакли я буду молиться\nчто бы твоих тентаклей\nбыло больше чем моих..."; break;
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
                if (Say(0,sov_ship,"Вы прекрасно справились\nв прошлый раз капитан.\nНам наконец то ничего\nне угрожает! =]...")){
                    nestStep();
                }
                break;
            case 1: // один враг(разогрев)
                if (Say(0,sov_ship,"Вторженцы в зоне\nвидимости капитан...")){
                    setNav(spawnEnemy(90,-1,targetBase,Pack.MISSION_NONE,0,5,0,0,0));
                }
                if (Say(1,sov_ship,"Словно пчёлы на\nмёд -_-... Пожалуйста,\nвпечатайте их  в\nкосмическую пустоту...")){
                    nestStep();
                }
                break;
            case 2: // два отряда справа и после 1 сильный слева слабый справа
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 0 && wait(5f) && Say(0,sov_ship,"Ещё парочка справа.\nДавно не кушали жареных\nпришельцев? Может\nподжарим их лазером\nкапитан(^_^?)...")){
                    spawnEnemy(275, -1, targetBase, Pack.MISSION_ATTACK, 1, 0, 2, 0, 0);
                    setNav(spawnEnemy(260,-1,targetBase,Pack.MISSION_ATTACK,0,3,1,0,0));
                }
                if (findPackWTag(1,Pack.TAG_TENTACLE) == 0 && Say(1,sov_ship,"Этим малышам всё мало,\nони теперь ещё и слева.\nПоиграйте с\nними немного,\nкапитан^^ ...")){
                    setNav(spawnEnemy(90,-1,targetBase,Pack.MISSION_ATTACK,2,3,2,0,0));
                }
                if (findPackWTag(2,Pack.TAG_TENTACLE) == 0 && Say(2,sov_ship,"Эти овощи уже не знают\nс какой стороны к нам\nподойти, опять справа\nатакуют -_-...")){
                    setNav(spawnEnemy(260,-1,targetBase,Pack.MISSION_ATTACK,0,3,0,0,0));
                    nestStep();
                }
                break;
            case 3: // линия из 4-х по фронту
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 0 && wait(10f) && Say(0,sov_ship,"Капитан! По фронту\nполномосштабное\nнаступление\nврага...")){
                    spawnEnemy(10,-1,targetBase,Pack.MISSION_ATTACK,1,0,2,0,0);
                    spawnEnemy(340,-1,targetBase,Pack.MISSION_ATTACK,0,0,1,0,0);
                    spawnEnemy(350,-1,targetBase,Pack.MISSION_ATTACK,0,3,0,0,0);
                    setNav(spawnEnemy(1,-1,targetBase,Pack.MISSION_ATTACK,0,3,1,0,0));
                }
                if (Say(1,sov_ship,"Придумайте что нибудь\nпока наши повара не\nприготовят вкусняшки!..."))
                    setNav(targetBase);
                if (findPackWTag(2,Pack.TAG_TENTACLE) == 0) Say(2,sov_ship,"Молодец капитан! Я буду\nзащищать ваши вкусняшки\nдо конца наступления...");
                if (Say(3,sov_ship,"Так что постарайтесь\nне умереть,\nхорошо(^_^?)...")){
                    nestStep();
                }
                break;
            case 4: // тянки сслева
                if (dialog == 0 && wait(10f) && Say(0,sov_ship,"Тянки слева от\nглавного корабля...")){
                    setNav(spawnTian(91,-1,targetBase,8));
                }
                if (Say(1,sov_ship,"Мы ведь спасём их,\nкапитан?\nПравда спасём?\nЧестно - честно?...")){
                    setNav(targetBase);
                    nestStep();
                }
                break;
            case 5: // <^,<v,v>
                if (findPackWTag(0,Pack.TAG_RESORCES) == 0 && wait(10f)) Say(0,sov_ship,"Несколько отрядов врага\nзамечены в нашем\nсекторе...");
                if (Say(1,sov_ship,"Разберёмся с ними\nпоочереди? Первые\nдолжны показаться\nна 10 часов...")){
                    setNav(spawnEnemy(60,-1,targetBase,Pack.MISSION_ATTACK,0,3,2,0,0));
                }
                if (findPackWTag(2,Pack.TAG_TENTACLE) == 0 && wait(5f) && Say(2,sov_ship,"Вторая волна уже\nна походе.\nНа 7 часов...")){
                    setNav(spawnEnemy(160,-1,targetBase,Pack.MISSION_ATTACK,2,0,3,0,0));
                }
                if (findPackWTag(3,Pack.TAG_TENTACLE) == 0 && wait(5f) && Say(3,sov_ship,"Последний враг\nпытается сбежать.\nНа 4 часа...")){
                    setNav(spawnEnemy(250,2f,targetBase,Pack.MISSION_ATTACK,1,3,3,0,0));
                }
                if (findPackWTag(4,Pack.TAG_TENTACLE) == 0 && Say(4,sov_ship,"Ах капитан. Жаль что вы не\nможете почувствовать\nзапах победы находясь на\nглавном корабле >=)...")){
                    nestStep();
                }
                break;
            case 6: // тян сверху
                if (dialog == 0 && wait(10f) && Say(0,sov_ship,"Ещё потерпевшие, на\nэтот раз сверху.\nВозможно это ловушка\nврага, как вы\nсчитаете капитан?...")){
                    setNav(spawnTian(1,-1,targetBase,8));
                    nestStep();
                }
                break;
            case 7: // <^ u ^> u <v почучуть
                if (findPackWTag(0, Pack.TAG_TIAN) == 0 && wait(10f) && Say(0,sov_ship,"Враг нас окружил!\nПовара попросили\nразделывать вторженцев\nпрямо в бою(>_<\")...")){
                    spawnEnemy(70,-1,targetBase,Pack.MISSION_ATTACK,0,0,3,0,0);
                    spawnEnemy(330,-1,targetBase,Pack.MISSION_ATTACK,0,3,0,0,0);
                    setNav(spawnEnemy(20,-1,targetBase,Pack.MISSION_ATTACK,2,1,0,0,0));
                    nestStep();
                }
                break;
            case 8: // <^ дофига
                if (findPackWTag(0, Pack.TAG_TENTACLE) == 0 && wait(10f) && Say(0,sov_ship,"На 10 часов враг. И их\nбольше чем\nпользователей\nай понта (0_о!)...")){
                    setNav(spawnEnemy(20,-1,targetBase,Pack.MISSION_ATTACK,4,3,2,0,0));
                }
                if (Say(1,sov_ship,"Самое время вспомнить о\nмегабомбе?...")){
                    nestStep();
                }
                break;
            case 9: // два отряла у базы тырят тянок
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 0 && Say(0, sov_ship, "Пока мы сражались две\nстаи врага проникли\nна базу и схватили\nтянок!...")){
                    if ((targetBase.base_ship.tian -= 22) < 0) targetBase.base_ship.tian = 0;
                    spawnEnemy(190,2,targetBase,Pack.MISSION_ESCAPE,1,5,3,0,0);
                    setNav(spawnEnemy(220,2,targetBase,Pack.MISSION_ESCAPE,1,5,3,0,0));
                    for (Unit u : units)
                        if (u.pack.tag == Pack.TAG_TENTACLE){
                            if (u.getClass().getSimpleName().equals("Captor"))
                                u.tian = 3;
                            else
                                u.tian = 1;
                        }
                }
                if (Say(1,sov_ship,"Я их даже есть не\nхочу(>_<!).\nДавайте их изничтожим\nкапитан?...")){
                    nestStep();
                }
                break;
            case 10: // победа
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 0 && Say(0,sov_ship,"Вы просто карамелька\nкапитан (>///<!)...")){
                    setNav(targetBase);
                }
                if (dialog == 1 && targetRadioUnit == null){
                    Victory(gl);
                    return;
                }
                break;
        }
        super.draw(gl);
    }

    @Override
    public void resource_load(GL10 gl) {
        loadBackground(gl, R.drawable.bg_level3);
        super.resource_load(gl);
    }

}
