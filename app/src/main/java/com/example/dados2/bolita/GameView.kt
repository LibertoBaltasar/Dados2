package com.example.dados2.bolita

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.dados2.basedatos.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameView(private val nombre: String, context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs), SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    private lateinit var gameThread: Thread
    private var playing = false
    private val ballPaint = Paint()
    private val ballMainPaint = Paint()
    private val squarePaint = Paint()
    private var ball1: Ball? = null
    private val balls: MutableList<Ball> = mutableListOf()
    private var score = 0
    private val squareSize = 20f
    private var squares: Array<Array<String>>? = null
    private var initializedBalls = false

    private var endGameButton: Button
    private var playPauseButton: Button
    private var scoreTextView: TextView
    private var surfaceView: SurfaceView

    init {
        // Inicializar el SurfaceView
        surfaceView = SurfaceView(context).apply {
            id = View.generateViewId() // Asignar ID
            holder.addCallback(this@GameView)
        }

        ballPaint.color = Color.BLACK
        ballMainPaint.color = Color.RED
        squarePaint.color = Color.WHITE
        surfaceView.setOnTouchListener(this)

        // Inicializar el botón de PLAY/PAUSE y el TextView del puntaje
        playPauseButton = Button(context).apply {
            id = View.generateViewId() // Asignar ID
            text = "PLAY"
            setOnClickListener {
                if (playing) {
                    pause()
                    text = "PLAY"
                } else {
                    resume()
                    text = "PAUSE"
                }
            }
        }

        scoreTextView = TextView(context).apply {
            id = View.generateViewId() // Asignar ID
            text = "Score: $score"
            textSize = 20f
            setTextColor(Color.BLACK)
        }
        endGameButton = Button(context).apply {
            id = View.generateViewId() // Asignar ID
            text = "END GAME"
            setOnClickListener {
                endGame(nombre)
            }
        }

        // Agregar los elementos a la vista
        addView(endGameButton)
        addView(surfaceView)
        addView(playPauseButton)
        addView(scoreTextView)

        // Configurar las restricciones de los elementos
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        // Configurar restricciones para surfaceView
        constraintSet.connect(surfaceView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(surfaceView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(surfaceView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(surfaceView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        // Configurar restricciones para playPauseButton
        constraintSet.connect(playPauseButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(playPauseButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 16)

        constraintSet.connect(endGameButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 16)
        constraintSet.connect(endGameButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        // Configurar restricciones para scoreTextView
        constraintSet.connect(scoreTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16)
        constraintSet.connect(scoreTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16)

        constraintSet.applyTo(this)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y
            ball1?.let {
                val angle = Math.atan2((touchY - it.y).toDouble(), (touchX - it.x).toDouble())
                it.dx = (5 * Math.cos(angle)).toFloat()
                it.dy = (5 * Math.sin(angle)).toFloat()
            }
        }
        return true
    }

    private fun checkCollisionWithList(ball: Ball, balls: MutableList<Ball>) {
        for (i in balls.indices.reversed()) {
            val otherBall = balls[i]
            if (ball !== otherBall) {
                val distanceX = ball.x - otherBall.x
                val distanceY = ball.y - otherBall.y
                val distance = Math.sqrt((distanceX * distanceX + distanceY * distanceY).toDouble()).toFloat()
                if (distance < ball.radius + otherBall.radius) {
                    score += otherBall.radius.toInt()
                    balls.removeAt(i)
                    invalidate()
                }
            }
        }
        if (balls.isEmpty()) {
            initializeBalls(increaseSpeed = true)
        }
    }

    override fun run() {
        while (playing) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        ball1?.updatePosition(true)
        balls.forEach { it.updatePosition(false) }
        ball1?.let { checkCollisionWithList(it, balls) }
    }

    private fun draw() {
        if (surfaceView.holder.surface.isValid) {
            val canvas = surfaceView.holder.lockCanvas()
            canvas.drawColor(Color.WHITE) // Limpiar el canvas con un color de fondo
            drawSquares(canvas)
            drawScore(canvas)
            balls.forEach { drawBall(canvas, it) }
            ball1?.let { drawBall1(canvas, it) }
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }
    }
    private fun drawScore(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 50f
        val text = "Score: $score"
        canvas.drawText(text, 50f, 100f, paint)
    }

    private fun control() {
        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun drawBall(canvas: Canvas, ball: Ball) {
        canvas.drawCircle(ball.x, ball.y, ball.radius, ball.paint)
    }

    private fun drawBall1(canvas: Canvas, ball: Ball) {
        canvas.drawCircle(ball.x, ball.y, ball.radius, ballMainPaint)
    }

    private fun drawSquares(canvas: Canvas) {
        val centerX = (width / 2 - squareSize / 2)
        val centerY = (height / 2 - squareSize / 2)
        canvas.drawRect(
            centerX,
            centerY,
            centerX + squareSize,
            centerY + squareSize,
            squarePaint
        )
    }

    fun pause() {
        playing = false
        gameThread.join()
    }

    fun resume() {
        if (!initializedBalls){
            initializeBalls()
            initializedBalls = true
        }
        playing = true
        gameThread = Thread(this)
        gameThread.start()
        Log.d("GameView", "Game resumed")

    }
    private fun endGame(nombre: String) {
        playing = false
        gameThread.join()
        actualizarPuntuacion()
        val intent = Intent(context, BolitaMenuActivity::class.java)
        intent.putExtra("nombre",nombre)
        context.startActivity(intent)
    }

    private fun actualizarPuntuacion() {
        CoroutineScope(Dispatchers.IO).launch {
            val jugadorDao = AppDatabase.getInstance(context).jugadorDao()
            val jugador = jugadorDao.findByName(nombre)
            if (jugador != null) {
                jugador.puntajeAcumulado += score
                if (score > jugador.puntajeMax) {
                    jugador.puntajeMax = score
                }
                val rowsUpdated = jugadorDao.updateJugador(jugador)
                if (rowsUpdated > 0) {
                    Log.d("GameView", "Jugador actualizado correctamente: $nombre")
                } else {
                    Log.e("GameView", "Error al actualizar el jugador: $nombre")
                }
            } else {
                Log.e("GameView", "Jugador no encontrado: $nombre")
            }
        }
    }

    private fun initializeBalls(increaseSpeed: Boolean = false) {
        balls.clear()
        val numBalls = Random.nextInt(3, 7)
        for (i in 0 until numBalls) {
            val radius = Random.nextInt(10, 30).toFloat()
            val speedMultiplier = if (increaseSpeed) 2 else 1
            val dx = (Random.nextFloat() * 4 + 1) * speedMultiplier
            val dy = (Random.nextFloat() * 4 + 1) * speedMultiplier
            val color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            val paint = Paint().apply { this.color = color }
            balls.add(Ball(x = Random.nextFloat() * width, y = Random.nextFloat() * height, dx = dx, dy = dy, radius = radius, paint = paint))
        }
    }

    inner class Ball(var x: Float, var y: Float, var dx: Float = 5f, var dy: Float = 5f, val radius: Float = squareSize / 2 + 10, val paint: Paint = Paint()) {
        fun updatePosition(redBall: Boolean) {
            x += dx
            y += dy
            checkCollision(this, redBall)
        }
    }

    private fun checkCollision(ball: Ball, redBall: Boolean) {
        if (ball.x - ball.radius < 0 || ball.x + ball.radius > width) {
            ball.dx = -ball.dx
            if (redBall) score++
        }
        if (ball.y - ball.radius < 0 || ball.y + ball.radius > height) {
            ball.dy = -ball.dy
            if (redBall) score++
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {
        ball1 = Ball(x = width / 4f, y = height / 2f, paint = ballMainPaint)
        initializeBalls()
        initializeSquares(width, height)
        resume()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
    }

    private fun initializeSquares(width: Int, height: Int) {
        val numSquaresX = (width / squareSize).toInt()
        val numSquaresY = (height / squareSize).toInt()
        squares = Array(numSquaresX) { Array(numSquaresY) { "WHITE" } }

        // Colocar un solo cuadrado en el centro de la pantalla
        val centerX = numSquaresX / 2
        val centerY = numSquaresY / 2
        squares?.let {
            for (i in it.indices) {
                for (j in it[i].indices) {
                    it[i][j] = if (i == centerX && j == centerY) "BLACK" else "WHITE"
                }
            }
        }
    }
}