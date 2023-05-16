package com.example.mallery4

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color.red
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mallery4.recyclerview.FriendItem
import com.example.mallery4.recyclerview.FriendItemAdapter
import com.example.mallery4.viewpager.GalleryAdapter
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_write_post2.*
import kotlinx.android.synthetic.main.fragment_write_post3.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class WritePostFragment3 (groupname: String, groupcount: String, groupid: Long, groupmembers: String, postdate: String, participants: List<String>) : Fragment(){

    var group_name = groupname
    var group_count = groupcount
    var group_id = groupid
    var group_members = groupmembers
    var post_date = postdate
    var participants_ = participants
    var imagesource: MutableList<String> = mutableListOf()

    lateinit var galleryAdapter: GalleryAdapter
    var imageList: ArrayList<Uri> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_post3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryAdapter = context?.let { GalleryAdapter(imageList, it) }!!

        photo_viewpager.adapter = galleryAdapter
        photo_viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // ViewPager의 Paging 방향은 Horizontal
        photo_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        })

        // back 뒤로가기 버튼 클릭시, 이전 날짜 선택 post 화면으로 이동
        btn_post3_backhome.setOnClickListener {
            (context as MainActivity).Post2(group_name, group_count, group_id, group_members, post_date)
        }

        // 사진 등록하기 버튼 클릭시 : 사진 여러장 선택해서 화면 띄우기
        add_photo_post.setOnClickListener {
            Toast.makeText(context, "사진을 길게 누르면, 여러장 선택이 가능합니다.", Toast.LENGTH_LONG).show()
            // 갤러리 호출
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 멀티 선택 가능
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 500)

        }

        //////////////
        //////////////
        // 완료 버튼 클릭시: 지금까지의 post 모든 정보를 서버에 보내기
        btn_posting.setOnClickListener {

            // imagelist에 있는 uri -> 주소 절대경로로 변경
            for (i in 0 until imageList.size) {
                val imagePath = imageList[i]

                val file = File(absolutelyPath(imagePath))
                val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
                val body = MultipartBody.Part.createFormData("profile", file.name, requestFile)

                imagesource.add(body.toString())
            }
            // 버튼 클릭시의 입력된 장소 정보를 서버로 보내면 된다.
            val place = post_place.text.toString().trim()
            Log.d("####################", post_date)
            Log.d("####################", participants_.toString())
            Log.d("####################", place)
            Log.d("####################", imageList.toString())
            Log.d("####################", imagesource.toString())

            //////////////
            //////////////
            // 서버로 정보 전송하기
            //send()
        }

    }

    // 갤러리 다중 이미지 선택:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 500) {
            if (data?.clipData != null) {
                val count = data?.clipData!!.itemCount

                if (count > 10) {
                    Toast.makeText(context, "사진은 최대 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show()
                    return
                }

                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imageList.add(imageUri)
                }
            } else {
                data?.data?.let { uri ->
                    val imageUri: Uri? = data?.data

                    if (imageUri != null) {
                        imageList.add(imageUri)
                    }
                }
            }

            galleryAdapter.notifyDataSetChanged()
        }

    }

    // 절대경로 변환

    fun absolutelyPath(uri: Uri): String {
        var columnIndex = 0
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = context?.contentResolver?.query(uri, proj, null, null, null)
        if (cursor?.moveToFirst() == true) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor?.getString(columnIndex).toString()
    }


}