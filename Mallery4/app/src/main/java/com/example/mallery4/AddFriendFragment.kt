package com.example.mallery4

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mallery4.datamodel.*
import com.example.mallery4.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_friend.*
import kotlinx.android.synthetic.main.fragment_make_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendFragment(albumid: Long) : Fragment(){

    val album_id = albumid
    var friend_id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 친구의 id를 추가할 수 있는지 여부 확인하는 flag
        var isPossible = false

        // back 뒤로가기 버튼 클릭시, home 화면으로 이동
        btn_ad_backhome.setOnClickListener {
            (context as MainActivity).replaceFragment(HomeFragment.newInstance())
        }

        // 추가하려는 친구의 id 현재 앱 상의 사용자인지 확인
        fri_check.setOnClickListener {

            friend_id = fri_id.text.toString().trim()

            // 필수로 입력 조건 걸기
            if (friend_id.isEmpty()){
                fri_id.error = "ID Required"
                fri_id.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.afterinstance.alreadyInUserID(IdCheck(friend_id))
                .enqueue(object: Callback<AlreadyInUserID> {

                    override fun onResponse(
                        call: Call<AlreadyInUserID>,
                        response: Response<AlreadyInUserID>
                    ) {

                        // 친구의 ID가 서버상 존재 X
                        if (Gson().toJson(response.body()?.isSuccess).toBoolean()){
                            //Toast.makeText(applicationContext,response.body()?.code, Toast.LENGTH_SHORT).show()
                            Toast.makeText(context,"입력한 친구의 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                            isPossible = false

                        }else{  // 친구의 ID가 서버상 존재 O
                            //Log.d("Is Match?", Gson().toJson(response.body()?.isSuccess).toString())
                            //Toast.makeText(applicationContext,Gson().toJson(response.body()?.code).toString(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(context,"친구 추가가 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                            isPossible = true
                        }
                    }

                    override fun onFailure(call: Call<AlreadyInUserID>, t: Throwable) {}

                })
        }

        // 완료 버튼 클릭시, 다음 화면으로 이동
        // 레트로핏으로 정보 put + 화면이동까지 이벤트 넣기
        fri_enroll.setOnClickListener {

            // 친구 아이디 정보 -> 서버로 변경된 정보 보내기
            // 중복확인 후 결과가 가능한 아이디였을 경우,
            if (isPossible){

                // album_id과 userId로 해당 앨범에 친구 추가하기
                RetrofitClient.afterinstance.addFriend(AddFriend(album_id,friend_id))
                    .enqueue(object : retrofit2.Callback<AddFriendResponse>{

                        // 올바른 응답이었을 경우
                        override fun onResponse(
                            call: Call<AddFriendResponse>,
                            response: Response<AddFriendResponse>
                        ) {
                            // 친구 추가 성공
                            if (response?.body()?.state.toString() == "200"){
                                Toast.makeText(context, "친구를 추가하였습니다.", Toast.LENGTH_LONG).show()
                                // 화면 이동
                                (context as MainActivity).replaceFragment(HomeFragment.newInstance()) // 홈화면으로 이동
                            }
                            // 이미 존재하는 사용자거나, 없는 사용자 아이디인 경우
                            else{
                                Toast.makeText(context, album_id.toString(), Toast.LENGTH_LONG).show()
                                Toast.makeText(context, response?.body()?.state.toString(), Toast.LENGTH_LONG).show()
                                Toast.makeText(context, "해당 친구 아이디를 다시 확인해주세요.", Toast.LENGTH_LONG).show()
                                //response.let { it -> Log.d("###############", it.toString()) }
                            }
                        }

                        // 서버 오류
                        override fun onFailure(call: Call<AddFriendResponse>, t: Throwable) {
                        }
                    })

            }

            // 중복확인 안했거나, 사용불가능한 아이디였을 경우
            else {
                Toast.makeText(context,"친구 아이디 중복확인을 먼저 확인해주세요.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}