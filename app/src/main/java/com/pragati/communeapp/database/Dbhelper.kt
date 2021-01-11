package com.pragati.communeapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException


class DbHelper(context: Context) : SQLiteOpenHelper(context,"Attendance.db",null,3) {

    //CLASS TABLE
    private val classTableName : String ="CLASS_TABLE"
    var cid : String= "_CID"
    var classNameKey :String ="CLASS_NAME"
    var subjectNameKey :String= "SUBJECT_NAME"

    private val createTable : String =
        "CREATE TABLE "+ classTableName + "( " +
            cid +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            classNameKey + " TEXT NOT NULL,"+
            subjectNameKey + " TEXT NOT NULL, "+
            "UNIQUE (" + classNameKey +","+subjectNameKey+")"+
            ");"

    private val dropClassTable : String = "DROP TABLE IF EXISTS $classTableName"
    private val selectClassTable : String = "SELECT * FROM $classTableName"


    //STUDENT TABLE
    private val studentTableName : String = "STUDENT_TABLE"
    val sid : String = "_SID"
    val studentNameKey="STUDENT_NAME"
    val studentRollKey = "ROLL"

    private val createStdTable : String =
        "CREATE TABLE " + studentTableName +
                "( " +
                sid + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                cid + " INTEGER NOT NULL, "+
                studentNameKey + " TEXT NOT NULL, "+
                studentRollKey + " INTEGER, "+
                " FOREIGN KEY ( " + cid + ") REFERENCES " + classTableName + "(" + cid + ")" +
                ");"

    private val dropStudentTable : String = "DROP TABLE IF EXISTS $studentTableName"
    private val selectStudentTable : String = "SELECT * FROM $studentTableName"


    //status table
    private val statusTableName: String? = "STATUS_TABLE"
    val statusId : String = "_STATUS_ID"
    val dateKey : String = "STATUS_DATE"
    val statusKey : String = "STATUS"

    private val createStatusTable = "CREATE TABLE " + statusTableName +
            "(" +
            statusId + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            sid + " INTEGER NOT NULL, " +
            cid + " INTEGER NOT NULL, " +
            dateKey + " DATE NOT NULL, " +
            statusKey + " TEXT NOT NULL, " +
            " UNIQUE (" + sid + "," + dateKey + ")," +
            " FOREIGN KEY (" + sid + ") REFERENCES " + studentTableName + "( " + sid + ")," +
            " FOREIGN KEY (" + cid  + ") REFERENCES " + classTableName + "( " + cid + ")" +
            ");"
    private val dropStatusTable = "DROP TABLE IF EXISTS $statusTableName"
    private val selectStatusTable = "SELECT * FROM $statusTableName"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable)
        db?.execSQL(createStdTable)
        db?.execSQL(createStatusTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL(dropClassTable)
            db?.execSQL(dropStudentTable)
            db?.execSQL(dropStatusTable)
        }catch(e : SQLException){
            e.printStackTrace()
        }
    }

    fun addClass(className : String,subjectName :String) : Long{
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(classNameKey,className)
        values.put(subjectNameKey,subjectName)

        return database.insert(classTableName,null,values)
    }

    fun getClassTable() : Cursor{

        var database : SQLiteDatabase = this.readableDatabase
        return database.rawQuery(selectClassTable,null)

    }

    fun deleteClass(cId : Long) : Int{
        var database : SQLiteDatabase = this.readableDatabase
        return database.delete(classTableName, "$cid=?", arrayOf(cId.toString()))
    }

    fun updateClass(cId : Long,className : String,subjectName :String) : Int {
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(classNameKey,className)
        values.put(subjectNameKey,subjectName)
        return database.update(classTableName,values,"$cid=?", arrayOf(cId.toString()))
    }

    fun addStudent(cId: Long, roll: Int, studentName: String) : Long{
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(cid,cId)
        values.put(studentRollKey,roll)
        values.put(studentNameKey,studentName)
        return database.insert(studentTableName,null,values)
    }

    fun getStudentTable(cId : Long) : Cursor{

        var database : SQLiteDatabase = this.readableDatabase
        return database.query(studentTableName,null, "$cid=?", arrayOf(cId.toString()),null,null,studentRollKey)

    }

    fun deleteStudent(sId : Long) : Int{
        var database : SQLiteDatabase = this.readableDatabase
        return database.delete(studentTableName, "$sid=?", arrayOf(sId.toString()))
    }

    fun updateStudent(sId : Long,studentName : String) : Int {
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(studentNameKey,studentName)
        return database.update(studentTableName,values,"$sid=?", arrayOf(sId.toString()))
    }

    fun addStatus(sId : Long,cId: Long,date : String,status :String):Long{
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(sid,sId)
        values.put(cid,cId)
        values.put(dateKey,date)
        values.put(statusKey,status)
        return database.insert(statusTableName, null,values)
    }

    fun updateStatus(sId : Long ,date : String,status: String) : Int {
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(statusKey,status)
        var whereClause : String = "$dateKey=' $date 'AND $sid=$sId"
        return database.update(statusTableName,values,whereClause,null)
    }

    fun getStatus(sId : Long,date: String): String? {
        var status : String? = null
        var database : SQLiteDatabase = this.readableDatabase
        var whereClause : String = "$dateKey=' $date 'AND $sid=$sId"
        var cursor : Cursor = database.query(statusTableName,null,whereClause,null,null,null,null)
        if(cursor.moveToFirst()){
            status = cursor.getString(cursor.getColumnIndex(statusKey))
        }
        return status
    }

    fun getDistinctMonths(cId : Long) : Cursor{
        var database : SQLiteDatabase = this.readableDatabase
        return database.query(statusTableName, arrayOf(dateKey), "$cid=$cId",null,
            "substr($dateKey ,4,7)",null,null)
    }



}