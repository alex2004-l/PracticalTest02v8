package ro.pub.cs.systems.eim.practicaltest02v8.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v8.general.Utilities
import java.io.IOException
import java.net.Socket

class CommunicationThread(private val serverThread: ServerThread, private val socket: Socket): Thread() {

    override fun run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try {
            val bufferedReader = Utilities.getReader(socket)
            val printWriter   = Utilities.getWriter(socket)

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client...")
            val url = bufferedReader.readLine()

            var result :String = ""

            if (url.isNullOrEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters!")
                return
            }

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting info from webservice...")

            if (url.contains("bad")) {
                printWriter.println("URL blocked by firewall")
                printWriter.flush()
            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Ok url...")
                val client = OkHttpClient()

                val request = Request.Builder().url(url).build()

                client.newCall(request).execute().use { response ->
                    val pageSourceCode = response.body?.string()

                    if (pageSourceCode != null) {
                        Log.i(Constants.TAG, pageSourceCode)

                        printWriter.println(pageSourceCode)
                        printWriter.flush()
                    } else {
                        printWriter.println("Empty page!")
                        printWriter.flush()
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error: ${e.message}")
            e.printStackTrace()
        } finally {
            try {
                socket.close()
            } catch (e: IOException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error closing socket: ${e.message}")
            }
        }
    }
}