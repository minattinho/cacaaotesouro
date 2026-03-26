package com.example.cacaotesouro.data

data class Clue(
    val question: String,
    val hint: String,
    val answer: String
)

val clues = listOf(
    Clue(
        question = "Tenho cidades, mas não tenho casas.\n" +
                "Tenho montanhas, mas não tenho árvores.\n" +
                "Tenho água, mas não tenho peixes.\n\nO que sou?",
        hint = "Você usa isso para se localizar e encontrar caminhos...",
        answer = "mapa"
    ),
    Clue(
        question = "Quanto mais você me tira, maior eu fico.\n\nO que sou?",
        hint = "Pense no chão quando alguém começa a escavar...",
        answer = "buraco"
    ),
    Clue(
        question = "Falo sem boca e ouço sem ouvidos.\n" +
                "Não tenho corpo, mas ganho vida com o vento.\n\nO que sou?",
        hint = "Quando você grita em uma montanha, o quê volta para você?",
        answer = "eco"
    )
)
