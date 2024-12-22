package com.example.to_do

import android.content.Context
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val TASKS_FILE_NAME = "task_todolist.dat"
const val DONE_FILE_NAME = "done_todolist.dat"
fun taskWriteData(items : MutableList<String> , context : Context){

    val fos = context.openFileOutput(TASKS_FILE_NAME , Context.MODE_PRIVATE)
    val oas = ObjectOutputStream(fos)
    val itemList = ArrayList<String>()
    itemList.addAll(items)
    oas.writeObject(itemList)
    oas.close()
}

fun taskReadData(context: Context) : MutableList<String>{

    var itemList : ArrayList<String>


    try {
        val fis = context.openFileInput(TASKS_FILE_NAME)
        val ois = ObjectInputStream(fis)
        itemList = ois.readObject() as ArrayList<String>


    }catch (e : FileNotFoundException){
        itemList = ArrayList()
    }

    val items = mutableListOf<String>()
    items.addAll(itemList)

    return items

}

fun doneWriteData(items : MutableList<String> , context : Context){
    val fos = context.openFileOutput(DONE_FILE_NAME , Context.MODE_PRIVATE)
    val oas = ObjectOutputStream(fos)
    val itemList = ArrayList<String>()
    itemList.addAll(items)
    oas.writeObject(itemList)
    oas.close()
}
fun doneReadData(context: Context) : MutableList<String>{
    var itemList : ArrayList<String>


    try {
        val fis = context.openFileInput(TASKS_FILE_NAME)
        val ois = ObjectInputStream(fis)
        itemList = ois.readObject() as ArrayList<String>


    }catch (e : FileNotFoundException){
        itemList = ArrayList()
    }

    val items = mutableListOf<String>()
    items.addAll(itemList)

    return items
}