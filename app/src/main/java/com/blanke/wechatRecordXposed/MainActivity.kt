package com.blanke.wechatRecordXposed

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.blanke.wechatRecordXposed.model.Setting
import com.blanke.wechatRecordXposed.model.Voice
import com.blanke.wechatRecordXposed.utils.JsonHelper
import com.weicheng.amrconvert.AmrConvertUtils
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), RefreshListView.IReflashListner{

    private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val REQUEST_PERMISSION_CODE = 1
    private val TAG = "MainActivity"

    private var m_amrs: MutableList<Voice> = mutableListOf()
    private var m_mp3s: MutableList<Voice> = mutableListOf()

    private var m_amrListView:RefreshListView? = null
    private var m_mp3ListView:RefreshListView? = null
    private var m_setting: Setting? = null

    var m_handler: MyHandler = MyHandler(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        requestPermissions()

        m_amrListView = findViewById(R.id.amrList)
        m_mp3ListView = findViewById(R.id.mp3List)

        m_amrListView?.setInterface(this, 0)
        m_mp3ListView?.setInterface(this, 1)


        initEvent()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_setting){
            startActivity(Intent(this, SettingActivity::class.java))
        }
        return false
    }

    private fun initData(){
        m_setting = JsonHelper.getSetting()

        loadAmrs()
        loadMp3s()
    }

    private fun initEvent(){
        m_amrListView?.setOnItemClickListener { parent, view, position, id ->
            try{
                var index = position - 2 //去掉滚动条
                var amrFilePath = m_amrs.get(index)
                convertToMP3(amrFilePath.filePath)
                loadMp3s()
            }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        m_mp3ListView?.setOnItemClickListener{ parent, view, position, id ->
            try {
                var index = position - 2
                val intent = Intent(this, PlayActivity::class.java)
                val bundle = Bundle()
                bundle.putString("path", m_mp3s.get(index).filePath)
                bundle.putString("name", m_mp3s.get(index).name)
                intent.putExtras(bundle)
                startActivity(intent)
            }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        initData()
        super.onResume()
    }

    private fun loadAmrs(){
        m_amrs.clear()
        searchAmrs();
        var adapter = VoiceAdapter(m_amrs, true,this )
        m_amrListView?.setAdapter(adapter)
    }

    private fun loadMp3s(){
        m_mp3s.clear()
        searchMp3s();
        var adapter = VoiceAdapter(m_mp3s, false,this )
        m_mp3ListView?.setAdapter(adapter)
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE)
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE)
            }
        }
    }

    private fun searchAmrs(){
        val dir = Environment.getExternalStorageDirectory()
        val f = File("${dir.path}/tencent/MicroMsg/")
        var voiceWechatPaths:MutableList<String> = ArrayList()
        if (f.exists() &&  f.isDirectory) {
            val files = f.listFiles()
            if (files != null && files.isNotEmpty()) {
                for (f0 in files) {
                    if (f0.isDirectory && f0.name.length > 24) {
                        voiceWechatPaths?.add(f0.absolutePath + "/voice2")
                    }
                }
            }
        }
        if(voiceWechatPaths.count() > 0){
            var file: File?
            for (path in voiceWechatPaths) {
                file = File(path)
                if (file.exists() && file.isDirectory) {
                    val stack = Stack<String>()
                    stack.push(path)
                    while (!stack.empty()) {
                        var fs: Array<File>? = null
                        val parent = stack.pop()
                        if (parent != null) {
                            file = File(parent)
                            if (file.isDirectory) { // ignore file, FIXME
                                fs = file.listFiles()
                            } else {
                                continue
                            }
                        }
                        if (fs == null || fs.isEmpty()) continue
                        for (i in fs.indices) {
                            val name = fs[i].name
                            if (fs[i].isDirectory && name != "."
                                    && name != "..") {
                                stack.push(fs[i].path)
                            } else if (fs[i].isFile) {
                                if (name.endsWith(".amr")) {
                                    m_amrs.add(Voice(fs[i].name, fs[i].absolutePath, fs[i].lastModified(), fs[i].length()))
                                }
                            }
                        }
                    }
                }
            }
        }
        if(m_amrs.size > 0){
            m_amrs.sortByDescending { it.updateTime }
        }
    }

    private fun searchMp3s(){
        val f = File(m_setting?.outputPath)
        if (f.exists() &&  f.isDirectory) {
            val files = f.listFiles()
            if(files != null && files.count() > 0){
                for (i in files.indices) {
                    val name = files[i].name
                    if (name.endsWith(".mp3")) {
                        m_mp3s.add(Voice(files[i].name, files[i].absolutePath, files[i].lastModified(), files[i].length()))
                    }
                }
            }
        }
        if(m_mp3s.size > 0){
            m_mp3s.sortByDescending { it.updateTime }
        }
    }

    private fun convertToMP3(amrFilePath:String) {
        var thread = Thread(Runnable { //amr 转换 mp3
            val amrFile = File(amrFilePath)
            Log.v(TAG, "f.exists : " + amrFile.exists())
            if(!amrFile.exists()){
                m_handler.sendEmptyMessage(-1)
                return@Runnable
            }
            var lastBackslashIndex = amrFilePath.lastIndexOf("/")
            var mp3FilePath=m_setting?.outputPath + "/" + amrFilePath.subSequence(lastBackslashIndex, amrFilePath.length) + ".mp3"
            val fmp3Dir = File(m_setting?.outputPath)
            if (!fmp3Dir.exists()) fmp3Dir.mkdirs()
            var systemSec = System.currentTimeMillis()
            var retAmrToMp3 = true
            if (amrFile.exists() || amrFile.length() > 0) {
                Log.v("convertToMP3", "开始")
                val start = System.currentTimeMillis()
                val ret = AmrConvertUtils.amr2Mp3(this@MainActivity, amrFilePath, mp3FilePath)
                val dur = System.currentTimeMillis() - start
                Log.v("convertToMP3", "amr转MP3单个文件dur : $dur")
                //0表示转换成功 -1 表示失败
                if (!ret) {
                    retAmrToMp3 = false
                    m_handler.sendEmptyMessage(0)
                    return@Runnable
                }
            } else {
                retAmrToMp3 = false
                m_handler.sendEmptyMessage(-1)
            }
            if (retAmrToMp3) {
                m_handler.sendEmptyMessage(1)
            }
            systemSec = System.currentTimeMillis() - systemSec
            Log.v(TAG, "systemSec : $systemSec")
        })
        thread.start()
        thread.join()
    }

    class MyHandler(private val m_context: Context) : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                Toast.makeText(m_context, "转换失败", Toast.LENGTH_LONG).show()
            } else if (msg.what == 1) {
                Toast.makeText(m_context, "转换成功", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(m_context, "amr文件不存在或者文件大小为0", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onReflash(type:Int) {
        var handler = Handler()
        handler.postDelayed(Runnable(){
            run() {
                if(type == 0){
                    Log.i(TAG, "amr onReflash" )
                    loadAmrs()
                    m_amrListView?.reflashComplete();
                }else{
                    Log.i(TAG, "mp3 onReflash" )
                    loadMp3s()
                    m_mp3ListView?.reflashComplete();
                }
            }
        }, 100)
    }
}
