package com.weicheng.amrconvert

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File

object  AmrConvertUtils{
    private const val TAG = "AmrConvertUtils"
    private val dir = Environment.getExternalStorageDirectory()


    init{
        System.loadLibrary("amrconvert")
    }

    /**
     *
     * @param src amr的绝对路径
     * @param dest mp3的绝对路经
     * @param tmp pcm的绝对路径
     * @return -1是失败，0是成功
     */
    private external fun amr2Mp3(src: String, dest: String, tmp: String): Int

    /**
     *
     * @param src mp3的绝对路经
     * @param dest amr的绝对路径
     * @param tmp pcm的绝对路径
     * @return -1是失败，0是成功
     */
    private external fun mp32Amr(src: String, dest: String, tmp: String): Int

    /**
     *
     * @param src mp3的绝对路经
     * @param dest amr的绝对路径
     * @param tmp pcm的绝对路径
     * @return -1是失败，0是成功
     */
    private external fun mp32MultAmr(src: String, dest: String, tmp: String): Int

    /**
     *
     * @param context 上下文对象
     * @param src amr的绝对路径
     * @param dest mp3的绝对路径
     * @return -1是失败，0是成功
     */
    fun amr2Mp3(context: Context, src: String, dest: String): Boolean {
        val tmp = "${dir.path}/tencent/MicroMsg/" + File.separator + "t.t"
        val f = File(tmp)
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: Exception) {
                Log.v(TAG, "createNewFile mp32Amr :$e")
                return false
            }
        }
        if (f.exists()) {
            val ret = amr2Mp3(src, dest, tmp)
            f.delete()
            return ret == 0
        }
        return false
    }

    /**
     *
     * @param context 上下文对象
     * @param src mp3的绝对路径
     * @param dest amr的绝对路径
     * @return -1是失败，0是成功
     */
    fun mp32Amr(context: Context, src: String, dest: String): Boolean {
        val tmp = "${dir.path}/tencent/MicroMsg/" + context.packageName + File.separator + "t1.t"
        val f = File(tmp)
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: Exception) {
                Log.v(TAG, "createNewFile mp32Amr :$e")
                return false
            }
        }
        if (f.exists()) {
            val ret = mp32Amr(src, dest, tmp)
            f.delete()
            return ret == 0
        }
        return false
    }

    fun mp32MultAmr(context: Context, src: String, dest: String): Boolean {
        val tmp = "${dir.path}/tencent/MicroMsg/" + context.packageName + File.separator + "t0.pcm"
        val f = File(tmp)
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: Exception) {
                Log.v(TAG, "createNewFile mp32MultAmr :$e")
                return false
            }
        }
        if (f.exists()) {
            val ret = mp32MultAmr(src, dest, tmp)
            //f.delete();
            return ret == 0
        }
        return false
    }
}