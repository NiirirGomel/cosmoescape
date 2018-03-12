package com.ermilitary.CosmoEscape;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 03.04.2015.
 * Рисует draw() игры
 */
public class GameRenderer extends AbstractRenderer {
    private Game game;
    GameRenderer(Game g){
        game = g;
    }

    @Override protected void resource_load(GL10 gl) {
        game.resource_load(gl);
    }

    @Override protected void draw(GL10 gl) {
        game.draw(gl);
    }
}
