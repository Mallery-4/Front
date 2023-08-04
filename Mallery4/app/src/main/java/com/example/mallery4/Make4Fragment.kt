package com.example.mallery4

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_make4.*
import org.chromium.base.Log

class Make4Fragment : Fragment() {
    private var selectedImages: ArrayList<Uri>? = null
    private lateinit var cut4: LinearLayout // Make sure to replace this with the actual ID from your layout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_make4, container, false)
    }

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cut4 = view.findViewById(R.id.cut4)

        // 뒤로가기 버튼
        val back = view.findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 처음에 안보여야 함
        val cut4_frame2 = view.findViewById<LinearLayout>(R.id.cut4_frame2)
        cut4_frame2.visibility = View.GONE
        val cut4_frame1 = view.findViewById<LinearLayout>(R.id.cut4_frame1)
        cut4_frame1.visibility = View.VISIBLE

        val title1 = view.findViewById<TextView>(R.id.title1)
        title1.visibility = View.VISIBLE
        val title2 = view.findViewById<TextView>(R.id.title2)
        title2.visibility = View.GONE

        //프레임 선택
        val frame1 = view.findViewById<ImageView>(R.id.frame1)
        val frame2 = view.findViewById<ImageView>(R.id.frame2)

        frame1.setOnClickListener {
            frame1.setImageDrawable(requireContext().resources.getDrawable(R.drawable.frame12))
            frame2.setImageDrawable(requireContext().resources.getDrawable(R.drawable.frame2))
            cut4_frame1.visibility = View.VISIBLE
            cut4_frame2.visibility = View.GONE
            title1.visibility = View.VISIBLE
            title2.visibility = View.GONE
        }

        frame2.setOnClickListener {
            frame1.setImageDrawable(requireContext().resources.getDrawable(R.drawable.frame1))
            frame2.setImageDrawable(requireContext().resources.getDrawable(R.drawable.frame22))
            cut4_frame2.visibility = View.VISIBLE
            cut4_frame1.visibility = View.GONE
            title2.visibility = View.VISIBLE
            title1.visibility = View.GONE
        }

        val saveText = view?.findViewById<TextView>(R.id.save)
        saveText?.setOnClickListener {
            // 저장전 스티커 조절 없애야함
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.sticker_save)
            val drawableSticker = DrawableSticker(drawable)
            saveImage()

            resetState()
            val decorateFragment = DecorateFragment() // Instantiate your DecorateFragment
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, decorateFragment)
            fragmentTransaction.addToBackStack(null) // Optional: Add the transaction to the back stack
            fragmentTransaction.commit()
        }


        // 사진 가져오기
        val canvas = view.findViewById<LinearLayout>(R.id.cut4)
        val selectedImages = arguments?.getParcelableArrayList<Uri>("selectedImages")
        if (selectedImages != null && selectedImages.size == 4) {
            val imageViews = listOf(
                view.findViewById<ImageView>(R.id.cut4_1),
                view.findViewById<ImageView>(R.id.cut4_2),
                view.findViewById<ImageView>(R.id.cut4_3),
                view.findViewById<ImageView>(R.id.cut4_4),
                view.findViewById<ImageView>(R.id.cut4_2_1),
                view.findViewById<ImageView>(R.id.cut4_2_2),
                view.findViewById<ImageView>(R.id.cut4_2_3),
                view.findViewById<ImageView>(R.id.cut4_2_4),
                view.findViewById<ImageView>(R.id.cut4_22_1),
                view.findViewById<ImageView>(R.id.cut4_22_2),
                view.findViewById<ImageView>(R.id.cut4_22_3),
                view.findViewById<ImageView>(R.id.cut4_22_4)
            )
            if (selectedImages != null && selectedImages.size == 4) {
                for (i in 0 until imageViews.size) {
                    val uri = selectedImages[i % selectedImages.size] // 순환하여 사진을 가져옴
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageViews[i].setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun resetState() {
        // Reset any variables or state here
        selectedImages?.clear() // Clear the list of selected images
        // You might have other variables that need to be reset

        // Reset the visibility of any views if needed
        cut4.visibility = View.GONE // Hide the image preview
        cut4_1.visibility = View.GONE
        cut4_2.visibility = View.GONE
        cut4_3.visibility = View.GONE
        cut4_4.visibility = View.GONE

        val textView = view?.findViewById<TextView>(R.id.deco_done)
        textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.background)) // Reset text color
    }


    // Rest of your methods including saveImage() function

    private fun saveImage() {
        cut4.setDrawingCacheEnabled(true) // 캐쉬허용
        // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
        val screenshot = Bitmap.createBitmap(cut4.drawingCache)
        cut4.setDrawingCacheEnabled(false) // 캐쉬닫기


        // 이미지 저장 정보
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "my.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Mallery4")
            Log.e("접근","접근")
        }

        // 저장소에 이미지 저장
        val contentResolver = requireContext().contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        uri?.let {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                screenshot.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                Toast.makeText(requireContext(), "저장 성공!", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_SHORT).show()
        }

    }
}

