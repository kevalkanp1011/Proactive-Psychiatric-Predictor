package dev.kevalkanapriya.util

import android.graphics.PointF
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import dev.kevalkanapriya.umbrellaacademy.ui.theme.PurpleGrey40
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DrawTest() {
//    Spacer(
//        modifier = Modifier
//            .fillMaxSize()
//            .drawBehind {
//                drawCircle(
//                    Color.Magenta
//                )
//            }
//    )

//    Canvas(
//        modifier = Modifier.fillMaxSize(),
//        onDraw = {
//            drawCircle(
//                color = Color.Red,
//                center = Offset(
//                    x = 150.dp.toPx(),
//                    y = 180.dp.toPx()
//                ),
//                radius = 60.dp.toPx()
//            )
//        }
//    )

    Box(
        modifier = Modifier
            .background(PurpleGrey40)
            .fillMaxSize()
    ) {

        val animationProgress = remember { Animatable(0f) }
        var highlightedWeek by remember { mutableStateOf<Int?>(null) }
        val localView = LocalView.current

        LaunchedEffect(highlightedWeek) {
            if (highlightedWeek != null) {
                localView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
        }

        LaunchedEffect(key1 = graphData, block = {
            animationProgress.animateTo(1f, tween(3000))
        })

        val coroutineScope = rememberCoroutineScope()
        val textMeasurer = rememberTextMeasurer()
        val labelTextStyle = MaterialTheme.typography.labelSmall

        //Icon(painter = , contentDescription = )

        Spacer(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        coroutineScope.launch {
                            animationProgress.snapTo(0f)
                            animationProgress.animateTo(1f, tween(3000))
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            highlightedWeek =
                                (offset.x / (size.width / (graphData.size - 1))).roundToInt()
                        },
                        onDragEnd = { highlightedWeek = null },
                        onDragCancel = { highlightedWeek = null },
                        onDrag = { change, _ ->
                            highlightedWeek =
                                (change.position.x / (size.width / (graphData.size - 1))).roundToInt()
                        }
                    )
                }
                .drawWithCache {

                    val path = generatePath(graphData, size)
                    val filledPath = Path()
                    filledPath.addPath(path)
                    filledPath.relativeLineTo(0f, size.height)
                    filledPath.lineTo(0f, size.height)
                    filledPath.close()

                    onDrawBehind {
                        drawRect(
                            color = Color(0xFFC2BBBB),
                            style = Stroke(1.dp.toPx())
                        )

                        val verticalLines = 4
                        val verticalLineWidth = (size.width) / (verticalLines + 1)
                        repeat(verticalLines) { i ->
                            val startX = verticalLineWidth * (i + 1)
                            drawLine(
                                color = Color(0xFFC2BBBB),
                                start = Offset(startX, 0f),
                                end = Offset(startX, size.height)
                            )
                        }

                        val horizontalLines = 4
                        val horizontalLineSize = (size.height) / (horizontalLines + 1)
                        repeat(horizontalLines) { i ->
                            val startY = horizontalLineSize * (i + 1)
                            drawLine(
                                color = Color(0xFFC2BBBB),
                                start = Offset(0f, startY),
                                end = Offset(size.width, startY)
                            )
                        }


                        // draw line
                        clipRect(right = size.width * animationProgress.value) {
                            drawPath(path, Color.Green, style = Stroke(2.dp.toPx()))

                            drawPath(
                                filledPath,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Green.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                style = Fill
                            )
                        }

                        // draw highlight if user is dragging
                        highlightedWeek?.let {
                            this.drawHighlight(it, graphData, textMeasurer, labelTextStyle)
                        }


                    }
                }

        )
    }
}

fun generatePath(data: List<Balance>, size: Size): Path {

    val path = Path()

    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height

    data.forEachIndexed { i, balance ->

        if (i == 0) {
            //shift the path start point
            path.moveTo(
                0f,
                size.height - (balance.amount - min.amount).toFloat() *
                        heightPxPerAmount
            )
        }

        val balanceX = (i)* weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() *
                heightPxPerAmount
        //path.lineTo(balanceX, balanceY)

        // to do smooth curve graph - we use cubicTo, uncomment section below for non-curve
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            balanceX, balanceY
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY


    }

    return path

}

fun DrawScope.drawHighlight(
    highlightedWeek: Int,
    graphData: List<Balance>,
    textMeasurer: TextMeasurer,
    labelTextStyle: TextStyle
) {
    val amount = graphData[highlightedWeek].amount
    val minAmount = graphData.minBy { it.amount }.amount
    val range = graphData.maxBy { it.amount }.amount - minAmount
    val percentageHeight = ((amount - minAmount).toFloat() / range.toFloat())
    val pointY = size.height - (size.height * percentageHeight)
    // draw vertical line on week
    val x = highlightedWeek * (size.width / (graphData.size - 1))
    drawLine(
        HighlightColor,
        start = Offset(x, 0f),
        end = Offset(x, size.height),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    )

    // draw hit circle on graph
    drawCircle(
        Color.Green,
        radius = 4.dp.toPx(),
        center = Offset(x, pointY)
    )

    // draw info box
    val textLayoutResult = textMeasurer.measure("$amount", style = labelTextStyle)
    val highlightContainerSize = (textLayoutResult.size).toIntRect().inflate(4.dp.roundToPx()).size
    val boxTopLeft = ( x - (highlightContainerSize.width / 2f))
        .coerceIn(0f, size.width - highlightContainerSize.width)
    drawRoundRect(
        Color.White,
        topLeft = Offset(boxTopLeft, 0f),
        size = highlightContainerSize.toSize(),
        cornerRadius = CornerRadius(8.dp.toPx())
    )
    drawText(
        textLayoutResult,
        color = Color.Black,
        topLeft = Offset(boxTopLeft + 4.dp.toPx(), 4.dp.toPx())
    )
}

@RequiresApi(Build.VERSION_CODES.O)
val graphData = listOf(
    Balance(LocalDate.now(), BigDecimal(65631)),
    Balance(LocalDate.now().plusWeeks(1), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(2), BigDecimal(65851)),
    Balance(LocalDate.now().plusWeeks(3), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(4), BigDecimal(66484)),
    Balance(LocalDate.now().plusWeeks(5), BigDecimal(67684)),
    Balance(LocalDate.now().plusWeeks(6), BigDecimal(66684)),
    Balance(LocalDate.now().plusWeeks(7), BigDecimal(66984)),
    Balance(LocalDate.now().plusWeeks(8), BigDecimal(70600)),
    Balance(LocalDate.now().plusWeeks(9), BigDecimal(71600)),
    Balance(LocalDate.now().plusWeeks(10), BigDecimal(72600)),
    Balance(LocalDate.now().plusWeeks(11), BigDecimal(72526)),
    Balance(LocalDate.now().plusWeeks(12), BigDecimal(72976)),
    Balance(LocalDate.now().plusWeeks(13), BigDecimal(73589)),
)

data class Balance(val date: LocalDate, val amount: BigDecimal)

val PurpleBackgroundColor = Color(0xff322049)
val BarColor = Color.White.copy(alpha = 0.3f)
val HighlightColor = Color.White.copy(alpha = 0.7f)

