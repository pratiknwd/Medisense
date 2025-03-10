package com.example.mediscan

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.mediscan.databinding.FragmentMedicineScanBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File


private const val MEDICINE_URI = "medicine_uri"
private const val DEFAULT_PROMPT = "I have an image of a medicine strip, specifically the backside, which contains important information such as the medicine's name. I want you to precisely extract and return only the name of the medicine from this image. Please ensure the result is accurate and reliable, as the information is crucial for safety. The name should be exactly as it appears on the strip, with no additional context or explanation."

class MedicineScanFragment : BaseFragment() {
    private var _binding: FragmentMedicineScanBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private lateinit var sharedViewModel: SharedViewModel
    private var response: String? = null
    private var llmresponse: String? = null
    
    private val pickImageLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            this.imageUri = uri
            val bitmapFromUri = ImageUtil.getBitmapFromUri(requireContext(), uri)
            sharedViewModel.getResponseForMedicine(bitmapFromUri, DEFAULT_PROMPT) { s: String? ->
                if (!s.isNullOrBlank()) {
                    
                    this.response = s
                    binding.textView.text = s
                    sharedViewModel.startSpeech(s)
                }
            }
            binding.imageView.setImageBitmap(bitmapFromUri)
        }
    }
    
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val bitmapFromUri = imageUri?.let { ImageUtil.getBitmapFromUri(requireContext(), it) }
            if (bitmapFromUri != null) {
                sharedViewModel.getResponseForMedicine(bitmapFromUri, DEFAULT_PROMPT) { s: String? ->
                    if (!s.isNullOrBlank()) {
                        
                        this.response = s
                        binding.textView.text = s
                        sharedViewModel.startSpeech(s)
                    }
                }
                binding.imageView.setImageBitmap(bitmapFromUri)
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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        Log.d("9155881234", "created $FRAG_NAME")
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMedicineScanBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            imageUri = Uri.parse(it.getString(MEDICINE_URI, ""))
            binding.imageView.setImageBitmap(ImageUtil.getBitmapFromUri(requireContext(), imageUri!!))
        }
        binding.startTTS.setOnClickListener { response?.let { it1 -> sharedViewModel.startSpeech(it1) } }
        binding.startTTS.setOnClickListener { llmresponse?.let { it1 -> viewModel.startSpeech(it1) } }
        binding.stopTTS.setOnClickListener { sharedViewModel.stopSpeech() }
        binding.chooseImageBtn.setOnClickListener {
//            pickImageLauncher.launch("image/*")
            showImagePickerDialog(requireContext())
        }
        binding.etQuery.setOnClickListener {
            if (binding.textView.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "No medicine detected!", Toast.LENGTH_SHORT).show()
            } else {
                showBottomSheet() // Show button options instead of opening the keyboard
            }
        }
        
    }
    
    private fun showBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_query, null)
        bottomSheet.setContentView(view)
        
        val btnDosage = view.findViewById<Button>(R.id.btnMedicineUsage)
        val btnSideEffects = view.findViewById<Button>(R.id.btnSideEffects)
        val btnClose = view.findViewById<Button>(R.id.btnClose) // Corrected ID
        val btnAskAnything = view.findViewById<Button>(R.id.btnAskAnything) // Custom query button
        
        btnDosage.setOnClickListener {
            binding.etQuery.requestFocus()
            
            sendQueryToLLM("What is the pharmacological class of ${binding.textView.text}, and what are its typical uses?")
            bottomSheet.dismiss()
            closeKeyboard(btnDosage, requireContext())
        }
        
        btnSideEffects.setOnClickListener {
            binding.etQuery.requestFocus()
            sendQueryToLLM("What are the side effects of ${binding.textView.text}?")
            bottomSheet.dismiss()
            closeKeyboard(btnSideEffects, requireContext())
        }
        
        btnClose.setOnClickListener {
            bottomSheet.dismiss()
            closeKeyboard(binding.etQuery, requireContext())
        }
        
        // Open keyboard for user input when "Ask Anything" is clicked
//        btnAskAnything.setOnClickListener {
//
//            // Ensure EditText is focusable
//            binding.etQuery.isFocusableInTouchMode = true
//            binding.etQuery.requestFocus()
//            openKeyboard(binding.etQuery, requireContext())
//
//
//            // Open keyboard
//            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.showSoftInput(binding.etQuery, InputMethodManager.SHOW_IMPLICIT)
//        }
        binding.etQuery.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {

                val query = binding.etQuery.text.toString().trim()
                val medicineName = binding.textView.text.toString()

                val prompt = "For the medicine $medicineName, you may relate the following question:\n\n$query"

                if (query.isNotEmpty()) {
                    sendQueryToLLM(prompt)
                }

                // Hide the keyboard after submission
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etQuery.windowToken, 0)

                true
            } else {
                false
            }
        }

        btnAskAnything.setOnClickListener {
            binding.etQuery.isFocusableInTouchMode = true
            bottomSheet.dismiss()
            binding.etQuery.requestFocus()
            openKeyboard(binding.etQuery, requireContext())
            
            
            
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etQuery, InputMethodManager.SHOW_IMPLICIT)
        }
        
        bottomSheet.show()
    }
    
    fun openKeyboard(view: View, context: Context) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    
    fun closeKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    
    
    private fun sendQueryToLLM(query: String) {
        binding.llm.text = "Getting information for You" // Show loading message
        val adjustedQuery = "$query. Provide a concise response (within 50 words) and avoid unnecessary details."
        
        sharedViewModel.getResponseForQuery(adjustedQuery) { response ->
            if (!response.isNullOrBlank()) {
                this.llmresponse = response
                binding.llm.text = response // Update UI with LLM response
                viewModel.startSpeech(response)
            } else {
                binding.llm.text = "No Available Response."
            }
        }
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