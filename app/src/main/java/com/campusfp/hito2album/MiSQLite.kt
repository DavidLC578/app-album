package com.campusfp.hito2album
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
class MiSQLite (context: Context): SQLiteOpenHelper(context, "album.db", null, 1 ){

    //En este método creamos la orden de creación de la base de datos y realizamos la orden
    override fun onCreate(db: SQLiteDatabase) {
        val creacion="CREATE TABLE fotos" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, imagen TEXT,titulo TEXT, descripcion TEXT, fecha TEXT)"

        db.execSQL(creacion)
        val creacionFechas = "CREATE TABLE fechas" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, fecha TEXT)"
        db.execSQL(creacionFechas)
    }
    fun tablaExiste(nombreTabla: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM sqlite_master WHERE type='table' AND name='$nombreTabla'"
        val cursor = db.rawQuery(query, null)
        val tablaExiste = cursor.count > 0
        cursor.close()
        return tablaExiste
    }
    //Aquí vamos a poner lo que haremos si la bbdd se actualiza
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        val borradoFotos = "DROP TABLE IF EXISTS fotos"
        val borradoFechas = "DROP TABLE IF EXISTS fechas"

        db.execSQL(borradoFotos)
        db.execSQL(borradoFechas)

        onCreate(db)

    }
    fun addFechasBDD(fecha: String){
        val datos = ContentValues()
        datos.put("fecha", fecha)
        val db=this.writableDatabase
        db.insert("fechas", null, datos)
        db.close()
    }


    //Creamos una función para añadir datos
    fun anadirDato(imagen: String, titulo: String, descripcion: String, fecha: String){
        val datos=ContentValues()
        datos.put("imagen", imagen)
        datos.put("titulo", titulo)
        datos.put("descripcion", descripcion)
        datos.put("fecha", fecha)

        val db=this.writableDatabase
        db.insert("fotos", null, datos)
        db.close()
    }

    


}