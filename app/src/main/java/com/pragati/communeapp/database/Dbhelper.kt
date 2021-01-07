package com.pragati.communeapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException


class DbHelper(context: Context) : SQLiteOpenHelper(context,"Attendance.db",null,1) {

    //CLASS TABLE
    private val tableName : String ="CLASS_TABLE"
    var cid : String= "_CID"
    var classNameKey :String ="CLASS_NAME"
    var subjectNameKey :String= "SUBJECT_NAME"

    private val createTable : String =
        "CREATE TABLE "+ tableName + "( " +
            cid +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            classNameKey + " TEXT NOT NULL,"+
            subjectNameKey + " TEXT NOT NULL, "+
            "UNIQUE (" + classNameKey +","+subjectNameKey+")"+
            ");"

    private val dropClassTable : String = "DROP TABLE IF EXISTS $tableName"
    private val selectClassTable : String = "SELECT * FROM $tableName"



    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTable)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL(dropClassTable)
        }catch(e : SQLException){
            e.printStackTrace()
        }
    }

    fun addClass(className : String,subjectName :String) : Long{
        var database : SQLiteDatabase = this.writableDatabase
        var values = ContentValues()
        values.put(classNameKey,className)
        values.put(subjectNameKey,subjectName)

        return database.insert(tableName,null,values)
    }

    fun getClassTable() : Cursor{
        var database : SQLiteDatabase = this.readableDatabase
        return database.rawQuery(selectClassTable,null)

    }
}