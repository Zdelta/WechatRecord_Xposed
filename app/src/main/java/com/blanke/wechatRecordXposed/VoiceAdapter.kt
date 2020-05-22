package com.blanke.wechatRecordXposed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.blanke.wechatRecordXposed.model.Voice
import java.text.SimpleDateFormat

class VoiceAdapter(var voices:MutableList<Voice>, var isAMR:Boolean, val context:Context) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        val nameTextView = view.findViewById<TextView>(R.id.nameTextview)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextview)
        val imageView = view.findViewById<ImageView>(R.id.iconImageView)
        nameTextView.text = voices[position].name
        if(voices[position].size > 0){
            timeTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(voices[position].updateTime) + "      " + voices[position].size / 1000 + "K"
        }else{
            timeTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(voices[position].updateTime) + "      0K"
        }
        if(isAMR){
            imageView.setImageResource(R.drawable.amr)
        }else{
            imageView.setImageResource(R.drawable.mp3)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return voices[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return voices.size
    }
}