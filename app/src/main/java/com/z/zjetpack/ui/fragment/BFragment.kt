package com.z.zjetpack.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.z.zjetpack.databinding.FragmentBBinding
import com.z.zjetpack.viewmodelandlivedata.SeekViewModel

class BFragment : Fragment() {
    private lateinit var binding: FragmentBBinding
    private val seekViewModel by activityViewModels<SeekViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekViewModel.cprogress.observe(viewLifecycleOwner, Observer {
            binding.seekbarTest.progress = it
            Log.v("zx","BBB返回$it")
        })


        binding.seekbarTest.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.v("zx","$p1")
                seekViewModel.setProgress(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

    }
}