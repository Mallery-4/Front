package com.example.mallery4

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class DecorateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_decorate, container, false)

        val decoLayout = view.findViewById<View>(R.id.deco_layout)
        decoLayout.setOnClickListener {
            val intent = Intent(activity, DecorateActivity::class.java)
            startActivity(intent)
        }

        return view


    }



    companion object {
        @JvmStatic
        fun newInstance() =
            DecorateFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}