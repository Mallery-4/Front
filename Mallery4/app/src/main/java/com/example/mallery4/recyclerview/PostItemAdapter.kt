package com.example.mallery4.recyclerview

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mallery4.MainActivity
import com.example.mallery4.R
import java.io.BufferedInputStream
import java.net.URL

class PostItemAdapter(val PostItemList:ArrayList<PostItem>) : RecyclerView.Adapter<PostItemAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CustomViewHolder{
        val view= LayoutInflater.from(parent.context).inflate(R.layout.grid_items,parent,false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos:Int=adapterPosition
                val postItem:PostItem=PostItemList.get(curPos)

            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position:Int){
        //현재 클릭한 위치와 연동
        val data = PostItemList.get(position)
        holder.setPostData(data)

        holder.postDate.text= PostItemList.get(position).postDate.trim('"')

        // 이미지 링크가 없는 경우
        if(PostItemList.get(position).postImg.isNullOrEmpty()) {
            holder.postImg.getResources().getDrawable(R.drawable.camera)
        }
        // 링크가 있는 경우 링크에서 이미지를 가져와서 보여준다.
        else {
            Log.d("####################", PostItemList.get(position).postImg.trim('"'))
            Glide.with(holder.postImg.context)
                .load(PostItemList.get(position).postImg.trim('"'))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(R.drawable.camera)
                .into(holder.postImg)
        }

        //////
        //해당 recyclerview 클릭시 fragment 화면 이동
        holder.itemView.setOnClickListener {
            Log.d("####################", PostItemList.get(position).postImg)
            Log.d("####################", holder.postDate.text.toString())
            (holder.itemView?.context as MainActivity).MoveDetailPost(holder.groupName,holder.groupCount,holder.groupMembers,holder.groupNicknames,holder.groupId,holder.postId)
        }

    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var groupName: String = "" //groupname
        var groupId: Long = 0  //groupid
        var groupCount: String = "" //groupcount
        var groupMembers: String = ""//groupmembers
        var groupNicknames: String = "" //groupnicknames
        var postId: Long = 0  //postid

        var postDate=itemView.findViewById<TextView>(R.id.postdate) //postdate
        var postImg = itemView.findViewById<ImageView>(R.id.gridImg) // postimg (대표이미지)


        fun setPostData(data: PostItem){
            groupName= data.groupName
            groupId = data.groupId
            groupCount = data.groupCount
            groupMembers = data.groupMembers
            groupNicknames = data.groupNicknames
            postId = data.postId
        }

    }

    override fun getItemCount(): Int {
        return PostItemList.size
    }

}