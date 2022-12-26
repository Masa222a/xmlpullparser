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
import org.xmlpull.v1.XmlPullParser.END_DOCUMENT
import org.xmlpull.v1.XmlPullParser.START_TAG
import org.xmlpull.v1.XmlPullParserException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var userList = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            userList = parseUser()
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
        // オブジェクトを格納するリストを作成する
        var users = mutableListOf<User>()

        // person_list.xml を解析するパーサーを取得する
        val parser = applicationContext.resources.getXml(R.xml.userdetails)

        // 解析が完了し、ドキュメントの終端に到達するまで処理を続ける
        while (parser.eventType != END_DOCUMENT) {

            // 開始タグでかつ、名称がPersonならば各アトリビュートを取得する
            if (parser.eventType == START_TAG && parser.name == "user") {
                val name = parser.getAttributeValue(null, "name")
                val designation = parser.getAttributeValue(null, "designation")
                users.add(User(name, designation))
                Log.d("ユーザー", "$users")
            }

            // 次の要素を読み込む
            parser.next()
        }

        // パーサーはクローズ処理が必要なので忘れずに実行する
        parser.close()
        Log.d("デバッグ", "$users")
        return users
    }
//    <user>
//        <name>Danny</name>
//        <designation>Music Director</designation>
//    </user>
    data class User(val name: String, val designation: String)

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