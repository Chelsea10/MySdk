package com.example.webviewdialog

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ListActivity: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = ArrayList<A>()
//        val map = HashMap<String, A>()
        for (i in 0..99999) {
            val a = A("id$i", "name$i","sex$i",i,"job$i", "country$i")
            list.add(a)
//            map.put("id$i", a)
        }
        var start = System.currentTimeMillis()
//        val map: MutableMap<String, A> = list.stream().collect(Collectors.toMap(
//            A::id, Function.identity(), mergeFunction()
//        ))
        val map = list.associateBy{ it.id } // list转map
        if (!map.isNullOrEmpty()) {
            Log.i( "===================,", "time: ${System.currentTimeMillis() - start} map size: ${map.size}")
        }
        start = System.currentTimeMillis()
        val a2 = list.firstOrNull { it.id == "id99999" }
        Log.i("============1","time: ${System.currentTimeMillis() - start} a: ${a2!!.id}, ${a2.name}")
        start = System.currentTimeMillis()
        val a = map!!["id99999"]
        Log.i("============1","time: ${System.currentTimeMillis() - start} a: ${a!!.id} ${a.name}")
    }
}


class A(var id: String, var name: String,val sex: String, val age:Int, val job:String, val country:String)

