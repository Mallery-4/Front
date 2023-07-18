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
import com.example.mallery4.datamodel.CommentRes
import com.example.mallery4.datamodel.DeleteCommentResponse
import com.example.mallery4.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentAdapter(private val comments: List<CommentRes>?) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.comment_name)
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

        holder.comment_erase.setOnClickListener {
            val comment_id = comment?.commentId
            if (comment_id != null) {
                RetrofitClient.afterinstance.deleteComment(comment_id)
                    .enqueue(object : Callback<DeleteCommentResponse> {

                        override fun onResponse(
                            call: Call<DeleteCommentResponse>,
                            response: Response<DeleteCommentResponse>
                        ) {

                            //탈퇴 성공
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

    }


    override fun getItemCount(): Int {
        return comments!!.size
    }
}


