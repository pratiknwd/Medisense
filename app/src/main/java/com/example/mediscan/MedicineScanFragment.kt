package com.example.mediscan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.mediscan.databinding.FragmentMedicineScanBinding


private const val MEDICINE_URI = "medicine_uri"
private const val DEFAULT_PROMPT = "I have an image of a medicine strip, specifically the backside, which contains important information such as the medicine's name. I want you to precisely extract and return only the name of the medicine from this image. Please ensure the result is accurate and reliable, as the information is crucial for safety. The name should be exactly as it appears on the strip, with no additional context or explanation."

class MedicineScanFragment : BaseFragment() {
    private var _binding: FragmentMedicineScanBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private lateinit var viewModel: SharedViewModel
    private var response: String? = null
    
    private val pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            this.uri = uri
            val bitmapFromUri = ImageUtil.getBitmapFromUri(requireContext(), uri)
            viewModel.getResponse(bitmapFromUri, DEFAULT_PROMPT) { s: String? ->
                if (!s.isNullOrBlank()) {
                    
                    this.response = s
                    binding.textView.text = s
                    viewModel.startSpeech(s)
                }
            }
            binding.imageView.setImageBitmap(bitmapFromUri)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        Log.d("9155881234", "created $FRAG_NAME")
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMedicineScanBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            uri = Uri.parse(it.getString(MEDICINE_URI, ""))
            binding.imageView.setImageBitmap(ImageUtil.getBitmapFromUri(requireContext(), uri!!))
        }
        binding.startTTS.setOnClickListener { response?.let { it1 -> viewModel.startSpeech(it1) } }
        binding.stopTTS.setOnClickListener { viewModel.stopSpeech() }
        binding.chooseImageBtn.setOnClickListener { pickImageLauncher.launch("image/*") }
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, FRAG_NAME)
    }
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d("9155881234", "destroyed $FRAG_NAME")
    }
    
    companion object {
        val FRAG_NAME = "Scan Medicine"
        
        @JvmStatic
        fun newInstance() = MedicineScanFragment()
    }
}