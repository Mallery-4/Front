package com.example.mallery4.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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


class CommentAdapter(private val comments: List<CommentRes>?) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.comment_name)
        private val commentTextView: TextView = itemView.findViewById(R.id.comment_text)
        private val timeTextView: TextView = itemView.findViewById(R.id.comment_date)
        val comment_erase: ImageView = itemView.findViewById(R.id.comment_erase)

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

        //사용자 name 가져오기, 쓰레기통 안보이게 하기
        /*
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

*/

        holder.comment_erase.setOnClickListener {
            val comment_id = comment?.commentId

            if (comment_id != null) {
                RetrofitClient.afterinstance.deleteComment(comment_id, LoginUserId)
                    .enqueue(object : Callback<DeleteCommentResponse> {

                        override fun onResponse(
                            call: Call<DeleteCommentResponse>,
                            response: Response<DeleteCommentResponse>
                        ) {


                            if (response.body()?.result == "success") {
                                Toast.makeText(holder.itemView.context, response.body()?.message, Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(holder.itemView.context, "댓글 삭제 실패했습니다.", Toast.LENGTH_SHORT).show()

                            }
                        }

                        override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {}

                    })
            }
        }

    }


    override fun getItemCount(): Int {
        return comments!!.size
    }
}


