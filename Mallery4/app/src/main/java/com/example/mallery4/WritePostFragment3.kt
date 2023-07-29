package com.example.mallery4

import android.Manifest.permission.*
import android.app.Activity.RESULT_OK
import android.app.appsearch.SetSchemaRequest.READ_EXTERNAL_STORAGE
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color.red
import android.graphics.Matrix
import android.net.Uri
import android.os.*
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
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
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
import java.io.*
import android.media.ExifInterface

class WritePostFragment3 (groupname: String, groupcount: String, groupid: Long, groupmembers: String, groupnicknames:String, postdate: String, participants: List<String>) : Fragment(){

    var group_name = groupname
    var group_count = groupcount
    var group_id = groupid
    var group_members = groupmembers
    var group_nicknames = groupnicknames
    var post_date = postdate
    var participants_ = participants
    var imagesource: MutableList<MultipartBody.Part> = mutableListOf()

    val map = HashMap<String, RequestBody>()

    // external storage 권한 확인받기
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        android.Manifest.permission.READ_MEDIA_VIDEO,
        android.Manifest.permission.READ_MEDIA_AUDIO,
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.POST_NOTIFICATIONS
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

            // 절대경로 변경시 시간지연
            val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
            // 권한이 허용되어 있는지 확인
            val granted = ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
            if (granted) {
                // 권한이 허용된 경우에 수행할 작업
                // imagelist에 있는 uri -> 주소 절대경로로 변경
                for (i in 0 until imageList.size) {

                    val imagePath = imageList[i]

                    val file: File = File(imagePath.path)


                    val absolutePath = file.absolutePath //사진의 절대경로
                    // 사진 회전 방지용 메타데이터
                    var exif: ExifInterface? = null
                    try {
                        exif = ExifInterface(absolutePath)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val orientation = exif?.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_ROTATE_90
                    ) ?: ExifInterface.ORIENTATION_ROTATE_90


                    val inputStream: InputStream? =
                        try {
                            requireContext().contentResolver.openInputStream(imagePath)
                        } catch (e: IOException) {
                            e.printStackTrace()
                            null
                        }

                    // 사진 압축
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                    //사진회전
                    val bmRotated: Bitmap? = bitmap?.let { it1 -> rotateBitmap(it1, orientation) }

                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bmRotated?.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
                    val requestBody = RequestBody.create(MediaType.parse("image/*"), byteArrayOutputStream.toByteArray())
                    val uploadFile = MultipartBody.Part.createFormData("images", file.name, requestBody)
                    imagesource.add(uploadFile)


                }
            } else {
                // 권한이 허용되지 않은 경우, 권한 요청을 진행해야 함
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                    )
                }
            }

            // 버튼 클릭시의 입력된 장소 정보를 서버로 보내면 된다.
            val place = post_place.text.toString().trim()

            // Hashmap data
            var GroupId : RequestBody = RequestBody.create(MediaType.parse("text/plain"), group_id.toString())
            var PostLocation : RequestBody  = RequestBody.create(MediaType.parse("text/plain"),place)
            var PostDate : RequestBody = RequestBody.create(MediaType.parse("text/plain"),post_date)
            var UserId : RequestBody = RequestBody.create(MediaType.parse("text/plain"), RetrofitClient.LoginUserId)
            var Participants : RequestBody = RequestBody.create(MediaType.parse("text/plain"),participants_.joinToString(separator = ","))


            map["albumId"] = GroupId
            map["postLocation"] = PostLocation
            map["postDate"] = PostDate
            map["userId"] = UserId
            map["participants"] = Participants

            // 서버로 파일 정보 전송하기
            sendImage(place)
        }

    }

    // 갤러리 다중 이미지 선택:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageList= ArrayList() //기존 올려놨던 사진 초기화

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
            galleryAdapter = context?.let { GalleryAdapter(imageList, it) }!!

            photo_viewpager.adapter = galleryAdapter
            photo_viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // ViewPager의 Paging 방향은 Horizontal
            photo_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})
        }

    }

    //웹서버로 이미지전송
    fun sendImage(place: String) {
        RetrofitClient.afterinstance.writeText(/*group_id,place,post_date,RetrofitClient.LoginUserId,participants_,*/ map,imagesource)
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
                        (context as MainActivity).replaceFragment(HomeFragment.newInstance()) // 홈화면으로 이동
                        response.let { it -> Log.d("###############", response.body()?.imagePaths.toString()) }
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

    //사진 회전 함수
    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            bitmap // OutOfMemoryError가 발생할 경우 원본 비트맵을 반환합니다.
        }
    }
}