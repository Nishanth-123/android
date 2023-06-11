package com.example.custom_camera.fragments

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.custom_camera.TestViewModel
import com.example.custom_camera.activities.TestActivity
import com.example.custom_camera.databinding.FragmentTest2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Test2Fragment : Fragment() {

    private lateinit var viewModel: TestViewModel
    private var _binding: FragmentTest2Binding? = null
    private val binding get() = _binding!!
    private val LOWER_EV = -12
    private val HIGHER_EV = 12

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentTest2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TestViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as TestActivity).checkPermissions()
        takePictureTest()
        viewModel.sendImageResponse.observe(this) { response ->
            if (response != null && response == true) {
                binding.test2Status.text = "Test Done"
            } else if (response != null && response == false) {
                binding.test2Status.text = "Test Failed"
            }
        }
    }

    private fun takePictureTest() {
        lifecycleScope.launch(Dispatchers.IO) {
            for (ev in LOWER_EV..HIGHER_EV) {
                val camera: Camera = Camera.open()
                camera.parameters.set("iso", "100")
                camera.parameters.focusMode = Camera.Parameters.FOCUS_MODE_MACRO
                camera.parameters.exposureCompensation = ev
                val surfaceTexture = SurfaceTexture(10)
                camera.setPreviewTexture(surfaceTexture)
                camera.startPreview()
                camera.takePicture(null, null) { data, camera ->
                    val saveResult = viewModel.savePicture(data!!, ev)
                    if (!saveResult) {
                        Toast.makeText(requireContext(), "Image with EV : $ev could not be saved", Toast.LENGTH_SHORT).show()
                    } else {
                        if (ev == HIGHER_EV) {
                            sendPicture()
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    binding.test2Status.text = "Capturing Image for Exposure : $ev"
                }
            }
        }
    }

    private fun sendPicture() {
        binding.test2Status.text = "Sending Image"
        try {
            viewModel.sendImage()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() =
                Test2Fragment()
    }
}