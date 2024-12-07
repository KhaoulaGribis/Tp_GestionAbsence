package com.example.tp1_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class DetailEtudActivity : AppCompatActivity() {

    private lateinit var studentNameTextView: TextView
    private lateinit var studentEtatTextView: TextView
    private lateinit var studentImageView: ImageView
    private lateinit var changeImageButton: Button
    private var imageUri: Uri? = null
    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            if (imageUri != null) {
                studentImageView.setImageURI(imageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_etud2)
        studentNameTextView = findViewById(R.id.studentNameTextView)
        studentEtatTextView = findViewById(R.id.studentEtatTextView)
        studentImageView = findViewById(R.id.studentImageView)
        changeImageButton = findViewById(R.id.changeImageButton)
        val studentName = intent.getStringExtra("studentName")
        val studentEtat = intent.getStringExtra("studentEtat")
        imageUri = intent.getParcelableExtra("studentImageUri")

        studentNameTextView.text = studentName
        studentEtatTextView.text = studentEtat
        if (imageUri != null) {
            studentImageView.setImageURI(imageUri)
        }

        // Changer l'image
        changeImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            photoPickerLauncher.launch(intent)
        }
    }
}
