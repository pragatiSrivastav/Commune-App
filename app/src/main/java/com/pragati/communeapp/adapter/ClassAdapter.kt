package com.pragati.communeapp.adapter

import android.content.Context
import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pragati.communeapp.R
import com.pragati.communeapp.activity.StudentActivity
import com.pragati.communeapp.model.ClassItem

class ClassAdapter(
    val context: Context,
    private val itemList: ArrayList<ClassItem>
):RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(view : View): RecyclerView.ViewHolder(view) , View.OnCreateContextMenuListener{

        val txtClass : TextView = view.findViewById(R.id.txt_year)
        val txtSubject : TextView = view.findViewById(R.id.txt_sub)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(adapterPosition,0,0,"EDIT")
            menu?.add(adapterPosition,1,0,"DELETE")

        }

        val longPress = view.setOnCreateContextMenuListener(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_item,parent,false)
        return ClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {

        val classItem = itemList[position]
        holder.txtClass.text= classItem.className
        holder.txtSubject.text=classItem.subjectName
        val className = holder.txtClass.text
        val subName = holder.txtSubject.text


        holder.llContent.setOnClickListener {
            val intent = Intent(holder.llContent.context,StudentActivity::class.java)
            intent.putExtra("Class",className)
            intent.putExtra("Subject",subName)
            intent.putExtra("position", position);
            intent.putExtra("cid", classItem.class_id);
            holder.llContent.context.startActivity(intent)
        }

        holder.longPress
    }
}
