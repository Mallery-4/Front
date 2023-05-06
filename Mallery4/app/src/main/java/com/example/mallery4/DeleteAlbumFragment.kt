package com.example.mallery4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mallery4.datamodel.*
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_delete_album.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAlbumFragment (albumid: Long) : Fragment(){

    val album_id = albumid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 버튼 클릭시, home 화면으로 이동
        del_false.setOnClickListener {
            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
        }

        // 탈퇴하기 누르면,
        del_true.setOnClickListener {

            RetrofitClient.afterinstance.deletealbum(album_id, DeleteUser(RetrofitClient.LoginUserId))
                .enqueue(object: Callback<DeleteAlbumResponse> {

                    override fun onResponse(
                        call: Call<DeleteAlbumResponse>,
                        response: Response<DeleteAlbumResponse>
                    ) {

                        //탈퇴 성공
                        if (response.body()?.state.toString() == "200") {
                            Toast.makeText(context, "해당 그룹에서 탈퇴하였습니다.", Toast.LENGTH_SHORT).show()
                            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
                        }
                        else{
                            Toast.makeText(context, "다시 한번 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DeleteAlbumResponse>, t: Throwable) {}

                })
        }

    }
}