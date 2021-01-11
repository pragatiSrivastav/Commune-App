package com.pragati.communeapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.reflect.KFunction4

class MyCalendar : DialogFragment() {

    var calendar : Calendar = Calendar.getInstance()

    interface OnCalendarOkClickListener{
        fun onClick(year : Int,month : Int,day: Int)
       // abstract fun apply(block: KFunction4<View, Int, Int, Int, Unit>)
    }

    lateinit var onCalendarOkClickListener : OnCalendarOkClickListener

    fun setOnCalendarOkClickListener(
        mylis: KFunction4<@ParameterName(name = "view") View, @ParameterName(
            name = "year"
        ) Int, @ParameterName(name = "month") Int, @ParameterName(name = "dayOfMonth") Int, Unit>
    ){
        //var onCalendarOkClickListener : OnCalendarOkClickListener = myli
       // onCalendarOkClickListener.apply{mylis}
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var onCalendarOkClickListener : OnCalendarOkClickListener? = null
        return DatePickerDialog(activity!!,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            onCalendarOkClickListener?.onClick(year,month,dayOfMonth)
        },
            calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))

    }

    fun setDate(year : Int,month : Int,day : Int){
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.DAY_OF_MONTH,day)
    }

    fun getDate(): String {
        return android.text.format.DateFormat.format("dd MMM y",calendar).toString()
    }
}