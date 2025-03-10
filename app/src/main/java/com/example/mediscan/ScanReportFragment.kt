package com.example.mediscan

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.mediscan.databinding.ActivityScanReportBinding
import com.example.mediscan.report.ReportModelItem
import java.io.File

private const val REPORT_PROMPT: String = """You are Senior Doctor and an expert in analyzing health reports.
    The question is delimited by <input> and </input>. Your task is to generate a response in json with
    - \"test_name\" field MUST be test name mentioned in the report
    - \"test_value\" field MUST be value of respective test_name
    - \"units\": field MUST be units mentioned in report for respective test_name
    - \"bio reference interval\": field MUST be reference interval mentioned for respective test_name
    - \"minimum_value\": field MUST be minimum value of reference interval
     - \"maximum_value\": field MUST be maximum value of reference interval
     - \"explanation\": field MUST be explanation of respective test_name
    "\n\n"
    <input>
    report:{image_file}
    </input>
    answer"""

class ScanReportFragment : BaseFragment() {
    private var _binding: ActivityScanReportBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private lateinit var sharedViewModel: SharedViewModel
    
    
    private val pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            this.imageUri = it
            val bitmapFromUri = ImageUtil.getBitmapFromUri(requireContext(), it)
            binding.imageView.setImageBitmap(bitmapFromUri)
            sharedViewModel.getResponseForReport(bitmapFromUri, REPORT_PROMPT)
        }
    }
    
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val bitmapFromUri = imageUri?.let { ImageUtil.getBitmapFromUri(requireContext(), it) }
            binding.imageView.setImageBitmap(bitmapFromUri)
            if (bitmapFromUri != null) {
                sharedViewModel.getResponseForReport(bitmapFromUri, REPORT_PROMPT)
            }
        }
    }
    
    private fun showImagePickerDialog(context: Context) {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(context)
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera(context)
                    1 -> openGallery()
                }
            }
            .show()
    }
    
    private fun openCamera(context: Context) {
        imageUri = createImageUri(context)
        takePictureLauncher.launch(imageUri)
    }
    
    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }
    
    private fun createImageUri(context: Context): Uri {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityScanReportBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.report.observe(viewLifecycleOwner) {
            it ?: return@observe
            Log.d("9155881234", "frag = $FRAG_NAME, state = $it")
            binding.reportTextView.text = when (it) {
                P_STATES.LOADING -> "Loading"
                P_STATES.ERROR -> "Could not identify report. Please upload a report image."
                P_STATES.EMPTY -> "Empty"
                P_STATES.NOT_EMPTY -> {
                    getFormattedReport(sharedViewModel.reportModel)
                }
            }
        }
        
        binding.chooseReportBtn.setOnClickListener {
//            pickImageLauncher.launch("image/*")
            showImagePickerDialog(requireContext())
        }
    }
    
    
    private fun getFormattedReport(list: List<ReportModelItem>): String {
        val sb = StringBuilder()
        list.forEach {
            sb.append(it.toString()).append("\n\n")
        }
        return sb.toString()
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
        const val FRAG_NAME = "ScanReport"
        
        @JvmStatic
        fun newInstance() = ScanReportFragment()
    }
}