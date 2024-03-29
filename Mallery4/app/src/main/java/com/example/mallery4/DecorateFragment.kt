package com.example.mallery4

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mallery4.datamodel.MypageResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_decorate.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Response

class DecorateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // 서버에서 받은 정보로, 해당 마이페이지에 정보 띄우기
       RetrofitClient.afterinstance.getUserInfo(RetrofitClient.LoginUserId)
            .enqueue(object : retrofit2.Callback<MypageResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<MypageResponse>,
                    response: Response<MypageResponse>
                ) {
                    //mypage의 text를 user 정보로 저장
                    RetrofitClient.public_username = response.body()?.username.toString()

                    decorate_nickname.setText(response.body()?.username)

                }


                // 서버 오류 or 올바르지 않은 userid인 경우
                override fun onFailure(call: Call<MypageResponse>, t: Throwable) {

                }

            })

        val view = inflater.inflate(R.layout.fragment_decorate, container, false)


        val decoLayout = view.findViewById<View>(R.id.deco_layout)
        decoLayout.setOnClickListener {
            val fragment = DecoFragment() // Instantiate your Cut4Fragment
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Add the transaction to the back stack
            fragmentTransaction.commit()
        }

        val cut4Layout = view.findViewById<View>(R.id.cut4_layout)
        cut4Layout.setOnClickListener {
            val fragment = Cut4Fragment() // Instantiate your Cut4Fragment
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Add the transaction to the back stack
            fragmentTransaction.commit()
        }
        return view


    }



    companion object {
        @JvmStatic
        fun newInstance() =
            DecorateFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}