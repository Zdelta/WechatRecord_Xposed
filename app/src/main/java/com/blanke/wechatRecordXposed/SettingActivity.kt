package com.blanke.wechatRecordXposed

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blanke.wechatRecordXposed.hook.Objects
import com.blanke.wechatRecordXposed.model.Setting
import com.blanke.wechatRecordXposed.utils.JsonHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class SettingActivity : AppCompatActivity(){

    private var m_btnReset: Button? = null
    private var m_btnOk: Button? = null
    private var m_editOutput: EditText? = null
    private var m_editMinute: EditText? = null

    private var m_setting: Setting? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        m_btnReset = findViewById(R.id.resetButton)
        m_editOutput = findViewById(R.id.outputEdit)
        m_btnOk = findViewById(R.id.okButton)
        m_editMinute = findViewById(R.id.timeEdit)

        initEvent()
        initData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initData(){
        m_setting = JsonHelper.getSetting()
        m_editOutput?.setText(m_setting?.outputPath)
        m_editMinute?.setText(m_setting?.minute?.toString())
    }

    fun initEvent(){

        m_btnReset?.setOnClickListener { v ->
            m_setting?.outputPath = Objects.MP3DefaultOutputDir
            m_editOutput?.setText(Objects.MP3DefaultOutputDir)
            m_setting?.minute = Objects.DEFAULT_MINUTE.toString()
            m_editMinute?.setText(Objects.DEFAULT_MINUTE.toString())

            if(m_setting!=null){
                JsonHelper.setSetting(m_setting)
            }
            Toast.makeText(this, "重置成功", Toast.LENGTH_LONG).show()
        }

        m_btnOk?.setOnClickListener{ v ->
            m_setting?.outputPath = m_editOutput?.text.toString()
            m_setting?.minute = m_editMinute?.text.toString()

            var fileDir = File(m_setting?.outputPath)
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            if(!fileDir.exists()){
                Toast.makeText(this, "路径错误", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            JsonHelper.setSetting(m_setting)
            Toast.makeText(this, "设置成功", Toast.LENGTH_LONG).show()
        }
    }
}