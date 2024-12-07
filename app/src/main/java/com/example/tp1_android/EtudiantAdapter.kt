package com.example.tp1_android

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EtudiantAdapter (
    private val etudiants: MutableList<Etudiant>,
    private val dbHelper: DatabaseHelper, // Ajoutez ce paramètre
    private val onPhotoUploadClick: (Int) -> Unit,
    private val onEtatToggle: (Int) -> Unit
    ) : RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

        class EtudiantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nomEtud: TextView = view.findViewById(R.id.nomEtud)
            val etatEtud: TextView = view.findViewById(R.id.etatEtud)
            val studentImg: ImageView = view.findViewById(R.id.studentImg)
            val detailButton: Button = view.findViewById(R.id.detailButton)
            val absentButton: Button = view.findViewById(R.id.absentButton)
            val overlayView: View = view.findViewById(R.id.overlayView)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.etudiant_item, parent, false)
            return EtudiantViewHolder(view)
        }
        override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
            val etudiant = etudiants[position]
            holder.nomEtud.text = etudiant.nom
            holder.etatEtud.text = etudiant.etat
            if (etudiant.imageUri != null) {
                holder.studentImg.setImageURI(etudiant.imageUri)
            } else {
                holder.studentImg.setImageResource(etudiant.defaultImageResId) }
            val borderColor = if (etudiant.etat == "Absent") Color.RED else Color.GREEN
            val drawable = GradientDrawable().apply {
                setColor(Color.TRANSPARENT)
                setStroke(4, borderColor)
                cornerRadius = 8f }
            holder.overlayView.background = drawable
            holder.detailButton.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailEtudActivity::class.java).apply {
                    putExtra("studentName", etudiant.nom)
                    putExtra("studentEtat", etudiant.etat)
                    putExtra("studentImageUri", etudiant.imageUri?.toString()) // Convert Uri to String
                }
                context.startActivity(intent) }
            holder.absentButton.setOnClickListener {
                onEtatToggle(position) } }
        override fun getItemCount() = etudiants.size

        fun addEtudiant(etudiant: Etudiant) {
            etudiants.add(etudiant)
            notifyItemInserted(etudiants.size - 1)
        }
        fun updateEtudiantPhoto(position: Int, uri: Uri) {
            etudiants[position].imageUri = uri
            notifyItemChanged(position)
        }
    fun updateEtudiants(etudiants: List<Etudiant>) {
        this.etudiants.clear()
        this.etudiants.addAll(etudiants)
        notifyDataSetChanged()
    }
        fun toggleEtatForEtudiant(position: Int) {
            val etudiant = etudiants[position]
            val newEtat = if (etudiant.etat == "Present") "Absent" else "Present"
            etudiant.etat = newEtat
            dbHelper.updateEtudiantEtat(etudiant.id, newEtat)

            notifyItemChanged(position)
        }
}