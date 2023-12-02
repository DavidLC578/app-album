    package com.campusfp.hito2album

    import android.app.Activity
    import android.content.Intent
    import android.net.Uri
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.activity.result.ActivityResultLauncher
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.content.FileProvider
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import java.io.File

    class CustomAdapterImagenes (private val context: AppCompatActivity, private val todasLasImagenes: List<Imagen>): RecyclerView.Adapter<CustomAdapterImagenes.ViewHolder>() {

        // Array de tipo de la clase Imagen
        private var imagenesFiltradas: List<Imagen> = todasLasImagenes




        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            val v =
                LayoutInflater.from(viewGroup.context).inflate(R.layout.lista_imagenes, viewGroup, false)
            return ViewHolder(v)
        }


        /**
         * Muestra los datos recuperados de la base de datos
         */
        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            val imagen = imagenesFiltradas[i]

            viewHolder.itemTitulo.text = imagen.titulo
            viewHolder.itemDescripcion.text = imagen.descripcion
            viewHolder.itemFecha.text = imagen.fecha
            Glide.with(context)
                .load(Uri.parse(imagen.imagenRes))
                .into(viewHolder.itemImagen)
        }

        /**
         * Devuelve el número de imagenes filtradas que hay en la lista de imagenes
         */
        override fun getItemCount(): Int {
            return imagenesFiltradas.size
        }


        /**
         * Filtrar las fechas para que cada imagen esté en la categoría de fecha correcta
         * Recibe como parámetro la fecha y comprueba que sea la misma
         */
        fun filtrarPorFecha(fecha: String) {
            imagenesFiltradas = todasLasImagenes.filter { it.fecha == fecha }
            notifyDataSetChanged()
        }
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var itemTitulo: TextView
            var itemImagen: ImageView
            var itemDescripcion: TextView
            var itemFecha: TextView
            init {
                itemTitulo = itemView.findViewById(R.id.txtTituloImagen)
                itemDescripcion = itemView.findViewById(R.id.txtDescripcionImagen)
                itemImagen = itemView.findViewById(R.id.imgImagen)
                itemFecha = itemView.findViewById(R.id.txtFechaImagen)
            }
        }
    }