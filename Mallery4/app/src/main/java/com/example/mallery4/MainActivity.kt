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
    fun MakingGroupFragment(index:Int, albumname:String){
        when(index){
            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer,MakeGroupFragment()).addToBackStack(Fragment::class.java.simpleName)
                    .commit()
            }

            2 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer,MakeGroupFragment2(albumname)).addToBackStack(Fragment::class.java.simpleName)
                    .commit()
            }
        }
    }

    // 친구추가 페이지 fragment 이동 함수
    fun AddFriend(albumname: String){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,AddFriendFragment(albumname)).addToBackStack(Fragment::class.java.simpleName)
            .commit()
    }

}