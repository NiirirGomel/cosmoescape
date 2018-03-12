package com.ermilitary.CosmoEscape.GameObject;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Ermilashka on 03.04.2015.
 */
public class Sprite extends GameObject {
    private final static int VERTS = 4;
    private static FloatBuffer vertexBuffer;
    private static FloatBuffer textureBuffer;
    private static ShortBuffer indicesBuffer;

    private FloatBuffer myTextureBuffer;

    public Integer texture;

    // Статичные буферы класса иничиализируются в статичном конструкторе
    static {
        // Выделение места под спрайт
        ByteBuffer vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        ByteBuffer tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        textureBuffer = tbb.asFloatBuffer();

        ByteBuffer ibb = ByteBuffer.allocateDirect(6 * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();

        float[] cords = {
                -1f, -1f, 0,
                1f, -1f, 0,
                1f, 1f, 0,
                -1f, 1f, 0};
        for(int i = 0; i < VERTS; i++)
            for (int j = 0; j < 3; j++)
                vertexBuffer.put(cords[i*3+j]);

        float[] tex_cords = {
                0f, 1f,
                1f, 1f,
                1f, 0f,
                0f, 0f};
        for(int i = 0; i < VERTS; i++)
            for (int j = 0; j < 2; j++)
                textureBuffer.put(tex_cords[i*2+j]);

        short[] index = {0,1,2,0,2,3};
        for(int i = 0; i < 6; i++)
            indicesBuffer.put(index[i]);

        vertexBuffer.position(0);
        textureBuffer.position(0);
        indicesBuffer.position(0);
    }

    public void setSprite(Integer tex_id){
        texture = tex_id;
    }

    public  void setTexCord(Vector lu, Vector rd){
        ByteBuffer tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        myTextureBuffer = tbb.asFloatBuffer();

        float[] tex_cords = {
                lu.x, rd.y,
                rd.x, rd.y,
                rd.x, lu.y,
                lu.x, lu.y};
        for(int i = 0; i < VERTS; i++)
            for (int j = 0; j < 2; j++)
                myTextureBuffer.put(tex_cords[i*2+j]);

        myTextureBuffer.position(0);
    }

    public void draw(GL10 gl){
        //gl.glColor4f(0,0,0,0.5f);
        gl.glPushMatrix();
        gl.glTranslatef(position.x, position.y, position.z);
        gl.glRotatef(rotation, 0, 0, 1);
        gl.glScalef(scale.x, scale.y, scale.z);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);
        if (myTextureBuffer == null)
            gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,textureBuffer);
        else
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTextureBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES,6,GL10.GL_UNSIGNED_SHORT,indicesBuffer);
        gl.glPopMatrix();
    }
}
