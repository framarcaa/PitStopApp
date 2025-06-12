package com.example.pitstopapp.ui.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun BestLapBarChart(bestLaps: List<Pair<String, Float>>) {
    val barColor = Color(0xFF3F51B5)

    var startAnimation by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "barChartProgress"
    )

    // Avvia animazione on composition
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFFF4F4F4), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val padding = 40f
            val width = size.width
            val height = size.height

            val plotWidth = width - 2 * padding
            val plotHeight = height - 2 * padding

            val maxTime = bestLaps.maxOfOrNull { it.second } ?: 5f
            val minTime = 0f

            val barCount = bestLaps.size
            val barWidth = plotWidth / barCount

            val barColors = listOf(
                Color(0xFF3F51B5), // Blu
                Color(0xFFF44336), // Rosso
                Color(0xFF4CAF50) // Verde
            )

            bestLaps.forEachIndexed { index, (track, time) ->
                val x = padding + index * barWidth
                val barHeight = ((time - minTime) / (maxTime - minTime)) * plotHeight * animatedProgress
                val y = height - padding - barHeight
                val color = barColors[index % barColors.size]

                // Disegna la barra
                drawRect(
                    color = color,
                    topLeft = Offset(x + barWidth * 0.1f, y),
                    size = Size(barWidth * 0.8f, barHeight)
                )

                // Etichette X (nomi pista)
                val paint = android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 28f
                    isAntiAlias = true
                }

                drawContext.canvas.nativeCanvas.save()
                drawContext.canvas.nativeCanvas.rotate(
                    45f, // ruota di 45 gradi (verticale)
                    x, // centro della rotazione X
                    height + 60f      // centro della rotazione Y
                )
                drawContext.canvas.nativeCanvas.drawText(
                    track,
                    x + barWidth / 2,
                    height - 10f,
                    paint
                )
                drawContext.canvas.nativeCanvas.restore()


                // Etichetta tempo sopra la barra
                drawContext.canvas.nativeCanvas.drawText(
                    "${"%.2f".format(time)}m",
                    x + barWidth / 2,
                    y - 10f,
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 26f
                    }
                )
            }

            // Asse Y
            drawLine(Color.Black, Offset(padding, padding), Offset(padding, height - padding), 2f)

            // Asse X
            drawLine(Color.Black, Offset(padding, height - padding), Offset(width - padding, height - padding), 2f)

            // Etichette Y (tempi ogni 5 secondi)
            val steps = 5
            val stepSize = ((maxTime - minTime) / steps)
            for (i in 0..steps) {
                val value = minTime + i * stepSize
                val y = height - padding - ((value - minTime) / (maxTime - minTime)) * plotHeight

                drawContext.canvas.nativeCanvas.drawText(
                    "${"%.2f".format(value)}m",
                    padding - 10f,
                    y + 10f,
                    android.graphics.Paint().apply {
                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }
                )
            }
        }
    }
}