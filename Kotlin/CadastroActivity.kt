package com.example.catalogolivros

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class CadastroActivity : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editAutor: EditText
    private lateinit var editData: EditText
    private lateinit var editCategoria: EditText
    private lateinit var editDescricao: EditText
    private lateinit var seekNota: SeekBar
    private lateinit var textNota: TextView
    private lateinit var btnSalvar: Button
    private lateinit var db: DatabaseHelper

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cadastro)
            Toast.makeText(this, "Tela de cadastro carregada", Toast.LENGTH_SHORT).show()

         editTitulo = findViewById(R.id.editTitulo)
         editAutor = findViewById(R.id.editAutor)
         editData = findViewById(R.id.editData)
         editCategoria = findViewById(R.id.editCategoria)
         editDescricao = findViewById(R.id.editDescricao)
         seekNota = findViewById(R.id.seekNota)
         textNota = findViewById(R.id.textNota)
         btnSalvar = findViewById(R.id.btnSalvar)

        db = DatabaseHelper(this)

        seekNota.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textNota.text = getString(R.string.nota_formatada, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnSalvar.setOnClickListener {
            salvarLivro()
        }
            val btnVerLivros = Button(this).apply {
                text = "Ver Livros"
                setOnClickListener {
                    val intent = Intent(this@CadastroActivity, ListaLivrosActivity::class.java)
                    startActivity(intent)
                }
            }
            (findViewById<LinearLayout>(R.id.layoutCadastro)).addView(btnVerLivros)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val canal = NotificationChannel(
                    "lembrete_leitura",
                    "Lembrete de Leitura",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val manager = getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(canal)
            }
        }

    private fun salvarLivro() {
        val titulo = editTitulo.text.toString().trim()
        val autor = editAutor.text.toString().trim()
        val data = editData.text.toString().trim()
        val categoria = editCategoria.text.toString().trim()
        val descricao = editDescricao.text.toString().trim()
        val nota = seekNota.progress

        if (titulo.isEmpty()) {
            editTitulo.error = "Informe o título"
            editTitulo.requestFocus()
            return
        }

        if (autor.isEmpty()) {
            editAutor.error = "Informe o autor"
            editAutor.requestFocus()
            return
        }

        if (data.isEmpty()) {
            editData.error = "Informe a data de leitura"
            editData.requestFocus()
            return
        }

        if (!dataValida(data)) {
            editData.error = "Data inválida (ex: 25/06/2025)"
            editData.requestFocus()
            return
        }

        if (categoria.isEmpty()) {
            editCategoria.error = "Informe a categoria"
            editCategoria.requestFocus()
            return
        }
        val livro = Livro(
            id = 0,
            titulo = titulo,
            autor = autor,
            dataLeitura = data,
            categoria = categoria,
            nota = nota,
            descricao = descricao
        )

        val sucesso = db.inserirLivro(livro)

        if (sucesso) {
            Toast.makeText(this, "Livro salvo com sucesso!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LembreteReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val triggerTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000 // 24 horas depois

            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )

            limparCadastro()

        } else {
            Toast.makeText(this, "Erro ao salvar o livro.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limparCadastro() {
        editTitulo.setText("")
        editAutor.setText("")
        editData.setText("")
        editCategoria.setText("")
        editDescricao.setText("")
        seekNota.progress = 0
        textNota.text = getString(R.string.nota_formatada, 0)
    }
    private fun dataValida(data: String): Boolean {
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        formato.isLenient = false
        return try {
            formato.parse(data)
            true
        } catch (e: Exception) {
            false
        }
    }
}
