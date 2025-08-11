package com.example.catalogolivros

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LivroAdapter(
    private val context: Context,
    private var listaLivros: List<Livro>,
    private val onItemClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    inner class LivroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.textTitulo)
        val autor: TextView = itemView.findViewById(R.id.textAutor)
        val data: TextView = itemView.findViewById(R.id.textData)
        val categoria: TextView = itemView.findViewById(R.id.textCategoria)
        val nota: TextView = itemView.findViewById(R.id.textNota)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluir)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(listaLivros[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = listaLivros[position]
        holder.titulo.text = livro.titulo
        holder.autor.text = context.getString(R.string.autor_formatada, livro.autor)
        holder.data.text = context.getString(R.string.data_formatada, livro.dataLeitura)
        holder.categoria.text = context.getString(R.string.categoria_formatada, livro.categoria)
        holder.nota.text = context.getString(R.string.nota_formatada, livro.nota)

        holder.btnEditar.setOnClickListener {
            val intent = Intent(context, CadastroActivity::class.java).apply {
                putExtra("id", livro.id)
                putExtra("titulo", livro.titulo)
                putExtra("autor", livro.autor)
                putExtra("data", livro.dataLeitura)
                putExtra("categoria", livro.categoria)
                putExtra("nota", livro.nota)
            }
            context.startActivity(intent)
        }

        holder.btnExcluir.setOnClickListener {
            val db = DatabaseHelper(context)
            val sucesso = db.deletarLivro(livro.id)

            if (sucesso && context is ListaLivrosActivity) {

                context.recreate()

            }
        }
    }

    override fun getItemCount(): Int = listaLivros.size

    fun updateLivros(novaLista: List<Livro>) {
        listaLivros = novaLista
        notifyDataSetChanged()
    }
}
