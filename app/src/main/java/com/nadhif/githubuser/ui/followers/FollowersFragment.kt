package com.nadhif.githubuser.ui.followers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nadhif.githubuser.R
import com.nadhif.githubuser.adapter.UserAdapter
import com.nadhif.githubuser.ui.detail.DetailActivity
import com.nadhif.githubuser.ui.ViewModelFactory
import com.nadhif.githubuser.utils.Resource
import kotlinx.android.synthetic.main.fragment_followers.*


class FollowersFragment : Fragment() {

    companion object{
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var adapter: UserAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        followersViewModel = ViewModelProvider(this, ViewModelFactory(username!!)).get(FollowersViewModel::class.java)

        adapter = UserAdapter {
            Intent(activity, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                startActivity(this)
            }

        }
        rvFollowers.adapter = adapter
        rvFollowers.layoutManager = LinearLayoutManager(activity)

        followersViewModel.followers.observe(viewLifecycleOwner, {
            when(it) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    adapter.submitList(it.data)
                    progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}