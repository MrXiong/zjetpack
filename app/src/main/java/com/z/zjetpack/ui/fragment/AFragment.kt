package com.z.zjetpack.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.z.zjetpack.R
import com.z.zjetpack.databinding.FragmentABinding
import com.z.zjetpack.viewmodelandlivedata.MyViewModel
import com.z.zjetpack.viewmodelandlivedata.SeekViewModel

class AFragment : Fragment() {

    private val seekViewModel by activityViewModels<SeekViewModel>()
    private lateinit var binding :FragmentABinding

    //可以让fragment和activity通信
    //那么fragment和fragment就可以通信了，因为frament可以先给activity，然后activity再给另一个fragment
    private val myViewModel by activityViewModels<MyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentABinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekViewModel.cprogress.observe(viewLifecycleOwner, Observer {
            binding.seekbarTest.progress = it
            Log.v("zx","AAA返回$it")
        })

        myViewModel.count.observe(viewLifecycleOwner, Observer {
            Log.v("zx","AAA返回$it")
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