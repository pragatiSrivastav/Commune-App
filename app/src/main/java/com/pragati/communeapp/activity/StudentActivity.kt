package com.pragati.communeapp.activity

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.pragati.communeapp.R
import com.pragati.communeapp.adapter.StudentAdapter
import com.pragati.communeapp.database.DbHelper
import com.pragati.communeapp.model.StudentItem

class StudentActivity : AppCompatActivity() {

   lateinit var toolbar: Toolbar
    var className : String? ="Title"
    var subjectName: String? = "Subtitle"
    lateinit var recyclerView: RecyclerView
    lateinit var studentAdapter : StudentAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var roll: EditText
    lateinit var name : EditText
    var cid :Long = 0
    lateinit var dbHelper: DbHelper
    var position : Int = 0


    private val studentItems = arrayListOf<StudentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        dbHelper = DbHelper(this)
        className = intent.getStringExtra("Class")
        subjectName = intent.getStringExtra("Subject")
        position = intent.getIntExtra("position",-1)
        cid = intent.getLongExtra("cid",-1)


        setToolbar()
        loadData()

        recyclerView = findViewById(R.id.student_recycler)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(this@StudentActivity, studentItems)

        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = layoutManager


    }

    private fun loadData(){
        var cursor : Cursor = dbHelper.getStudentTable(cid)
        //Log.i("1234567890", "loadData$cid")
        studentItems.clear()
        while (cursor.moveToNext()){
            var sid : Long = cursor.getLong(cursor.getColumnIndex(dbHelper.sid))
            var roll : Int = cursor.getInt(cursor.getColumnIndex(dbHelper.studentRollKey))
            var name : String = cursor.getString(cursor.getColumnIndex(dbHelper.studentNameKey))
            studentItems.add(
                StudentItem(sid,roll,name)
            )
        }
        cursor.close()
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

        roll = view.findViewById(R.id.rollNo)
        name =view.findViewById(R.id.nameOfStd)

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

        val rollNo : Int = roll.text.toString().toInt()
        val stdName : String = name.text.toString()
        var sid : Long = dbHelper.addStudent(cid ,rollNo,stdName)
        studentItems.add(
            StudentItem(
               sid,
                rollNo,
                stdName
            )
        )
        studentAdapter.notifyDataSetChanged()
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            0->{
                showUpdateDialog(item.groupId)
            }
            1->{
                deleteStudent(item.groupId)
            }
        }
        return super.onContextItemSelected(item)
    }


    private fun deleteStudent(pos : Int){
        dbHelper.deleteStudent(studentItems[pos].sid)
        studentItems.removeAt(pos)
        studentAdapter.notifyItemRemoved(pos)
    }

    private fun showUpdateDialog(pos:Int){
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_update_student, null)
        builder.setView(view)
        val dialog : AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


        roll = view.findViewById(R.id.rollNo)
        name = view.findViewById(R.id.nameOfStd)


        val cancel : Button = view.findViewById(R.id.btn_cancel)
        val add : Button = view.findViewById(R.id.btn_update)

        add.text = "Update"

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        add.setOnClickListener{
           // val rollNo : String = roll.text.toString()
            val nameStd : String = name.text.toString()
            updateClass(pos,nameStd)
            dialog.dismiss()
        }
    }


    private fun updateClass(pos : Int, nameStd : String){
        dbHelper.updateStudent(studentItems[pos].sid,nameStd)
        studentItems[pos].name=nameStd
        studentAdapter.notifyItemChanged(pos)
    }

}












