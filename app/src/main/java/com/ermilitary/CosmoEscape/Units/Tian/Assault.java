package com.ermilitary.CosmoEscape.Units.Tian;

import com.ermilitary.CosmoEscape.ActiveElement;
import com.ermilitary.CosmoEscape.GameObject.Collider;
import com.ermilitary.CosmoEscape.GameObject.Sprite;
import com.ermilitary.CosmoEscape.GameObject.Vector;
import com.ermilitary.CosmoEscape.Level.GeoSystem;
import com.ermilitary.CosmoEscape.Packs.Base;
import com.ermilitary.CosmoEscape.Packs.Pack;
import com.ermilitary.CosmoEscape.Units.Unit;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Assault extends TanUnit {
    Bullet bullets;
    Sprite shield_poly;

    float prev_fire;
    float fire_rate;

    public Assault(GeoSystem gs){
        super(gs);
        setSphere(0.1f);

        max_tian = 6;
        tian = need_tian = 2;

        setScale(0.07f, 0.1f, 1);
        health = max_health = 600;
        shield = max_shield = 600;
        energy = max_energy = 3;

        max_m_speed = 0.2f;
        max_r_speed = 100f;

        vis_zone = 0.7f;

        prev_fire = 0;
        fire_rate = 0.3f;
        energy_regen = max_energy/6;

        bullets = new Bullet(geoSistem);
        //bullets.setSprite(geoSistem.textures[19]);
        shield_poly = new Sprite();
        shield_poly.setScale(sphere_collider+0.02f);
        shield_poly.position = position;
        delete = 0.3f;
    }

    @Override
    public void draw(GL10 gl) {
        if(death){ Death(gl); return; }
        // если хп мало, мы не убегаем и в отряде есть медик то летим к нему

        if (pack.target_enemy != null){
            // развидеть Х_x
            if(target != null && (Vector.Distance(position,target.position) - sphere_collider - target.sphere_collider > vis_zone || target.death)) target = null;

            // видеть 0_0
            float cur_dist = Float.MAX_VALUE;
            for (Unit unit : geoSistem.units)
                if (!unit.death && unit.pack == pack.target_enemy && Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < vis_zone) {
                    if (Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < cur_dist){
                        cur_dist = Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider;
                        target = unit;
                    }
                }
            // *поиск вражины*
            if (target == null) {
                max_m_speed = 0.2f;
                max_r_speed = 140f;
                // уступай и не толкай сородичей своих
                notPush();
                // в стан врага лети ты
                if (!moving && !rotating) {
                    rotating = true;
                    moving = true;
                    rot_to(pack.target_enemy.position);
                }
            }

            // *

            // тарджет есть
            else {
                // не толкатцо! ч_ч
                max_m_speed = 0.2f;
                max_r_speed = 100f;
                notPush();
                // замедло пока вражина в фокусе
                max_m_speed = 0.1f;
                max_r_speed = 40;
                // ***побег если враг близко***
                if (!moving) {
                    for (Unit unit : geoSistem.units)
                        if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && Vector.Distance(position, unit.position) - sphere_collider - unit.sphere_collider < vis_zone / 5) {
                            moving = true;
                            rotating = true;
                            rot_from(unit.position);
                            break;
                        }
                    // ***

                    // поворот лицом к врагу
                    if (!moving) {
                        float razn = Vector.getAngle(target.position.minus(position));
                        float rot_a;
                        if ((rot_a = rotation % 360) < 0) rot_a = 360 + rot_a;
                        float angle;
                        if ((angle = rot_a - razn) < 0) angle += 360;  // от а до б справа
                        if (angle > 5f && angle < 360 - 5f) { // радиус атаки 3 градуса
                            rotating = true;
                            rot_to(target.position);
                        }
                        // **стрельба пока еcть энергия и таймаут не откачан**
                        else {
                            rotating = true;
                            rot_to(target.position);
                            if (!moving && energy > 1 && fire_rate < (prev_fire += ActiveElement.delayTime / 1000)) { // не убегаем
                                bullets.Shot(position, target.position);
                                energy -= 1;
                                prev_fire = 0;
                            }
                        }
                        // **
                    }
                }
            }

        } else {
            max_m_speed = 0.2f;
            max_r_speed = 140f;
        }
        // рисуем и удаляем снаряды
        bullets.bullets_draw(gl);
        super.draw(gl);
        if (shield > 0) shield_poly.draw(gl);

    }

    boolean check_bullets = true;
    public void Death(GL10 gl) {
        if(check_bullets) {
            float timer = 1 - delete / 0.3f * 1;
            explosion.setPosition(position.x + 0.02f, position.y - 0.01f, 0);
            explosion.setScale(0.03f + 0.06f * timer);
            explosion.draw(gl);
            explosion.setPosition(position.x - 0.01f, position.y + 0.05f, 0);
            explosion.setScale(0.02f + 0.04f * timer);
            explosion.draw(gl);
            explosion.setPosition(position.x - 0.03f, position.y - 0.03f, 0);
            explosion.setScale(0.03f + 0.06f * timer);
            explosion.draw(gl);
            airblade.setPosition(position.x + Vector.getDirection(rotation + 8).x * 0.2f * timer, position.y + Vector.getDirection(rotation + 8).y * 0.2f * timer, 0);
            airblade.setRotation(rotation + 8);
            airblade.setScale(0.1f, 0.03f, 1);
            airblade.draw(gl);
            airblade.setPosition(position.x + Vector.getDirection(rotation + 172).x * 0.2f * timer, position.y + Vector.getDirection(rotation + 172).y * 0.2f * timer, 0);
            airblade.setRotation(rotation + 172);
            airblade.setScale(0.1f, 0.03f, 1);
            airblade.draw(gl);
            delete -= ActiveElement.delayTime / 1000;
            bullets.bullets_draw(gl);
            if (delete <= 0) { check_bullets = false; delete = 1; }
        } else if(bullets.next != null) {
            bullets.bullets_draw(gl);
        } else delete = 0;

    }
    @Override
    public void resLoad() {
        super.resLoad();
        setSprite(geoSistem.textures[11]);
        bullets.setSprite(geoSistem.textures[19]);
        shield_poly.setSprite(geoSistem.textures[20]);
    }

    public class Bullet extends Collider{

        public float speed;
        Vector direction;
        float life_time;
        float start_time;
        float dmg;
        public boolean death;

        GeoSystem gs;

        Bullet next;

        public Bullet(GeoSystem gs){
            this.gs = gs;
            next = null;
        }

        public void Shot(Vector position, Vector target){
            Bullet shot = new Bullet(gs);

            shot.setPosition(position);
            shot.setSphere(0.015f);
            shot.setScale(0.01f,0.02f,1);
            shot.direction = Vector.normalize(target.minus(position));
            shot.setRotation(Vector.getAngle(shot.direction));
            shot.dmg = 600;
            shot.speed = 1.5f;
            shot.life_time = 3;
            shot.start_time = 0;
            shot.death = false;
            //shot.next = null; // список

            if(next != null)
                shot.next = next;

            next = shot;
            setSprite(geoSistem.textures[19]);
        }
        public void bullets_draw(GL10 gl) {
            Bullet cur = this;
            while (cur.next != null)
                if (cur.next.death) {
                    cur.next = cur.next.next;
                } else {
                    cur.next.draw(gl);
                    cur = cur.next;
                }
        }

        public void setSprite(Integer tex_id) {
            Bullet cur = next;
            while (cur != null){
                cur.texture = tex_id;
                cur = cur.next;
            }
        }

        @Override
        public void draw(GL10 gl) {
            if(!death) {
                // полет
                position.x += direction.x * speed * ActiveElement.delayTime / 1000;
                position.y += direction.y * speed * ActiveElement.delayTime / 1000;
                // попадание
                for (Unit unit : geoSistem.units)
                    if (!unit.death && unit.pack.tag == Pack.TAG_TENTACLE && unit.isCollision(this)){
                        int cash = unit.dealDamage(dmg);
                        if (cash > 0) for (Pack p : geoSistem.packs) if(p.mission == Pack.MISSION_BASE){ Base base = (Base)p; if (base.base_ship != null && !base.base_ship.death) base.base_ship.cash += cash;}

                        death = true;
                    }
                // самоуничтожение
                if (life_time < (start_time += ActiveElement.delayTime / 1000)) {
                    death = true;
                }
                super.draw(gl);
            }
        }
    }
}
