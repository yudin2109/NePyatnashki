package com.styudint.nepyatnashki.gameviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.Observer
import com.styudint.nepyatnashki.R
import com.styudint.nepyatnashki.data.AndroidGameState
import com.styudint.nepyatnashki.data.BitmapCache
import com.styudint.nepyatnashki.data.ImageHolder
import javax.inject.Inject

class GameView(private val mContext: Context, attrs: AttributeSet? = null) :
        SurfaceView(mContext, attrs) {

    private var mGameThread: GameDrawingThread? = null

    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    private val mSurfaceHolder: SurfaceHolder = holder

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
    }

    fun pause() {
        mGameThread!!.setRunning(false)
        try {
            mGameThread!!.join()
        } catch (e: InterruptedException) {
            print(e.stackTrace)
        }
    }

    fun resume() {
        mGameThread = GameDrawingThread(mSurfaceHolder, context)
        mGameThread!!.setRunning(true)
        mGameThread!!.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Set coordinates of flashlight cone.
                mGameThread!!.onTileClick(
                        (x / (mViewWidth / mGameThread!!.width)).toInt(),
                        (y / (mViewHeight / mGameThread!!.height)).toInt()
                )
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                invalidate()
            }
        }
        return true
    }

    fun restart() {

    }
}