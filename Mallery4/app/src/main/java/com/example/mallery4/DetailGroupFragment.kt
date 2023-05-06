package com.example.mallery4

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mallery4.datamodel.AllAlbumResponse
import com.example.mallery4.datamodel.DeleteAlbumResponse
import com.example.mallery4.recyclerview.MainItem
import com.example.mallery4.recyclerview.MainItemAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_detail_group.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_make_group2.*
import retrofit2.Call
import retrofit2.Response

class DetailGroupFragment (groupname: String, groupcount: String, groupid: String, groupmembers: String) : Fragment(){

    var group_name = groupname
    var group_id = groupid.toLong()
    var group_count = groupcount
    var group_members = groupmembers

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

        // 닉네임 변경하기 버튼 클릭시
        /*
        ch_nick.setOnClickListener {
            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
        }

         */

        // 친구추가 버튼 클릭시, 친구추가하기 화면으로 이동
        ad_fri.setOnClickListener {
            (context as MainActivity).AddFriend(group_id)
        }

    }

}