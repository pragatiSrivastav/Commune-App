package com.pragati.communeapp.activity

import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.pragati.communeapp.R
import com.pragati.communeapp.adapter.ClassAdapter
import com.pragati.communeapp.adapter.StudentAdapter
import com.pragati.communeapp.model.ClassItem
import com.pragati.communeapp.model.StudentItem
import kotlinx.android.synthetic.main.dialog_student_class.*
import kotlinx.android.synthetic.main.student_item.*

class StudentActivity : AppCompatActivity() {

   lateinit var toolbar: Toolbar
    var className : String? ="Title"
    var subjectName: String? = "Subtitle"
    lateinit var recyclerView: RecyclerView
    lateinit var studentAdapter : StudentAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var roll: EditText
    lateinit var name : EditText


    private val studentItems = arrayListOf<StudentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        className = intent.getStringExtra("Class")
        subjectName = intent.getStringExtra("Subject")

        setToolbar()

        recyclerView = findViewById(R.id.student_recycler)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(this@StudentActivity, studentItems)

        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = layoutManager


    }
    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar)
        var title: TextView = findViewById(R.id.title_toolbar)
        var subtitle: TextView = findViewById(R.id.subtitle_toolbar)
        var back: ImageButton = findViewById(R.id.back)
        var save: ImageButton = findViewById(R.id.save)

        title.text = className
        subtitle.text = subjectName
        back.setOnClickListener {
            onBackPressed()
        }
        toolbar.inflateMenu(R.menu.student_menu)

        toolbar.setOnMenuItemClickListener {
            menuItem -> onMenuItemClick(menuItem)
        }
    }
    private fun onMenuItemClick(menuItem: MenuItem) : Boolean{
        if(menuItem.itemId== R.id.add_student){
            setDialog()
        }
        return true
    }
    private fun setDialog(){
        val builder = AlertDialog.Builder(this)

        val view = layoutInflater.inflate(R.layout.dialog_student_class, null)
        builder.setView(view)
        val dialog : AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val cancel : Button = view.findViewById(R.id.btn_cancel)
        val ok: Button = view.findViewById(R.id.btn_add)

        roll = view.findViewById<EditText>(R.id.rollNo)
        name =view.findViewById<EditText>(R.id.nameOfStd)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        ok.setOnClickListener{
            addStudent()
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun addStudent(){

        val roll : String = roll.text.toString()
        val name : String = name.text.toString()
        val status : String = " "
        studentItems.add(
            StudentItem(
                roll,
                name,
                status
            )
        )
        studentAdapter.notifyDataSetChanged()
    }


}








