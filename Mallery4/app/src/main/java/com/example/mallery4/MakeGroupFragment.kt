package com.example.mallery4

import android.icu.util.UniversalTimeScale.toLong
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mallery4.MakeGroupFragment.Companion.newInstance

import com.example.mallery4.datamodel.CreateAlbum
import com.example.mallery4.datamodel.CreateAlbumResponse
import com.example.mallery4.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_make_group.*
import retrofit2.Call
import retrofit2.Response
import kotlin.properties.Delegates

class MakeGroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_make_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // back 뒤로가기 버튼 클릭시, home 화면으로 이동
        btn_backtohome.setOnClickListener {
            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
        }

        // 완료 버튼 클릭시, 다음 화면으로 이동
        // 레트로핏으로 정보 post + 화면이동까지 이벤트 넣기
        newgroup_enroll_btn.setOnClickListener {

            // 그룹 이름 정보 -> 서버로 변경된 정보 보내기
            val albumname = write_groupname.text.toString().trim()

            // 필수로 입력 조건 걸기
            if (albumname.isEmpty()){
                write_groupname.error = "AlbumName is Required"
                write_groupname.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.afterinstance.createAlbum(CreateAlbum(albumname, RetrofitClient.LoginUserId))
                .enqueue(object : retrofit2.Callback<CreateAlbumResponse>{

                    // 올바른 응답이었을 경우
                    override fun onResponse(
                        call: Call<CreateAlbumResponse>,
                        response: Response<CreateAlbumResponse>
                    ) {

                        val albumid = Gson().toJson(response.body()?.albumId).toLong()
                        Toast.makeText(context, "그룹이 생성되었습니다.", Toast.LENGTH_LONG).show()
                        // 화면 이동
                        (context as MainActivity).MakingGroupFragment(albumid) //변경하는 페이지로 이동
                    }


                    // 서버 오류
                    override fun onFailure(call: Call<CreateAlbumResponse>, t: Throwable) {
                        t.message?.let { it1 -> Log.d("###############", it1) }

                    }

                })



        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MakeGroupFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}