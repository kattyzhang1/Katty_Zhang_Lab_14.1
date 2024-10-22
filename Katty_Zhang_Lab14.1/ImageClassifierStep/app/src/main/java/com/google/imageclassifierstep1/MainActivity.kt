package com.google.imageclassifierstep1

// Importaciones necesarias para manejar contextos de Android, imágenes y la clasificación con ML Kit.
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {
    // Método que se ejecuta al crear la actividad principal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo de borde a borde en la interfaz de la app
        setContentView(R.layout.activity_main) // Establece el layout principal de la actividad

        // Busca el ImageView definido en el layout
        val img: ImageView = findViewById(R.id.imageToLabel)

        // Nombre del archivo de imagen ubicado en la carpeta 'assets'
        val fileName = "flower1.jpg"

        // Convierte la imagen de la carpeta 'assets' a un Bitmap
        val bitmap: Bitmap? = assetsToBitmap(fileName)

        // Si el bitmap se ha generado correctamente, lo establece en el ImageView
        bitmap?.apply {
            img.setImageBitmap(this)
        }

        // Busca el TextView y Button definidos en el layout
        val txtOutput : TextView = findViewById(R.id.txtOutput)
        val btn: Button = findViewById(R.id.btnTest)

        // Configura el listener del botón para que realice una acción cuando sea clicado
        btn.setOnClickListener {
            // Crea un etiquetador de imágenes usando las opciones predeterminadas de ML Kit
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            // Convierte el Bitmap en un objeto InputImage que será procesado
            val image = InputImage.fromBitmap(bitmap!!, 0)

            var outputText = "" // Variable que almacenará el resultado del etiquetado

            // Procesa la imagen con el etiquetador
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Si la tarea de etiquetado es exitosa, itera sobre las etiquetas
                    for (label in labels) {
                        // Extrae el texto y la confianza de cada etiqueta
                        val text = label.text
                        val confidence = label.confidence

                        // Agrega el texto y la confianza a la variable de salida
                        outputText += "$text : $confidence\n"
                    }

                    // Muestra el resultado en el TextView
                    txtOutput.text = outputText
                }
                .addOnFailureListener { e ->
                    // Manejo de errores en caso de que el proceso de etiquetado falle
                }
        }
    }

    // Función para convertir un archivo en la carpeta 'assets' a un Bitmap
    fun Context.assetsToBitmap(fileName: String): Bitmap?{
        return try {
            with(assets.open(fileName)){
                BitmapFactory.decodeStream(this) // Decodifica el archivo de imagen en un Bitmap
            }
        } catch (e: IOException) {
            null // Si ocurre un error, devuelve null
        }
    }

}
