package com.example.mallery4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.mallery4.datamodel.AlreadyInUserID
import com.example.mallery4.datamodel.CreateUser
import com.example.mallery4.datamodel.DefaultResponse
import com.example.mallery4.datamodel.IdCheck
import com.example.mallery4.retrofit.RetrofitClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //ID 중복확인 버튼 클릭시
        check_btn.setOnClickListener {

            var id = signup_id.text.toString().trim()

            // 필수로 입력 조건 걸기
            if (id.isEmpty()){
                signup_id.error = "ID Required"
                signup_id.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.alreadyInUserID(IdCheck(id))
                .enqueue(object: Callback<AlreadyInUserID>{

                    override fun onResponse(
                        call: Call<AlreadyInUserID>,
                        response: Response<AlreadyInUserID>
                    ) {

                        // 중복 ID 존재 X
                        if (Gson().toJson(response.body()?.isSuccess).toBoolean()){
                            //Toast.makeText(applicationContext,response.body()?.code, Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext,"사용 가능한 ID 입니다.", Toast.LENGTH_SHORT).show()

                        }else{  // 중복 ID 존재O
                            //Log.d("Is Match?", Gson().toJson(response.body()?.isSuccess).toString())
                            //Toast.makeText(applicationContext,Gson().toJson(response.body()?.code).toString(), Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext,"이미 존재하는 사용자 ID 입니다.", Toast.LENGTH_SHORT).show()
                            signup_id.setText("") //빈칸으로 초기화
                        }
                    }

                    override fun onFailure(call: Call<AlreadyInUserID>, t: Throwable) {}

                })
        }

        // 비밀번호 조건에 만족시 체크표시 이벤트
        val pwpattern = "^[A-Za-z0-9]{6,12}$" // 영문+숫자 포함의 6~12자리 이내
        val pattern = Pattern.compile(pwpattern) // 패턴 컴파일
        signup_pw.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 정규식 패턴 조건대로 pw 입력시,
                if (pattern.matcher(signup_pw.text.toString().trim()).find().toString() == "true"){
                    setImage.setImageResource(R.drawable.pw_approve)
                    //Log.d("Is Match?", pattern.matcher(signup_pw.text.toString().trim()).find().toString())
                }else{
                    setImage.setImageResource(R.drawable.pw_cancel)
                    //Log.d("Is Match?", pattern.matcher(signup_pw.text.toString().trim()).find().toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


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

            RetrofitClient.instance.createUser(CreateUser(id, pw, hp, nick))
                .enqueue(object: Callback<DefaultResponse>{

                    // 회원가입 성공시,
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        Toast.makeText(applicationContext,"회원가입 완료!", Toast.LENGTH_SHORT).show()
                        // 로그인 화면으로 이동
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    }

                    // 회원가입 실패시,
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }
}