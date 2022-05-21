package com.example.tetris.domain.game.models;

import android.graphics.Color;
import android.graphics.Point;
import androidx.annotation.NonNull;

import com.example.tetris.domain.game.constants.FieldConstants;

import java.util.Random;

public class Block {

    private final int _shapeIndex;
    private int _frameNumber;
    private Point _position;
    private final BlockColor _color;

    public static int getColor(byte value){

        for (BlockColor color : BlockColor.values())
        {
            return color._rgbValue;
        }

        return -1;
    }

    /**
     * Set frameNumber and postion of block
     * @param frameNumber - number of {@link com.example.tetris.domain.game.models.Frame}
     * @param position - position of block
     */
    public final void setState(int frameNumber, Point position){

        this._frameNumber = frameNumber;
        this._position = position;
    }

    /**
     * @param frameCount - possible states of tetromino
     * @return shape represent as jagged byte array]
     */
    @NonNull
    public final byte[][] getShape(int frameCount){
        return Shape.values()[_shapeIndex]
                .getFrame(frameCount).asMatrix();
    }

    /**
     * Get position of {@link com.example.tetris.domain.game.models.Block}
     * @return {@link android.graphics.Point}
     */
    public Point getPosition() {
        return _position;
    }

    /**
     * Get count of {@link com.example.tetris.domain.game.models.Frame}
     * @return Count of {@link com.example.tetris.domain.game.models.Frame}
     */
    public final int getFrameCount()
    {
        return  Shape.values()[_shapeIndex].getFrameCount();
    }

    /***
     * Get frame number
     * @return number of frame
     */
    public final int getFrameNumber(){
        return  _frameNumber;
    }

    /**
     *Get color of block
     * @return {@literal int} - rgb color
     */
    public int getColor(){
        return _color._rgbValue;
    }

    /**
     * Get byte value color from {@link com.example.tetris.domain.game.models.Block.BlockColor}
     * @return {@literal byte} from {@link com.example.tetris.domain.game.models.Block.BlockColor}
     */
    public byte getStaticValue() {
        return _color._byteValue;
    }

    private Block(int shapeIndex, BlockColor color){

        _frameNumber = 0;

        _shapeIndex = shapeIndex;

        _color = color;

        _position = new Point(FieldConstants.COLUMN_COUNT.getValue() / 2, 0);
    }

    /**
     * Creates block with random shape, color, position
     * @return {@link com.example.tetris.domain.game.models.Block}
     */
    @NonNull
    public static Block createBlock(){

        Random random = new Random();

        int shapeIndex = random.nextInt(Shape.values().length);

        BlockColor blockColor = BlockColor.
                values()[random.nextInt(BlockColor.values().length - 1)];

        Block block = new Block(shapeIndex, blockColor);

        block._position.x =
                block._position.x - Shape.values()[shapeIndex].getStartPosition();

        return block;
    }

    /***
     *Colors for tetromino-block
    */
    public enum BlockColor{
        PINK(Color.rgb(255, 105, 180),(byte)2),

        GREEN(Color.GREEN,(byte)3),

        ORANGE(Color.rgb(255, 140, 0),(byte) 4),

        YELLOW(Color.YELLOW,(byte) 5),

        CYAN(Color.CYAN,(byte) 6);

        BlockColor(int rgbValue, byte value) {
            this._rgbValue = rgbValue;
            this._byteValue = value;
        }

        private final int _rgbValue;
        private final byte _byteValue;
    }
}
