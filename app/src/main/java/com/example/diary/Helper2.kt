package com.example.diary

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Helper2 {
    fun <T> List<T>.customCount(function: (T) -> Boolean): Int {
        var counter = 0;
        for(string in this) {
            if(function(string)) {
                counter++
            }
        }
        return counter;
    }

    fun toast(context: Context, text:String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun Int.isOdd(): Boolean{
        return this % 2 == 1
    }

    fun String.isEmpty2(): Boolean{
        return this.isEmpty()
    }

    public inline fun CharSequence.isEmpty(): Boolean = length == 0

    public fun CharSequence.isBlank(): Boolean = isBlank()

    fun String.isEmpty3(): Boolean{
        return true     // (this.isEmpty())
    }

    fun Int.abs() : Int{
        return if(this < 0) -this else this
    }

    fun Any.anyOrElse(defaultVal:Any, convert: Any.() -> Unit) : Any {
        return try{
            this.convert()
        }catch(e: Exception){
            defaultVal
        }
    }

    fun Any.anyOrElseV2(defaultVal:Any, convert: (Any) -> Unit) : Any {
        return try{
            convert(this)
        }catch(e: Exception){
            defaultVal
        }
    }

    fun Any.orElse(defaultVal:String) : String {
        if(this.toString() == "null") return defaultVal;
        return this.toString();
    }

    fun Any?.toString(): String{
        return if (this == null) "is null" else "is not null"
    }

    fun Any.orElseV2(defaultVal:String) : String {
        return try{
            this.toString()
        }catch(e: Exception){
            defaultVal
        }
    }

    fun String.orElse(defaultVal:Int) : Int {
        return try{
            Integer.parseInt(this)
        }catch(e: Exception){
            defaultVal
        }
    }

    fun EditText.orElse(defaultVal:String) : String {
        return try{
            this.text()
        }catch(e: Exception){
            defaultVal
        }
    }

    fun TextView.orElse(defaultVal:String) : String {
        return try{
            this.text()
        }catch(e: Exception){
            defaultVal
        }
    }

    fun EditText.text(): String{
        return this.text.toString()
    }

    fun TextView.text(): String{
        return this.text.toString()
    }

    fun range(start:Int, end:Int) {
        for (i in start..end) {
//        println(i)
        }
    }
}