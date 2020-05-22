package com.blanke.wechatRecordXposed

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_play.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PlayActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private var m_mediaPlayer: MediaPlayer = MediaPlayer()
    private var m_btnPlay: ImageButton? = null
    private var m_btnPause:ImageButton? = null
    private var m_btnStop:ImageButton? = null
    private var m_tvMusicName: TextView? = null
    private var m_tvMusicLength:TextView? = null
    private var m_tvMusicCur:TextView? = null
    private var m_seekBar: SeekBar? = null
    private var m_timer:Timer? = null
    private var m_simpleDateFormat:SimpleDateFormat = SimpleDateFormat("mm:ss")

    private var m_isSeekBarChanging = false //互斥变量，防止进度条与定时器冲突。
    private var m_currentPosition = 0 //当前音乐播放的进度
    private var m_filePath:String = ""
    private var m_name:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()
        initMediaPlayer()
        initEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(){
        m_btnPlay = findViewById(R.id.playButton)
        m_btnPause = findViewById(R.id.pauseButton)
        m_btnStop = findViewById(R.id.stopButton)
        m_tvMusicName = findViewById(R.id.musicNameTextView)
        m_tvMusicLength = findViewById(R.id.musicLengthTextView)
        m_tvMusicCur = findViewById(R.id.musicCurTextView)
        m_seekBar = findViewById(R.id.seekBar)

        var bundle = this.intent.extras
        m_filePath = bundle?.get("path").toString()
        m_name = bundle?.get("name").toString()
    }

    private fun initMediaPlayer(){
        try {
            m_mediaPlayer?.setDataSource(m_filePath)
            m_mediaPlayer?.setOnPreparedListener(this)
            m_mediaPlayer?.setOnErrorListener(this)
            m_mediaPlayer?.prepareAsync()
            m_mediaPlayer?.setLooping(false)
            m_seekBar?.setOnSeekBarChangeListener(MySeekBar(m_mediaPlayer))
        }catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initEvent(){
        m_btnPlay?.setOnClickListener { onPlay()}
        m_btnPause?.setOnClickListener{ v ->
            if(m_mediaPlayer.isPlaying){
                m_mediaPlayer.pause()
            }
        }
        m_btnStop?.setOnClickListener{v->
            if(m_mediaPlayer.isPlaying){
                m_mediaPlayer.reset()
                initMediaPlayer()
            }
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        m_seekBar?.setMax(m_mediaPlayer.getDuration());
        m_tvMusicLength?.setText(m_simpleDateFormat.format(m_mediaPlayer.getDuration()));
        m_tvMusicCur?.setText("00:00");
        m_tvMusicName?.setText(m_name);
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.i("MediaPlayer", "初始化MediaPlayer异常")
        return false
    }

    private fun onPlay(){
        if(!m_mediaPlayer.isPlaying()){
            m_mediaPlayer.start()
            m_mediaPlayer.seekTo(m_currentPosition)
            m_timer = Timer()
            var updateUITask = object:TimerTask(){
                override fun run(){
                    Log.i("updateUITask", "updateUITask正在执行中")
                    if(!m_isSeekBarChanging){
                        m_seekBar?.setProgress(m_mediaPlayer?.currentPosition)
                        runOnUiThread(Runnable {
                            fun run(){
                                musicCurTextView.setText(m_simpleDateFormat.format(m_mediaPlayer.currentPosition))
                                Log.i("runOnUiThread", "runOnUiThread正在执行中")
                            }
                        })
                    }
                }
            }
            m_timer?.schedule(updateUITask,0, 50)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        m_isSeekBarChanging = true;
        if (m_mediaPlayer != null) {
            m_mediaPlayer.stop();
            m_mediaPlayer.release();
        }
        if (m_timer != null) {
            m_timer?.cancel();
            m_timer = null;
        }

    }

    inner class MySeekBar(var mediaPlayer: MediaPlayer) : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            /*滚动时,应当暂停后台定时器*/
            m_isSeekBarChanging = true
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            /*滑动结束后，重新设置值*/
            m_isSeekBarChanging = false
            mediaPlayer.seekTo(seekBar.getProgress())
        }
    }
}