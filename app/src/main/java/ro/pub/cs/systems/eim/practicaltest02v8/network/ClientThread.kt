package ro.pub.cs.systems.eim.practicaltest02v8.network

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v8.general.Utilities
import java.io.IOException
import java.net.Socket

import android.os.Handler
import android.os.Looper

class ClientThread(
    private val address : String,
    private val port : Int,
    private val url : String,
    private val bodyTextView: TextView,
    private val context: android.content.Context
): Thread() {
    private var socket: Socket? = null

    override fun run() {
        try {
            socket = Socket(address, port)

            val bufferedReader = Utilities.getReader(socket!!)
            val printWriter = Utilities.getWriter(socket!!)

            printWriter.println(url)
            printWriter.flush()
            var weatherInformation: String?
            val finalizedWeatherInformation = StringBuilder()

            while (bufferedReader.readLine().also { weatherInformation = it } != null) {
                finalizedWeatherInformation.append(weatherInformation)

                Log.i(Constants.TAG, "[Client Thread] Received something...")

                if (finalizedWeatherInformation != null) {
                    finalizedWeatherInformation.append(weatherInformation)
                }

                // Afiseaza cu toast
//                Handler(Looper.getMainLooper()).post {
//                    Toast.makeText(context, "Mesaj: $finalizedWeatherInformation", Toast.LENGTH_SHORT).show()
//                }

                // Afiseaza in textview
            }

            bodyTextView.post {
                bodyTextView.text = finalizedWeatherInformation.toString()
            }
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: ${ioException.message}")
        } finally {
            try {
                socket?.close()
            } catch (ioException: IOException) {
                Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: ${ioException.message}")
            }
        }
    }
}