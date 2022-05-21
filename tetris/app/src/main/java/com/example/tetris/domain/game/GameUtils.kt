package com.example.tetris.domain.game

typealias Matrix = Array<ByteArray>

fun jaggedArrayOf(rowsCount:Byte, columnsCount:Byte):Array<ByteArray> =
    Array(rowsCount.toInt()){ ByteArray(columnsCount.toInt()) }