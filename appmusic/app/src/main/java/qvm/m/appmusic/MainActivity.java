package qvm.m.appmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    ImageButton btnPrevious, btnplay, btnNext;
    SeekBar sbMusic;

    ImageView imgDisc;
    ArrayList<Song> arrayListSong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Anhxa();
        AddSong();
        animation = AnimationUtils.loadAnimation(this,R.anim.disc_rotate);
        KhoiTaoMediaplayer();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if(position > arrayListSong.size() - 1){
                    position = 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                KhoiTaoMediaplayer();
                mediaPlayer.start();
                btnplay.setImageResource(R.drawable.icon_pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if(position < 0){
                    position = arrayListSong.size() - 1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                KhoiTaoMediaplayer();
                mediaPlayer.start();
                btnplay.setImageResource(R.drawable.icon_pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mediaPlayer.stop();
//                mediaPlayer.release(); // giải phóng ngay tại chỗ đó
//                btnplay.setImageResource(R.drawable.icon_play);
//                KhoiTaoMediaplayer();
//            }
//        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    // nếu đang hát pause -> đổi hình
                    mediaPlayer.pause();
                    btnplay.setImageResource(R.drawable.icon_play);
                }else{
                    // đang ngừng phát -> đổi hình play
                    mediaPlayer.start();
                    btnplay.setImageResource(R.drawable.icon_pause);
                    SetTimeTotal();
                    UpdateTimeSong();
                    imgDisc.startAnimation(animation);
                }
            }
        });

        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbMusic.getProgress());
            }
        });
    }

    private void UpdateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhdanggio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhdanggio.format(mediaPlayer.getCurrentPosition()));

                // update progess sbsong
                sbMusic.setProgress(mediaPlayer.getCurrentPosition());

                // kiểm tra thời gian bài hát -> nếu kết thúc next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position > arrayListSong.size() - 1){
                            position = 0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        KhoiTaoMediaplayer();
                        mediaPlayer.start();
                        btnplay.setImageResource(R.drawable.icon_pause);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }

    private void SetTimeTotal(){
        SimpleDateFormat dinhdanggio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhdanggio.format(mediaPlayer.getDuration()));
        // gán max của sbMusic = mediaPlayer.getDuration())
        sbMusic.setMax(mediaPlayer.getDuration());
    }

    private void KhoiTaoMediaplayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this,arrayListSong.get(position).getFile());
        txtTitle.setText(arrayListSong.get(position).getTitle());
    }

    private void AddSong() {
        arrayListSong = new ArrayList<>();
        arrayListSong.add(new Song("Ngày đầu tiên",R.raw.ngay_dau_tien));
        arrayListSong.add(new Song("Ôm em lần cuối", R.raw.om_em_lan_cuoi));
        arrayListSong.add(new Song("Đào nương", R.raw.dao_nuong));
    }

    private void Anhxa() {
        txtTitle = (TextView) findViewById(R.id.textViewTitle);
        txtTimeSong = (TextView) findViewById(R.id.textViewTimesong);
        txtTimeTotal = (TextView) findViewById(R.id.textViewTimetotal);
        btnPrevious = (ImageButton) findViewById(R.id.imageViewPrevious);
        btnplay = (ImageButton) findViewById(R.id.imageViewPlay);
        btnNext = (ImageButton) findViewById(R.id.imageViewNext);
        sbMusic = (SeekBar) findViewById(R.id.seekBarMusic);
        imgDisc = (ImageView) findViewById(R.id.imageViewDisc);
    }
}