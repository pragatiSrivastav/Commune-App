package com.pragati.communeapp.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pragati.communeapp.model.ClassItem
import com.pragati.communeapp.adapter.ClassAdapter
import com.pragati.communeapp.R


class AddClassFragment : Fragment() {

    lateinit var fab:  FloatingActionButton
    lateinit var recView : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var classAdapter: ClassAdapter
    lateinit var classedt : EditText
    lateinit var subjectedt : EditText
    lateinit var toolbar  : Toolbar

    private val classItems = arrayListOf<ClassItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add_class, container, false)

        fab = view.findViewById(R.id.add)
        recView = view.findViewById(R.id.rec_view)
        recView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        classAdapter = ClassAdapter(activity as Context,classItems)
        recView.adapter=classAdapter
        recView.layoutManager=layoutManager


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
                addClass()
                dialog.dismiss()
            }

        }
        return view
    }

    private fun addClass(){
        val className : String = classedt.text.toString()
        val subjectName : String = subjectedt.text.toString()
        classItems.add(
            ClassItem(
                className,
                subjectName
            )
        )
        classAdapter.notifyDataSetChanged()
        }

}