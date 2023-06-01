package com.example.mallery4

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mallery4.datamodel.getAllPostResponse
import com.example.mallery4.datamodel.getDetailPostResponse
import com.example.mallery4.recyclerview.PostItem
import com.example.mallery4.recyclerview.PostItemAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.example.mallery4.viewpager.DetailPostAdapter
import com.example.mallery4.viewpager.GalleryAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_detail_group.*
import kotlinx.android.synthetic.main.fragment_detail_post.*
import kotlinx.android.synthetic.main.fragment_write_post3.*
import retrofit2.Call
import retrofit2.Response
import java.net.URL

class DetailPostFragment (groupname: String, groupcount: String, groupmembers: String, groupnicknames: String, groupid: Long, postid: Long) : Fragment(){

    var group_name = groupname
    var group_count = groupcount
    var group_members = groupmembers
    var group_nicknames = groupnicknames

    var group_id = groupid
    var post_id = postid

    lateinit var detailPostAdapter: DetailPostAdapter
    var imageLists: ArrayList<String> = ArrayList()


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