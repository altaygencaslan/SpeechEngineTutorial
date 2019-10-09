package com.example.speechtotext_tutorial1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onPartialResults(partialResults: Bundle?) {
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(error: Int) {
    }

    override fun onResults(results: Bundle?) {
        var matches: ArrayList<String>? = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        if (matches != null)
            txtSpeechInput.text = matches.get((0))

        var speech: String = txtSpeechInput.text.toString().toLowerCase()
        var write: String = edtSpeechThis.text.toString().toLowerCase()
        if (speech.equals(write)) {
            Toast.makeText(this, "Tebrikler! =)", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Tekrar denemelisin =( !", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        promtSpeechRecognizer()
        promtTextToSpeech()

        var intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))

        btnSpeak.setOnTouchListener { v,
                                      event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    speechRecognizer.stopListening()
                }

                MotionEvent.ACTION_DOWN -> {
                    speechRecognizer.startListening(intent)
                }

            }

            return@setOnTouchListener true
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                var intent: Intent = Intent(
                    android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName())
                )
                startActivity(intent)
                finish()
            }
        }
    }

    lateinit var speechRecognizer: SpeechRecognizer
    private fun promtSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)
    }

    lateinit var textToSpeech: TextToSpeech
    private fun promtTextToSpeech() {
        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {

        })
        textToSpeech.setLanguage(Locale.getDefault())
    }

    fun btnSpeechThis_Click(v: View) {

        textToSpeech.speak(edtSpeechThis.text, TextToSpeech.QUEUE_ADD, null, "")
    }
}
