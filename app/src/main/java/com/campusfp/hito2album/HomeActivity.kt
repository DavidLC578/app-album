package com.campusfp.hito2album

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


class HomeActivity: AppCompatActivity() {



    // Al abrir la vista de home
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)

        val btnAdd = findViewById<ImageView>(R.id.imgAdd)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_fechas)
        val adapter = CustomAdapterFechas(this)
        val miSQLite = MiSQLite(this)

        // Recupera el intent de fecha y añade a la base de datos a la tabla de fechas
        val intent = intent
        val fechaIntent = intent.getStringExtra("fecha")
        if(fechaIntent != null) {
            miSQLite.addFechasBDD(fechaIntent)

        }
        // Después recorre la tabla y lo agrega al array de fechas del adaptador de fechas
        val db = miSQLite.readableDatabase
        val cursor = db.query(
            "fechas", // Nombre de la tabla
            arrayOf("fecha"),
            null,
            null,
            null,
            null,
            null,
        )
        cursor.use {
            var fecha = ""
            while(it.moveToNext()){
                fecha = it.getString(it.getColumnIndexOrThrow("fecha"))
                if(!adapter.obtenerFechas().contains(fecha)){
                    adapter.addFechas(fecha)
                }
            }

        }

        // Redirige al pulsar al formulario
        btnAdd.setOnClickListener {
            redigirFormulario()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { fechaSeleccionada ->
            abrirImagenesActivityConFecha(fechaSeleccionada)
        }

    }

    fun redigirFormulario(){
        val intent = Intent(this, FormularioImagen::class.java)
        startActivity(intent)
    }

    private fun abrirImagenesActivityConFecha(fecha: String) {
        val intent = Intent(this, ImagenesActivity::class.java)
        intent.putExtra("FECHA_SELECCIONADA", fecha)
        startActivity(intent)
    }

}