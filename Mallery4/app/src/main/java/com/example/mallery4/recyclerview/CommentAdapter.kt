package com.example.mallery4.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mallery4.R
import com.example.mallery4.datamodel.CommentRes

class CommentAdapter(private val comments: List<CommentRes>?) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.comment_name)
        private val commentTextView: TextView = itemView.findViewById(R.id.comment_text)
        private val timeTextView: TextView = itemView.findViewById(R.id.comment_date)

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
    }

    override fun getItemCount(): Int {
        return comments!!.size
    }
}

