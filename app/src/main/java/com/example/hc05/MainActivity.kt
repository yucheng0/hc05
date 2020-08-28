package com.example.hc05

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    companion object {
       lateinit var mBluetoothAdapter:BluetoothAdapter
        lateinit var btSocket:BluetoothSocket
  //      lateinit var bytes:ByteArray

        //       val myUUID = UUID.fromString("8989063a-c9af-463a-b3f1-f21d9b2b827b")
        var myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CheckBt()
        Connect()
  //     writeData("1")
        timer(initialDelay = 0, period = 100){
          readData()            //注意讀寫都去協程做
        }
    }

    private fun CheckBt() {
        Toast.makeText(applicationContext, "It has started", Toast.LENGTH_SHORT).show()
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter.enable()) {
            Toast.makeText(applicationContext, "Bluetooth Disabled !", Toast.LENGTH_SHORT).show()
            /* It tests if the bluetooth is enabled or not, if not the app will show a message. */
            finish()
        }
        if (mBluetoothAdapter == null) {
            Toast.makeText(applicationContext, "Bluetooth null !", Toast.LENGTH_SHORT).show()
        }
    }
    fun Connect() {
        val device = mBluetoothAdapter.getRemoteDevice("24:79:F3:8E:55:AA")   //Renox10 mac  配對
        Log.d("", "Connecting to ... $device")
        Toast.makeText(applicationContext, "Connecting to ... ${device.name} mac: ${device.uuids[0]} address: ${device.address}", Toast.LENGTH_LONG).show()
        mBluetoothAdapter.cancelDiscovery()
        try {
            btSocket = device.createRfcommSocketToServiceRecord(myUUID)
            /* Here is the part the connection is made, by asking the device to create a RfcommSocket (Unsecure socket I guess), It map a port for us or something like that */
            btSocket.connect()
            Log.d("", "Connection made.")
            Toast.makeText(applicationContext, "Connection made.", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            try {
                btSocket.close()
            } catch (e2: IOException) {
                Log.d("", "Unable to end the connection")
                Toast.makeText(applicationContext, "Unable to end the connection", Toast.LENGTH_SHORT).show()
            }
            Log.d("", "Socket creation failed")
            Toast.makeText(applicationContext, "Socket creation failed", Toast.LENGTH_SHORT).show()
        }
        //beginListenForData()
        /* this is a method used to read what the Arduino says for example when you write Serial.print("Hello world.") in your Arduino code */
    }
    private fun writeData(data: String) {
        var outStream = btSocket.outputStream
        try {
            outStream = btSocket.outputStream
        } catch (e: IOException) {
            //Log.d(FragmentActivity.TAG, "Bug BEFORE Sending stuff", e)
        }
        val msgBuffer = data.toByteArray()
        try {
            outStream.write(msgBuffer)
        } catch (e: IOException) {
            //Log.d(FragmentActivity.TAG, "Bug while sending stuff", e)
        }
    }
    private fun readData(){
        var inStream = btSocket.inputStream
        try {
            inStream = btSocket.inputStream
        } catch (e:IOException) { }
        println ("Test")
         val buffer: ByteArray = ByteArray(10)
         inStream.read(buffer)   //讀取資料放在buffer內, 輸出多少筆資料, 後面00是多出來的因為上面size=10）
         for (i in buffer)  {
         println(i) }    //收到49,13,10   (49=31h="1", 13=0d, 10=0a)

        }


    suspend fun  delay100ms(){
        delay(100)
        readData()
    }
}