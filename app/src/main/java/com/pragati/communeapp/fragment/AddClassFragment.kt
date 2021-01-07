package com.pragati.communeapp.fragment

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pragati.communeapp.model.ClassItem
import com.pragati.communeapp.adapter.ClassAdapter
import com.pragati.communeapp.R
import com.pragati.communeapp.database.DbHelper


class AddClassFragment : Fragment() {

    lateinit var fab:  FloatingActionButton
    lateinit var recView : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    private lateinit var classAdapter: ClassAdapter
    lateinit var classedt : EditText
    lateinit var subjectedt : EditText
    lateinit var dbHelper: DbHelper

    private val classItems = arrayListOf<ClassItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add_class, container, false)

        dbHelper = DbHelper(activity as Context)

        fab = view.findViewById(R.id.add)
        recView = view.findViewById(R.id.rec_view)
        recView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        classAdapter = ClassAdapter(activity as Context,classItems)
        recView.adapter=classAdapter
        recView.layoutManager=layoutManager

        loadData()

        fab.setOnClickListener {
            val builder = AlertDialog.Builder(activity as Context)
            val view = layoutInflater.inflate(R.layout.dialog_add_class, null)
            builder.setView(view)
            val dialog : AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            classedt = view.findViewById(R.id.et_year)
            subjectedt=view.findViewById(R.id.et_sub)


            val cancel : Button = view.findViewById(R.id.btn_cancel)
            val add : Button = view.findViewById(R.id.btn_add)

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            add.setOnClickListener{
                addingClass()
                dialog.dismiss()
            }

        }

        return view
    }

    private fun loadData(){

        val cursor : Cursor = dbHelper.getClassTable()
        classItems.clear()
        while(cursor.moveToNext()){
            val id : Long = cursor.getLong(cursor.getColumnIndex(dbHelper.cid))
            val className : String = cursor.getString(cursor.getColumnIndex(dbHelper.classNameKey))
            val subjectName : String = cursor.getString(cursor.getColumnIndex(dbHelper.subjectNameKey))

            classItems.add(
                ClassItem(
                    id,
                    className,
                    subjectName
                )
            )
        }

    }


    private fun addingClass(){
        val className : String = classedt.text.toString()
        val subjectName : String = subjectedt.text.toString()
        val cid : Long = dbHelper.addClass(className,subjectName)

        classItems.add(
            ClassItem(
                cid,
                className,
                subjectName
            ))
        classAdapter.notifyDataSetChanged()
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           0->{
               showUpdateDialog(item.groupId)
           }
           1->{
               deleteClass(item.groupId)
           }
       }

        return super.onContextItemSelected(item)
    }

    private fun showUpdateDialog(pos: Int){
        val builder = AlertDialog.Builder(activity as Context)
        val view = layoutInflater.inflate(R.layout.dialog_update_class, null)
        builder.setView(view)
        val dialog : AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        classedt = view.findViewById(R.id.e_year)
        subjectedt=view.findViewById(R.id.e_sub)


        val cancel : Button = view.findViewById(R.id.btn_cancel)
        val add : Button = view.findViewById(R.id.btn_update)

        add.text = "Update"

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        add.setOnClickListener{
            val clsName : String = classedt.text.toString()
            val subName : String = subjectedt.text.toString()
            updateClass(pos,clsName,subName)
            dialog.dismiss()
        }
    }

    private fun updateClass(pos: Int, clsName: String, subName: String){
        dbHelper.updateClass(classItems[pos].class_id,clsName,subName)
        classItems[pos].className=clsName
        classItems[pos].subjectName=subName
        classAdapter.notifyItemChanged(pos)
    }

    private fun deleteClass(pos : Int){
        dbHelper.deleteClass(classItems[pos].class_id )
        classItems.removeAt(pos)
        classAdapter.notifyItemRemoved(pos)
    }
}



