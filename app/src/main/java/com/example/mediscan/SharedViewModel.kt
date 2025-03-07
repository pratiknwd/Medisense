package com.example.mediscan

import android.app.Application
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediscan.prescription.PrescriptionModel
import com.example.mediscan.prescription.PrescriptionModelItem
import com.example.mediscan.report.ReportModel
import com.example.mediscan.report.ReportModelItem
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

private const val API_KEY = "AIzaSyB5h_FCHGPN_Fp-egPHqqxKU4NfHf6eqgs"
private const val MODEL_NAME_1 = "gemini-1.5-flash"
private const val MODEL_NAME_2 = "gemini-pro-vision"
private const val GEMINI_TAG = "Gemini"
private const val TTS_TAG = "TTS"

enum class P_STATES {
    LOADING,
    ERROR,
    EMPTY,
    NOT_EMPTY
}

class SharedViewModel(applicationContext: Application) : AndroidViewModel(applicationContext) {
    private lateinit var tts: TextToSpeech
    private var generativeModel: GenerativeModel
    private val _prescription = MutableLiveData<P_STATES>()
    val prescription: LiveData<P_STATES> get() = _prescription
    var prescriptionModel: MutableList<PrescriptionModelItem> = mutableListOf()

    private val _report = MutableLiveData<P_STATES>()
    private val report: LiveData<P_STATES> get() = _report
    var reportModel: MutableList<ReportModelItem> = mutableListOf()
    
    init {
        val generationConfig = generationConfig {
            temperature = 0.4f
        }
        generativeModel = GenerativeModel(
            modelName = MODEL_NAME_1,
            apiKey = API_KEY,
            generationConfig = generationConfig
        )
        
        val ttsOnInitListener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.ENGLISH) // Set language to US English
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TTS_TAG, "Language not supported!")
                }
            } else {
                Log.e(TTS_TAG, "Initialization failed!")
            }
        }
        
        tts = TextToSpeech(applicationContext, ttsOnInitListener)
    }
    
    fun startSpeech(text: String) {
        if (text.isNotEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
    
    fun stopSpeech() = tts.stop()
    
    fun getResponseForPrescription(bitmap: Bitmap, prompt: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _prescription.postValue(P_STATES.LOADING)
            val response = getResponseFromGemini(bitmap, prompt)
            getPrescription(response)
        }
    }
    
    fun getResponseForMedicine(bitmap: Bitmap, prompt: String, callback: (String?) -> Unit) {
        callback("loading response ...")
        CoroutineScope(Dispatchers.Main).launch {
            val response = getResponseFromGemini(bitmap, prompt)
            callback(response)
        }
    }

    fun getResponseForReport(bitmap: Bitmap, prompt: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _report.postValue(P_STATES.LOADING) // Update state for UI
            val response = getResponseFromGemini(bitmap, prompt) // Get response from Gemini
            getReport(response) // Process the response
        }
    }


    private suspend fun getResponseFromGemini(bitmap: Bitmap, prompt: String): String? {
        try {
            val inputContent: com.google.ai.client.generativeai.type.Content = content {
                image(bitmap)
                text(prompt)
            }
            val response = generativeModel.generateContent(inputContent)
            return response.text
        } catch (e: Exception) {
            Log.e(GEMINI_TAG, "exception: $e")
            return null
        }
    }

    suspend fun getResponseFromGemini(query: String): String? {
        return try {
            val response = generativeModel.generateContent(query)
            response.text ?: "No response received"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }


    override fun onCleared() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onCleared()
    }

    private fun getPrescription(json: String?) {
        val trimmedJson = trimJson(json)
        val model: PrescriptionModel? = formatJsonForPrescription(trimmedJson)
        if (model == null) {
            _prescription.postValue(P_STATES.ERROR)
            return
        }
        val modelList: List<PrescriptionModelItem> = model.toList()
        if (modelList.isEmpty()) {
            _prescription.postValue(P_STATES.EMPTY)
            return
        }
        prescriptionModel.clear()
        prescriptionModel.addAll(modelList)
        _prescription.postValue(P_STATES.NOT_EMPTY)
    }

    private fun getReport(json: String?) {
        val trimmedJson = trimJson(json)
        val model: ReportModel? = formatJsonForReport(trimmedJson) // Convert JSON to ReportModel
        if (model == null) {
            _report.postValue(P_STATES.ERROR)
            return
        }
        val modelList: List<ReportModelItem> = model.toList() // Convert to list
        if (modelList.isEmpty()) {
            _report.postValue(P_STATES.EMPTY)
            return
        }
        reportModel.clear()
        reportModel.addAll(modelList)
        _report.postValue(P_STATES.NOT_EMPTY)
    }

    private fun trimJson(json: String?): String? {
        json ?: return null
        var trim = json.trim().trimIndent()
        trim = trim.substring(7, trim.length - 3)
        return trim
    }
    
    private fun formatJsonForPrescription(json: String?): PrescriptionModel? {
        return try {
            if (json.isNullOrBlank()) {
                Log.e("9155881234", "JSON is null or empty")
                null
            } else {
                Gson().fromJson(json, PrescriptionModel::class.java)
            }
        } catch (e: JsonSyntaxException) {
            Log.e("9155881234", "Error parsing JSON: ${e.message}")
            null
        }
    }

    private fun formatJsonForReport(json: String?): ReportModel? {
        return try {
            Gson().fromJson(json, ReportModel::class.java)
        } catch (e: Exception) {
            Log.e("JSON_PARSE", "Error parsing report JSON: ${e.message}")
            null
        }
    }

    fun getResponseForQuery(query: String, callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            _prescription.postValue(P_STATES.LOADING) // Show loading state if needed
            val response = getResponseFromGemini(query) // Call Gemini API
            callback(response) // Send result back to UI
        }
    }

}