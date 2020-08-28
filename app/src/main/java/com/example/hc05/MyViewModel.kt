package com.example.hc05

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MyViewModel:ViewModel() {
    val TAG = "myTag"

    private fun readData() {
        var inStream = MainActivity.btSocket.inputStream
        try {
            inStream = MainActivity.btSocket.inputStream
        } catch (e: IOException) {
        }

        var buffer: ByteArray = ByteArray(1024)
        val sb = StringBuffer()
        var numBytes: Int // bytes returned from read()
        numBytes = inStream.read(buffer)  //3個在buffer內
        for (i in 0..numBytes - 1) {   //先知道nubBytes的數字再去讀
            sb.append(buffer[i])
        }
        println(sb)         // 讀出491310 的值
    }
        fun init() {
            viewModelScope.launch(Dispatchers.Main) {
                delay100ms()
            }
// 先執行再delay

        }

        suspend fun delay100ms() {
            delay(100)
            readData()

        }
    }
