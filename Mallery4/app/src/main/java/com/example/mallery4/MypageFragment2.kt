package com.example.mallery4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.example.mallery4.datamodel.LoginResponse
import com.example.mallery4.datamodel.LoginUser
import com.example.mallery4.datamodel.MypageResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage2.*
import retrofit2.Call
import retrofit2.Response

class MypageFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mypage2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //mypage의 닉네임, 아이디는 수정 불가하므로 -> 기존 정보를 text user 정보로 저장
        mypage2_id.setText(RetrofitClient.public_id)
        mypage2_nickname.setText(RetrofitClient.public_username)




        // 프레그먼트 내의 버튼 클릭시 변경 완료 + 화면 이동
        change2_btn.setOnClickListener {

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 수정한 핸드폰 번호 -> 서버로 변경된 정보 보내기
            val update_hp = mypage2_hp.text.toString().trim()
            Toast.makeText(getActivity(), update_hp, Toast.LENGTH_LONG).show()
            (context as MainActivity).replaceFragment(MypageFragment.newInstance()) //마이페이지 기존걸로 이동
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MypageFragment2().apply {
                arguments = Bundle().apply {}
            }
    }
}