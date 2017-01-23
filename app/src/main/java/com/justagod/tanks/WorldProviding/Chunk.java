package com.justagod.tanks.WorldProviding;


import com.justagod.tanks.Entities.Blocks.Block;
import com.justagod.tanks.Entities.Blocks.BlockWrapper;
import com.justagod.tanks.R;
import com.justagod.tanks.Rendering.Tesselator;
import com.justagod.tanks.Rendering.WorldRenderer;
import com.justagod.tanks.WorldProviding.Vectors.BlockPos;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_COLOR_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_COORD_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;
import static javax.microedition.khronos.opengles.GL10.GL_VERTEX_ARRAY;

/**
 * Создано Юрием в 31.12.16.
 *
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class Chunk implements Iterable<BlockWrapper> {

    private HashMap<BlockPos, BlockWrapper> blocks = new HashMap<>();
    private static int texture = -1;

    public void generate() {

    }

    public void setBlock(BlockPos pos, Block block) {
        blocks.put(pos, new BlockWrapper(block, pos));
    }

    public void setBlock(BlockPos pos, BlockWrapper block) {
        blocks.put(pos, block);
    }

    @Override
    public Iterator<BlockWrapper> iterator() {
        return blocks.values().iterator();
    }

    public void onDraw(GL10 gl) {
        drawBackground(gl);

        try {
            for (Map.Entry<BlockPos, BlockWrapper> entry : blocks.entrySet()) {
                BlockWrapper block = entry.getValue();
                BlockPos pos = entry.getKey();

                block.onDraw(gl);

            }
        } catch (ConcurrentModificationException ignore) {}
    }

    private void drawBackground(GL10 gl) {
        float x = -1;
        float y = -1f;


        for (int i = 0; i < World.CHUNK_SIZE; i++) {
            for (int j = 0; j < World.CHUNK_SIZE; j++) {
                gl.glPushMatrix();



                drawBackgroundTile(x, y, gl);

                gl.glPopMatrix();



                x += 0.2f;
            }
            y += 0.2f;
            x = -1;
        }

    }

    private void drawBackgroundTile(float x, float y, GL10 gl) {

        float[] vertexes = {
                0, 0, 0,
                0, 0.2f, 0,
                0.2f, 0.2f, 0,
                0.2f, 0, 0
        };

        float[] texCoords = {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };



        byte[] indices = {0, 1, 2, 0, 3, 2};



        Tesselator.bindVertexArray(vertexes, gl);
        Tesselator.bindTexCoordArray(texCoords, gl);
        Tesselator.bindIndices(indices);
        Tesselator.bindTexture(R.drawable.cobblestone, gl);

        Tesselator.translated(x, y, 0 , gl);

        Tesselator.drawElements(gl, GL_TRIANGLES);

        /*gl.glDisableClientState(GL_COLOR_ARRAY);
        gl.glDisableClientState(GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);*/

    }

    public BlockWrapper getBlockByPos(BlockPos pos) {
        pos = pos.convertToChunkPos();

        return blocks.get(pos);
    }

    public void destroyBlock(BlockPos pos) {
        blocks.remove(pos.convertToChunkPos());
    }
}
