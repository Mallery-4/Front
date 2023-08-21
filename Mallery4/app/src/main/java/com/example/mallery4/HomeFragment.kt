package com.example.mallery4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mallery4.datamodel.AllAlbumResponse
import com.example.mallery4.datamodel.MypageResponse
import com.example.mallery4.recyclerview.MainItem
import com.example.mallery4.recyclerview.MainItemAdapter
import com.example.mallery4.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_decorate.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment() {

    //recycycler view list
    val MainItemList= arrayListOf<MainItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        RetrofitClient.afterinstance.getUserInfo(RetrofitClient.LoginUserId)
            .enqueue(object : retrofit2.Callback<MypageResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<MypageResponse>,
                    response: Response<MypageResponse>
                ) {
                    //mypage의 text를 user 정보로 저장
                    RetrofitClient.public_username = response.body()?.username.toString()

                    home_nickname.setText(response.body()?.username)

                }


                // 서버 오류 or 올바르지 않은 userid인 경우
                override fun onFailure(call: Call<MypageResponse>, t: Throwable) {

                }

            })
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 서버의 정보로 해당 유저가 속한 모든 그룹 정보 get +
        // 서버에서 받은 정보로, 해당 홈페이지에 정보 띄우기
        RetrofitClient.afterinstance.getAllAlbumInfo(RetrofitClient.LoginUserId)
            .enqueue(object : retrofit2.Callback<AllAlbumResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<AllAlbumResponse>,
                    response: Response<AllAlbumResponse>
                ) {
                    //homepage의 text를 user 정보로 저장
                    //home_nickname.setText(response.body()?.username.toString())

                    //recycler view 안의 객체 만들기 //main 작업시 주석 처리할 것
                   for (i in 0 until response.body()?.albums?.size!!){
                        var album_id= Gson().toJson(response.body()?.albums?.get(i)?.albumId).toLong()
                        var album_name=Gson().toJson(response.body()?.albums?.get(i)?.albumName).toString()
                        var album_count=Gson().toJson(response.body()?.albums?.get(i)?.memberCnt).toString()
                        var album_members=response.body()?.albums?.get(i)?.members?.joinToString(",").toString()
                        var album_nicknames=response.body()?.albums?.get(i)?.nicknames?.joinToString(",").toString()

                        //recyclerview 연결
                        MainItemList.add(MainItem(album_name,album_count,album_id,album_members,album_nicknames))

                        list_rv.layoutManager = LinearLayoutManager(context)
                        list_rv.setHasFixedSize(true)
                        list_rv.adapter= MainItemAdapter(MainItemList)
                    }

                }


                // 서버 오류 or 올바르지 않은 userid인 경우
                override fun onFailure(call: Call<AllAlbumResponse>, t: Throwable) {

                }

            })


        list_rv.layoutManager = LinearLayoutManager(context)
        list_rv.setHasFixedSize(true)
        list_rv.adapter= MainItemAdapter(MainItemList)

        btn_makegroup.setOnClickListener {
            (context as MainActivity).replaceFragment(MakeGroupFragment.newInstance()) //변경하는 페이지로 이동
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}