package com.wack.drawing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wack.drawing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var drawingView: DrawingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Brush의 두께 조절
        binding.drawingView.setSizeForBrush(20.toFloat())
    }
}