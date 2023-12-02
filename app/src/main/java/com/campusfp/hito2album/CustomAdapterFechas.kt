package com.campusfp.hito2album

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterFechas(private val context: AppCompatActivity): RecyclerView.Adapter<CustomAdapterFechas.ViewHolder>() {

    // Guardo las fechas en un array mutable
    private val fechas = mutableListOf<String>()
    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.lista_fechas, viewGroup, false)
        return ViewHolder(v)
    }

    /**
     *  Muestra los datos del array de fechas y lo envia en un intent
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val fecha = fechas[i]

        viewHolder.itemFechas.text = fecha

        viewHolder.itemFlechita.setOnClickListener {
            // Envía la fecha seleccionada
            val intent = Intent(context, ImagenesActivity::class.java)
            intent.putExtra("FECHA_SELECCIONADA", fecha)
            context.startActivity(intent)
        }

    }
    fun addFechas(fecha: String){
        fechas.add(fecha)
    }
    fun obtenerFechas(): MutableList<String> {
        return fechas
    }

    override fun getItemCount(): Int {
        return fechas.size
    }
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemFechas: TextView
        var itemFlechita: ImageView


        init {
            itemFechas = itemView.findViewById(R.id.txtFechaIMG)
            itemFlechita = itemView.findViewById(R.id.imgFlechita)


        }

    }
}