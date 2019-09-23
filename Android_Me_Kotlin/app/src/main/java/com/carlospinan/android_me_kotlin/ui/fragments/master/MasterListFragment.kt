package com.carlospinan.android_me_kotlin.ui.fragments.master

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.carlospinan.android_me_kotlin.R
import com.carlospinan.android_me_kotlin.data.*
import com.carlospinan.android_me_kotlin.databinding.FragmentMasterListBinding
import com.carlospinan.android_me_kotlin.ui.AndroidMeActivity
import com.carlospinan.android_me_kotlin.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar

const val HEAD_INDEX_KEY = "HEAD_INDEX"
const val BODY_INDEX_KEY = "BODY_INDEX"
const val LEG_INDEX_KEY = "LEG_INDEX"

class MasterListFragment : Fragment() {

    private val mainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private var headIndex = 0
    private var bodyIndex = 0
    private var legIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMasterListBinding.inflate(inflater)
        val isTablet = resources.getBoolean(R.bool.isTablet)

        val adapter = MasterListAdapter(
            object : MasterClickListener {
                override fun onClick(index: Int) {
                    when (resourceType(index)) {
                        BodyType.HEAD -> {
                            headIndex = index
                            if (isTablet)
                                mainViewModel.updateHeadIndex(headIndex)
                        }
                        BodyType.BODY -> {
                            bodyIndex = index - heads.size
                            if (isTablet)
                                mainViewModel.updateBodyIndex(bodyIndex)
                        }
                        BodyType.LEG -> {
                            legIndex = index - (heads.size + bodies.size)
                            if (isTablet)
                                mainViewModel.updateLegIndex(legIndex)
                        }
                        BodyType.NONE -> throw IllegalArgumentException("MUST NOT BE HERE")
                    }
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "$index selected",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        )
        binding.masterRecyclerView.adapter = adapter
        adapter.submitList(all)
        if (!isTablet) {
            binding.nextButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(HEAD_INDEX_KEY, headIndex)
                bundle.putInt(BODY_INDEX_KEY, bodyIndex)
                bundle.putInt(LEG_INDEX_KEY, legIndex)
                val intent = Intent(activity, AndroidMeActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        } else {
            binding.nextButton.visibility = View.GONE
            binding.executePendingBindings()
        }

        return binding.root
    }

}