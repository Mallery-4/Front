package com.example.mallery4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.mallery4.datamodel.CreateUser
import com.example.mallery4.datamodel.LoginResponse
import com.example.mallery4.datamodel.LoginUser
import com.example.mallery4.retrofit.RetrofitClient
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 버튼 클릭시 -> 메인 페이지 창으로 이동
        login_btn.setOnClickListener {
            val userId = login_id.text.toString().trim()
            val password = login_pw.text.toString().trim()

            // 필수로 입력 조건 걸기
            if (userId.isEmpty()){
                login_id.error = "ID Required"
                login_id.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                login_pw.error = "Password Required"
                login_pw.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(LoginUser(userId,password))
                .enqueue(object: retrofit2.Callback<LoginResponse> {

                    // 로그인 버튼 응답시,
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        //로그인에 성공했다면,
                        //Toast.makeText(applicationContext, response.body()?.state.toString(), Toast.LENGTH_SHORT).show()
                        //Toast.makeText(applicationContext, response.body()?.result.toString(), Toast.LENGTH_SHORT).show()

                        if (response.body()?.result.toString() == "success"){
                            Toast.makeText(applicationContext, "로그인 성공!", Toast.LENGTH_SHORT).show()
                            
                            // 로그인시 받는 access token으로 auth client 토큰, id 갱신
                            RetrofitClient.AFTER_AUTH = "Bearer " + response.body()?.Logindata?.accessToken.toString()
                            RetrofitClient.LoginUserId = response.body()?.userId.toString()

                            // 메인 화면으로 이동
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }else{
                            Toast.makeText(applicationContext, "다시 한번 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // 로그인 버튼 응답 실패시,
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "아이디, 패스워드를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        //Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                })

        }
        //회원가입 버튼 클릭시 -> 회원가입 창으로 이동
        signup_btn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
    }
}

