package com.example.catalogolivros

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListaLivrosActivity : AppCompatActivity() {

    private lateinit var recyclerLivros: RecyclerView
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: LivroAdapter
    private lateinit var fabAdicionar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_livros)

        db = DatabaseHelper(this)

        recyclerLivros = findViewById(R.id.recyclerLivros)
        recyclerLivros.layoutManager = LinearLayoutManager(this)

        val livros = db.buscarTodosLivros()

        adapter = LivroAdapter(this, livros) { livroClicado ->
            val intent = Intent(this, DetalhesLivroActivity::class.java).apply {
                putExtra("titulo", livroClicado.titulo)
                putExtra("autor", livroClicado.autor)
                putExtra("data", livroClicado.dataLeitura)
                putExtra("categoria", livroClicado.categoria)
                putExtra("nota", livroClicado.nota)
            }
            startActivity(intent)
        }
        recyclerLivros.adapter = adapter

        fabAdicionar = findViewById<FloatingActionButton>(R.id.fabAdicionar)
        fabAdicionar.setOnClickListener {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
    }
    }

    fun excluirLivro(livro: Livro) {
        val builder = android.app.AlertDialog.Builder(this@ListaLivrosActivity)
        builder.setTitle("Excluir livro")
        builder.setMessage("Tem certeza que deseja excluir \"${livro.titulo}\"?")
        builder.setPositiveButton("Sim") { _, _ ->
            db.deletarLivro(livro.id)
            atualizarLista()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun atualizarLista() {
        val livrosAtualizados = db.buscarTodosLivros()

        adapter.updateLivros(livrosAtualizados)
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }
}
