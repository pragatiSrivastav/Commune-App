package com.pragati.communeapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION
import java.sql.SQLException


class DbHelper(context: Context) : SQLiteOpenHelper(context,"Attendance.db",null,1) {

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
    private val STUDENT_TABLE_NAME: String = "STUDENT_TABLE"
    private val S_ID : String= "_SID"
    private val STUDENT_NAME_KEY : String= "STUDENT_NAME"
    private val STUDENT_ROLL_KEY  : String= "ROLL"

    private val CREATE_STUDENT_TABLE = "CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
            S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            STUDENT_NAME_KEY + "TEXT NOT NULL," +
            STUDENT_ROLL_KEY + "INTEGER," +
            "FOREIGN KEY (" + cid + ") REFERENCES " + classTableName + "(" + cid + ")" +
            ");"

    private val DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS $STUDENT_TABLE_NAME"
    private val SELECT_STUDENT_TABLE = "SELECT * FROM $STUDENT_TABLE_NAME"




    //STATUS TABLE
    private val STATUS_TABLE_NAME: String = "STATUS_TABLE"
    private val STATUS_ID : String = "_STATUS_ID"
    private val DATA_KEY : String= "STATUS_DATE"
    private val STATUS_KEY : String = "STATUS"

    private val CREATE_STATUS_TABLE = "CREATE TABLE " + STATUS_TABLE_NAME + "(" +
            STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            DATA_KEY + "DATE NOT NULL," +
            STATUS_KEY + "TEXT NOT NULL," +
            " UNIQUE (" + S_ID + "," + DATA_KEY + ")," +
            "FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")" +
            ");"

    private val DROP_STATUS_TABLE = "DROP TABLE IF EXISTS $STATUS_TABLE_NAME"
    private val SELECT_STATUS_TABLE = "SELECT * FROM $STATUS_TABLE_NAME"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable)
        db?.execSQL(CREATE_STUDENT_TABLE)
        db?.execSQL(CREATE_STATUS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL(dropClassTable)
            db?.execSQL(DROP_STUDENT_TABLE);
            db?.execSQL(DROP_STATUS_TABLE);
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
    

}