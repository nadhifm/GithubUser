package com.nadhif.consumerapp

import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.let {
            it.title = "Consumer App"
        }

        updateUI()
    }

    private fun updateUI() {
        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()

        rvUser.adapter = adapter
        rvUser.layoutManager = LinearLayoutManager(this)

        val contentResolver: ContentResolver = this.contentResolver
        val cursor: Cursor? = contentResolver.query(
            Utility.CONTENT_USER_URI,
            null,
            null,
            null,
            null
        )

        if (cursor != null && cursor.count > 0) {
            adapter.setData(cursor)
        }
    }
}
