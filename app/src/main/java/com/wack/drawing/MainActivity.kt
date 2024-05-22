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
            val colorTag = imageButton.tag.toString()

            // drawingView 색상 설정
            drawingView?.setColor(colorTag)

            // 현재 이미지 버튼의 배경을 선택된 배경으로 변경
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_pressed
                )
            )

            // 이전 이미지 버튼의 배경을 원래대로 변경
            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pallet_normal
                )
            )

            // mImageButtonCurrentPaint 업데이트
            mImageButtonCurrentPaint = imageButton
        }
    }
}