package com.example.dados2.musica

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dados2.R
import com.example.dados2.musica.ui.theme.Dados2Theme
import kotlin.random.Random

class ReproductorMusicaActivity : ComponentActivity() {
    private lateinit var soundPool: SoundPool
    private var soundId1: Int = 0
    private var soundId2: Int = 0
    private var soundId3: Int = 0
    private var streamId: Int = 0
    private var isPlaying: Boolean = false
    private var isLoaded1: Boolean = false
    private var isLoaded2: Boolean = false
    private var isLoaded3: Boolean = false
    private var isEffectApplied: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private var transitionRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                when (sampleId) {
                    soundId1 -> isLoaded1 = true
                    soundId2 -> isLoaded2 = true
                    soundId3 -> isLoaded3 = true
                }
            }
        }

        soundId1 = soundPool.load(this, R.raw.audio1, 1)
        soundId2 = soundPool.load(this, R.raw.audio2, 1)
        soundId3 = soundPool.load(this, R.raw.audio3, 1)

        setContent {
            Dados2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReproductorMusicaUI(
                        modifier = Modifier.padding(innerPadding),
                        onPlayAudio = { audioId ->
                            playAudio(audioId)
                        },
                        onApplyEffect = { effect ->
                            applyEffect(effect)
                        }
                    )
                }
            }
        }
    }

    private fun playAudio(audioId: Int) {
        if (isPlaying) {
            soundPool.stop(streamId)
        }
        when (audioId) {
            R.raw.audio1 -> if (isLoaded1) streamId = soundPool.play(soundId1, 1f, 1f, 1, -1, 1f)
            R.raw.audio2 -> if (isLoaded2) streamId = soundPool.play(soundId2, 1f, 1f, 1, -1, 1f)
            R.raw.audio3 -> if (isLoaded3) streamId = soundPool.play(soundId3, 1f, 1f, 1, -1, 1f)
        }
        isPlaying = true
    }

    private fun applyEffect(effect: String) {
        if (!isPlaying) return
        if (isEffectApplied) {
            soundPool.setRate(streamId, 1f)
            soundPool.setVolume(streamId, 1f, 1f)
            transitionRunnable?.let { handler.removeCallbacks(it) }
            isEffectApplied = false
        } else {
            when (effect) {
                "VELOCIDAD" -> soundPool.setRate(streamId, 1.5f)
                "OREJA_DERECHA" -> soundPool.setVolume(streamId, 1f, 0f)
                "OREJA_IZQUIERDA" -> soundPool.setVolume(streamId, 0f, 1f)
                "TRANSICION" -> startTransitionEffect()
                "RANDOM"->randomizarAudio()
            }
            isEffectApplied = true
        }
    }

    private fun randomizarAudio() {
        val audioIds = listOf(R.raw.audio1, R.raw.audio2, R.raw.audio3)
        val selectorAudio= audioIds[Random.nextInt(audioIds.size)]
        playAudio(audioId = selectorAudio )
    }

    private fun startTransitionEffect() {
        var leftVolume = 1f
        var rightVolume = 0f
        val transitionDuration = 1000L // 1 second

        transitionRunnable = object : Runnable {
            override fun run() {
                soundPool.setVolume(streamId, leftVolume, rightVolume)
                val temp = leftVolume
                leftVolume = rightVolume
                rightVolume = temp
                handler.postDelayed(this, transitionDuration)
            }
        }
        handler.post(transitionRunnable as Runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        transitionRunnable?.let { handler.removeCallbacks(it) }
    }

}

@Composable
fun ReproductorMusicaUI(
    modifier: Modifier = Modifier,
    onPlayAudio: (Int) -> Unit,
    onApplyEffect: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Button(onClick = { onPlayAudio(R.raw.audio1) }) {
                Text("Reproducir Audio 1")
            }
        }
        item {
            Button(onClick = { onPlayAudio(R.raw.audio2) }) {
                Text("Reproducir Audio 2")
            }
        }
        item {
            Button(onClick = { onPlayAudio(R.raw.audio3) }) {
                Text("Reproducir Audio 3")
            }
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            Button(onClick = { onApplyEffect("VELOCIDAD") }) {
                Text("Aplicar Efecto Velocidad")
            }
        }
        item {
            Button(onClick = { onApplyEffect("OREJA_DERECHA") }) {
                Text("Aplicar Efecto Oreja Derecha")
            }
        }
        item {
            Button(onClick = { onApplyEffect("OREJA_IZQUIERDA") }) {
                Text("Aplicar Efecto Oreja Izquierda")
            }
        }
        item {
            Button(onClick = { onApplyEffect("TRANSICION") }) {
                Text("Aplicar Efecto Transición")
            }
        }
        item {
            Button(onClick={onApplyEffect("RANDOM")}) {
                Text("Aleatorio")
            }
        }
    }
}