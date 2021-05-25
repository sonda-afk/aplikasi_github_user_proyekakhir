package com.sonda.bangkit.fundamentaltiga.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sonda.bangkit.fundamentaltiga.R
import com.sonda.bangkit.fundamentaltiga.adapter.ViewPagerAdapter
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.COMPANY
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.CONTENT_URI
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.FAVORITE
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.FOLLOWERS
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.FOLLOWING
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.IMAGE
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.LOCATION
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.NAME
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.REPOSITORY
import com.sonda.bangkit.fundamentaltiga.database.Database.Columns.Companion.USERNAME
import com.sonda.bangkit.fundamentaltiga.database.FavoriteHelper
import com.sonda.bangkit.fundamentaltiga.model.Favorite
import com.sonda.bangkit.fundamentaltiga.model.User

class UserDetail : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var username: TextView
    private lateinit var name: TextView
    private lateinit var avatar: ImageView
    private lateinit var location: TextView
    private lateinit var company: TextView
    private lateinit var repository: TextView
    private lateinit var following: TextView
    private lateinit var followers: TextView
    private lateinit var favHelper: FavoriteHelper

    private lateinit var image: String
    private var checkFav = false
    private var favorites: Favorite? = null

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAV = "extra_fav"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }

    private fun viewPagerSetUp() {
        val viewpagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewpagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.title = "Detail User"

        name = findViewById(R.id.detail_name)
        avatar = findViewById(R.id.detail_avatar)
        location = findViewById(R.id.detail_location)
        company = findViewById(R.id.detail_company)
        repository = findViewById(R.id.detail_repository)
        following = findViewById(R.id.detail_following)
        followers = findViewById(R.id.detail_followers)
        username = findViewById(R.id.detail_username)

        fab = findViewById(R.id.add_favorite)
        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)


        val back: TextView = findViewById(R.id.detail_back)
        back.setOnClickListener(this)

        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.openDatabase()

        favorites = intent.getParcelableExtra(EXTRA_FAV)
        if (favorites != null) {
            checkFav = true

            val favUser = intent.getParcelableExtra<Favorite>(EXTRA_FAV) as Favorite
            name.text = favUser.name
            location.text = favUser.location
            company.text = favUser.company
            repository.text = favUser.repository
            following.text = favUser.following
            followers.text = favUser.followers
            username.text = favUser.username

            Glide.with(this)
                    .load(favUser.avatar)
                    .apply(RequestOptions())
                    .into(avatar)
            fab.setColorFilter(application.resources.getColor(R.color.teal_200))
            image = favUser.avatar.toString()

        } else {
            val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
            name.text = user.name
            location.text = user.location
            company.text = user.company
            repository.text = user.repository
            following.text = user.following
            followers.text = user.followers
            username.text = user.username

            Glide.with(this)
                    .load(user.avatar)
                    .apply(RequestOptions())
                    .into(avatar)
            image = user.avatar.toString()
        }

        viewPagerSetUp()
        fab.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.detail_back -> {
                val moveIntent = Intent(this@UserDetail, MainActivity::class.java)
                moveIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

                startActivity(moveIntent)
            }
            R.id.add_favorite -> {
                if (checkFav) {
                    favHelper.delete(favorites?.username.toString())
                    Toast.makeText(this, "Data Has Been Deleted From Favorite", Toast.LENGTH_SHORT)
                            .show()
                    fab.setColorFilter(application.resources.getColor(R.color.grey))
                    checkFav = false
                } else {
                    checkFav = true
                    val values = ContentValues()
                    val dbusername = username.text.toString()
                    val dbavatar = image
                    val dbname = name.text.toString()
                    val dblocation = location.text.toString()
                    val dbcompany = company.text.toString()
                    val dbrepository = repository.text.toString()
                    val dbfollowing = following.text.toString()
                    val dbfollowers = followers.text.toString()
                    val dbFavorite = "1"

                    values.put(USERNAME, dbusername)
                    values.put(IMAGE, dbavatar)
                    values.put(NAME, dbname)
                    values.put(LOCATION, dblocation)
                    values.put(REPOSITORY, dbrepository)
                    values.put(COMPANY, dbcompany)
                    values.put(FOLLOWING, dbfollowing)
                    values.put(FOLLOWERS, dbfollowers)
                    values.put(FAVORITE, dbFavorite)

                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, "Data Has Been Added To Favorite", Toast.LENGTH_SHORT).show()
                    fab.setColorFilter(application.resources.getColor(R.color.teal_200))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.closeDatabase()
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
            val intent = Intent(this@UserDetail, Notifications::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.action_favorite) {
            val intent = Intent(this@UserDetail, FavoriteActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


}