package ro.pub.cs.systems.eim.practicaltest02v8

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.practicaltest02v8.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v8.network.ClientThread
import ro.pub.cs.systems.eim.practicaltest02v8.network.ServerThread

class PracticalTest02v8MainActivity : AppCompatActivity() {
    private lateinit var serverConnectButton: Button
    private lateinit var serverPortEditText : EditText
    private lateinit var clientAddressEditText : EditText
    private lateinit var clientPortEditText : EditText
    private lateinit var clientUrlEditText : EditText
    private lateinit var clientGetButton: Button
    private lateinit var bodyTextView: TextView
    private var serverThread: ServerThread? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02v8_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        serverConnectButton = findViewById<Button>(R.id.server_connect_button)
        serverPortEditText = findViewById<EditText>(R.id.server_port_edit_text)
        clientAddressEditText = findViewById<EditText>(R.id.client_address_edit_text)
        clientPortEditText = findViewById<EditText>(R.id.client_port_edit_text)
        clientUrlEditText = findViewById<EditText>(R.id.client_url_edit_text)
        clientGetButton = findViewById<Button>(R.id.get_body_button)
        bodyTextView = findViewById<TextView>(R.id.body_text_view)

        serverConnectButton.setOnClickListener {
            val serverPort = serverPortEditText.text.toString()
            if (serverPort.isEmpty()) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] Server port should be filled!",
                    Toast.LENGTH_SHORT).show()

                return@setOnClickListener // Iesire fortata
            }

            serverThread?.stopThread()

            val newServerThread = ServerThread(serverPort.toInt())
            if (newServerThread.serverSocket == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!")
                Toast.makeText(applicationContext, "Port $serverPort is already in use!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            serverThread = newServerThread
            serverThread?.start()
        }

        clientGetButton.setOnClickListener {
            val clientAddress = clientAddressEditText.text.toString()
            val clientPort = clientPortEditText.text.toString()

            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] Client parameters should be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentServerThread = serverThread
            if (currentServerThread == null || !currentServerThread.isAlive) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val word = clientUrlEditText.text.toString()

            if (word.isEmpty()) {
                Toast.makeText(applicationContext, "[MAIN ACTIVITY] City / information type should be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            bodyTextView.text = ""

            val clientThread = ClientThread(
                clientAddress,
                clientPort.toInt(),
                word,
                bodyTextView,
                this
            )
            clientThread.start()
        }

    }

    override fun onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked")

        serverThread?.stopThread()
        super.onDestroy()
    }
}