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
import com.example.mediscan.databinding.ActivityScanReportBinding
import com.example.mediscan.prescription.PrescriptionModelItem

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

class ScanReportFragment : BaseFragment(){
    private var _binding: ActivityScanReportBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private lateinit var sharedViewModel: SharedViewModel


    private val pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            this.uri = it
            val bitmapFromUri = ImageUtil.getBitmapFromUri(requireContext(), it)
            binding.imageView.setImageBitmap(bitmapFromUri)
            sharedViewModel.getResponseForPrescription(bitmapFromUri, REPORT_PROMPT)
        }
    }


    override fun onNavigationIconClick(iconTapped: View?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        Log.d("9155881234", "created $FRAG_NAME")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityScanReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.prescription.observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.prescriptionTextView.text = when (it) {
                P_STATES.LOADING -> "Loading"
                P_STATES.ERROR -> "Could not identify prescription. Please upload a prescription image."
                P_STATES.EMPTY -> "Empty"
                P_STATES.NOT_EMPTY -> getFormattedPrescription(sharedViewModel.prescriptionModel)
            }
        }

        binding.chooseReportBtn.setOnClickListener { pickImageLauncher.launch("image/*") }
    }

    private fun getFormattedPrescription(list: List<PrescriptionModelItem>): String {
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
        const val FRAG_NAME = "Prescription"

        @JvmStatic
        fun newInstance() = PrescriptionFragment()
    }
}