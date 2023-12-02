package com.campusfp.hito2album

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.time.LocalDate



class FormularioImagen : AppCompatActivity() {
    private val REQUEST_STORAGE_PERMISSION = 1001
    private var selectedImagePath = ""
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>

    /**
     * Función que comprueba si tiene ya permisos
     */
    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // El permiso no está concedido, se solicita al usuario
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION)
        } else {
            // Permiso ya concedido, puedes acceder a la galería
            abrirGaleria()
        }
    }

    /**
     * Comprueba el resultado de checkPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes acceder a la galería
                abrirGaleria()
            } else {
                // Permiso denegado, muestra un mensaje al usuario
                Toast.makeText(this, "Permiso rechazados", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Abre la galería mediante el selectImageLauncher de la función onCreate
     */
     private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" // Esto asegura que solo se puedan seleccionar imágenes
        selectImageLauncher.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formulario_layout)
        val btnUpload = findViewById<Button>(R.id.btnUpload)
        var imgFoto = findViewById<ImageView>(R.id.imgFoto)
        var fecha = LocalDate.now().toString()
        val albumBDD = MiSQLite(this)
        // Configurar el lanzador para seleccionar imágenes
        selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val selectedImage: Intent? = result.data
                    val imageUri = selectedImage?.data
                    // La URI de la imagen seleccionada
                    imageUri?.let { uri ->
                        // Carga la imagen con la URI
                        imgFoto.setImageURI(uri)
                        // Guarda la ruta de la imagen como String
                        selectedImagePath = uri.toString()
                        btnUpload.setOnClickListener {
                            var entryTitulo = findViewById<EditText>(R.id.entryTitulo).text.toString()
                            var entryDescripcion = findViewById<EditText>(R.id.entryDescripcion).text.toString()

                            // Guardo la ruta URI de la imagen, titulo, la descripcion y la fecha en la base de datos
                            albumBDD.anadirDato(selectedImagePath,entryTitulo,entryDescripcion,fecha)

                            Toast.makeText(applicationContext,"Datos insertados", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,HomeActivity::class.java)
                            intent.putExtra("fecha",fecha)
                            startActivity(intent)
                        }
                    }
                } else {
                    // El usuario no seleccionó ninguna imagen o hubo un error
                    Toast.makeText(this, "Error al seleccionar la imagen", Toast.LENGTH_SHORT)
                        .show()
                }
            }
                // Boton para añadir imagen
                val btnAddImg = findViewById<Button>(R.id.btnAddImg)
                btnAddImg.setOnClickListener {
                    checkPermissions()
                }
            }
}
