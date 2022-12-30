package com.example.xmlpullparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aaaaaaaaaaaaaaaaaaaa.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.*
import org.xmlpull.v1.XmlPullParserException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var userList = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            userList = parseUser()
            Log.d("デバッグonCreate", "$userList")
        } catch (e: Exception) {
            Log.d("エラー", "$e")
        } catch (e: XmlPullParserException) {
            Log.d("エラー", "$e")
        }

        recyclerView = findViewById(R.id.user_list)
        recyclerView.adapter = RecyclerAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun parseUser(): MutableList<User> {

        var users = mutableListOf<User>()

        val parser = applicationContext.resources.getXml(R.xml.userdetails)
        var name = mutableListOf<String>()
        var designation = mutableListOf<String>()
        var tagName:String? = null
        var eventType = parser.eventType

        while (parser.eventType != END_DOCUMENT) {

            when(eventType) {
                START_DOCUMENT -> {

                }
                START_TAG -> {
                    tagName = parser.name

                }
                TEXT -> {
                    if (tagName != null) {
                        when(tagName) {
                            "name" -> {
                                if (tagName != null) {
                                    name.add(parser.text)
                                }
                            }
                            "designation" -> {
                                if (tagName != null) {
                                    designation.add(parser.text)
                                }
                            }
                        }
                    }
                }
            }

            eventType = parser.next()
        }

        parser.close()
        for (i in 0..name.count() - 1) {
            users.add(User(name[i], designation[i]))
        }
        return users
    }

    data class User(val name: String?, val designation: String?)

    class RecyclerAdapter(val list: MutableList<User>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolderList>() {
        class ViewHolderList (item: View) : RecyclerView.ViewHolder(item) {
            val name: TextView = item.findViewById(R.id.name)
            val designation: TextView = item.findViewById(R.id.designation)
        }

        override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): ViewHolderList {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
            return ViewHolderList(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
            var user = list[position]
            holder.name.text = user.name
            holder.designation.text = user.designation
        }

        override fun getItemCount(): Int = list.size
    }
}