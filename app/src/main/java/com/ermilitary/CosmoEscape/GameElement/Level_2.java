package com.ermilitary.CosmoEscape.GameElement;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.Level.Level;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Packs.Squad;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Tian.TanUnit;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Level_2 extends Level {
    Base base;
    Squad sov;
    Unit sov_ship;
    Pack sov_pack;

    public Level_2(){
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
                if (Say(0,targetBase.base_ship,"Мы не стали рабами\nвторженцев?\nВы не плохо справляетесь\nкапитан(-_- )...")) {
                    setNav(sov_pack = (targetUnit = sov_ship = spawnSov(targetBase)).pack);
                }
                Say(1,sov_ship,"Капитан, впереди обломки\nстарой космической базы.\nВторженцы повсюду =(...");
                if (Say(2,sov_ship,"Будет обидно если они\nостанутся в живых после\nнашего визита(>_<!)...")){
                    nestStep();
                }
                break;
            case 1: // кревы
                if (Say(0, sov_ship, "Я собрала досье на\nвторженцев. Стая врага\nвпереди состоит из\nкревов...")){
                    setNav(sov_pack);
                }
                Say(1,sov_ship,"Кревы-это такие шустрые\nсолдатики уклоняющиеся\nот атак и отпугивающие\nмедленные корабли...");
                Say(2,sov_ship,"Когда отакуете их\nубедитесь что в отряде\nдостаточно штурмовиков\nготовых подставить свои\nпопки...");
                if (Say(3,sov_ship,"А вот и они!\nПопробуем сделать из\nних пиццу\nкапитан(+_+?)...")) {
                    setNav(spawnEnemy(-1,4,targetBase,Pack.MISSION_NONE,0,5,0,0,0));
                }
                if (Say(4, sov_ship, "Создайте отряд и\nприкажите ему\nатаковать...")){
                    setNav(targetBase);
                }
                if (findPackWTag(5,Pack.TAG_TENTACLE) == 0 && Say(5,sov_ship,"Тян заметили что кревы\nтянут свои щупальца\nк самой красивой\nиз тянок...")){
                    targetBase.base_ship.tian += 5;
                    setNav(targetBase);
                }
                Say(6,sov_ship, "Эта информация\nпозволила мобилизовать\nещё несколько\nпилотов...");
                if (Say(7,sov_ship,"Можно я\nпромолчу(-_-?)...")){
                    nestStep();
                }
                break;
            case 2: // бутоны
                if (Say(0,sov_ship,"Ещё один доклад капитан.\nВпереди стая бутонов.\nБутоны похожи на\nплотоядные цветы^^...")){
                    setNav(spawnEnemy(-1,4,targetBase,Pack.MISSION_NONE,0,0,4,0,0));
                }
                Say(1,sov_ship,"Они стреляют семенами с\nбольшой дистанции, зато\nв близи не представляют\nникакой угрозы =Ь...");
                if (Say(2,sov_ship,"Для понижения урона\nврага их придётся\nуничтожить и ваши\nстрелки  с радостью\nэтим займутся...")){
                    setNav(targetBase);
                }
                if (findPackWTag(3,Pack.TAG_TENTACLE) == 0 && Say(3,sov_ship,"За исключением пары тян\nотравившихся вашей\nпиццей из кревов\nнаши потери\nминимальны(^_^!)...")){
                    nestStep();
                }
                break;
            case 3: // коканы
                if (Say(0,sov_ship,"У нас опять проблемы\nкапитан. Коканы уже\nздесь...")){
                    setNav(spawnEnemy(-1,4,targetBase,Pack.MISSION_NONE,3,0,0,0,0));
                }
                Say(1,sov_ship,"Они результат симбиоза\nразных паразитов. Коканы\nмедленные, но их\nщупальца черезвычайно\nопасны >=0...");
                Say(2,sov_ship,"Попав к ним лишь раз\nврядли будет возможно\nвернуть тян из состояния\nшока...");
                if (Say(3,sov_ship,"Попробуйте атаковать их\nиздалека...")){
                    setNav(targetBase);
                }
                if (findPackWTag(4,Pack.TAG_TENTACLE) == 0 && Say(4,sov_ship,"Какое чудо капитан!...")){
                    setNav(targetBase);
                }
                Say(5,sov_ship,"Поджаренные лазером\nлистики бутонов\nимеют замечательный\nвкус!...");
                if (Say(6,sov_ship,"В конце миссии я вас\nобязательно угощу^^ ...")){
                    nestStep();
                }
                break;
            case 4: // тактика
                Say(0,sov_ship,"Я надеюсь вы получили\nдостаточно опыта\nкапитан, потому\nчто мы окружены!...");
                if (Say(1,sov_ship,"Коммандуйте капитан!...")){
                    spawnEnemy(1, 4, targetBase, Pack.MISSION_NONE, 1, 2, 0, 0, 0);
                    spawnEnemy(135, 4, targetBase, Pack.MISSION_NONE, 1, 0, 2, 0, 0);
                    spawnEnemy(225, 4, targetBase, Pack.MISSION_NONE, 0, 2, 3, 0, 0);
                    nestStep();
                }
                break;
            case 5: // победа
                if (findPackWTag(0,Pack.TAG_TENTACLE) == 0 && Say(0,sov_ship,"Я хочу поблагодарить\nвсех кто внес вклад\nв наше межгалактическое\nменю^^...")){
                    setNav(targetBase);
                }
                Say(1,sov_ship,"Думаю вы заслужили\nвкусняшек капитан...");
                if (dialog == 2 && targetRadioUnit == null){
                    Victory(gl);
                    return;
                }
                break;
//                Say(0,base.base_ship, "Капитан, после вашего\nподвига тянки воспряли\nдухом и теперь готовы\nидти в бой!...");
//                Say(1,base.base_ship, "Правда некоторые ещё\nмолятся присвятой тян\nдабы ваши деяния не\nпривели их к концу\nнашей рассы...");
//                Say(2,base.base_ship, "Сейчас гораздо\nважнее то, что мы\nдобрались до обломков\nорбитальной станции...");
//                Say(3,base.base_ship, "Здесь должны\nнаходиться выжившие\nпосле катастрофы тян.\nВаш долг спасти их от\nнеминуемой смерти...");
//                if(Say(4,base.base_ship, "Поверьте моему слову,\nони вам ещё не раз\nпригодятся. Я же\nваш советник. Верить\nмне тоже ваш долг...")) nestStep();
//                break;
//            // копим тян, отбиваемся от ушлепков
//            case 1:
//                if(Say(0,base.base_ship, "Присвятая тян, враг\nпрямо у базы! Капитан,\nскорее запускайте\nперехватчиков!..."))
//                    setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,0,3,0,0,1));
//                if(findPackWTag(1, Pack.TAG_TENTACLE) == 0 && Say(1,base.base_ship, "Если они продолжат\nпоявляться прямо у\nнас под носом нам\nдаже себя не спасти!..."))
//                    setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,0,4,0,0,0));
//                if(Say(2,base.base_ship, "Я вручу вам медаль\nесли вы отправите\nменя патрулировать\nтерриторию..."))
//                    setNav(sov = spawnShip(-1, 0.5f, base, Pack.MISSION_PARKING_BASE, 0, 0, 0, 1));
//                if(dialog == 3 && sov_ship == null)
//                    for (Unit u : units)
//                        if (u.pack == sov){ sov_ship = u; ((TanUnit)sov_ship).setAvatar(17); }
//                if(findPackWTag(3,Pack.TAG_TENTACLE) == 0 && sov.mission == Pack.MISSION_SCOUT)
//                    if(Say(3,sov_ship, "Хороший капитан,\nвот тебе медалька.\nНам даже удалось\nобнаружить тян..."))
//                        setNav(spawnTian(-1,-1,base,8));
//                Say(4,sov_ship, "Может стоит\nпозаботиться о ещё\nодном отряде\nразведки?...");
//                if(findPackWTag(5,Pack.TAG_RESORCES) == 0 && Say(5,sov_ship, "Враг на три часа.\nсвистать всех на\nпалубу! Будем жарить\nэти морепродукты..."))
//                    setNav(spawnEnemy(270,-1,base,Pack.MISSION_ATTACK,0,0,0,1,3));
//                if (findPackWTag(6,Pack.TAG_TENTACLE) == 0) {
//                    boolean complete = true;
//                    for (Unit u : units)
//                        if (u != base.base_ship && u.tian > u.need_tian) complete = false;
//                    if (complete && Say(6, sov_ship, "Кэп, к нам гости..."))
//                        setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,1,2,0,0,0));
//                }
//                if(Say(7,sov_ship, "И похоже они\nпривели своих друзей..."))
//                    setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,1,0,0,1,3));
//                if(findPackWTag(8,Pack.TAG_TENTACLE) < 2 && Say(8,base.base_ship, "Смотри кэп,\nтам тяночки..."))
//                    setNav(spawnTian(-1,-1,base,16));
//                Say(9,sov_ship, "Собери их всех!...");
//                if(findPackWTag(10,Pack.TAG_RESORCES) == 0 && Say(10,sov_ship, "Смотрите капитан,\nжалкие вторженцы\nжелают принять смерть\nот рук наших тян..."))
//                    setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,3,3,0,2,3));
//                Say(11,sov_ship, "Сделайте им такое\nодолжение.\nУбейте их...");
//                if(findPackWTag(12,Pack.TAG_TENTACLE) == 0 && Say(12,sov_ship, "Упс, мы что то\nпоспешили кэп.\nСпасите нас\nпожалуйста...")){
//                    setNav(base);
//                    spawnEnemy(115, -1, base, Pack.MISSION_ATTACK, 0, 0, 2, 1, 2);
//                    spawnEnemy(135, -1, base, Pack.MISSION_ATTACK, 1, 0, 0, 0, 3);
//                    spawnEnemy(45 , -1, base, Pack.MISSION_ATTACK, 0, 3, 1, 0, 0);
//                    nestStep();
//                }
//                break;
//            // последняя волна
//            case 2:
//                if(findPackWTag(0,Pack.TAG_TENTACLE) == 0 && Say(0,sov_ship, "Враг тут..."))
//                    setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,1,2,0,0,0));
//                if(Say(1,sov_ship, "Враг там..."))
//                    setNav(spawnEnemy(-1,-1,base,Pack.MISSION_ATTACK,0,0,2,1,0));
//                Say(2,sov_ship, "У меня уже параноя!\nВы же отпустите\nменя в оплачиваемый\nотпуск капитан?...");
//                if(findPackWTag(3,Pack.TAG_TENTACLE) < 2 && Say(3,sov_ship, "У них подкрепление,\nа у нас всё как\nвсегда капитан, неочем\nбеспокоиться...")) {
//                    spawnEnemy(-1, -1, base, Pack.MISSION_ATTACK, 0, 3, 0, 0, 0);
//                    spawnEnemy(-1, -1, base, Pack.MISSION_ATTACK, 0, 0, 2, 0, 4);
//                }
//                if(findPackWTag(4,Pack.TAG_TENTACLE) < 3 && Say(4,sov_ship, "Я всегда мечтала\nвыйти замуж..."))
//                    setNav(spawnEnemy(-1, -1, base, Pack.MISSION_ATTACK, 0, 0, 0, 2, 2));
//                if(Say(5,sov_ship, "Надеюсь сейчас\nне поздно выйти\nхотя бы из окна..."))
//                    setNav(spawnEnemy(-1, -1, base, Pack.MISSION_ATTACK, 1, 0, 0, 0, 2));
//                if(findPackWTag(6,Pack.TAG_TENTACLE) == 0) Say(6,sov_ship, "Прислушайтесь капитан.\nВы не слышите вопли\nтян по рации?...");
//                Say(7,sov_ship, "Я тоже! Это значит\nЧто мы победили\nкапитан...");
//                Say(8, sov_ship, "Какое же\nумиротворение приходит\nкогда тян перестают\nвопить...");
//                if(dialog == 9 && targetRadioUnit == null){
//                    Victory(gl);
//                    return;
//                }
//                break;
        }
        super.draw(gl);
    }

    @Override
    public boolean inPut(MotionEvent event) {
        switch (step) {
            case 0:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                break;
            case 1:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 5) clearMask();
                break;
            case 2:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 3) clearMask();
                break;
            case 3:
                fillMask();
                if (targetRadioUnit != null) setXMask(event, text_frame);
                if (dialog == 4) clearMask();
                break;
//                fillMask();
//                setXMask(event,text_frame);
//                break;
//            case 1:
//            case 2:
//                break;
        }
        return super.inPut(event);
    }

    @Override
    public void resource_load(GL10 gl) {
        loadBackground(gl, R.drawable.bg_level2);
        super.resource_load(gl);
    }

}
