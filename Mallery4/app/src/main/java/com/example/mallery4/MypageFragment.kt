package com.example.mallery4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.core.view.children
import com.example.mallery4.datamodel.LoginResponse
import com.example.mallery4.datamodel.LoginUser
import com.example.mallery4.datamodel.MypageResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Response

class MypageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Log.d("###############",RetrofitClient.LoginUserId.toString())
        //Log.d("###############",RetrofitClient.AFTER_AUTH.toString())

        // 서버에서 받은 정보로, 해당 마이페이지에 정보 띄우기
        RetrofitClient.afterinstance.getUserInfo(RetrofitClient.LoginUserId)
            .enqueue(object : retrofit2.Callback<MypageResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<MypageResponse>,
                    response: Response<MypageResponse>
                ) {
                    //mypage의 text를 user 정보로 저장
                    RetrofitClient.public_id = response.body()?.userId.toString()
                    RetrofitClient.public_username = response.body()?.username.toString()
                    RetrofitClient.public_hp = response.body()?.phoneNumber.toString()

                    mypage_id.setText(response.body()?.userId)
                    mypage_nickname.setText(response.body()?.username)
                    mypage_hp.setText(response.body()?.phoneNumber)
                }


                // 서버 오류 or 올바르지 않은 userid인 경우
                override fun onFailure(call: Call<MypageResponse>, t: Throwable) {

                }

            })
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        change_btn.setOnClickListener {
            (context as MainActivity).replaceFragment(MypageFragment2.newInstance()) //변경하는 페이지로 이동
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MypageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}