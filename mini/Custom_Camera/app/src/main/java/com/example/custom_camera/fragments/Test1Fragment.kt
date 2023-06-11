package com.example.custom_camera.fragments

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.custom_camera.R
import com.example.custom_camera.TestViewModel
import com.example.custom_camera.activities.TestActivity
import com.example.custom_camera.databinding.FragmentTest1Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt


class Test1Fragment : Fragment() {

    companion object {
        fun newInstance() = Test1Fragment()
    }

    private lateinit var viewModel: TestViewModel
    private var _binding: FragmentTest1Binding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTest1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TestViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as TestActivity).checkPermissions()
        takeAndSavePicture()
    }


    private fun takeAndSavePicture() {
        lifecycleScope.launch(Dispatchers.IO) {
            val camera: Camera = Camera.open()
            val surfaceTexture = SurfaceTexture(10)
            camera.setPreviewTexture(surfaceTexture)
            camera.startPreview()
            camera.parameters.set("iso", "100")
            camera.parameters.focusMode = Camera.Parameters.FOCUS_MODE_MACRO
            camera.takePicture(null, null) { data, camera ->
                val isSaved = viewModel.savePicture(data!!, null)
                if (isSaved) {
                    startProgressBar()
                }
            }
        }
    }

    private fun startProgressBar() {
        binding.circularProgressBar.visibility = VISIBLE
        binding.progressText.visibility = VISIBLE
        object : CountDownTimer(300000, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                //just rounding off to an integer which is divisible 5 as millis would be in not in an exact amount we expected
                val secondsRemaining = 5 * floor(((millisUntilFinished / 1000f) + 2.5) / 5).toInt()
                binding.circularProgressBar.progress = (((300 - secondsRemaining) * 100) / 300f).roundToInt()
                binding.progressText.text = "$secondsRemaining secs"
            }

            override fun onFinish() {
                findNavController().navigate(R.id.action_test1Fragment_to_test2Fragment)
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}