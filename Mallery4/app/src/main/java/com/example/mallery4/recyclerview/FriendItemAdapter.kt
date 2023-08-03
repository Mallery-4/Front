package com.example.mallery4.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mallery4.MainActivity
import com.example.mallery4.R
import kotlinx.android.synthetic.main.friend_recyclerview_btn.view.*

class FriendItemAdapter() : RecyclerView.Adapter<FriendItemAdapter.ItemViewHolder> () {

    private lateinit var  friendList : ArrayList<FriendItem>


    //생성자
    constructor(friendList: ArrayList<FriendItem>): this(){
        this.friendList = friendList
    }

    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ItemViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.friend_recyclerview_btn,parent,false)
        return ItemViewHolder(view)
    }

    // 데이터 설정
    override fun onBindViewHolder(holder: ItemViewHolder, position:Int){
        //아이템 담기
        val item : FriendItem = friendList[position]
        holder.itemText.text = item.friendname


        // 클릭 이벤트
        holder.layout.setOnClickListener{
            // 멀티 선택 함수
            setMultipleSelection(position)
        }

        // 선택값에 따른 배경색 설정
        holder.itemText.setOnClickListener {
            holder.itemText.setBackgroundColor(Color.parseColor("#F7CCC7"))
        }

    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    private fun setMultipleSelection(position:Int){
        friendList[position].selected = !friendList[position].selected
        notifyItemChanged(position)
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val layout: LinearLayout = itemView.findViewById(R.id.layout)
        val itemText: TextView = itemView.findViewById(R.id.friend_name)
    }


}