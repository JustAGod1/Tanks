package com.justagod.tanks.WorldProviding;


import com.justagod.tanks.Entities.Blocks.Block;
import com.justagod.tanks.Entities.Blocks.BlockWrapper;
import com.justagod.tanks.Entities.Entity;
import com.justagod.tanks.Entities.TankEntity;
import com.justagod.tanks.WorldProviding.Vectors.BlockPos;
import com.justagod.tanks.WorldProviding.Vectors.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

import static com.justagod.tanks.Entities.Blocks.Blocks.stone;

/**
 * Создано Юрием в 31.12.16.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class World implements Iterable<BlockWrapper> {
    public static final int CHUNK_SIZE = 10;
    private HashMap<BlockPos, Chunk> chunks = new HashMap<>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> updatableEntities = new ArrayList<>();
    private Thread thread;
    public final TankEntity player = new TankEntity();

    private static World instance;

    public static void generateNewWorld() {
        Random random = new Random();
        instance = new World();
        for (int i = 0; i < 555; i++) {
            Block block;
            switch (i % 30) {
                case 10: {
                    block = stone;
                    break;
                }
                case 0: {
                    block = stone;
                    break;
                }
                default: {
                    block = stone;
                }
            }
            BlockPos pos = new BlockPos(random.nextInt(CHUNK_SIZE) - CHUNK_SIZE / 2, random.nextInt(CHUNK_SIZE) - CHUNK_SIZE / 2);
            instance.setBlock(pos, block);
        }
    }

    public World() {


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loop();
            }
        });
        thread.start();

        addEntity(player);
    }



    public static void load(String worldName) {

    }

    public static World getInstance() {
        return instance;
    }

    public void setBlock(BlockPos pos, Block block) {
        Chunk chunk = getChunkByBlockPos(pos);

        chunk.setBlock(pos.convertToChunkPos(), block);
    }

    public Chunk getChunkByBlockPos(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();

        x = x / (CHUNK_SIZE);
        y = y / (CHUNK_SIZE);

        pos = new BlockPos(x, y);

        return getChunk(pos);
    }

    public Chunk getChunk(BlockPos pos) {
        Chunk res = chunks.get(pos);
        if (res == null) {
            res = new Chunk();
            res.generate();
            chunks.put(pos, res);
        }
        return res;
    }

    public Collection<Chunk> getChuncks() {
        return chunks.values();
    }

    public Set<Map.Entry<BlockPos, Chunk>> getChunksSet() {
        return chunks.entrySet();
    }


    @Override
    public Iterator<BlockWrapper> iterator() {
        return new WorldIterator(chunks);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        if (entity.isUpdatable()) updatableEntities.add(entity);
    }

    public boolean hasBlockAtPos(BlockPos pos) {
        return getBlockByPos(pos) != null;
    }

    public boolean hasBlockAtPos(Vector2 pos) {


        for (BlockWrapper block : this) {
            if (block.isVectorInBounds(pos)) return true;
        }
        return false;
    }

    public BlockWrapper getBlockByPos(BlockPos pos) {
        Chunk chunk = getChunkByBlockPos(pos);

        return chunk.getBlockByPos(pos);
    }

    public BlockWrapper getBlockByPos(Vector2 pos) {

        for (BlockWrapper block : this) {
            if (block.isVectorInBounds(pos)) return block;
        }
        return null;
    }

    private void loop() {
        while (true) {
            try {
            for (Entity entity : updatableEntities) {
                entity.updateEntity();
            }


                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException ignored) {

            }
        }
    }

    public void onDraw(GL10 gl) {
        try {
            for (Map.Entry<BlockPos, Chunk> entry : chunks.entrySet()) {
                gl.glPushMatrix();
                {
                    float x = entry.getKey().getX();
                    float y = entry.getKey().getY();
                    gl.glTranslatef((float) ((x / 2.0) * CHUNK_SIZE * 0.1), (float) ((y / 2.0) * CHUNK_SIZE * 0.1), 0);
                    entry.getValue().onDraw(gl);
                    gl.glTranslatef((float) ((x / 2.0) * CHUNK_SIZE * -0.1), (float) ((y / 2.0) * CHUNK_SIZE * -0.1), 0);
                }
                gl.glPopMatrix();
            }
            for (Entity entity : entities) {
                gl.glPushMatrix();
                {
                    entity.onDraw(gl);
                }
                gl.glPopMatrix();
            }
        } catch (ConcurrentModificationException ignore) {}
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        updatableEntities.remove(entity);
    }

    public void destroyBlock(BlockPos pos) {
        getChunkByBlockPos(pos).destroyBlock(pos);
    }

    /*public void createExplosion(BlockPos pos, int power) {
        World world = World.getInstance();
        if (world.hasBlockAtPos(pos.upperBlock()))
            World.getInstance().getBlockByPos(pos.upperBlock()).explosion(power);
        if (world.hasBlockAtPos(pos.downBlock()))
            World.getInstance().getBlockByPos(pos.downBlock()).explosion(power);
        if (world.hasBlockAtPos(pos.leftBlock()))
            world.getBlockByPos(pos.leftBlock()).explosion(power);
        if (world.hasBlockAtPos(pos.rightBlock()))
            world.getBlockByPos(pos.rightBlock()).explosion(power);
    }*/

    public BlockPos toBlockPos(Vector2 vector) {
        return new BlockPos(Math.round(vector.x / 0.1f) - 1, Math.round(vector.y / 0.1f));
    }

    public Entity findEntity(Vector2 pos, Entity... except) {
        ArrayList<Entity> exceptions = new ArrayList<>();
        Collections.addAll(exceptions, except);
        for (Entity entity : entities) {
            if (exceptions.contains(entity)) continue;
            if (entity.isVectorInBounds(pos)) return entity;
        }

        return null;
    }

    private class WorldIterator implements Iterator<BlockWrapper> {

        private HashMap<BlockPos, Chunk> map;
        private Iterator<Chunk> mapIterator;
        private Iterator<BlockWrapper> currentIterator;

        public WorldIterator(HashMap<BlockPos, Chunk> map) {
            this.map = map;
            mapIterator = map.values().iterator();
        }

        @Override
        public boolean hasNext() {
            if (currentIterator == null) {
                if (!mapIterator.hasNext()) return false;
                currentIterator = mapIterator.next().iterator();
            }
            if (!currentIterator.hasNext()) {
                if (!mapIterator.hasNext()) {
                    return false;
                }
                currentIterator = mapIterator.next().iterator();
            }
            return currentIterator.hasNext();
        }

        @Override
        public BlockWrapper next() {
            if (!hasNext()) return null;
            return currentIterator.next();
        }

        @Override
        public void remove() {

        }

    }
}
