package com.example.mallery4.viewpager

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mallery4.R
import java.net.URL

class DetailPostAdapter(): RecyclerView.Adapter<DetailPostAdapter.ViewHolder>() {

    var imageList: ArrayList<String> = ArrayList()
    lateinit var context: Context

    constructor(imageList: ArrayList<String>, context: Context): this(){
        this.imageList = imageList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.postview_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position]) // 이미지 위치
            .placeholder(R.drawable.camera)
            .into(holder.postView) // 보여줄 위치

    }

    override fun getItemCount(): Int = imageList.size



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postView: ImageView = view.findViewById(R.id.postView)
    }
}