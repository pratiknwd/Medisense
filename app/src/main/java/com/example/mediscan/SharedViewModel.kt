package com.example.mediscan

import android.app.Application
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

private const val API_KEY = "AIzaSyB5h_FCHGPN_Fp-egPHqqxKU4NfHf6eqgs"
private const val MODEL_NAME_1 = "gemini-1.5-flash"
private const val MODEL_NAME_2 = "gemini-pro-vision"
private const val GEMINI_TAG = "Gemini"
private const val TTS_TAG = "TTS"

class SharedViewModel(applicationContext: Application) : AndroidViewModel(applicationContext) {
    private lateinit var tts: TextToSpeech
    private var generativeModel: GenerativeModel
    
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
    
    fun getResponse(bitmap: Bitmap, prompt: String, callback: (String?) -> Unit) {
        callback("loading response ...")
        CoroutineScope(Dispatchers.Main).launch {
            val response = getResponseFromGemini(bitmap, prompt)
            callback(response)
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
    
    override fun onCleared() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onCleared()
    }
}