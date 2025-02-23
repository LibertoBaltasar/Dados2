package com.example.dados2.hub

import android.animation.Animator
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dados2.R
import com.example.dados2.bolita.BolitaJuegoActivity
import com.example.dados2.bolita.BolitaMenuActivity
import com.example.dados2.databinding.ActivityHubactivityBinding
import com.example.dados2.pig.MainActivity
import com.example.dados2.musica.ReproductorMusicaActivity
import kotlin.random.Random

class HUBActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHubactivityBinding
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var streamId: Int = 0
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHubactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val usuario = intent.getStringExtra("usuario")
        binding.textViewUsuario.text = usuario
        gestionarMusica()
        lanzarMusica()
        lanzarPigGame()
        lanzarCamara(usuario)
        lanzarVideoPlayer(usuario)
        sparkName()
        buhHub()
        lanzarBallGame(usuario)
//        randomizarAudio()
    }

    private fun lanzarBallGame(usuario:String?) {
        binding.ballGameImage.setOnClickListener{
            val intent= Intent(this, BolitaMenuActivity::class.java)
            intent.putExtra("nombre", usuario)
            ContextCompat.startActivity(this, intent, null)
        }
    }

    private fun randomizarAudio(){
//    }
//        var audioId
//        if (isPlaying) {
//                soundPool.stop(streamId)
//            }
//            when (audioId) {
//                R.raw.audio1 -> if (isLoaded1) streamId = soundPool.play(soundId1, 1f, 1f, 1, -1, 1f)
//                R.raw.audio2 -> if (isLoaded2) streamId = soundPool.play(soundId2, 1f, 1f, 1, -1, 1f)
//                R.raw.audio3 -> if (isLoaded3) streamId = soundPool.play(soundId3, 1f, 1f, 1, -1, 1f)
//            }
//            isPlaying = true
//    }
        binding.randomMusic.setOnClickListener {
            var soundId1: Int = 0
            var soundId2: Int = 0
            var soundId3: Int = 0
            var streamId: Int = 0
            var isPlaying: Boolean = false
            var isLoaded1: Boolean = false
            var isLoaded2: Boolean = false
            var isLoaded3: Boolean = false
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
        val audioIds = listOf(R.raw.audio1, R.raw.audio2, R.raw.audio3)
        val randomAudioId = audioIds[Random.nextInt(audioIds.size)]

        if (isPlaying) {
            soundPool.stop(streamId)
        }

        when (randomAudioId) {
            R.raw.audio1 -> if (isLoaded1) streamId = soundPool.play(soundId1, 1f, 1f, 1, -1, 1f)
            R.raw.audio2 -> if (isLoaded2) streamId = soundPool.play(soundId2, 1f, 1f, 1, -1, 1f)
            R.raw.audio3 -> if (isLoaded3) streamId = soundPool.play(soundId3, 1f, 1f, 1, -1, 1f)
        }
    }
}

    private fun gestionarMusica() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        soundPool.setOnLoadCompleteListener { _, _, _ ->
            resumeMusic()
        }
        soundId = soundPool.load(this, R.raw.friendly_music, 1)

        binding.lottieAnimationViewRabbit.setAnimation("rabbit.lottie")
        binding.lottieAnimationViewRabbit.visibility = View.VISIBLE
        binding.lottieAnimationViewRabbit.bringToFront()
        binding.lottieAnimationViewRabbit.playAnimation()

        binding.playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                resumeMusic()
            }
        }
    }

    private fun buhHub() {
        binding.textHub.setOnClickListener {
            binding.lottieAnimationViewBuh.setAnimation("buh.lottie")
            binding.lottieAnimationViewBuh.visibility = View.VISIBLE
            binding.lottieAnimationViewBuh.bringToFront()
            binding.lottieAnimationViewBuh.playAnimation()
            binding.lottieAnimationViewBuh.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    binding.lottieAnimationViewBuh.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        }
    }

    private fun sparkName() {
        binding.textViewUsuario.setOnClickListener {
            binding.lottieAnimationViewSpark.setAnimation("spark.lottie")
            binding.lottieAnimationViewSpark.visibility = View.VISIBLE
            binding.lottieAnimationViewSpark.bringToFront()
            binding.lottieAnimationViewSpark.playAnimation()
            binding.lottieAnimationViewSpark.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    binding.lottieAnimationViewSpark.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
        }
    }

    private fun pauseMusic() {
        soundPool.pause(streamId)
        binding.lottieAnimationViewRabbit.pauseAnimation()
        isPlaying = false
        binding.playPauseButton.text = "Play"
    }

    private fun resumeMusic() {
        streamId = soundPool.play(soundId, 1f, 1f, 1, -1, 1f)
        binding.lottieAnimationViewRabbit.resumeAnimation()
        isPlaying = true
        binding.playPauseButton.text = "Pause"
    }

    override fun onPause() {
        super.onPause()
        if (isPlaying) {
            pauseMusic()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isPlaying) {
            pauseMusic()
        }
    }

    private fun lanzarPigGame() {
        binding.imagePig.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            mostrarAnimacionTransicionActivity(intent)
        }
    }

    private fun lanzarCamara(usuario: String?) {
        binding.CamaraImage.setOnClickListener {
            val intent = Intent(this, com.example.dados2.camara.CamaraMenu::class.java)
            intent.putExtra("usuario", usuario)
            mostrarAnimacionTransicionActivity(intent)
        }
    }

    private fun lanzarVideoPlayer(usuario: String?) {
        binding.VideoPlayerImage.setOnClickListener {
            val intent = Intent(this, com.example.dados2.videoplayer.VideoPlayerSeleccionActivity::class.java)
            intent.putExtra("usuario", usuario)
            mostrarAnimacionTransicionActivity(intent)
        }
    }

    private fun lanzarMusica() {
        binding.musicPlayerImage.setOnClickListener {
            val intent = Intent(this, ReproductorMusicaActivity::class.java)
            mostrarAnimacionTransicionActivity(intent)
        }
    }

    private fun mostrarAnimacionTransicionActivity(intent: Intent) {
        binding.lottieAnimationView.setAnimation("transicion_activities.lottie")
        binding.lottieAnimationView.visibility = View.VISIBLE
        binding.lottieAnimationView.bringToFront()
        binding.lottieAnimationView.playAnimation()
        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                binding.lottieAnimationView.visibility = View.GONE
                startActivity(intent)
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }
}