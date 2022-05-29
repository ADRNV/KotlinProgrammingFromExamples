package com.example.tetris.domain.game

import android.graphics.Point
import com.example.tetris.domain.game.constants.CellsStatuses
import com.example.tetris.domain.game.constants.FieldConstants
import com.example.tetris.domain.game.models.Block
import com.example.tetris.storage.AppPreferences

class AppModel {

    var score = 0

    private var _preferences:AppPreferences? = null

    var currentBlock: Block? = null

    var currentState = Statuses.AWAITING_START.name

    private val _field:Array<ByteArray> =
        jaggedArrayOf(FieldConstants.ROW_COUNT.value, FieldConstants.COLUMN_COUNT.value)

    enum class Statuses{
        AWAITING_START,
        ACTIVE,
        INACTIVE,
        OVER
    }

    enum class Motions{
        LEFT,
        RIGHT,
        DOWN,
        ROTATE
    }

    var preferneces
    get() = _preferences
    set(value) {
        _preferences = value
    }

    fun isGameActive() = currentState == Statuses.ACTIVE.name

    fun isGameAwaitingStart() = currentState == Statuses.AWAITING_START.name

    fun isGameOver() = currentState == Statuses.OVER.name

    fun startGame(){

        if (!isGameActive()){

            currentState = Statuses.ACTIVE.name

            generateNextBlock()
        }
    }

    fun restartGame(){

        resetModel()

        startGame()
    }

    fun endGame(){

        score = 0

        currentState = Statuses.OVER.name
    }

    private fun resetModel(){

        resetField(false)

        currentState = Statuses.AWAITING_START.name

        score = 0
    }


    fun getCellStatus(row:Int, column:Int):Byte{
        return _field[row][column]
    }

    private fun setCellStatus(row:Int, column:Int, status:Byte){

        _field[row][column] = status
    }

    private fun incrementScore() {

        score += 10

        if (score > _preferences?.getHighScore() as Int)
            _preferences?.saveHighScore(score)
    }

    private fun generateNextBlock(){
        currentBlock = Block.createBlock()
    }

    private fun validTranslation(position: Point, shape: Array<ByteArray>):Boolean{

        return if(position.y < 0 || position.x < 0){

            false

        }else if (position.y + shape.size > FieldConstants.ROW_COUNT.value)
        {

            false

        }else if(position.x + shape[0].size > FieldConstants.COLUMN_COUNT.value){

            false

        }else{

            for (i in 0 until shape.size){

                for (j in 0 until shape[i].size){
                    val y = position.y + i
                    val x = position.x + j
                    if (CellsStatuses.EMPTY.filled != shape[i][j] && CellsStatuses.EMPTY.filled != _field[y][x]){
                        return false
                    }
                }
            }
            true
        }
    }

    private fun translateBlock(position: Point, frameNumber: Int) {

        synchronized(_field){

            val shape:Array<ByteArray>? = currentBlock?.getShape(frameNumber)

            if (shape != null){

                for (i in shape.indices){

                    for (j in 0 until shape[i].size) {

                        val y = position.y + i

                        val x = position.x + j

                        if(CellsStatuses.EMPTY.filled != shape[i][j]){
                            _field[y][x] = shape[i][j]
                        }
                    }
                }
            }
        }

    }


    private fun moveValid(position: Point, frameNumber:Int?):Boolean{

        val shape:Array<ByteArray>? = currentBlock?.getShape(frameNumber as Int)

        return validTranslation(position, shape as Array<ByteArray>)
    }

    fun generateField(action:String){

        if(isGameActive()){

            resetField()

            var frameNumber:Int? = currentBlock?.frameNumber

            val coordinate:Point? = Point()

            coordinate?.x = currentBlock?.position?.x

            coordinate?.y = currentBlock?.position?.y

            when(action){

                Motions.LEFT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.minus(1)
                }

                Motions.RIGHT.name -> {
                    coordinate?.x = currentBlock?.position?.x?.plus(1)
                }

                Motions.DOWN.name -> {
                    coordinate?.y = currentBlock?.position?.y?.plus(1)
                }

                Motions.ROTATE.name -> {
                    frameNumber = frameNumber?.plus(1)

                    if(frameNumber != null) {
                        if (frameNumber >= currentBlock?.frameCount as Int) {
                            frameNumber = 0
                        }
                    }
                }
            }

            if(!moveValid(coordinate as Point, frameNumber)){
                translateBlock(currentBlock?.position as Point, currentBlock?.frameNumber as Int)

                if (Motions.DOWN.name == action){
                    incrementScore()
                    persistCellData()
                    assessField()
                    generateNextBlock()
                    if(!blockAdditionPossible()){
                        currentState = Statuses.OVER.name
                        currentBlock = null
                        resetField(false)
                    }
                }
            } else{
                if(frameNumber != null){
                    translateBlock(coordinate, frameNumber)
                    currentBlock?.setState(frameNumber, coordinate)
                }
            }
        }
    }

    private fun assessField() {

        for (i in 0 until _field.size){

            var emptyCells = 0

            for (j in 0 until _field[i].size){

                val status = getCellStatus(i, j)

                val isEmpty = CellsStatuses.EMPTY.filled == status

                if(isEmpty){
                    emptyCells++
                }
                if(emptyCells == 0){
                    shiftRows(i)
                }
            }

        }
    }

    private fun shiftRows(nToRow:Int) {

        if(nToRow > 0){

            for (j in nToRow - 1 downTo 0){

                for (m in 0 until _field[j].size){
                    setCellStatus(j + 1, m, getCellStatus(j, m))
                }
            }
        }

        for (j in 0 until _field[0].size){
            setCellStatus(0, j, CellsStatuses.EMPTY.filled)
        }
    }

    private fun persistCellData() {

        for (i in 0 until _field.size){

            for (j in 0 until _field[i].size){

                var status = getCellStatus(i, j)

                if(status == CellsStatuses.EPTHEMERAL.filled){

                    status = currentBlock?.staticValue!!

                    setCellStatus(i, j, status)
                }
            }
        }
    }

    private fun blockAdditionPossible(): Boolean {
       if(!moveValid(currentBlock?.position as Point, currentBlock?.frameNumber)){
           return false
       }
        return true
    }

    private fun resetField(ephemeralCellOnly:Boolean = true) {

        for (i in 0 until FieldConstants.ROW_COUNT.value) {
            (0 until FieldConstants.COLUMN_COUNT.value)
                .filter { !ephemeralCellOnly || _field[i][it] == CellsStatuses.EPTHEMERAL.filled }
                .forEach { _field[i][it] = CellsStatuses.EMPTY.filled }
        }
    }
}

