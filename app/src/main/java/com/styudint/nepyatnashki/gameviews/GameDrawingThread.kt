package com.styudint.nepyatnashki.gameviews


import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.Log.i
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
import com.styudint.nepyatnashki.NePyatnashkiApp
import com.styudint.nepyatnashki.data.AndroidGameState
import com.styudint.nepyatnashki.data.BitmapCache
import com.styudint.nepyatnashki.data.GameStartStateGenerator
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

class GameDrawingThread(surfaceHolder: SurfaceHolder, context: Context) :
        Thread() {
    private var myThreadSurfaceHolder: SurfaceHolder = surfaceHolder
    @Volatile
    private var myThreadRun: Boolean = false

    public var height = 4
    public var width = 4

    @Inject
    lateinit var cache: BitmapCache

    @Inject
    lateinit var generator: GameStartStateGenerator

    var gameState: AndroidGameState? = null
    private var isBusy = false

    init {
        i("log", "thred created")
        (context.applicationContext as NePyatnashkiApp).appComponent.inject(this)
        height = cache.height
        width = cache.width
    }

    fun setRunning(b: Boolean) {
        myThreadRun = b
    }

    fun onTileClick(x: Int, y: Int) {
        if (isBusy)
            return

        synchronized(gameState!!) {
            gameState!!.handleTap(getId(x, y))
        }
    }

    fun getId(x: Int, y: Int): Int {
        return y * width + x
    }

    fun cordsFromId(id: Int): Pair<Int, Int> {
        return Pair(id % width, id / width)
    }

    fun emtyTileId(): Int {
        return width * height - 1
    }

    @Override
    override fun run() {
        super.run()
        //i("log", "run")

        while (myThreadRun) {
            //i("log", "start")
            if (!cache.isInitialized())
                continue
            if (gameState == null) {
                if (generator == null)
                    continue
                else
                    gameState = generator.gameState
            }
            //i("log", "()_/")

            var canvas: Canvas? = null
            try {
                canvas = myThreadSurfaceHolder.lockCanvas(null)
                canvas?.let {
                    //i("log", "\\_()")
                    synchronized(myThreadSurfaceHolder) {
                        draw(it)
                    }
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    myThreadSurfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawRGB(255, 255, 255)
        val sizeW = canvas.width / width
        val sizeH = canvas.height / height


        synchronized(gameState!!) {
            for (x in 0 until  width) {
                for (y in 0 until  height) {
                    if (gameState!!.permutation()[getId(x, y)] == emtyTileId())
                        continue
                    canvas.drawBitmap(
                            cache.getBitmapForId(gameState!!.permutation()[getId(x, y)]),
                            null,
                            Rect(sizeW * x, sizeH * y, sizeW * (x + 1), sizeH * (y + 1)),
                            null
                    )
                }
            }
        }
    }
}
