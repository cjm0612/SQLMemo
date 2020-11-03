package com.example.sqliteex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card.view.*

class MainActivity : AppCompatActivity() {

    val dataList = mutableListOf<MutableMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter()
        
        fab.setOnClickListener{

            val dialog = WriteDialog()
            dialog.listener = {title, post->
                saveData(title, post)

                updateRecyclerView()
            }

            dialog.show(supportFragmentManager, "dialog")
        }

        updateRecyclerView()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView : TextView
        val postTextView : TextView
        val timeTextView : TextView
        val editButton : ImageButton
        val deleteButton : ImageButton

        init {
            titleTextView = itemView.titleTextView
            postTextView = itemView.postTextView
            timeTextView = itemView.timeTextView
            editButton = itemView.editButton
            deleteButton = itemView.deleteButton
        }
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(this@MainActivity).inflate(R.layout.card, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.titleTextView.text = dataList[position].get("title").toString()
            holder.postTextView.text = dataList[position].get("post").toString()
            holder.timeTextView.text = dataList[position].get("time").toString()

            holder.deleteButton.setOnClickListener{
                removeData(dataList[position].get("id").toString())
                updateRecyclerView()
            }

            holder.editButton.setOnClickListener{
                val dialog = WriteDialog()
                dialog.title = dataList[position].get("title").toString()
                dialog.post = dataList[position].get("post").toString()
                dialog.listener = {title, post->

                    editData(dataList[position].get("id").toString(), title, post)
                    updateRecyclerView()
                }
                dialog.show(supportFragmentManager, "dialog")
            }
        }
    }

    fun updateRecyclerView(){
        dataList.clear()
        dataList.addAll(readAllData())
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun saveData(title : String, post: String){
        val sql = "INSERT INTO post (title, post) values( '${title}', '${post}')"
        val dbHelper = PostDbHelper(applicationContext)
        dbHelper.writableDatabase.execSQL(sql)
    }

    fun readAllData() : MutableList<MutableMap<String, String>>{
        val dbHelper = PostDbHelper(applicationContext)
        val resultList = mutableListOf<MutableMap<String, String>>()
        val cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM post", null)

        if(cursor.moveToFirst()){
            do {
                val map = mutableMapOf<String, String>()
                map["id"] = cursor.getString(cursor.getColumnIndex("id"))
                map["title"] = cursor.getString(cursor.getColumnIndex("title"))
                map["post"] = cursor.getString(cursor.getColumnIndex("post"))
                map["time"] = cursor.getString(cursor.getColumnIndex("time"))
                resultList.add(map)
            } while (cursor.moveToNext())
        }
        return resultList
    }

    fun removeData(id : String){
        val dbHelper = PostDbHelper(applicationContext)
        val sql = "DELETE FROM post where id = ${id}"
        dbHelper.writableDatabase.execSQL(sql)
    }

    fun editData(id: String, title: String, post: String){
        val dbHelper = PostDbHelper(applicationContext)
        val sql = "UPDATE post set title = '${title}', post = '${post}', time = CURRENT_TIMESTAMP" +
                " where id = ${id}"
        dbHelper.writableDatabase.execSQL(sql)
    }
}
