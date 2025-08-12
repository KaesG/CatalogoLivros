package com.example.catalogolivros

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, "livros.db", null, 1
    ) {
    companion object {
        const val TABLE_NAME = "livros"
        const val COL_ID = "id"
        const val COL_TITULO = "titulo"
        const val COL_AUTOR = "autor"
        const val COL_DATA = "dataLeitura"
        const val COL_CATEGORIA = "categoria"
        const val COL_NOTA = "nota"
        const val COL_DESCRICAO = "descricao"
    }
        override fun onCreate(db: SQLiteDatabase) {
            val createTable ="""
                CREATE TABLE $TABLE_NAME(
                        $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        $COL_TITULO TEXT NOT NULL,
                        $COL_AUTOR TEXT NOT NULL,
                        $COL_DATA TEXT,
                        $COL_CATEGORIA TEXT,
                        $COL_NOTA INTEGER,
                        $COL_DESCRICAO TEXT
                        );
            """.trimIndent()
                    db.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun inserirLivro(livro: Livro): Boolean {
            val db = this.writableDatabase
            val cv = ContentValues().apply {
                put(COL_TITULO, livro.titulo)
                put(COL_AUTOR, livro.autor)
                put(COL_DATA, livro.dataLeitura)
                put(COL_CATEGORIA, livro.categoria)
                put(COL_NOTA, livro.nota)
                put(COL_DESCRICAO, livro.descricao)
            }
            val result = db.insert(TABLE_NAME, null, cv)
            return result != -1L
        }

        fun listarLivros(): List<Livro>{
            val lista = mutableListOf<Livro>()
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
            if (cursor.moveToFirst()) {
                do {
                    val livro = Livro(
                        id = cursor.getInt(0),
                        titulo = cursor.getString(1),
                        autor = cursor.getString(2),
                        dataLeitura = cursor.getString(3),
                        categoria = cursor.getString(4),
                        nota = cursor.getInt(5),
                        descricao = cursor.getString(6)
                    )
                    lista.add(livro)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return lista
        }

        fun atualizarLivro(livro: Livro): Boolean{
            val db = this.writableDatabase
            val cv = ContentValues().apply {
                put(COL_TITULO, livro.titulo)
                put(COL_AUTOR, livro.autor)
                put(COL_DATA, livro.dataLeitura)
                put(COL_CATEGORIA, livro.categoria)
                put(COL_NOTA, livro.nota)
                put(COL_DESCRICAO, livro.descricao)
            }
            val result = db.update(TABLE_NAME, cv, "$COL_ID = ?", arrayOf(livro.id.toString()))
            return result > 0
        }

        fun deletarLivro(id: Int): Boolean {
            val db = this.writableDatabase
            val result = db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
            return result > 0
        }

    fun buscarTodosLivros(): List<Livro> {
        val lista = mutableListOf<Livro>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM livros", null)

        if (cursor.moveToFirst()) {
            do {
                val livro = Livro(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    autor = cursor.getString(cursor.getColumnIndexOrThrow("autor")),
                    dataLeitura = cursor.getString(cursor.getColumnIndexOrThrow("dataLeitura")),
                    categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                    nota = cursor.getInt(cursor.getColumnIndexOrThrow("nota")),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
                )
                lista.add(livro)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }
}
