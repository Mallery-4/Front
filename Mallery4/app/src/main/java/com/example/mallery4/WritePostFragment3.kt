package com.example.mallery4

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.app.appsearch.SetSchemaRequest.READ_EXTERNAL_STORAGE
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color.red
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mallery4.datamodel.AddFriend
import com.example.mallery4.datamodel.AddFriendResponse
import com.example.mallery4.datamodel.PostWriteResponse
import com.example.mallery4.recyclerview.FriendItem
import com.example.mallery4.recyclerview.FriendItemAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.example.mallery4.viewpager.GalleryAdapter
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_write_post2.*
import kotlinx.android.synthetic.main.fragment_write_post3.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Part
import java.io.File
import java.io.FileInputStream


class WritePostFragment3 (groupname: String, groupcount: String, groupid: Long, groupmembers: String, groupnicknames:String, postdate: String, participants: List<String>) : Fragment(){

    var group_name = groupname
    var group_count = groupcount
    var group_id = groupid
    var group_members = groupmembers
    var group_nicknames = groupnicknames
    var post_date = postdate
    var participants_ = participants
    var imagesource: MutableList<MultipartBody.Part> = mutableListOf()

    // external storage 권한 확인받기
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )




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

        // 권한 확인
        val permission = activity?.let {
            ActivityCompat.checkSelfPermission(
                it,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        if (permission != PackageManager.PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }


        galleryAdapter = context?.let { GalleryAdapter(imageList, it) }!!

        photo_viewpager.adapter = galleryAdapter
        photo_viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // ViewPager의 Paging 방향은 Horizontal
        photo_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})

        // back 뒤로가기 버튼 클릭시, 이전 날짜 선택 post 화면으로 이동
        btn_post3_backhome.setOnClickListener {
            (context as MainActivity).Post2(group_name, group_count, group_id, group_members, group_nicknames, post_date)
        }

        // 사진 등록하기 버튼 클릭시 : 사진 여러장 선택해서 화면 띄우기
        add_photo_post.setOnClickListener {
            Toast.makeText(context, "사진을 길게 누르면, 여러장 선택이 가능합니다.", Toast.LENGTH_LONG).show()
            // 갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 멀티 선택 가능
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 500)

        }

        // 완료 버튼 클릭시: 지금까지의 post 모든 정보를 서버에 보내기
        btn_posting.setOnClickListener {


            // imagelist에 있는 uri -> 주소 절대경로로 변경
            for (i in 0 until imageList.size) {
                val imagePath = imageList[i]
                val file = File(absolutelyPath(requireContext(),imagePath))
                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                val body = MultipartBody.Part.createFormData("profile", file.name, requestFile)
                imagesource.add(body)
            }


            // 버튼 클릭시의 입력된 장소 정보를 서버로 보내면 된다.
            val place = post_place.text.toString().trim()
            Log.d("####################", group_id.toString())
            Log.d("####################", post_date)
            Log.d("####################", participants_.toString())
            Log.d("####################", place)
            Log.d("####################", RetrofitClient.LoginUserId)
            Log.d("####################", imageList.toString())
            Log.d("####################", imagesource.toString())

    /////////////////////////////////////////////
            // 서버로 파일 정보 전송하기
            sendImage(place)
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
    fun absolutelyPath(context : Context, path: Uri?): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    //웹서버로 이미지전송
    fun sendImage(place: String) {
        RetrofitClient.afterinstance.writeText(group_id,place,post_date,RetrofitClient.LoginUserId,participants_,imagesource)
            .enqueue(object : retrofit2.Callback<PostWriteResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<PostWriteResponse>,
                    response: Response<PostWriteResponse>
                ) {
                    // post 성공
                    if (response?.body()?.state.toString() == "200"){
                        Toast.makeText(context, "게시글을 등록하였습니다.", Toast.LENGTH_LONG).show()
                        // 화면 이동
                        //////////////////////////////////////////
                        (context as MainActivity).replaceFragment(HomeFragment.newInstance()) // 홈화면으로 이동
                    }
                    // post 실패
                    else{
                        Toast.makeText(context, "다시 한번 시도해주세요.", Toast.LENGTH_LONG).show()
                        response.let { it -> Log.d("###############", it.toString()) }
                    }
                }

                // 서버 오류
                override fun onFailure(call: Call<PostWriteResponse>, t: Throwable) {
                    t.message?.let { it1 -> Log.d("###############", it1) }
                }
            })
    }
}

