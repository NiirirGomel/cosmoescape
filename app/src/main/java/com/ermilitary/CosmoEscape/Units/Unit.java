package com.ermilitary.CosmoEscape.Units;

import android.view.MotionEvent;
import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Pack;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public abstract class Unit extends Collider {
    public static final int exit = 0;
    public static final int next = 1;
    public static final int setPack = 2;

    boolean select;

    public float max_health;
    public float max_shield;
    public float max_energy;
    public float health;
    public float shield;
    public float energy;
    public float energy_regen;

    public float m_speed;
    public float r_speed;
    public float max_m_speed;
    public float max_r_speed;
    public int r_dir;
    public Vector direction;
    public float cur_dir;
    public float vis_zone;

    public boolean moving;
    public boolean rotating;

    protected GeoSystem geoSistem;
    public Pack pack;
    public Unit target;
    public boolean death;
    public float delete;

    public int cash;
    public int tian;
    public int max_tian;
    public int need_tian;

    public String radio;

    public Unit(GeoSystem geoSistem) {
        this.geoSistem = geoSistem;
        select = false;
        radio = "";
        max_tian = 0;
        need_tian = 0;
        tian = 0;
        max_health = 0;
        max_shield = 0;
        max_energy = 0;
        health = 0;
        energy = 0;
        shield = 0;
        energy_regen = 0;
        m_speed = 0;
        max_m_speed = 0;
        r_speed = 0;
        max_r_speed = 0;
        r_dir = 0;
        direction = new Vector(0, 1, 0);
        vis_zone = 0;
        death = false;
        delete = 1f;
        cash = 0;

        moving = false;
        rotating = false;
    }

    public abstract void resLoad();

    public abstract void GUIdraw(GL10 gl);

    public abstract int GUIinPut(MotionEvent event);

    public void Death() {
        death = true;
    }

    public void setPack(Pack pack) {
        if (this.pack != null) {
            this.pack.dellOne(this);
        }
        if (pack != null) {
            pack.addOne(this);
            this.pack = pack;
        } else
            this.pack = null;
    }

    public void findTian(){
        if (pack.target_resource != null) {
            // развидеть Х_x
            if (target != null && (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death))
                target = null;

            // видеть 0_0
            float cur_dist = Float.MAX_VALUE;
            for (Unit unit : geoSistem.units)
                if (!unit.death && unit.pack == pack.target_resource && Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < vis_zone) {
                    if (Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < cur_dist){
                        cur_dist = Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider;
                        target = unit;
                    }
                }
            // *поиск вражины*
            if (target == null) {
                // уступай и не толкай сородичей своих
                notPush();
                // в стан врага лети ты
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_to(pack.target_resource.position);
                }
            }
            // *
            else { // тарджет есть
                // не толкатцо! ч_ч
                notPush();
                if (!moving && !rotating) {
                    if (tian < max_tian) {
                        if (Vector.Distance(position, target.position) - sphere_collider - target.sphere_collider > 0.05f) {
                            moving = true;
                            rotating = true;
                            rot_to(target.position);
                            // если добрался до цели
                        } else {
                            pickUp(target);
                        }
                    } else {
                        moving = true;
                        rotating = true;
                        rot_from(target.position);
                    }
                }
            }
        }
    }

    public void pickUp(Unit res){
        res.Death();
        tian++;
    }

    public int dealDamage(float damage) {
        if (shield > 0)
            shield -= damage;
        else
            health -= damage;
        if (health <= 0) {
            Death();
            return cash;
        }
        return 0;
    }

    @Override
    public void draw(GL10 gl) {
        if (energy < max_energy && (energy += energy_regen * ActiveElement.delayTime / 1000) > max_energy)
            energy = max_energy;
        if (isSelect()) {
            gl.glColor4f(0.3f, 1, 0.3f, 1);
            super.draw(gl);
            gl.glColor4f(1, 1, 1, 1);
        } else
            super.draw(gl);
    }

    public void rot_to(float rot_b) {
        float rot_a;
        if ((rot_b = rot_b % 360) < 0) rot_b = 360 + rot_b;
        if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
        float angle;
        if ((angle = rot_a - rot_b) < 0) angle += 360;  // расстояние от а до б справа
        if (angle < 180)
            r_dir = -1;
        else
            r_dir = 1;
        cur_dir = angle;
    }

    public void rot_to(Vector v) {
        float rot_b = Vector.getAngle(v.minus(position));
        float rot_a;
        if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
        float angle;
        if ((angle = rot_a - rot_b) < 0) angle += 360;  // расстояние от а до б справа
        if (angle < 180)
            r_dir = -1;
        else
            r_dir = 1;
        cur_dir = angle;
    }

    public void rot_from(Vector v) {

        float rot_b = Vector.getAngle(v.minus(position));
        float rot_a;
        if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
        float angle;
        if ((angle = rot_a - rot_b) < 0) angle += 360;  // от а до б справа
        if (angle < 180)
            r_dir = 1;
        else
            r_dir = -1;
        cur_dir = angle;
    }
    public void rot_from(float angle){
        float rot_b = angle;
        float rot_a;
        if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
        float anglex;
        if ((anglex = rot_a - rot_b) < 0) anglex += 360;  // от а до б справа
        if (anglex < 180)
            r_dir = 1;
        else
            r_dir = -1;
        cur_dir = anglex;
    }


    // **не влетает в чужие отряды, не выходит за пределы своего, уступает юнитам дорогу**
    public void packFormation() {
        for (Pack p : geoSistem.packs) {
            // не мешай присутствием своим остальным отрядам
            if (!moving && !rotating && p != pack && Vector.Distance(position, p.position) < p.pack_zone) {
                rot_to(Vector.getAngle(pack.position.minus(p.position)));
                moving = true;
                rotating = true;
                break;
            }
        }
        if (!moving && !rotating) {
            // не выходи за пределы отряда своего
            if (Vector.Distance(position, pack.position) > pack.pack_zone) {
                rotating = true;
                moving = true;
                rot_to(pack.position);
            } else // уступай и не толкай сородичей своих
                notPush();
        }
    }

    public void notPush() {
        // уступай и не толкай сородичей своих
        for (Unit unit : geoSistem.units) {
            if (unit != this && isCollision(unit)) {
                rot_from(unit.position);
                float razn = Vector.getAngle(unit.position.minus(position));
                float rot_a;
                float rot_b;
                if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
                if ((rot_b = unit.rotation % 360) < 0) rot_a = 360 + rot_a;
                float angle;
                if ((angle = rot_a - razn) < 0) angle += 360;  // от а до б справа
                if (angle < 90 || angle > 270) {
                    if ((angle = razn - rot_b) < 0) angle += 360;  // gradusi от а до б справа
                    if (angle < 90 || angle > 270) {
                        continue;
                    }
                }
                moving = true;
                rotating = true;
                break;
            }
        }
    }

    public void notPush(int tag) {
        // уступай и не толкай сородичей своих
        for (Unit unit : geoSistem.units) {
            if (unit != this && unit.pack != null && unit.pack.tag == tag && isCollision(unit)) {
                rot_from(unit.position);
                float razn = Vector.getAngle(unit.position.minus(position));
                float rot_a;
                float rot_b;
                if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
                if ((rot_b = unit.rotation % 360) < 0) rot_a = 360 + rot_a;
                float angle;
                if ((angle = rot_a - razn) < 0) angle += 360;  // от а до б справа
                if (angle < 90 || angle > 270) {
                    if ((angle = razn - rot_b) < 0) angle += 360;  // gradusi от а до б справа
                    if (angle < 90 || angle > 270) {
                        continue;
                    }
                }
                moving = true;
                rotating = true;
                break;
            }
        }
    }
    public void move() {
        // --движение--
        if (rotating)

        {
            if (r_dir > 0) {
                if (r_speed < max_r_speed && (r_speed += max_r_speed * ActiveElement.delayTime / 200) > max_r_speed)
                    r_speed = max_r_speed;
                if (max_r_speed != 0 && r_speed > cur_dir) r_speed = cur_dir;
            } else {
                if (r_speed > -max_r_speed && (r_speed -= max_r_speed * ActiveElement.delayTime / 200) < -max_r_speed)
                    r_speed = -max_r_speed;
                if (max_r_speed != 0 && r_speed < -cur_dir) r_speed = -cur_dir;
            }
            direction = Vector.getDirection(rotation += r_speed * ActiveElement.delayTime / 1000);
        } else if (r_speed != 0)

        {
            if (r_speed > 0 && (r_speed -= max_r_speed * ActiveElement.delayTime / 200) < 0) r_speed = 0;
            else if (r_speed < 0 && (r_speed += max_r_speed * ActiveElement.delayTime / 200) > 0) r_speed = 0;
            direction = Vector.getDirection(rotation += r_speed * ActiveElement.delayTime / 1000);
        }

        if (moving)

        {
            if (m_speed < max_m_speed && (m_speed += max_m_speed * ActiveElement.delayTime / 1000) > max_m_speed)
                m_speed = max_m_speed;
            else if (m_speed > max_m_speed && (m_speed -= max_m_speed * ActiveElement.delayTime / 210) < max_m_speed)
                m_speed = max_m_speed;
            position.x += direction.x * m_speed * ActiveElement.delayTime / 1000;
            position.y += direction.y * m_speed * ActiveElement.delayTime / 1000;
        } else if (m_speed > 0)

        {
            if ((m_speed -= max_m_speed * ActiveElement.delayTime / 210) < 0) m_speed = 0;
            position.x += direction.x * m_speed * ActiveElement.delayTime / 1000;
            position.y += direction.y * m_speed * ActiveElement.delayTime / 1000;
        }
        // ----
        moving = false;
        rotating = false;
    }
    public void selection(){
        select = true;
    }
    public void unselection(){
        select = false;
    }
    public boolean isSelect(){return select;}
}
