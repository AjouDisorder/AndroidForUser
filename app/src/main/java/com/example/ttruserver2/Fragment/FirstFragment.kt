package com.example.ttruserver2.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ttruserver2.Fragment.RestaurantInfo.RestaurantInfoActivity
import com.example.ttruserver2.R
import kotlinx.android.synthetic.main.fragment_first.view.*

/**
 * A simple [Fragment] subclass.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_first, container, false)

        val list_array = arrayListOf<ContentsListModel>(
            ContentsListModel("a1", "안성찜닭", 1, "c1"),
            ContentsListModel("a2", "제육볶음", 2, "c2"),
            ContentsListModel("a3", "치킨", 3, "c3"),
            ContentsListModel("a4", "우동", 4, "c4"),
            ContentsListModel("a5", "피자", 5, "c5"),
            ContentsListModel("a6", "도넛츠", 6, "c6"),
            ContentsListModel("a7", "짜장면", 7, "c7")
        )

        val list_adapter = FirstFragAdapter(requireContext(), list_array)
        view.listview_first_fragment.adapter= list_adapter

        view.listview_first_fragment.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(requireContext(), RestaurantInfoActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
