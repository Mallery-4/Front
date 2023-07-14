package com.example.mallery4

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mallery4.datamodel.Comment
import com.example.mallery4.datamodel.CommentResponse
import com.example.mallery4.datamodel.getDetailPostResponse
import com.example.mallery4.recyclerview.CommentR
import com.example.mallery4.recyclerview.CommentAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.example.mallery4.viewpager.DetailPostAdapter
import kotlinx.android.synthetic.main.fragment_detail_post.*
import org.chromium.base.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailPostFragment (groupname: String, groupcount: String, groupmembers: String, groupnicknames: String, groupid: Long, postid: Long) : Fragment(){

    var group_name = groupname
    var group_count = groupcount
    var group_members = groupmembers
    var group_nicknames = groupnicknames

    var group_id = groupid
    var post_id = postid

    lateinit var detailPostAdapter: DetailPostAdapter
    var imageLists: ArrayList<String> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.commentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 댓글 데이터 생성
        val comments = listOf(
            CommentR("사용자1", "첫 번째 댓글", "10:00 AM"),
            CommentR("사용자2", "두 번째 댓글", "11:30 AM"),
            CommentR("사용자3", "세 번째 댓글", "12:45 PM")
        )

        // CommentAdapter 설정
        val adapter = CommentAdapter(comments)
        recyclerView.adapter = adapter

        //현재 시간
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy/mm/dd hh:mm")
        val getTime = dateFormat.format(date).toString()

        // 댓글 전송 버튼 클릭 이벤트 처리
        comment_btn.setOnClickListener {
            // EditText에서 댓글 내용 가져오기
            val commentText = comment_box.text.toString()

            // 댓글 데이터 생성
            val comment = Comment(post_id, "사용자",commentText)
            println(comment)

            // Retrofit을 사용하여 서버에 댓글 전송
            RetrofitClient.afterinstance.comment(Comment(post_id, "mall",commentText))
                .enqueue(object : retrofit2.Callback<CommentResponse> {
                override fun onResponse(
                    call: Call<CommentResponse>,
                    response: Response<CommentResponse>
                ) {
                    val commentResponse = response.body()
                    println(response.body()?.toString())

                    if (response.body()?.comment_text == commentText){


                        if (commentResponse?.comment_text == "200") {
                            val commentId = commentResponse?.comment_text
                            if (commentId != null) {
                                println("댓글 작성 성공! ID: $commentId")
                            }
                        }

                        else {
                            val errorMessage = response.errorBody()?.string()
                            println("댓글 작성 실패1: $errorMessage")
                        }
                    }

                    else if (response.body()?.comment_text.toString() == "200") {
                        println("ji")
                    }
                    else {
                        // 서버 응답이 실패한 경우 처리
                        val errorMessage = response.errorBody()?.string()
                        println("댓글 작성 실패2: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                    Log.e(TAG, "네트워크 요청 실패: ${t.message}")
                }


                })
        }





        // 뒤로가기 버튼 클릭시, 이전 화면으로 이동
        btn_detail_group_backhome.setOnClickListener {
            (context as MainActivity).MoveGroups (group_name, group_count, group_id, group_members, group_nicknames)
        }

        // 수정하기 버튼 클릭시, 수정 fragment로 이동
        ch_post.setOnClickListener {
            //(context as MainActivity).DeleteAlbum(group_id)
        }

        // 삭제하기 버튼 클릭시, 삭제 fragment로 이동
        del_post.setOnClickListener {
            //(context as MainActivity).ChangeAlbumName(group_id)
        }


        // 서버의 정보로 해당 앨범의 모든 post 정보 get +
        // 서버에서 받은 정보로, 해당 홈페이지에 정보 띄우기
        RetrofitClient.afterinstance.getDetailPostInfo(group_id, post_id)
            .enqueue(object : retrofit2.Callback<getDetailPostResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<getDetailPostResponse>,
                    response: Response<getDetailPostResponse>
                ) {

                    // response가 제대로 되었다면,
                    if (response.body()?.state.toString() == "200"){
                        // 서버에서 받은 정보로 화면에 정보 띄우기
                        post_date.setText(response.body()?.postDate.toString().trim('"'))
                        post_destination.setText(response.body()?.postLocation.toString().trim('"'))
                        post_members.setText(response.body()?.nicknames?.joinToString(",").toString().trim('"'))

                        for (i in 0 until response.body()?.imagePaths!!.size) {
                            val imageURL = response.body()?.imagePaths!![i].toString().trim('"')
                            imageLists.add(imageURL)
                        }


                        // 화면 이동 -> 시간지연 넣기
                        Handler(Looper.getMainLooper()).postDelayed({
                            // viewpager에 이미지 띄우기
                            detailPostAdapter = context?.let { DetailPostAdapter(imageLists, it) }!!
                            post_viewpager.adapter = detailPostAdapter
                            post_viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // ViewPager의 Paging 방향은 Horizontal
                            post_viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})
                        }, 1000)

                    }

                }

                // 서버 오류 or 올바르지 않은 경우
                override fun onFailure(call: Call<getDetailPostResponse>, t: Throwable) {}
            })
    }

}