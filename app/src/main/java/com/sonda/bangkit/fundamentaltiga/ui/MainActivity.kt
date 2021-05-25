package com.sonda.bangkit.fundamentaltiga.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonda.bangkit.fundamentaltiga.R
import com.sonda.bangkit.fundamentaltiga.adapter.UserAdapter
import com.sonda.bangkit.fundamentaltiga.model.User
import com.sonda.bangkit.fundamentaltiga.ui.viewModel.MainVM

class MainActivity : AppCompatActivity() {

    private lateinit var rvShow: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvShow = findViewById(R.id.rv_user)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.search)

        progressBar.visibility = View.VISIBLE
        showRecylcerConfig()
        showMainViewModelConfig()
        fetchData()
        searchData()
    }

    private fun fetchData() {
        viewModel.fetchDataUser()
        viewModel.getUser().observe(this, Observer { data ->

            if (data != null) {
                progressBar.visibility = View.GONE
                userAdapter.listUser = data
            }
        })


    }


    private fun showMainViewModelConfig() {
        viewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
        ).get(MainVM::class.java)
    }

    private fun showRecylcerConfig() {
        rvShow.setHasFixedSize(true)
        rvShow.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()
        rvShow.adapter = userAdapter


        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, UserDetail::class.java)
                intent.putExtra(UserDetail.EXTRA_USER, data)
                startActivity(intent)
            }
        })

    }


    private fun searchData() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    progressBar.visibility = View.VISIBLE
                    viewModel.fetchDataUserSearch(query)
                    Toast.makeText(applicationContext, "" + query, Toast.LENGTH_SHORT).show()

                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        viewModel.getUserSearch().observe(this@MainActivity, Observer { data ->
            progressBar.visibility = View.VISIBLE
            if (data != null) {
                progressBar.visibility = View.GONE
                userAdapter.listUser = data
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        } else if (item.itemId == R.id.action_notifications) {
            val intent = Intent(this@MainActivity, Notifications::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.action_favorite) {
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


}