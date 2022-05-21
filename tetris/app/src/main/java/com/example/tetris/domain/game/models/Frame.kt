package com.example.tetris.domain.game.models

import com.example.tetris.domain.game.jaggedArrayOf

/**
 * Represents frame occupied [Shape]
 */
class Frame(private val width:Int) {

    /**
     * Get byte array of [Frame]
     */
    private val data:ArrayList<ByteArray> = ArrayList()

    /**
     * Add row in [Frame]
     * @param [byteString] shape in string representation
     * @return new [Frame]
     */
    fun addRow(byteString:String): Frame {

        val row = ByteArray(byteString.length)

        for (index in byteString.indices){
            row[index] = "${byteString[index]}".toByte()
        }

        data.add(row)

        return this
    }

    /**
     *Represent [Frame] as jagged array
     */
    fun asMatrix():Array<ByteArray>{

        val bytes = jaggedArrayOf(data.size.toByte(), width.toByte())

        return data.toArray(bytes)
    }
}