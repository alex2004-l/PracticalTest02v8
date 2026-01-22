package ro.pub.cs.systems.eim.practicaltest02v8.general

import java.io.BufferedReader
import java.io.PrintWriter
import java.net.Socket

object Utilities {
    fun getReader(socket: Socket): BufferedReader {
        return socket.getInputStream().bufferedReader()
    }

    fun getWriter(socket: Socket): PrintWriter {
        // 'true' enables auto-flush
        return PrintWriter(socket.getOutputStream(), true)
    }
}