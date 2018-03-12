package com.ermilitary.CosmoEscape.Level;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.GameObject;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.R;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;
import java.util.LinkedList;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class GeoSystem extends ActiveElement {

    //res
    public int textures[] = new int[62];
    public LinkedList<Unit> units;
    public LinkedList<Pack> packs;
    Sprite backGround;
    // синхронное добавление записей в поток draw()
    boolean lock = false;
    Unit locked_unit;
    Pack locked_pack;
    LinkedList<Unit> locked_units;
    LinkedList<Pack> locked_packs;

    public GeoSystem(){
        locked_unit = null;
        locked_pack = null;
        units = new LinkedList<Unit>();
        packs = new LinkedList<Pack>();
        locked_units = new LinkedList<Unit>();
        locked_packs = new LinkedList<Pack>();
        backGround = new Sprite();
        backGround.setScale(1.8f,1,1);
    }
    public void loadBackground(GL10 gl,int res_id){
        gl.glDeleteTextures(1,textures,0);
        textures[0] = res_to_tex(gl,res_id);
        backGround.setSprite(textures[0]);
    }

    @Override
    public void resource_load(GL10 gl) {

        // gui
        textures[1] = res_to_tex(gl,R.drawable.gui_button_group);
        textures[2] = res_to_tex(gl,R.drawable.gui_button_yes);
        textures[3] = res_to_tex(gl,R.drawable.gui_button_not);
        textures[60] = res_to_tex(gl,R.drawable.gui_resource_tian);
        textures[61] = res_to_tex(gl,R.drawable.gui_resource_money);
        // tian gui
        textures[4] = res_to_tex(gl,R.drawable.gui_unit_avatarframe);
        // all unit
        textures[5] = res_to_tex(gl,R.drawable.gui_unit_textframe);
        textures[6] = res_to_tex(gl,R.drawable.gui_unit_avatar_tian);
        textures[7] = res_to_tex(gl,R.drawable.gui_unit_healthbar);
        textures[8] = res_to_tex(gl,R.drawable.gui_unit_shieldbar);
        textures[9] = res_to_tex(gl,R.drawable.gui_unit_energybar);
        textures[18] = res_to_tex(gl,R.drawable.unit_laser);
        textures[20] = res_to_tex(gl,R.drawable.unit_shield);
        textures[35] = res_to_tex(gl,R.drawable.unit_airblade);
        // tian ships
        textures[10] = res_to_tex(gl,R.drawable.unit_tian_fighter);
        textures[11] = res_to_tex(gl,R.drawable.unit_tian_assault);
        textures[12] = res_to_tex(gl,R.drawable.unit_tian_medic);
        textures[13] = res_to_tex(gl,R.drawable.unit_tian_scout);
        textures[19] = res_to_tex(gl,R.drawable.unit_tian_assault_bullet);
        textures[34] = res_to_tex(gl,R.drawable.unit_tian_explosion);
        // tentacle gui
        textures[36] = res_to_tex(gl,R.drawable.gui_unit_avatar_disturbing);
        // tentacle ships
        textures[37] = res_to_tex(gl,R.drawable.unit_tentacle_explosion);
        textures[38] = res_to_tex(gl,R.drawable.unit_tentacle_captor);
        textures[39] = res_to_tex(gl,R.drawable.unit_tentacle_solider);
        textures[40] = res_to_tex(gl,R.drawable.unit_tentacle_heavy);
        textures[41] = res_to_tex(gl,R.drawable.unit_tentacle_sniper);
        textures[42] = res_to_tex(gl,R.drawable.unit_tentacle_parasite);
        textures[43] = res_to_tex(gl,R.drawable.unit_tentacle_sniper_bullet);
        textures[44] = res_to_tex(gl,R.drawable.unit_tentacle_tentacle);
        textures[46] = res_to_tex(gl,R.drawable.unit_tentacle_infectedplanet);
        textures[47] = res_to_tex(gl,R.drawable.unit_tentacle_infectedplanet_tentacle);
        textures[48] = res_to_tex(gl,R.drawable.unit_tentacle_infectedplanet_hand);
        textures[49] = res_to_tex(gl,R.drawable.unit_tentacle_octopus);
        textures[50] = res_to_tex(gl,R.drawable.unit_tentacle_octopus_tentacle);
        textures[51] = res_to_tex(gl,R.drawable.unit_tentacle_octopus_hand);
        //aggrived
        textures[45] = res_to_tex(gl,R.drawable.unit_drop_aggrived);
        // base ship
        textures[14] = res_to_tex(gl,R.drawable.unit_tian_baseship);
        textures[15] = res_to_tex(gl,R.drawable.unit_tian_baseship_gun_hiperlaser);
        textures[16] = res_to_tex(gl,R.drawable.unit_tian_baseship_gun_megabomb);
        textures[17] = res_to_tex(gl,R.drawable.gui_unit_avatar_basetian);
        textures[21] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_menuframe);
        textures[22] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_fighter);
        textures[23] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_assault);
        textures[24] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_medic);
        textures[25] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_scout);
        textures[26] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_megabomb);
        textures[27] = res_to_tex(gl,R.drawable.gui_unit_tian_baseship_hiperlaser);
        textures[28] = res_to_tex(gl,R.drawable.unit_tian_baseship_hiperlaser);
        textures[29] = res_to_tex(gl,R.drawable.unit_tian_baseship_hiperlaser_lightning);
        textures[30] = res_to_tex(gl,R.drawable.unit_tian_baseship_megabomb);
        textures[31] = res_to_tex(gl,R.drawable.unit_tian_baseship_megabomb_explosion1);
        textures[32] = res_to_tex(gl,R.drawable.unit_tian_baseship_megabomb_explosion2);
        textures[33] = res_to_tex(gl,R.drawable.unit_tian_baseship_megabomb_explosion3);
        // pack
        textures[52] = res_to_tex(gl,R.drawable.gui_pack_emblem_tian);
        textures[53] = res_to_tex(gl,R.drawable.gui_pack_emblem_tentacle);
        // squad
        textures[54] = res_to_tex(gl,R.drawable.gui_squad_tian_line);
        textures[55] = res_to_tex(gl,R.drawable.gui_squad_tian_attack);
        textures[56] = res_to_tex(gl,R.drawable.gui_squad_tian_help);
        textures[57] = res_to_tex(gl,R.drawable.gui_squad_tian_disband);
        textures[58] = res_to_tex(gl,R.drawable.gui_squad_tian_escape);
        textures[59] = res_to_tex(gl,R.drawable.gui_squad_tian_scout);




        for(Pack pack : packs){
            pack.resLoad();
        }
        for(Unit unit : units){
            unit.resLoad();
        }
        super.resource_load(gl);
    }

    @Override
    public void resource_free(GL10 gl) {
        gl.glDeleteTextures(62,textures,0);
        super.resource_free(gl);
    }

    @Override
    public void draw(GL10 gl) {

        // добавление из инпута
        if(lock){
            if(locked_pack != null){
                locked_pack.resLoad();
                locked_packs.add(locked_pack);
                locked_pack = null;
            } else if(locked_unit != null) {
                locked_unit.resLoad();
                locked_units.add(locked_unit);
                locked_unit = null;
            }
            lock = false;
        }
        // перенос из локедов в основные массивы
        if (locked_packs.size() > 0) {
            for (Pack p : locked_packs)
                packs.add(p);
        } locked_packs.clear();
        if (locked_units.size() > 0) {
            for (Unit u : locked_units)
                units.add(u);
        } locked_units.clear();

        // рисование и удаление всех гео-объектов
        Pack del_pack = null;
        for (Pack pack : packs)
            if(pack.size != 0)
                pack.draw(gl);
            else
                del_pack = pack;
        if (del_pack != null){
            packs.remove(del_pack);
        }
        Unit del_unit = null;
        for(Unit unit : units)
            if (unit.delete > 0)
                unit.draw(gl);
            else
                del_unit = unit;
        if (del_unit != null){
            units.remove(del_unit);
        }
    }

    public Pack getDellPack(){
        for (Pack p : packs)
            if (p.size <= 0)
                return p;
        return null;
    }
    public void addUnit(Unit unit){
        while(lock){}
        locked_unit = unit;
        lock = true;
    }
    public void addPack(Pack pack){
        while(lock){}
        pack.resLoad();
        locked_pack = pack;
        lock = true;
    }
    public void addUnitInDraw(Unit unit){
        unit.resLoad();
        locked_units.add(unit);
    }
    public void addPackInDraw(Pack pack){
        pack.resLoad();
        locked_packs.add(pack);
    }

    public void setNav(Pack p){};
    public Pack getNav(){ return null; };
    public GameObject getCam(){ return new GameObject(); };
    public Vector cam_set_event(MotionEvent event){ return  null; };
}
