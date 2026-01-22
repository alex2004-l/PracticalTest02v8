package ro.pub.cs.systems.eim.practicaltest02v8.network

import android.util.Log
import okio.IOException
import java.net.ServerSocket

import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import java.net.Socket

class ServerThread(port: Int): Thread() {

    var serverSocket: ServerSocket? = null
        private set

    init {
        try {
            this.serverSocket = ServerSocket(port)
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "An exception has occured : ${ioException.message}")

            if (Constants.DEBUG)
                ioException.printStackTrace()
        }
    }

    override fun run() {
        try {
            while (!isInterrupted) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...")

                val socket: Socket = serverSocket?.accept() ?: break
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from ${socket.inetAddress}:${socket.localPort}")

                val communicationThread: CommunicationThread = CommunicationThread(this, socket)
                communicationThread.start()
            }
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occured ${ioException.message}")

            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        }
    }

    fun stopThread() {
        interrupt()

        try {
            serverSocket?.close()
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occured ${ioException.message}")

            if (Constants.DEBUG) {
                ioException.printStackTrace()
            }
        }
    }

}