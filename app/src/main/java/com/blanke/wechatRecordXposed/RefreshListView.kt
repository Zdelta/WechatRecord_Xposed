package com.blanke.wechatRecordXposed

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView

class RefreshListView : ListView, AbsListView.OnScrollListener {
    var TAG = "RefreshListView"
    var header : View? = null // 顶部布局文件
    var headerHeight = 0 // 顶部布局文件的高度
    var firstVisibleItem = 0 // 当前第一个可见的item的位置
    var scrollState = 0 // listview 当前滚动状态
    var isRemark = false // 标记，当前是在listview最顶端摁下的
    var startY = 0 // 摁下时的Y值
    var state = 0 // 当前的状态
    val NONE = 0 // 正常状态
    val PULL = 1 // 提示下拉状态
    val RELESE = 2 // 提示释放状态
    val REFLASHING = 3 // 刷新状态
    var iReflashListener : IReflashListner? = null //刷新数据的接口
    var type = 0 //amr=0 mp3=1

    constructor(context: Context):super(context){
        initView(context!!)
    }

    constructor(context: Context, attrs: AttributeSet):super(context, attrs){
        initView(context!!)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int):super(context,attrs, defStyle) {
        initView(context!!)
    }


    /**
     * 初始化界面，添加顶部布局文件到 listview
     *
     * @param context
     */
    private fun initView(context: Context) {
        val inflater = LayoutInflater.from(context)
        header = inflater.inflate(R.layout.header_layout, null) //下拉刷新试图
        measureView(header)
        headerHeight = header?.getMeasuredHeight() ?: 0
        topPadding(-headerHeight) //设置内边高度为负数是就会隐藏
        this.addHeaderView(header)
        setOnScrollListener(this)
    }

    /**
     * 通知父布局，占用的宽，高；
     *
     * @param view
     */
    private fun measureView(view: View?) {
        var p = view!!.layoutParams
        if (p == null) {
            p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val width = ViewGroup.getChildMeasureSpec(0, 0, p.width)
        val height: Int
        val tempHeight = p.height
        height = if (tempHeight > 0) {
            MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY)
        } else {
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        }
        view.measure(width, height)
    }

    /**
     * 设置header 布局 上边距；
     *
     * @param topPadding
     */
    private fun topPadding(topPadding: Int) {
        header!!.setPadding(header!!.paddingLeft, topPadding,
                header!!.paddingRight, header!!.paddingBottom)
        header!!.invalidate()
    }

    var firstVisibleItemjiehe = 0
    var totalItemCount = 0
    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        this.firstVisibleItem = firstVisibleItem
        firstVisibleItemjiehe = firstVisibleItem + visibleItemCount
        this.totalItemCount = totalItemCount
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        this.scrollState = scrollState
        if (firstVisibleItemjiehe == totalItemCount) {
            Log.i(TAG, "onScrollStateChanged: 结束")
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> if (firstVisibleItem == 0) {
                isRemark = true
                startY = ev.y.toInt() //按下时的值5
            }
            MotionEvent.ACTION_MOVE -> onMove(ev)
            MotionEvent.ACTION_UP -> if (state == RELESE) {
                state = REFLASHING
                // 加载最新数据；
                reflashViewByState()
                iReflashListener!!.onReflash(type)
            } else if (state == PULL) {
                state = NONE
                isRemark = false
                reflashViewByState()
            }
        }
        return super.onTouchEvent(ev)
    }

    /**
     * 判断移动过程操作；
     *
     * @param ev
     */
    private fun onMove(ev: MotionEvent) {
        if (!isRemark) {
            return
        }
        val tempY = ev.y.toInt()
        val space = tempY - startY
        val topPadding = space - headerHeight
        when (state) {
            NONE -> if (space > 0) {
                state = PULL
                reflashViewByState()
            }
            PULL -> {
                topPadding(topPadding)
                if (space > headerHeight + 30
                        && scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE
                    reflashViewByState()
                }
            }
            RELESE -> {
                topPadding(topPadding) //负数越来越少变成正数
                if (space < headerHeight + 30) {
                    state = PULL
                    reflashViewByState()
                } else if (space <= 0) {
                    state = NONE
                    isRemark = false
                    reflashViewByState()
                }
            }
        }
    }

    /**
     * 根据当前状态，改变界面显示；
     */
    private fun reflashViewByState() {
        val tip = header!!.findViewById<View>(R.id.tip) as TextView
        val progress = header!!.findViewById<View>(R.id.progress) as ProgressBar
        val anim = RotateAnimation(0.toFloat(), 180.toFloat(),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 500
        anim.fillAfter = true
        val anim1 = RotateAnimation(180.toFloat(), 0.toFloat(),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        anim1.duration = 500
        anim1.fillAfter = true
        when (state) {
            NONE -> topPadding(-headerHeight)
            PULL -> {
                progress.visibility = View.GONE
                tip.text = "下拉可以刷新！"
            }
            RELESE -> {
                progress.visibility = View.GONE
                tip.text = "松开可以刷新！"
            }
            REFLASHING -> {
                topPadding(50)
                progress.visibility = View.VISIBLE
                tip.text = "正在刷新..."
            }
        }
    }

    /**
     * 获取完数据；
     */
    fun reflashComplete() {
        state = NONE
        isRemark = false
        reflashViewByState()
    }

    fun setInterface(iReflashListener: IReflashListner?, type:Int) {
        this.iReflashListener = iReflashListener
        this.type = type
    }

    /**
     * 刷新数据接口
     * @author Administrator
     */
    interface IReflashListner {
        fun onReflash(type:Int)
    }

    init {
        initView(context)
    }
}