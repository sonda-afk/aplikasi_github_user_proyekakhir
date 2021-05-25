package com.sonda.bangkit.fundamentaltiga.ui

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonda.bangkit.fundamentaltiga.MappingHelper
import com.sonda.bangkit.fundamentaltiga.R
import com.sonda.bangkit.fundamentaltiga.adapter.FavoriteAdapter
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.CONTENT_URI
import com.sonda.bangkit.fundamentaltiga.model.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var rvFav: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_FAV = "EXTRA_FAV"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite List"

        rvFav = findViewById(R.id.rv_fav)
        progressBar = findViewById(R.id.progressBarFav)

        showRecylcerConfig()

        val thread = HandlerThread("DataObserver")

        thread.start()

        val handler = Handler(thread.looper)
        val observer = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                setData()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        if (savedInstanceState != null) {

            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_FAV)

            if (list != null) {
                adapter.listFav = list
            }
        } else {
            setData()
        }
    }

    private fun setData() {
        GlobalScope.launch(Dispatchers.Main) {

            progressBar.visibility = View.VISIBLE

            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursor(cursor)
            }
            val favData = deferredNotes.await()
            progressBar.visibility = View.INVISIBLE
            if (favData.size < 0) {
                adapter.listFav = ArrayList()
                Toast.makeText(this@FavoriteActivity, "Empty List Favorite", Toast.LENGTH_SHORT).show()
            } else {
                adapter.listFav = favData
            }
        }
    }

    private fun showRecylcerConfig() {
        rvFav.setHasFixedSize(true)
        rvFav.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter(this@FavoriteActivity)
        rvFav.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_FAV, adapter.listFav)
    }

    override fun onResume() {
        super.onResume()
        setData()
    }
}