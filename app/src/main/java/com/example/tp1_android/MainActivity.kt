package com.example.tp1_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)

    val loginEditText: EditText = findViewById(R.id.login)
    val passwordEditText: EditText = findViewById(R.id.password)
    val loginButton: Button = findViewById(R.id.login_button)

    loginButton.setOnClickListener()
    {
        val login = loginEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (login == "khaoula" && password == "1234") {
            val intent = Intent(this, ListEtudActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show()
        }
    }
}
}




