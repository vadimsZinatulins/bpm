package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun Background() {
    val colorBase = Color(0xFFB3E5FC)
    val lighterShade = colorBase.copy(alpha = 0.66f)
    val lightestShade = colorBase.copy(alpha = 0.33f)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val constraints = constraints
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val heightOffset = height / 5f

        val getPath = { offsetCount: Int ->
            // val offset = heightOffset * height

            Path().apply {
                moveTo(0f, height / 2f + offsetCount * heightOffset)
                lineTo(0f, height)
                lineTo(width, height)
                lineTo(width, height / 3f + offsetCount * heightOffset)

                close()
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val lightestPath = getPath(0)

            drawPath(
                path = lightestPath,
                color = lightestShade
            )

            val lighterPath = getPath(1)

            drawPath(
                path = lighterPath,
                color = lighterShade
            )

            val basePath = getPath(2)

            drawPath(
                path = basePath,
                color = colorBase
            )
        }
    }
}
