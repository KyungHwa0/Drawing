package com.wack.drawing

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.wack.drawing.databinding.ActivityMainBinding
import com.wack.drawing.databinding.DialogBrushSizeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageBackground = binding.ivBackground
                imageBackground.setImageURI(result.data?.data)
            }
        }

    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val perMissionName = it.key
                val isGranted = it.value
                //권환 부여시 Toast 표시 후 실행
                if (isGranted) {
                    Toast.makeText(
                        this@MainActivity,
                        "권한 부여",
                        Toast.LENGTH_LONG
                    ).show()

                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                } else {

                    if (perMissionName == Manifest.permission.READ_EXTERNAL_STORAGE)
                        Toast.makeText(
                            this@MainActivity,
                            "권한 거부",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawingView = binding.drawingView
        val ibBrush = binding.ibBrush
        val linearLayoutPaintColors = binding.llPaintColors
        val ibGallery = binding.ibGallery

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

        ibGallery.setOnClickListener {
            requestStoragePermission()
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

    private fun requestStoragePermission(){
        // 권한 체크
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_MEDIA_IMAGES)
        ){
            //권한 요청에 대한 이유 다이얼로그
            showRationaleDialog("Kids Drawing App","이 앱은 외부 저장소에 권한이 필요합니다.")
        }
        else {
            // 권한 요청
            requestPermission.launch(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            )
        }

    }

    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
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