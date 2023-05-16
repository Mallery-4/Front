package com.example.mallery4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_friend.*
import kotlinx.android.synthetic.main.fragment_detail_group.*
import kotlinx.android.synthetic.main.fragment_write_post1.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


// 날짜 작성하는 post page
class WritePostFragment1 (groupname: String, groupcount: String, groupid: Long, groupmembers: String) : Fragment(){

    var group_name = groupname
    var group_id = groupid
    var group_count = groupcount
    var group_members = groupmembers
    var day=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_write_post1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // back 뒤로가기 버튼 클릭시, 이전 세부 그룹 화면으로 이동
        btn_post1_backhome.setOnClickListener {
            (context as MainActivity).MoveGroups(group_name, group_count, group_id.toString(), group_members)
        }

        // calendarview event
        // 처음 선택된(초기화값) 으로 버튼 text 유지
        val dateFormat : DateFormat = SimpleDateFormat("yyyy년MM월dd일")
        val date : Date = Date(calendarview.date)
        btn_post_date.setText(dateFormat.format(date).toString()+" 추억 기록하기")

        // calendarview 날짜 변환 이벤트 : 버튼 텍스트, backgroundcolor 변경
        calendarview.setOnDateChangeListener{ calendarview, year, month, dayOfMonth ->
            day = "${year}년 ${month+1}월 ${dayOfMonth}일"
            btn_post_date.setText(day+" 추억 기록하기")
            btn_post_date.setBackgroundResource(R.drawable.rounded_pink)

        }

        // 추억 기록하기 버튼 클릭시, 함께한 멤버 선택하는 post fragment로 이동
        btn_post_date.setOnClickListener {
            /// 이동할 fragment 추가하기
            (context as MainActivity).Post2(group_name, group_count, group_id, group_members, day)
        }

    }
}