package com.example.mallery4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        addFragment(HomeFragment.newInstance())

        bottomNavigation.show(1)
        //bottom navigation과 연결
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.deco))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.home))
        bottomNavigation.add(MeowBottomNavigation.Model(2,R.drawable.id_card))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    Toast.makeText(this,"꾸미기 화면", Toast.LENGTH_SHORT).show()
                    // 이동되는 화면
                    replaceFragment(DecorateFragment.newInstance())

                }
                1 -> {
                    Toast.makeText(this,"홈 화면", Toast.LENGTH_SHORT).show()
                    // 이동되는 화면
                    replaceFragment(HomeFragment.newInstance())
                }
                2 -> {
                    Toast.makeText(this,"마이페이지 화면", Toast.LENGTH_SHORT).show()
                    // 이동되는 화면
                    replaceFragment(MypageFragment.newInstance())
                }
                else-> {
                    Toast.makeText(this,"홈 화면", Toast.LENGTH_SHORT).show()
                    // 이동되는 화면
                    replaceFragment(HomeFragment.newInstance())
                }
            }
        }
    }

    // fragment 화면 전환 함수
    fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    // fragment 화면 전환 함수
    fun addFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    // 그룹 생성 페이지 fragment 이동 함수
    fun MakingGroupFragment(albumid:Long){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer,MakeGroupFragment2(albumid)).addToBackStack(Fragment::class.java.simpleName)
                .commit()
    }

    // 친구추가 페이지 fragment 이동 함수
    fun AddFriend(albumid: Long){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,AddFriendFragment(albumid)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

    // 세부 그룹항목 클릭시 이동 fragment
    fun MoveGroups(groupname: String, groupcount: String, groupid: String, groupmembers: String, groupnicknames:String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,DetailGroupFragment(groupname, groupcount, groupid, groupmembers, groupnicknames)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

    // 탈퇴하기클릭시 이동 fragment
    fun DeleteAlbum(groupid: Long){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,DeleteAlbumFragment(groupid)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

    // 그룹이름 변경하기 이동 fragment
    fun ChangeAlbumName(groupid: Long){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,ChangeAlbumNameFragment(groupid)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

    // post 글쓰기(날짜) 이동 fragment
    fun Post1(groupname: String, groupcount: String, groupid: Long, groupmembers: String, groupnicknames: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,WritePostFragment1(groupname, groupcount, groupid, groupmembers, groupnicknames)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

    // post 글쓰기(함께한친구)페이지로 이동 fragment
    fun Post2(groupname: String, groupcount: String, groupid: Long, groupmembers: String, groupnicknames: String, postdate: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,WritePostFragment2(groupname, groupcount, groupid, groupmembers, groupnicknames, postdate)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

    // post 글쓰기(장소, 사진등록)페이지로 이동 fragment
    fun Post3(groupname: String, groupcount: String, groupid: Long, groupmembers: String, groupnicknames : String, postdate: String, participants: List<String>){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,WritePostFragment3(groupname, groupcount, groupid, groupmembers, groupnicknames, postdate, participants)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }
}