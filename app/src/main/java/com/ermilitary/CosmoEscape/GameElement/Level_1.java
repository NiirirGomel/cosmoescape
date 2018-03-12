package com.ermilitary.CosmoEscape.GameElement;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.Level;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Packs.Squad;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Tentacle.Captor;
import com.ermilitary.CosmoEscape.Units.Tian.Medic;
import com.ermilitary.CosmoEscape.Units.Tian.Scout;
import com.ermilitary.CosmoEscape.Units.Tian.TanUnit;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Level_1 extends Level {
    Base base;
    Pack enemy;
    Unit sov_ship;
    Pack sov_pack;

    public Level_1(){
        base = new Base(this, Pack.TAG_TIAN);
        base.base_ship.cash = 65;
        base.base_ship.tian = 7;
        packs.add(base);
        targetBase = base;
        targetPack = base;
        setNav(base);
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
                Say(0,base.base_ship, "Проверка связи\nКапитан.\nНажмите на сообщение...");
                Say(1,base.base_ship, "Как мы рады что с вами\nвсе в порядке. Мы думали\nчто вас поймали\nвторженцы...");
                Say(2,base.base_ship, "И уже начали\nподыскивать\nнового капитана...");
                Say(3,base.base_ship, "Мы в беде (>.<)!,\nвся наша планета\nзахвачена вторженцами...");
                Say(4,base.base_ship, "Все кто сумел спастись\nсейчас перед вами...");
                Say(5,base.base_ship, "Я ваш советник, поэтому\nмне прийдется\nпомогать вам...");
                if (Say(6,base.base_ship, "Посторайтесь побыстрее\nосвоиться.\nХорошо^^ ?..."))
                    nestStep();
                break;
            case 1: // интерфейс базы
                Say(0,base.base_ship, "Поднимитесь на\nкапитанский мостик.\nДля этого тыкните\nпо главному кораблю...");
                if(targetUnit == targetBase.base_ship) Say(1,base.base_ship, "Сверху показанным\nздоровье и энергия\nкорабля...");
                Say(2,base.base_ship, "Справа от них механизм\nзапуска мегабомбы и\nгиперлазера...");
                Say(3,base.base_ship, "Слева снизу показанно\nнаселение корабля и\nресурс, нужный для\nсоздания новых\nкораблей...");
                if (Say(4,base.base_ship, "Над ними ваша верфь. На\nней вы можете построить\nновые корабли посадив\nна них тян...")) {
                    sov_ship = spawnSov(targetBase);
                    sov_pack = sov_ship.pack;
                    nestStep();
                }
                break;
            case 2: // разведка
                if (Say(0,sov_ship, "Я командир разведки\nпоэтому буду поставлять\nвам самые свежие новости\nкосмического формата...")){
                    targetUnit = sov_ship;
                    setNav(sov_pack);
                }
                Say(1,sov_ship, "Но для этого мне нужны\nкарабли\nразведки(+_+)!...");
                if (Say(2,targetBase.base_ship, "Их иконка на вефи в\nсамом низу. Тыкните\nпо ней и создайте\nмне разведчика...")){
                    targetUnit = targetBase.base_ship;
                    setNav(targetBase);
                }
                if (findPackWMission(3, Pack.MISSION_SCOUT) == 2) {
                    if (targetRadioUnit != null) targetRadioUnit.radio = "";
                    if (Say(3,sov_ship, "Вам не прийдётся\nими управлять\nпотому что для\nэтого есть я...")) {
                        for (Unit u : units)
                            if (u.pack.mission == Pack.MISSION_SCOUT && u != sov_ship) {
                                setNav(u.pack);
                                break;
                            }
                        targetUnit = sov_ship;
                    }
                }
                if (Say(4,sov_ship, "Чем больше\nразведчиков, тем\nбольше и дальше вы\nсможете увидеть...")) {
                    nestStep();
                }
                break;
            case 3: // штурмовик
                if(Say(0,targetBase.base_ship, "В бою лучше использовать\nштурмовиков. Построй\nодного. Их иконка\nраспалогается на\nсамом верху верфи...")){
                    targetUnit = targetBase.base_ship;
                    setNav(targetBase);
                }
                if(dialog == 1 && findUWC(1,"Fighter") > 0) {
                    if (targetRadioUnit != null) targetRadioUnit.radio = "";
                    if (Say(1, sov_ship, "Они разрезают врагов на\nблизкой дистанции, а на\nдальних стреляют из\nлазера..."))
                        targetUnit = sov_ship;
                }
                if (Say(2,sov_ship, "Лазер быстро\nразряжается, поэтому\nего эффективность\nсо временем пропадает...")) {
                    targetUnit = targetBase.base_ship;
                    nestStep();
                }
                break;
            case 4: // стрелки
                Say(0,targetBase.base_ship, "Теперь построй стрелка.\nЕго иконка прямо под\nиконкой штурмовика...");
                if(dialog == 1 && findUWC(1,"Assault") > 0){
                    if (targetRadioUnit != null) targetRadioUnit.radio = "";
                    if (Say(1,sov_ship, "Они выполняют роль\nподдержки и хорошо\nподходят для тотальной\nдизинтеграции вражеских\nщупалец(^^!)...\n"))
                        targetUnit = sov_ship;
                }
                Say(2,sov_ship,"Эти ребята не любят\nспешить, однако\nпопадания из их пушек\nвызывают у врага\nбадхёрт(-_-)...");
                if (Say(3,sov_ship, "Увы из за их низкой\nманёвренности в близи\nих легко\nприпарируют..."))
                    nestStep();
                break;
            case 5: // отряд\навигация
                if (Say(0,sov_ship,"Что бы управлять\nотрядом тыкни по\nодному из кораблей...")){
                    //targetUnit_off = true;
                }
                if (findPackWTag(1,Pack.TAG_TIAN) == 4) {
                    if (targetRadioUnit != null) targetRadioUnit.radio = "";
                    if (Say(1, sov_ship, "Заметь что когда твой\nотряд оказываеться\nдалеко от остальных,\nпоявляется иконка\nнавигации..."))
                        targetUnit = sov_ship;
                }
                if(Say(2,sov_ship,"Она отображает действие\nотряда, а если по ней\nтыкнуть можнон\nпереключиться на\nэтот отряд...")) {
                    if (targetUnit != null) targetUnit_off = true;
                    nestStep();
                }
                break;
            case 6: // медик
                if (targetUnit == targetBase.base_ship) Say(0,sov_ship,"В бою ваши корабли\nпостоянно разбиваются.\nЧто бы это исправить\nсуществуют медики...");
                Say(1, targetBase.base_ship, "Создайте одного прямо\nсейчас...");
                if(dialog == 2 && findUWC(2,"Medic") > 0) {
                    if (targetRadioUnit != null) targetRadioUnit.radio = "";
                if (Say(2,sov_ship, "Вне боя они ремонтируют\nваши корабли давая им\nвозможность пережить\nследующий бой..."))
                    targetUnit = sov_ship;
                }
                Say(3, sov_ship, "Если в отряде медика\nесть прокаженные, то\nон получает бонус к\nмобильности...");
                if (Say(4,sov_ship, "Однако по одиночке\nони лишь закуска\nдля вторженцев...")) {
                    //targetUnit = targetBase.base_ship;
                    nestStep();
                }
                break;
            case 7: // показательное лечение
                if (Say(0, sov_ship, "Нам повезло! Отряд\nтян влетел в обломки\nметеора и его\nнеслабо потрепало...")){
                    for (Pack p : packs)
                        if (p.mission == Pack.MISSION_PARKING_BASE) {
                            setNav(p);
                            for (Unit u : units)
                                if (u.pack == p) u.health /= 2;
                            break;
                        }
                }
                if (Say(1, sov_ship, "Попробуйте выбрать\nмедика и тыкнуть на\nиконку помощи, а\nпотом выбрать юнита из\nпострадавшего отряда...")){
                    setNav(targetBase);
                }
                if (dialog == 2) {
                    int p_size = 0;
                    Pack first_p = null;
                    for (Unit u : units)
                        if (u.pack.mission == Pack.MISSION_PARKING_BASE){
                            if (first_p == null) first_p = u.pack;
                            if (u.pack == first_p) p_size++;
                        }
                    if (p_size == 3 && Say(2, sov_ship, "Медик присоединился\nк отряду и приступил\nк ремонту..."))
                        nestStep();
                }
                break;
            case 8: // разделение
                if (dialog == 0){
                    boolean health_over = true;
                    for (Unit u : units)
                        if(u.health < u.max_health) health_over = false;
                    if (health_over)
                        if (Say(0, sov_ship, "Отправте медика\nобратно на базу.\nПросто создайте\nобласть выделения и\nзахватите в неё медика...")){
                            if (targetUnit != null) targetUnit_off = true;
                            if (targetPack != null) targetPack_off = true;
                        }
                }
                if (findPackWTag(1,Pack.TAG_TIAN) == 5){
                    Pack m_pack = null;
                    int cut = 0;
                    for (Unit u : units)
                        if (u.getClass().getSimpleName().equals("Medic")) {m_pack = u.pack; break;}
                    for (Unit u : units)
                        if (u.pack == m_pack) cut++;
                    if (cut == 1 && Say(1, sov_ship, "Теперь прикажите ему\nотступить тыкнув на\nиконку отступления...")){
                        for (Unit u : units)
                            if (u.getClass().getSimpleName().equals("Assault")){
                                for (Unit u2 : units)
                                    if (u2.getClass().getSimpleName().equals("Fighter")){
                                        if (u.pack != u2.pack) u.setPack(u2.pack);
                                    }
                                break;
                            }
                        for (Unit u : units)
                            if (u.getClass().getSimpleName().equals("Medic")){
                                m_pack = u.pack;
                                break;
                            }
                        if(targetUnit != null) targetUnit_off = true;
                        targetPack = m_pack;
                        setNav(m_pack);
                    }
                }
                if (dialog == 2) {
                    for (Unit u : units)
                        if (u.getClass().getSimpleName().equals("Medic") && u.pack.mission == Pack.MISSION_BASE) {
                            Say(2, sov_ship, "Вы прекрасно\nсправляетесь...");
                            nestStep();
                            break;
                        }
                }
                break;
            case 9: // атака\агриведы\гиперлазер
                if (Say(0, sov_ship, "У нас проблемка.\nЭто вторженец!...")){
                    setNav(spawnEnemy(-1,7,targetBase,Pack.MISSION_NONE,1,0,0,0,0));
                }
                if (Say(1, sov_ship, "Прикажите отряду\nизбавиться от\nнего(>.<!)...")) {
                    for (Unit u : units)
                        if (u.getClass().getSimpleName().equals("Captor")){
                            u.health = u.max_health = 15000;
                            u.max_m_speed = 0.32f;
                            u.max_r_speed = 100f;
                            u.max_tian = 20;
                            u.vis_zone = 1f;
                            ((Captor)u).t_dps = 2000;
                            break;
                        }
                    for (Pack p : packs)
                        if (p.mission == Pack.MISSION_PARKING_BASE){
                            if (targetUnit != null) targetUnit_off = true;
                            targetPack = p;
                            setNav(p);
                            break;
                        }
                }
                if (findPackWTag(2,Pack.TAG_RESORCES) > 0 && Say(2, sov_ship, "Когда кораблю\nприходит конец все\nтянки на нём\nотправляются дрейфовать\nв космосе...")){
                    for (Pack p : packs)
                        if (p.tag == Pack.TAG_RESORCES) {
                            setNav(p);
                            break;
                        }
                }
                if (findPackWTag(3,Pack.TAG_RESORCES) == 0 && Say(3, sov_ship, "Если враг увидит\nтян то захватит\nих(-///-), а если\nнаберёт много тян то\nпосторается сбежать...")){
                    for (Pack p : packs)
                        if (p.tag == Pack.TAG_TENTACLE) {
                            setNav(p);
                            break;
                        }
                }
                if (Say(4, targetBase.base_ship, "Наша задача спасти\nих! Выстрели по нему\nиз гиперлазера.\nТыкни на его иконку\nи выбери цель...")){
                    targetPack = targetBase;
                    targetUnit = targetBase.base_ship;
                    setNav(targetBase);
                }
                if (findPackWTag(5,Pack.TAG_TENTACLE) == 0 && Say(5, sov_ship, "Враг уничтожен!\nСамое время забрать\nтянок. Отправь медика\nим на помошь...")){
                    //targetPack = targetBase;
                    targetBase.base_ship.cash = 100;
                    setNav(targetBase);
                }
                if (findPackWTag(6,Pack.TAG_RESORCES) == 0 && Say(6, sov_ship, "По возвращению\nсобранные тян\nпереносятся на базу\nи вы можете снова\nотправлять их в бой...")){
                    for (Unit u : units)
                        if (u.getClass().getSimpleName().equals("Medic")){
                            setNav(u.pack);
                            break;
                        }
                    nestStep();
                }
                break;
            case 10: // мегабомба
                if (dialog == 0){
                    boolean complete = true;
                    for (Unit u : units)
                        if (u != base.base_ship && u.tian > u.need_tian) complete = false;
                    if (complete && Say(0, sov_ship, "Присвятая тян, им \nнет конца!...")){
                        setNav(spawnEnemy(-1,7,targetBase,Pack.MISSION_NONE,1,4,3,2,4));
                    }
                }
                if (Say(1, targetBase.base_ship, "Капитан, самое время\nиспользовать\nмегабомбу!\nОна работает так же...")){
                    targetPack = targetBase;
                    targetUnit = targetBase.base_ship;
                    setNav(targetBase);
                }
                Say(2, targetBase.base_ship, "Как гиперлазер, но\nеё использование\nстоит много\nресурсов. Попробуйте\nзапустить её...");
                if (findPackWTag(3, Pack.TAG_TENTACLE) == 0 && Say(3, sov_ship, "Мегабомба способна\nуничтожить всё в\nогромном радиусе,\nвключая наших\nтянок!..."))
                    nestStep();
                break;
            case 11: // победа
                if (dialog == 0 && wait(1f) && Say(0, sov_ship, "Мы вылители за\nпределы нашей планеты.\nСамое время\nпопращаться с нашим\nдомом капитан...")) {
                    targetUnit = sov_ship;
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
    public boolean inPut(MotionEvent event) {
        switch (step) {
            case 0:
                fillMask();
                setXMask(event, text_frame);
                break;
            case 1:
                if (dialog != 1) {
                    fillMask();
                    setXMask(event, text_frame);
                }
                break;
            case 2:
                fillMask();
                if (dialog == 3){
                    setGeoXMask(event, targetBase.base_ship);
                    if (targetRadioUnit != null) setXMask(event, text_frame);
                    if (targetUnit == targetBase.base_ship && findPackWMission(3, Pack.MISSION_SCOUT) == 1) {
                        setXMask(event, targetBase.base_ship.gui_scout);
                    }
                } else setXMask(event, text_frame);
                break;
            case 3:
                fillMask();
                if (dialog == 1){
                    setGeoXMask(event,targetBase.base_ship);
                    if (targetRadioUnit != null) setXMask(event, text_frame);
                    if (targetUnit == targetBase.base_ship &&  findUWC(1,"Fighter") == 0) {
                        setXMask(event, targetBase.base_ship.gui_fighter);
                    }
                } else setXMask(event, text_frame);
                break;
            case 4:
                fillMask();
                if (dialog == 1){
                    setGeoXMask(event,targetBase.base_ship);
                    if (targetRadioUnit != null) setXMask(event, text_frame);
                    if (targetUnit == targetBase.base_ship && findUWC(1,"Assault") == 0) {
                        setXMask(event, targetBase.base_ship.gui_assault);
                    }
                } else setXMask(event, text_frame);
                break;
            case 5:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 1){
                    for (Unit u : units)
                        if (u.getClass().getSimpleName().equals("Assault") || u.getClass().getSimpleName().equals("Fighter"))
                            setGeoXMask(event,u);
                }
                break;
            case 6:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 0){
                    //setGeoXMask(event,targetBase.base_ship);
                    for (Pack p : packs)
                        setXMask(event,p.emblem);
                }
                if (dialog == 2){
                    setGeoXMask(event,targetBase.base_ship);
                    if (targetUnit == targetBase.base_ship && findUWC(2,"Medic") == 0) {
                        setXMask(event, targetBase.base_ship.gui_medic);
                    }
                }
                break;
            case 7:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 2)
                    clearMask();
                break;
            case 8:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 1){
                    clearMask();
                    if (targetPack != null && targetUnit == null)
                        setMask(event,((Squad)targetPack).gui_disband);
                }
                if (dialog == 2 && targetPack != null && targetUnit == null) {
                    setXMask(event,((Squad)targetPack).gui_disband);
                }
                break;
            case 9:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 2){
                    if (targetPack != null && getNav() == targetPack) setXMask(event,((Squad)targetPack).gui_atack);
                    else clearMask();
                }
                if (dialog == 5) {
                    if (targetBase.base_ship.hl_ready){
                        if (targetRadioUnit != null) targetRadioUnit.radio = "";
                        clearMask();
                    } else {
                        setGeoXMask(event, targetBase.base_ship);
                        setXMask(event, targetBase.base_ship.gui_hiperlaser);
                    }
                }
                if (dialog == 6) clearMask();
                break;
            case 10:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 3){
                    if (targetBase.base_ship.mb_ready) {
                        if (targetRadioUnit != null) targetRadioUnit.radio = "";
                        clearMask();
                    } else {
                        setGeoXMask(event, targetBase.base_ship);
                        setXMask(event, targetBase.base_ship.gui_megabomb);
                    }
                }
                break;
            case 11:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                break;
        }
        return super.inPut(event);
    }

    @Override
    public void resource_load(GL10 gl) {
        loadBackground(gl, R.drawable.bg_level1);
        super.resource_load(gl);
    }
}
