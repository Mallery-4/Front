package com.example.mallery4

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mallery4.datamodel.*
import com.example.mallery4.recyclerview.PostItem
import com.example.mallery4.recyclerview.PostItemAdapter
import com.example.mallery4.recyclerview.CommentAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.example.mallery4.retrofit.RetrofitClient.LoginUserId
import com.example.mallery4.viewpager.DetailPostAdapter
import kotlinx.android.synthetic.main.fragment_detail_post.*
import org.chromium.base.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        // 뒤로가기 버튼 클릭시, 이전 화면으로 이동
        btn_detail_group_backhome.setOnClickListener {
            (context as MainActivity).MoveGroups (group_name, group_count, group_id, group_members, group_nicknames)
        }
///////////////////////////////////////
        ///////////////////////////////////////////////////
        // 수정하기 버튼 클릭시, 수정 fragment로 이동
        ch_post.setOnClickListener {
            ///(context as MainActivity).PutDetailPage(group_name,group_count, group_id, post_id,group_members, group_nicknames, post_date.text.toString(), post_members.text.split(",").toList())
        }
        ////////////////////////////////////////


        // 삭제하기 버튼 클릭시, 삭제후 홈으로 이동
        del_post.setOnClickListener {
            RetrofitClient.afterinstance.deleteText(group_id, post_id)
                .enqueue(object: retrofit2.Callback<DeleteWriteResponse> {

                    override fun onResponse(
                        call: Call<DeleteWriteResponse>,
                        response: Response<DeleteWriteResponse>
                    ) {
                        // response가 제대로 되었다면,
                        if (response.body()?.result=="success") {
                            Toast.makeText(context, "해당 게시물을 삭제하였습니다.", Toast.LENGTH_LONG).show()
                            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
                        }
                        else{
                            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteWriteResponse>, t: Throwable) {}

                })
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


        // 댓글 전송 버튼 클릭 이벤트 처리
        comment_btn.setOnClickListener {
            // EditText에서 댓글 내용 가져오기
            val commentText = comment_box.text.toString()

            // Retrofit을 사용하여 서버에 댓글 전송
            RetrofitClient.afterinstance.comment(Comment(post_id, LoginUserId,commentText))
                .enqueue(object : Callback<CommentResponse> {
                    override fun onResponse(
                        call: Call<CommentResponse>,
                        response: Response<CommentResponse>
                    ) {
                        if (response.body()?.comment_text == commentText) {
                            Toast.makeText(context, "댓글 작성 완료! ", Toast.LENGTH_LONG).show()
                            refreshComments()
                        }

                        else {
                            // 서버 응답이 실패한 경우 처리
                            val errorMessage = response.errorBody()?.string()
                            println("댓글 작성 실패: $errorMessage")
                        }
                    }

                    override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                        Log.e(TAG, "네트워크 요청 실패: ${t.message}")
                    }
                })
            comment_box.setText("")//댓글 박스 초기화
        }

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.commentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // 댓글 불러오기
        RetrofitClient.afterinstance.getCommentInfo(post_id)
            .enqueue(object : retrofit2.Callback<getCommentInfoResponse>{
                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<getCommentInfoResponse>,
                    response: Response<getCommentInfoResponse>
                ) {

                    // response가 제대로 되었다면,
                    val commentsList = response.body()?.comments

                    if (response.body()?.result.toString() == "success"){
                        // 서버에서 받은 정보로 화면에 정보 띄우기
                        if (!commentsList.isNullOrEmpty()) {
                            for (comment in commentsList) {
                                // CommentAdapter 설정
                                val adapter = CommentAdapter(commentsList)
                                recyclerView.adapter = adapter

                            }
                        }

                    }
                }

                // 서버 오류 or 올바르지 않은 경우
                override fun onFailure(call: Call<getCommentInfoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun refreshComments() {
        RetrofitClient.afterinstance.getCommentInfo(post_id).enqueue(object : retrofit2.Callback<getCommentInfoResponse> {
            override fun onResponse(call: Call<getCommentInfoResponse>, response: Response<getCommentInfoResponse>) {
                if (response.isSuccessful && response.body()?.result == "success") {
                    val commentsList = response.body()?.comments
                    if (!commentsList.isNullOrEmpty()) {
                        // CommentAdapter에 새로운 댓글 목록을 설정합니다.
                        val adapter = CommentAdapter(commentsList)
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<getCommentInfoResponse>, t: Throwable) {
                // 실패 처리를 수행합니다.
            }
        })
    }

}