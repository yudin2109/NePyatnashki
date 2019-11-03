package com.styudint.nepyatnashki.gameviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log.i
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(private val mContext: Context, attrs: AttributeSet? = null) :
        SurfaceView(mContext, attrs), SurfaceHolder.Callback {

    private var mGameThread: GameDrawingThread? = null

    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    private val mSurfaceHolder: SurfaceHolder = holder

    init {
        mSurfaceHolder.addCallback(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
    }

    fun pause() {
        mGameThread!!.setRunning(false)
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

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        resume()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        i("log", "PAUSE|PAUSE|PAUSE|PAUSE|PAUSE|PAUSE")
        pause()
        var retry: Boolean = true
        while (retry) {
            try {
                mGameThread!!.join()
                retry = false
            } catch (e: InterruptedException) {
                print(e.stackTrace)
            }
        }
    }
}