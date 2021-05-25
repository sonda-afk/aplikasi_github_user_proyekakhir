package com.sonda.bangkit.fundamentaltiga.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sonda.bangkit.fundamentaltiga.R
import com.sonda.bangkit.fundamentaltiga.adapter.FollowingAdapter
import com.sonda.bangkit.fundamentaltiga.model.Favorite
import com.sonda.bangkit.fundamentaltiga.model.User
import com.sonda.bangkit.fundamentaltiga.ui.viewModel.FollowingVM


class FollowingFragment : Fragment() {
    companion object {
        const val EXTRA_USER = "extra_user"

    }

    private lateinit var rvShowFollowing: RecyclerView
    private lateinit var progressBarFollowing: ProgressBar
    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var viewModel: FollowingVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_following, container, false)

        rvShowFollowing = view.findViewById(R.id.recycleShowFollowing)
        progressBarFollowing = view.findViewById(R.id.progressBarFollowing)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingAdapter = FollowingAdapter()
        showRecylcerConfig()
        showFollowingViewModelConfig()

        var favorite: Favorite? = null
        favorite = activity!!.intent.getParcelableExtra(UserDetail.EXTRA_FAV)

        if(favorite == null){
            val user = activity?.intent?.getParcelableExtra<User>(FollowersFragment.EXTRA_USER) as User
            fetchData(user.username.toString())
        }else{
            val fav = activity?.intent?.getParcelableExtra<Favorite>(FollowersFragment.EXTRA_FAV) as Favorite
            fetchData(fav.username.toString())

        }

    }

    private fun fetchData(username: String) {
        viewModel.getFollowing(username)
        activity?.let {
            viewModel.getUser().observe(it, Observer { data->

                if(data != null){
                    progressBarFollowing.visibility = View.GONE
                    followingAdapter.listUser = data
                }
            })
        }
    }


    private fun showFollowingViewModelConfig() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingVM::class.java)
    }


    private fun showRecylcerConfig() {
        rvShowFollowing.setHasFixedSize(true)
        rvShowFollowing.layoutManager = LinearLayoutManager(activity)
        rvShowFollowing.adapter = followingAdapter
    }

}