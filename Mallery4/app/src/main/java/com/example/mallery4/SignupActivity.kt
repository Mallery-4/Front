package com.example.mallery4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mallery4.datamodel.DefaultResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        

        //회원가입 버튼 클릭시
        enroll_btn.setOnClickListener {

            val id = signup_id.text.toString().trim()
            val pw = signup_pw.text.toString().trim()
            val hp = signup_hp.text.toString().trim()
            val nick = signup_nickname.text.toString().trim()

            // 필수로 입력 조건 걸기
            if (id.isEmpty()){
                signup_id.error = "ID Required"
                signup_id.requestFocus()
                return@setOnClickListener
            }
            if (pw.isEmpty()){
                signup_pw.error = "Password Required"
                signup_pw.requestFocus()
                return@setOnClickListener
            }
            /*
            if (hp.isEmpty()){
                signup_hp.error = "Phonenumber Required"
                signup_hp.requestFocus()
                return@setOnClickListener
            }

             */
            if (nick.isEmpty()){
                signup_nickname.error = "Nickname Required"
                signup_nickname.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.createUser(id, pw, hp, nick)
                .enqueue(object: Callback<DefaultResponse>{

                    // 회원가입 성공시,
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        Toast.makeText(applicationContext,"회원가입 완료!", Toast.LENGTH_SHORT).show()
                    }

                    // 회원가입 실패시,
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }
}