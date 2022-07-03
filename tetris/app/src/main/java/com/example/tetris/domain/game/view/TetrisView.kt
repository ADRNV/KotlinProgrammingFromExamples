package com.example.tetris.domain.game.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.example.tetris.R
import com.example.tetris.domain.game.AppModel
import com.example.tetris.domain.game.constants.CellsStatuses
import com.example.tetris.domain.game.constants.FieldConstants
import com.example.tetris.domain.game.models.Block
import kotlin.properties.Delegates

class TetrisView: View {

    constructor(context:Context, attrs: AttributeSet):super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle:Int):super(context, attrs, defStyle)

    private val _paint = Paint()

    private var _lastMove:Long = 0

    private var _model: AppModel? = null

    private var _viewContext:Context? = null

    private val _viewHandler = ViewHandler(this)

    private lateinit var _scoreChanged:(score:Int) -> Unit

    private  var _score:Int by Delegates.observable(0){
        _, oldScore, newScore ->
        _scoreChanged.invoke(newScore)
    }

    private var _cellSize:Dimension = Dimension(0, 0)

    private var _frameOffset:Dimension = Dimension(0, 0)

    var model
    get() = _model
    set(value){
        _model = value
    }

    var viewContext
    get() = _viewContext
    set(value) {
        _viewContext = value
    }

    var score
    get() = _score
    private set(value) {
        _score = value
    }

    fun setOnScoreChangedListener(handler:(score: Int) -> Unit){
        _scoreChanged = handler
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        drawFrame(canvas)

        if (_model != null){

            for (i in 0 until FieldConstants.ROW_COUNT.value){

                for (j in 0 until FieldConstants.COLUMN_COUNT.value){

                    drawCell(canvas, i, j)

                }

            }

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)

        val cellWidth = (w - 2 * FRAME_OFFSET_BASE) / FieldConstants.COLUMN_COUNT.value

        val  cellHeight = (h - 2 * FRAME_OFFSET_BASE) / FieldConstants.ROW_COUNT.value

        val n = Math.min(cellWidth, cellHeight)

        this._cellSize = Dimension(n, n)

        val offsetX = (w - FieldConstants.COLUMN_COUNT.value * n) / 2

        val offsetY = (h - FieldConstants.ROW_COUNT.value * n) / 2

        this._frameOffset = Dimension(offsetX, offsetY)
    }

    private fun drawFrame(canvas: Canvas){

        _paint.color = Color.LTGRAY

        canvas.drawRect(_frameOffset.width.toFloat(),
            _frameOffset.height.toFloat(),
            width - _frameOffset.width.toFloat(),
        height - _frameOffset.height.toFloat(), _paint)

    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int){

        val cellStatus = _model?.getCellStatus(row, col)

        if (CellsStatuses.EMPTY.filled != cellStatus) {


            val color = if (CellsStatuses.EPTHEMERAL.filled != cellStatus) {
                _model?.currentBlock?.color
            }else{
                Block.getColor(cellStatus)
            }
            drawCell(canvas, col, row, color as Int)
        }
    }

    private fun drawCell(canvas: Canvas, x: Int, y: Int, rgbColor: Int){

        _paint.color = rgbColor

        val top:Float = (_frameOffset.height + y * _cellSize.height + BLOCK_OFFSET).toFloat()

        val left:Float = (_frameOffset.width + x * _cellSize.width + BLOCK_OFFSET).toFloat()

        val bottom:Float = (_frameOffset.height + (y + 1) * _cellSize.height - BLOCK_OFFSET).toFloat()

        val right:Float = (_frameOffset.width + (x + 1) * _cellSize.width - BLOCK_OFFSET).toFloat()

        val rectangle = RectF(left, top, right, bottom)

        canvas.drawRoundRect(rectangle, 4f, 4f, _paint)

    }

    fun setGameCommand(move:AppModel.Motions){

        if(null != model && (model?.currentState ==
                    AppModel.Statuses.ACTIVE.name)){
            if(AppModel.Motions.DOWN == move){

                _model?.generateField(move.name)
                invalidate()
                return
            }

            setGameCommandWithDelay(move)
        }
    }

    fun setGameCommandWithDelay(move: AppModel.Motions){

        val now = System.currentTimeMillis()

        if(now - _lastMove > DELAY){

            _model?.generateField(move.name)
            invalidate()
            _lastMove = now
        }

        updateScores()

        _viewHandler.sleep(DELAY.toLong())
    }

    private fun updateScores(){
        score = model?.score!!
    }

    companion object {

        private const val DELAY = 400

        private const val BLOCK_OFFSET = 2

        private const val FRAME_OFFSET_BASE = 10
    }

    private class ViewHandler(private val owner: TetrisView) : Handler(){

        override fun handleMessage(message: Message){

            if(message.what == 0){

                if (owner.model != null){

                    if(owner.model!!.isGameOver()){

                        owner.model?.endGame()

                        Toast.makeText(owner.viewContext, R.string.game_over, Toast.LENGTH_LONG)
                            .show()
                    }

                    if(owner.model!!.isGameActive()){
                        owner.setGameCommandWithDelay(AppModel.Motions.DOWN)
                    }
                }


            }

        }

        fun sleep(delay:Long){

            this.removeMessages(0)

            sendMessageDelayed(obtainMessage(0), delay)
        }

    }

    private data class Dimension(val width:Int, val height:Int)
}