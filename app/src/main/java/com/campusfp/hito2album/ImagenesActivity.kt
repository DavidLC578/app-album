package com.campusfp.hito2album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate

class ImagenesActivity: AppCompatActivity() {
    private val imagenes = mutableListOf<Imagen>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagenes_layout)

        // Variable que guarda la fecha seleccionada del adaptador de fechas
        val fechaSeleccionada = intent.getStringExtra("FECHA_SELECCIONADA")

        // Variable de tipo lista de imagenes que guarda las imagenes con las fechas seleccionadas
        val todasLasImagenes: List<Imagen> = obtenerTodasLasImagenes(fechaSeleccionada)

        // Cargo el recycler view de imagenes
        val recyclerViewImagenes: RecyclerView = findViewById(R.id.recycler_imagenes)
        val adapterImagenes = CustomAdapterImagenes(this, todasLasImagenes)
        recyclerViewImagenes.adapter = adapterImagenes
        recyclerViewImagenes.layoutManager = LinearLayoutManager(this)

        // Filtrar imágenes por la fecha seleccionada
        if (fechaSeleccionada != null) {
            adapterImagenes.filtrarPorFecha(fechaSeleccionada)
        } else {
            // Si no se seleccionó ninguna fecha, se muestran todas las imágenes
            adapterImagenes.filtrarPorFecha("")
        }
    }

    /**
     * Esta función obtiene las imagenes de la base de datos según la fecha seleccionada
     * Devuelve la lista con los objetos imagenes que haya obtenido desde la base de datos
     */
    private fun obtenerTodasLasImagenes(fechaSeleccionada: String?): List<Imagen> {
        val miSQLite = MiSQLite(this)
        val db = miSQLite.readableDatabase

        // Condición de selección basada en la fecha (si está disponible)
        val selection = if (fechaSeleccionada != null) "fecha = ?" else null
        val selectionArgs = if (fechaSeleccionada != null) arrayOf(fechaSeleccionada) else null

        // Guarda la consulta con los datos seleccionados para después recorrerlo
        val cursor = db.query(
            "fotos", // Nombre de la tabla
            arrayOf("imagen", "titulo", "descripcion", "fecha"),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        // Recorre la consulta y las guarda en un objeto imagen y la añade al array imagenes
        cursor.use {
            while (it.moveToNext()) {
                val imagen = it.getString(it.getColumnIndexOrThrow("imagen"))
                val titulo = it.getString(it.getColumnIndexOrThrow("titulo"))
                val descripcion = it.getString(it.getColumnIndexOrThrow("descripcion"))
                val fecha = it.getString(it.getColumnIndexOrThrow("fecha"))

                // Crea un objeto Imagen con los datos obtenidos de la base de datos
                val imagenObj = Imagen(imagen, titulo, descripcion, fecha)
                imagenes.add(imagenObj)
            }
        }
        db.close()
        return imagenes
    }
}
