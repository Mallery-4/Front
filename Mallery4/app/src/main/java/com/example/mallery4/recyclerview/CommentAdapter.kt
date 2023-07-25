package com.example.mallery4.recyclerview

import android.content.ContentValues
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.mallery4.R
import com.example.mallery4.datamodel.*
import com.example.mallery4.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_decorate.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mallery4.retrofit.RetrofitClient.LoginUserId
import kotlinx.android.synthetic.main.fragment_detail_post.*
import org.chromium.base.Log


class CommentAdapter(private val comments: List<CommentRes>?) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.comment_name)
        val commentTextView: TextView = itemView.findViewById(R.id.comment_text)
        private val timeTextView: TextView = itemView.findViewById(R.id.comment_date)
        val comment_erase: ImageView = itemView.findViewById(R.id.comment_erase)
        val comment_edit: ImageView = itemView.findViewById(R.id.comment_edit)
        val comment_edit_text: EditText = itemView.findViewById(R.id.comment_edit_text)
        val comment_edit_btn: AppCompatButton = itemView.findViewById(R.id.comment_edit_btn)

        fun bind(comment: CommentRes) {
            userNameTextView.text = comment.writer
            commentTextView.text = comment.content
            timeTextView.text = comment.date
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments?.get(position)
        if (comment != null) {
            holder.bind(comment)
        }

        //사용자 name 가져오기
        RetrofitClient.afterinstance.getUserInfo(RetrofitClient.LoginUserId)
            .enqueue(object : retrofit2.Callback<MypageResponse>{

                // 올바른 응답이었을 경우
                override fun onResponse(
                    call: Call<MypageResponse>,
                    response: Response<MypageResponse>
                ) {
                    //mypage의 text를 user 정보로 저장
                    RetrofitClient.public_username = response.body()?.username.toString()
                    val writer = comment?.writer
                    val user_id=RetrofitClient.public_username
                    if(writer!=user_id) {
                        holder.comment_erase.visibility = View.INVISIBLE
                    }

                }


                // 서버 오류 or 올바르지 않은 userid인 경우
                override fun onFailure(call: Call<MypageResponse>, t: Throwable) {

                }

            })



        //삭제
        holder.comment_edit_text.visibility = View.INVISIBLE
        holder.comment_edit_btn.visibility=View.INVISIBLE
        holder.comment_erase.setOnClickListener {
            val comment_id = comment?.commentId
            if (comment_id != null) {
                RetrofitClient.afterinstance.deleteComment(comment_id)
                    .enqueue(object : Callback<DeleteCommentResponse> {

                        override fun onResponse(
                            call: Call<DeleteCommentResponse>,
                            response: Response<DeleteCommentResponse>
                        ) {

                            if (response.body()?.result == "success") {
                                Toast.makeText(holder.itemView.context, "댓글 삭제 성공!", Toast.LENGTH_SHORT).show()


                            } else {
                                Toast.makeText(holder.itemView.context, "댓글 삭제 실패!", Toast.LENGTH_SHORT).show()

                            }
                        }

                        override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {}

                    })
            }
        }

        //수정
        holder.comment_edit.setOnClickListener {
            val comment_id = comment?.commentId
            holder.commentTextView.visibility = View.GONE
            holder.comment_edit_text.visibility = View.VISIBLE
            holder.comment_edit_btn.visibility=View.VISIBLE

            //완료버튼 클릭시
            holder.comment_edit_btn.setOnClickListener{
                val commentText = holder.comment_edit_text.text.toString()
                if (comment_id != null) {
                    RetrofitClient.afterinstance.editComment(comment_id,commentContent(commentText))
                        .enqueue(object : Callback<EditCommentResponse> {
                            override fun onResponse(
                                call: Call<EditCommentResponse>,
                                response: Response<EditCommentResponse>
                            ) {
                                Log.d(ContentValues.TAG,commentText)
                                Log.d(ContentValues.TAG,response.body()?.content.toString())
                                if (response.isSuccessful) {
                                    Log.d(
                                        "CommentAdapter",
                                        response.body()?.content ?: "Response is null"
                                    )
                                    if (response.body()?.content == commentText) {
                                        Toast.makeText(
                                            holder.itemView.context,
                                            response.body()?.result,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        holder.commentTextView.visibility = View.VISIBLE
                                        holder.comment_edit_text.visibility = View.GONE
                                        holder.comment_edit_btn.visibility = View.GONE
                                    }
                                }

                                else {
                                    // 서버 응답이 실패한 경우 처리
                                    val errorMessage = response.errorBody()?.string()
                                    println("댓글 작성 실패: $errorMessage")
                                }
                            }

                            override fun onFailure(call: Call<EditCommentResponse>, t: Throwable) {}

                        })
                }

            }


        }
    }


    override fun getItemCount(): Int {
        return comments!!.size
    }

}


