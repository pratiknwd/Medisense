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
import com.example.mediscan.prescription.PrescriptionModelItem

private const val PRESCRIPTION_PROMPT: String = """
Extract the following details from the prescription in a structured JSON format:
Medicine name
Dose
Frequency (how many times per day)
Duration (how many days)
Times (when the medicine should be taken)
Food instructions (before or after food)
Rules for frequency, times, and food instructions:
If "every X hours" is mentioned, calculate the number of doses per day based on a 24-hour period, subtracting 10 hours for sleep. Distribute the doses evenly throughout the waking hours.

Example: "Take 1 tablet every 6 hours for 5 days after food."
{
  "medicine_name": "Paracetamol",
  "dose": "500mg",
  "frequency": "3 times per day",
  "duration": "5 days",
  "times": ["08:00 AM", "02:00 PM", "08:00 PM"],
  "food_instruction": "after food"
}
If "bedtime" is mentioned, the medicine is taken once at 09:00 PM.

Example: "Take at bedtime for 5 days before food."
{
  "medicine_name": "Paracetamol",
  "dose": "500mg",
  "frequency": "once at bedtime",
  "duration": "5 days",
  "times": ["09:00 PM"],
  "food_instruction": "after food"
}
If the frequency is given in "1-0-1" format (morning-noon-night):

"1" means the medicine should be taken, and "0" means it should be skipped.
Example: "1-0-1 for 7 days, after food."
{
  "medicine_name": "Amoxicillin",
  "dose": "250mg",
  "frequency": "2 times per day",
  "duration": "7 days",
  "times": ["08:00 AM", "08:00 PM"],
  "food_instruction": "after food"
}
If no frequency is specified, assume 3 times per day at 08:00 AM, 02:00 PM, and 08:00 PM.

Example: "Take for 5 days before food."
{
  "medicine_name": "Ibuprofen",
  "dose": "400mg",
  "frequency": "3 times per day",
  "duration": "5 days",
  "times": ["08:00 AM", "02:00 PM", "08:00 PM"],
  "food_instruction": "before food"
}
Fallback Answer:
If no prescription details are provided or if the prescription cannot be interpreted, respond with the following:

json
Copy
{
  "message": "Could not identify prescription. Please upload a prescription image."
}
"""

class PrescriptionFragment : BaseFragment() {
    private var _binding: FragmentPrescriptionBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private lateinit var sharedViewModel: SharedViewModel
    
    
    private val pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            this.uri = it
            val bitmapFromUri = ImageUtil.getBitmapFromUri(requireContext(), it)
            binding.imageView.setImageBitmap(bitmapFromUri)
            sharedViewModel.getResponseForPrescription(bitmapFromUri, PRESCRIPTION_PROMPT)
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
        _binding = FragmentPrescriptionBinding.inflate(inflater, container, false)
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
        
        binding.chooseImageBtn.setOnClickListener { pickImageLauncher.launch("image/*") }
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