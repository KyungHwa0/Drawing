package com.wack.drawing

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.wack.drawing.databinding.ActivityMainBinding
import com.wack.drawing.databinding.DialogBrushSizeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawingView = binding.drawingView
        val ibBrush = binding.ibBrush
        val linearLayoutPaintColors = binding.llPaintColors

        mImageButtonCurrentPaint = linearLayoutPaintColors.getChildAt(3) as ImageButton
        mImageButtonCurrentPaint?.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pallet_pressed
            )
        )

        ibBrush.setOnClickListener {
            showBrushSizeSelectDialog()
        }

        drawingView?.setSizeForBrush(20.toFloat())
    }

    private fun showBrushSizeSelectDialog() {
        val brushDialog = Dialog(this)
        val dialogBinding = DialogBrushSizeBinding.inflate(layoutInflater)
        brushDialog.setContentView(dialogBinding.root)

        with(dialogBinding) {
            // Brush Size
            ibSmallBrush.setOnClickListener { setBrushSizeAndDismiss(10.toFloat(), brushDialog) }
            ibMediumBrush.setOnClickListener { setBrushSizeAndDismiss(20.toFloat(), brushDialog) }
            ibLargeBrush.setOnClickListener { setBrushSizeAndDismiss(30.toFloat(), brushDialog) }
        }

        brushDialog.setTitle("Brush size:")
        brushDialog.show()
    }

    private fun setBrushSizeAndDismiss(size: Float, dialog: Dialog) {
        drawingView?.setSizeForBrush(size)
        dialog.dismiss()
    }

    fun paintClicked(view: View) {
        if (view !== mImageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            // 이 태그는 색상을 교체 할 때 사용
            val colorTag = imageButton.tag.toString()
            // 태그에 따라 색상을  설정
            drawingView?.setColor(colorTag)
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed))
            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_normal
                )
            )
            mImageButtonCurrentPaint = view
        }
    }
}