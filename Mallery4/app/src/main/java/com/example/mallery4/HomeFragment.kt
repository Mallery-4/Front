package com.example.mallery4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mallery4.datamodel.MypageResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 서버에서 받은 정보로, 해당 홈페이지에 정보 띄우기
        RetrofitClient.afterinstance.getUserInfo(RetrofitClient.LoginUserId)
            .enqueue(object : retrofit2.Callback<MypageResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<MypageResponse>,
                    response: Response<MypageResponse>
                ) {
                    //homepage의 text를 user 정보로 저장
                    RetrofitClient.public_id = response.body()?.userId.toString()
                    RetrofitClient.public_username = response.body()?.username.toString()
                    RetrofitClient.public_hp = response.body()?.phoneNumber.toString()

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