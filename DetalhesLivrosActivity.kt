package com.example.catalogolivros

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalhesLivroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_livro)

        val titulo = intent.getStringExtra("titulo")
        val autor = intent.getStringExtra("autor")
        val data = intent.getStringExtra("data")
        val categoria = intent.getStringExtra("categoria")
        val nota = intent.getIntExtra("nota", 0)

        findViewById<TextView>(R.id.textTituloDetalhe).text = titulo
        findViewById<TextView>(R.id.textAutorDetalhe).text = "Autor: $autor"
        findViewById<TextView>(R.id.textDataDetalhe).text = "Data de leitura: $data"
        findViewById<TextView>(R.id.textCategoriaDetalhe).text = "Categoria: $categoria"
        findViewById<TextView>(R.id.textNotaDetalhe).text = "Nota: $nota"
    }
}
