package com.cse.coari.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class RoundImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    private var radius = 12.0f // 값이 클수록 더욱 둥글게 됩니다.

    fun setRadius(radius:Float){
        this.radius = radius
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val path = Path() // 1)
        val rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat()) // 2)
        path.addRoundRect(rect, radius, radius, Path.Direction.CW) // 3)
        canvas!!.clipPath(path) // 4)
        super.onDraw(canvas)
    }
}