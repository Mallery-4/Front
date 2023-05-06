package com.example.mallery4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mallery4.datamodel.*
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_change_album_name.*
import retrofit2.Call
import retrofit2.Response

class ChangeAlbumNameFragment(albumid: Long) : Fragment(){

    val album_id = albumid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_album_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 취소하기 버튼 클릭시, 홈화면으로 이동
        nch_name.setOnClickListener {
            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
        }


        // 완료 버튼 클릭시,
        // 레트로핏으로 정보 put + 화면이동까지 이벤트 넣기
        ch_name.setOnClickListener {

            var ch_gpname = renew_gpname.text.toString().trim()

            RetrofitClient.afterinstance.updateAlbumName(album_id,UpdateAlbumname(ch_gpname))
                .enqueue(object : retrofit2.Callback<CreateAlbumResponse>{

                    // 올바른 응답이었을 경우
                    override fun onResponse(
                        call: Call<CreateAlbumResponse>,
                        response: Response<CreateAlbumResponse>
                    ) {

                        if (response?.body()?.state.toString() == "200"){
                            Toast.makeText(context, "그룹 이름을 변경하였습니다.", Toast.LENGTH_LONG).show()
                            // 화면 이동
                            (context as MainActivity).replaceFragment(HomeFragment.newInstance()) // 홈화면으로 이동
                        }
                    }

                    // 서버 오류
                    override fun onFailure(call: Call<CreateAlbumResponse>, t: Throwable) {
                    }
                })
        }
    }
}