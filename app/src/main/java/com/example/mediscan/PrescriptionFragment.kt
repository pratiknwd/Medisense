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
import com.example.mediscan.databinding.FragmentPrescriptionBinding

private const val PRESCRIPTION_PROMPT = "I have an image of a prescription, and I need you to extract the following details in a structured JSON Array with proper brackets []format:\n" +
        "\n" +
        "Medicine name\n" +
        "Dose\n" +
        "Frequency (how many times per day)\n" +
        "Duration (how many days)\n" +
        "Times (when the medicine should be taken)\n" +
        "Rules for frequency and times:\n" +
        "If \"every X hours\" is mentioned, calculate how many times the medicine should be taken in a day based on a 24-hour day, subtracting 10 hours for sleep. If the frequency isn't specified, assume 3 times per day, with the first dose at 08:00 AM.\n" +
        "If \"bedtime\" is mentioned, the medicine is taken once at 21:00.\n" +
        "Example:\n" +
        "For \"Take 1 tablet of Paracetamol 500mg every 6 hours for 5 days.\"\n" +
        "\n" +
        "JSON format:\n" +
        "\n" +
        "json\n" +
        "Copy\n" +
        "{\n" +
        "  \"medicine_name\": \"Paracetamol\",\n" +
        "  \"dose\": \"500mg\",\n" +
        "  \"frequency\": \"3 times per day\",\n" +
        "  \"duration\": \"5 days\",\n" +
        "  \"times\": [\n" +
        "    \"08:00 AM\",\n" +
        "    \"02:00 PM\",\n" +
        "    \"08:00 PM\"\n" +
        "  ]\n" +
        "}\n" +
        "If it says \"take at bedtime,\" return this:\n" +
        "\n" +
        "json\n" +
        "Copy\n" +
        "{\n" +
        "  \"medicine_name\": \"Paracetamol\",\n" +
        "  \"dose\": \"500mg\",\n" +
        "  \"frequency\": \"once at bedtime\",\n" +
        "  \"duration\": \"5 days\",\n" +
        "  \"times\": [\n" +
        "    \"09:00 PM\"\n" +
        "  ]\n" +
        "}"

class PrescriptionFragment : BaseFragment() {
    private var _binding: FragmentPrescriptionBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private lateinit var viewModel: SharedViewModel
    
    
    private val pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            this.uri = uri
            val bitmapFromUri = ImageUtil.getBitmapFromUri(requireContext(), uri)
            binding.imageView.setImageBitmap(bitmapFromUri)
            viewModel.getResponse(bitmapFromUri, PRESCRIPTION_PROMPT) { s: String? ->
                viewModel.getPrescription(s) { s -> binding.prescriptionTextView.text = s }
            }
        }
    }
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        Log.d("9155881234", "created $FRAG_NAME")
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPrescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chooseImageBtn.setOnClickListener { pickImageLauncher.launch("image/*") }
    }
    
    private fun onSendPrescription() {
    
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, FRAG_NAME)
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
        const val FRAG_NAME = "Prescription"
        
        @JvmStatic
        fun newInstance() = PrescriptionFragment()
    }
}