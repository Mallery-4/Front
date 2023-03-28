package com.example.mallery4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.mallery4.datamodel.DefaultResponse
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

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