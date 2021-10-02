package com.nadhif.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.nadhif.githubuser.R
import com.nadhif.githubuser.adapter.ViewPagerAdapter
import com.nadhif.githubuser.network.response.User
import com.nadhif.githubuser.ui.followers.FollowersFragment
import com.nadhif.githubuser.ui.following.FollowingFragment
import com.nadhif.githubuser.utils.Resource
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val FOLLOWERS = 0
        const val FOLLOWING = 1
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        detailViewModel = ViewModelProvider(this, DetailViewModel.Factory(application, username!!))
            .get(DetailViewModel::class.java)

        detailViewModel.checkUser(username)?.observe(this, {
            if (it.isNotEmpty()) {
                isFavorite = true
                btnFavorite.text = getString(R.string.favorited)
            }
        })

        detailViewModel.detailUser.observe(this, {
            when(it) {
                is Resource.Loading -> {
                    progressBarDetail.visibility = View.VISIBLE
                    mainAppBar.visibility = View.GONE
                    nestedScrollView.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressBarDetail.visibility = View.GONE
                    mainAppBar.visibility = View.VISIBLE
                    nestedScrollView.visibility = View.VISIBLE

                    it.data.let { data ->
                        tvUsername.text = data.login
                        tvName.text = data.name.toString()
                        tvCompany.text = data.company.toString()
                        tvLocation.text = data.location.toString()
                        tvFollowing.text = data.following
                        tvFollower.text = data.followers
                        tvRepository.text = data.repos

                        Glide.with(this)
                            .load(data.avatarUrl)
                            .circleCrop()
                            .into(ivUser)

                        btnFavorite.setOnClickListener {
                            val user = User(data.id, data.login, data.avatarUrl)
                            if (isFavorite) {
                                isFavorite = false
                                detailViewModel.deleteUser(user)
                                btnFavorite.text = getString(R.string.favorite)
                                Toast.makeText(
                                    this,
                                    "Berhasil Menghapus Favorit",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                isFavorite = true
                                detailViewModel.saveUser(user)
                                btnFavorite.text = getString(R.string.favorited)
                                Toast.makeText(
                                    this,
                                    "Berhasil Menambahkan Favorit",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        })

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPagerAdapter.addFragment(FollowersFragment.newInstance(username))
        viewPagerAdapter.addFragment(FollowingFragment.newInstance(username))
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                FOLLOWERS -> tab.text = getString(R.string.followers)
                FOLLOWING -> tab.text = getString(R.string.following)
            }
        }.attach()
    }
}