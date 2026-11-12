package com.example.reproductorpablo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SeekBar seekBar;
    private TextView tvActual, tvTotal;
    private boolean arrastrando = false;
    private SoundPool soundPool;
    private final int[] idSonidos = new int[4];
    private Button[] botonesEfecto;
    private int sonidosCargados = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable actualizador = new Runnable() {
        @Override
        public void run() {
            if (player != null && !arrastrando) {
                long pos = player.getCurrentPosition();
                seekBar.setProgress((int) pos);
                tvActual.setText(formatear(pos));
            }
            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        seekBar = findViewById(R.id.seekBar);
        tvActual = findViewById(R.id.tvActual);
        tvTotal = findViewById(R.id.tvTotal);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);

        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        btnPlay.setOnClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });
        btnPausa.setOnClickListener(v -> player.pause());

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvTotal.setText(formatear(duracion));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar sb, int progreso, boolean delUsuario) {
                if (delUsuario) {
                    tvActual.setText(formatear(progreso));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {
                arrastrando = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                player.seekTo(sb.getProgress());
                arrastrando = false;
            }
        });
        iniciarSoundPool();
    }
    private void iniciarSoundPool() {
        botonesEfecto = new Button[]{
                findViewById(R.id.btnEfecto1),
                findViewById(R.id.btnEfecto2),
                findViewById(R.id.btnEfecto3),
                findViewById(R.id.btnEfecto4)
        };

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        // Activa los botones cuando los 4 sonidos estén cargados
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                sonidosCargados++;
                if (sonidosCargados == 4) {
                    for (Button b : botonesEfecto) {
                        b.setEnabled(true);
                    }
                }
            }
        });

        idSonidos[0] = soundPool.load(this, R.raw.sonido1, 1);
        idSonidos[1] = soundPool.load(this, R.raw.sonido2, 1);
        idSonidos[2] = soundPool.load(this, R.raw.sonido3, 1);
        idSonidos[3] = soundPool.load(this, R.raw.sonido4, 1);

        for (int i = 0; i < 4; i++) {
            final int indice = i;
            botonesEfecto[i].setOnClickListener(v ->
                    soundPool.play(idSonidos[indice], 1f, 1f, 1, 0, 1f));
        }
    }

    private String formatear(long ms) {
        long totalSegundos = ms / 1000;
        long minutos = totalSegundos / 60;
        long segundos = totalSegundos % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);

    }
    @Override
    protected void onStart() {
        super.onStart();
        handler.removeCallbacks(actualizador);
        handler.post(actualizador);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
        handler.removeCallbacks(actualizador);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(actualizador);
        player.release();
        soundPool.release();
    }
}