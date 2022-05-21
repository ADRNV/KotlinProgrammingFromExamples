package com.example.tetris.domain.game.models

import java.lang.IllegalArgumentException

/***
 * Tetromino models, each represents shape of tetromino
 */
enum class Shape(val frameCount:Int, val startPosition:Int) {

    TetrominoI(2, 2) {

        override fun getFrame(frameNumber: Int): Frame {
            return when (frameNumber) {

                0 -> Frame(4).addRow("1111")

                1 -> Frame(1)
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                    .addRow("1")
                else -> throw IllegalArgumentException("$frameNumber is an invalid number")
            }
        }
    },

    TetrominoO(1,1){

        override fun getFrame(frameNumber: Int) =
            Frame(2).addRow("11")
                            .addRow("11")
    },

    TetrominoZ(2, 1){

        override fun getFrame(frameNumber: Int): Frame {
            return when(frameNumber){

                0 -> Frame(3)
                    .addRow("110")
                    .addRow("011")

                1 -> Frame(2)
                    .addRow("01")
                    .addRow("11")
                    .addRow("10")
                else -> throw IllegalArgumentException("$frameNumber is invalid number")
            }
        }
    },

    TetrominoT(4, 1){

        override fun getFrame(frameNumber: Int): Frame {

            return when(frameNumber){

                0 -> Frame(3)
                    .addRow("111")
                    .addRow("010")

                1 -> Frame(3)
                    .addRow("010")
                    .addRow("111")

                2 -> Frame(4)
                    .addRow("100")
                    .addRow("110")
                    .addRow("100")

                3 -> Frame(4)
                    .addRow("001")
                    .addRow("011")
                    .addRow("001")

                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    },

    TerominoL(4, 1){

        override fun getFrame(frameNumber: Int): Frame {

            return when(frameNumber){

                0 -> Frame(2)
                    .addRow("10")
                    .addRow("10")
                    .addRow("10")
                    .addRow("11")

                1 -> Frame(2)
                    .addRow("11")
                    .addRow("01")
                    .addRow("01")
                    .addRow("01")

                2 -> Frame(4)
                    .addRow("1111")
                    .addRow("0001")

                3 -> Frame(4)
                    .addRow("100")
                    .addRow("1111")
                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }


        }
    },

    TetrominoJ(4, 1){
        override fun getFrame(frameNumber: Int): Frame {
           return when(frameNumber){

                0 -> Frame(3)
                    .addRow("001")
                    .addRow("111")

                1 -> Frame(2)
                    .addRow("11")
                    .addRow("10")
                    .addRow("10")

                2 -> Frame(3)
                    .addRow("111")
                    .addRow("001")

                3 -> Frame(3)
                    .addRow("01")
                    .addRow("01")
                    .addRow("11")

                else -> throw IllegalArgumentException("$frameNumber is invalid")
            }
        }
    };

    /**
     * Returns [Frame] depending on the teromino shape
     * @param frameNumber - count of possible states of tetromino
     * @throws [IllegalArgumentException] if supplied [frameCount] not pass
     * to possible states
     */
    abstract fun getFrame(frameNumber: Int): Frame
}
