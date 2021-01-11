package com.pragati.communeapp.fragment

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.room.util.DBUtil
import com.pragati.communeapp.R
import com.pragati.communeapp.database.DbHelper


class ViewAttendanceFragment : Fragment() {

    lateinit var sheetList : ListView
    private lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem : ArrayList<String>
    private var cid : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view_attendance, container, false)
        loadListItem()
        sheetList = view.findViewById(R.id.sheetList)
        adapter = ArrayAdapter(activity as Context,R.layout.sheet_list,R.id.date_list_item)
        sheetList.adapter = adapter
        return view
    }

    private fun loadListItem(){
        var cursor: Cursor = DbHelper(activity as Context).getDistinctMonths(cid)

        while (cursor.moveToNext()){
            var date = cursor.getString(cursor.getColumnIndex(DbHelper(activity as Context).dateKey))
            listItem.add(date.substring(3))
        }
    }


}