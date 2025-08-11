package com.example.catalogolivros

data class Livro (
    var id: Int,
    var titulo: String,
    var autor: String,
    var dataLeitura: String,
    var categoria: String,
    var nota: Int,
    var descricao: String
)
