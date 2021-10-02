package com.nadhif.githubuser.ui.main.fragment

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
import com.nadhif.githubuser.ui.main.MainViewModel
import com.nadhif.githubuser.utils.Resource
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        adapter = UserAdapter {
            Intent(activity, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                startActivity(this)
            }
        }


        rvUser.adapter = adapter
        rvUser.layoutManager = LinearLayoutManager(activity)

        mainViewModel.users.observe(viewLifecycleOwner, {
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