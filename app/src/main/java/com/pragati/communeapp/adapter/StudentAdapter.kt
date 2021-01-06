
package com.pragati.communeapp.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.pragati.communeapp.R
import com.pragati.communeapp.activity.StudentActivity
import com.pragati.communeapp.model.StudentItem
import org.w3c.dom.Text
import java.util.ArrayList

class StudentAdapter(val context: Context, val itemList: ArrayList<StudentItem>):RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(view : View): RecyclerView.ViewHolder(view){

        val roll : TextView = view.findViewById(R.id.txt_roll)
        val nameOfStd : TextView = view.findViewById(R.id.txt_name)
        val status : TextView = view.findViewById(R.id.status)
        val rrContent : RelativeLayout = view.findViewById(R.id.rrContent)
        val cardView : CardView = view.findViewById(R.id.cardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item,parent,false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {

        val studentItem = itemList[position]
        holder.roll.text= studentItem.roll
        holder.nameOfStd.text=studentItem.name
        holder.status.text = studentItem.status
        var status = holder.status.text
        holder.rrContent.setOnClickListener {

            if(status == "P"){
                status = "A"
            }
            else{
                status="P"
            }
            studentItem.status= status.toString()
            notifyDataSetChanged()

            if (status == "P") {
                holder.cardView.setCardBackgroundColor(
                    Color.parseColor(
                        "#" + Integer.toHexString(
                            ContextCompat.getColor(context, R.color.present)
                        )
                    )
                )
            } else if (status == "A") {
                holder.cardView.setCardBackgroundColor(
                    Color.parseColor(
                        "#" + Integer.toHexString(
                            ContextCompat.getColor(context, R.color.absent)
                        )
                    )
                )

            } else {
                holder.cardView.setCardBackgroundColor(
                    Color.parseColor(
                        "#" + Integer.toHexString(
                            ContextCompat.getColor(context, R.color.normal)
                        )
                    )
                )


            }
        }


    }





}




