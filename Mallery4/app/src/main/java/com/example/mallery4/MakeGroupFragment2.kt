package com.example.mallery4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mallery4.MakeGroupFragment.Companion.newInstance

import com.example.mallery4.datamodel.CreateAlbum
import com.example.mallery4.datamodel.CreateAlbumResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_make_group.*
import kotlinx.android.synthetic.main.fragment_make_group2.*
import retrofit2.Call
import retrofit2.Response

class MakeGroupFragment2(albumid: Long) : Fragment(){

    val album_id = albumid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_make_group2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 나중에 버튼 클릭시, home 화면으로 이동
        btn_later.setOnClickListener {
            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
        }

        // 친구추가 버튼 클릭시, 친구추가하기 화면으로 이동
        btn_addfriend.setOnClickListener {
            (context as MainActivity).AddFriend(album_id)
        }

    }

}