package com.ermilitary.CosmoEscape;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Ermilashka on 06.03.2015.
 */
public abstract class AbstractRenderer implements GLSurfaceView.Renderer{

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //gl.glEnable(GL10.GL_ALPHA_TEST);
        //gl.glAlphaFunc(GL10.GL_GREATER, 0.4f);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glActiveTexture(GL10.GL_TEXTURE0);
        resource_load(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Game.width = width;
        Game.height = height;
        Game.ratio = (float) width / height;

        gl.glViewport(0,0,width,height);
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        //gl.glFrustumf(-ratio,ratio, -1,1, 0.5f,7);
        gl.glOrthof(-ratio, ratio, -1, 1, 0, 10);
    }

    public void onDrawFrame(GL10 gl) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV,GL10.GL_TEXTURE_ENV_MODE,GL10.GL_MODULATE);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, 1, 0, 0, 0, 0, 1, 0);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        draw(gl);
    }
    protected abstract void draw(GL10 gl);
    protected abstract void resource_load(GL10 gl);
}
