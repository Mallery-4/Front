package com.example.mallery4.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mallery4.HomeFragment
import com.example.mallery4.MainActivity
import com.example.mallery4.R

class MainItemAdapter(val MainItemList:ArrayList<MainItem>) : RecyclerView.Adapter<MainItemAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CustomViewHolder{
        val view= LayoutInflater.from(parent.context).inflate(R.layout.main_items,parent,false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos:Int=adapterPosition
                val mainitem:MainItem=MainItemList.get(curPos)
                Toast.makeText(parent.context,"그룹 이름 : ${mainitem.groupname}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position:Int){
        //현재 클릭한 위치와 연동

        val data = MainItemList.get(position)

        holder.group_name.text=MainItemList.get(position).groupname.trim('"')
        holder.group_count.text=MainItemList.get(position).groupcount.trim('"')
        holder.setGroupData(data)
        holder.group_nicknames.text= MainItemList.get(position).groupnicknames.trim('"')

        //해당 recyclerview 클릭시 fragment 화면 이동
        holder.itemView.setOnClickListener {
            (holder.itemView?.context as MainActivity).MoveGroups(holder.group_name.text.toString(),holder.group_count.text.toString(),holder.group_id,holder.group_members,holder.group_nicknames.text.toString())
        }
    }

    class CustomViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var group_id: Long = 0 // long 자료형 멤버 변수 추가
        var group_members: String = ""

        var group_name=itemView.findViewById<TextView>(R.id.gp_name) //그룹이름
        var group_count=itemView.findViewById<TextView>(R.id.gp_cnt) //그룹명수
        var group_nicknames=itemView.findViewById<TextView>(R.id.gp_nicknames) //그룹멤버닉네임

        fun setGroupData(data: MainItem){
            group_id = data.groupid
            group_members = data.groupmembers
        }

    }

    override fun getItemCount(): Int {
        return MainItemList.size
    }

}