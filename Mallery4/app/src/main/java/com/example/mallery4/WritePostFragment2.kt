package com.example.mallery4

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mallery4.recyclerview.FriendItem
import com.example.mallery4.recyclerview.FriendItemAdapter
import kotlinx.android.synthetic.main.fragment_write_post1.*
import kotlinx.android.synthetic.main.fragment_write_post2.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// 함께한 친구 작성하는 post page
class WritePostFragment2 (groupname: String, groupcount: String, groupid: Long, groupmembers: String, postdate: String) : Fragment(){

    var group_name = groupname
    var group_id = groupid
    var group_count = groupcount
    var group_members = groupmembers
    lateinit var member_list : List<String>
    var post_date = postdate
    lateinit var participants: MutableList<String>

    lateinit var itemList: ArrayList<FriendItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_post2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        member_list = group_members.split(",")


        // back 뒤로가기 버튼 클릭시, 이전 날짜 선택 post 화면으로 이동
        btn_post2_backhome.setOnClickListener {
            (context as MainActivity).Post1(group_name, group_count, group_id, group_members)
        }

        // 동적으로 버튼 생성
        // member을 ,을 구분자로 string 구분 + 동적으로 친구 이름 버튼 만들기
        friend_recyclerview.layoutManager = LinearLayoutManager(context)
        friend_recyclerview.adapter = FriendItemAdapter(getData())


        // 선택한 친구들과 추억 기록하기 버튼 클릭시
        btn_post_friend.setOnClickListener {
            // 클릭된 함께한 친구 목록 추가
            for ((index, item) in itemList.withIndex().reversed()){
                val item : FriendItem = item

                if (item.selected){
                    participants.add(item.friendname)
                }
            }

            // 다음 화면으로 이동
            (context as MainActivity).Post3(group_name, group_count, group_id, group_members, post_date, participants)
        }


    }

    // recyclerview 내의 친구 목록 데이터 생성
    private fun getData(): ArrayList<FriendItem>{
        val itemList: ArrayList<FriendItem> = ArrayList()

        for (i in 0 until member_list.size){
            itemList.add(FriendItem(member_list[i], false))
        }

        return itemList
    }
}