package com.example.mallery4

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mallery4.datamodel.AllAlbumResponse
import com.example.mallery4.datamodel.DeleteAlbumResponse
import com.example.mallery4.datamodel.getAllPostResponse
import com.example.mallery4.recyclerview.MainItem
import com.example.mallery4.recyclerview.MainItemAdapter
import com.example.mallery4.recyclerview.PostItem
import com.example.mallery4.recyclerview.PostItemAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_detail_group.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_make_group2.*
import retrofit2.Call
import retrofit2.Response

class DetailGroupFragment (groupname: String, groupcount: String, groupid: String, groupmembers: String, groupnicknames:String) : Fragment(){

    var group_name = groupname
    var group_id = groupid.toLong()
    var group_count = groupcount
    var group_members = groupmembers
    var group_nicknames = groupnicknames

    //recycycler view list
    val PostItemList= arrayListOf<PostItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_detail_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 앞에서 클릭한 해당 detail group 정보로 화면 띄우기
        de_groupname.setText(group_name)
        de_cnt.setText(group_count)

        // 탈퇴하기 버튼 클릭시, 탈퇴 확인 fragment로 이동
        delete_btn.setOnClickListener {
            (context as MainActivity).DeleteAlbum(group_id)
        }

        // 그룹이름 변경하기 버튼 클릭시
        ch_nick.setOnClickListener {
            (context as MainActivity).ChangeAlbumName(group_id)
        }


        // 친구추가 버튼 클릭시, 친구추가하기 화면으로 이동
        ad_fri.setOnClickListener {
            (context as MainActivity).AddFriend(group_id)
        }

        // 게시물 추가하기 (floating button 클릭시), post의 날짜 캘린더뷰 fragment로 이동
        add_post.setOnClickListener { view ->
            (context as MainActivity).Post1(group_name, group_count, group_id, group_members, group_nicknames)
        }


        // 서버의 정보로 해당 앨범의 모든 post 정보 get +
        // 서버에서 받은 정보로, 해당 홈페이지에 정보 띄우기
        RetrofitClient.afterinstance.getAllPostInfo(group_id)
            .enqueue(object : retrofit2.Callback<getAllPostResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<getAllPostResponse>,
                    response: Response<getAllPostResponse>
                ) {

                    //recycler view 안의 객체 만들기
                    for (i in 0 until response.body()?.posts?.size!!){
                        var post_id= Gson().toJson(response.body()?.posts?.get(i)?.postId).toInt()
                        var post_date=Gson().toJson(response.body()?.posts?.get(i)?.postDate)
                        var post_img=Gson().toJson(response.body()?.posts?.get(i)?.mainImage)


                        //recyclerview 연결
                        PostItemList.add(PostItem(post_id,post_img,post_date))

                        grid_rv.layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
                        grid_rv.setHasFixedSize(true)
                        grid_rv.adapter= PostItemAdapter(PostItemList)
                    }

                }


                // 서버 오류 or 올바르지 않은 userid인 경우
                override fun onFailure(call: Call<getAllPostResponse>, t: Throwable) {

                }

            })


        grid_rv.layoutManager = GridLayoutManager(context,2)
        grid_rv.setHasFixedSize(true)
        grid_rv.adapter= PostItemAdapter(PostItemList)

    }

}