package com.example.cacaotesouro.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cacaotesouro.data.clues

@Composable
fun ClueScreen(
    clueIndex: Int,
    totalClues: Int,
    onNextClue: (Int) -> Unit,
    onBack: () -> Unit
) {
    val clue = clues[clueIndex]
    val focusManager = LocalFocusManager.current

    var answer by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }

    fun tryAdvance() {
        focusManager.clearFocus()
        if (answer.trim().lowercase() == clue.answer.lowercase()) {
            showError = false
            onNextClue(clueIndex + 1)
        } else {
            showError = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Cabeçalho com progresso ───────────────────────────────────────────
        Text(
            text = "Pista ${clueIndex + 1} de $totalClues",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp
        )

        LinearProgressIndicator(
            progress = { (clueIndex + 1f) / totalClues },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── Card com o enigma ─────────────────────────────────────────────────
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "🔍 Enigma",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = clue.question,
                    fontSize = 16.sp,
                    lineHeight = 26.sp
                )
            }
        }

        // ── Dica (opcional) ───────────────────────────────────────────────────
        if (showHint) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = "💡 Dica: ${clue.hint}",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 15.sp
                )
            }
        } else {
            TextButton(onClick = { showHint = true }) {
                Text("Precisa de uma dica?")
            }
        }

        // ── Campo de resposta ─────────────────────────────────────────────────
        OutlinedTextField(
            value = answer,
            onValueChange = {
                answer = it
                showError = false
            },
            label = { Text("Digite sua resposta") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError,
            supportingText = {
                if (showError) {
                    Text(
                        text = "❌ Resposta incorreta. Tente novamente!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { tryAdvance() }),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── Botões de navegação ───────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("← Voltar")
            }

            Button(
                onClick = { tryAdvance() },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (clueIndex == totalClues - 1) "Finalizar 🏆" else "Próxima Pista →"
                )
            }
        }
    }
}
