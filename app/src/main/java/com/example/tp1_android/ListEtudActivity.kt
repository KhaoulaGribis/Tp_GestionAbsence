package com.example.tp1_android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListEtudActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var studentNameEditText: EditText
    private lateinit var presentRadioButton: RadioButton
    private lateinit var absentRadioButton: RadioButton
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var etudiantAdapter: EtudiantAdapter
    private var currentPhotoPosition: Int = -1

    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data

            if (selectedImageUri != null && currentPhotoPosition != -1) {
                etudiantAdapter.updateEtudiantPhoto(currentPhotoPosition, selectedImageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_etud)

        studentNameEditText = findViewById(R.id.nom_std)
        presentRadioButton = findViewById(R.id.present_btn)
        absentRadioButton = findViewById(R.id.absent_btn)
        addButton = findViewById(R.id.ajouter_btn)
        recyclerView = findViewById(R.id.studentRecyclerView)
        dbHelper = DatabaseHelper(this)
        etudiantAdapter = EtudiantAdapter(
            mutableListOf(),
            onPhotoUploadClick = { position ->
                currentPhotoPosition = position
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                photoPickerLauncher.launch(intent)
            },
            onEtatToggle = { position ->
                etudiantAdapter.toggleEtatForEtudiant(position)
            },
            dbHelper = dbHelper
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = etudiantAdapter
        val etudiants = dbHelper.getAllEtudiants()
        etudiantAdapter.updateEtudiants(etudiants)
        addButton.setOnClickListener {
            val studentName = studentNameEditText.text.toString()
            if (studentName.isNotBlank()) {
                val etat = if (presentRadioButton.isChecked) "Present" else "Absent"
                val imageUriString: String? = null  // ou obtenez-la d'une autre manière
                val imageUri: Uri? = imageUriString?.let { Uri.parse(it) }
                val studentId = dbHelper.addEtudiant(studentName, etat, imageUri)
                val etudiant = Etudiant(studentId,studentName, etat, imageUri) // Utilisez Uri ici
                etudiantAdapter.addEtudiant(etudiant)
                studentNameEditText.text.clear()
                Toast.makeText(this, "$studentName added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter the student's name", Toast.LENGTH_SHORT).show()
            }

        }
    }


}