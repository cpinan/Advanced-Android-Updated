package com.carlospinan.android_me_kotlin.ui.fragments.body

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carlospinan.android_me_kotlin.R
import com.carlospinan.android_me_kotlin.databinding.FragmentBodyPartBinding

class BodyPartFragment : Fragment() {

    companion object {
        private const val INDEX_KEY = "INDEX"
        private const val LIST_KEY = "LIST"

        fun create(index: Int, list: ArrayList<Int>): BodyPartFragment {
            val fragment = BodyPartFragment()
            val bundle = Bundle()
            bundle.putInt(INDEX_KEY, index)
            bundle.putIntegerArrayList(LIST_KEY, list)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentBodyPartBinding
    private lateinit var viewModel: BodyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBodyPartBinding.inflate(inflater)
        arguments?.let { args ->
            val index = args.getInt(INDEX_KEY)
            val list = args.getIntegerArrayList(LIST_KEY) as ArrayList<Int>
            val viewModelFactory =
                BodyViewModelFactory(index, list)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(BodyViewModel::class.java)
        }

        if (!resources.getBoolean(R.bool.isTablet)) {
            binding.bodyPartImageView.setOnClickListener {
                viewModel.updateImageResource()
            }
        }

        viewModel.resource.observe(
            this,
            Observer { value ->
                binding.bodyPartImageView.setImageResource(value)
            }
        )

        return binding.root
    }

}