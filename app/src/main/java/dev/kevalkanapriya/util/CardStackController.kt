package dev.kevalkanapriya.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import dev.kevalkanapriya.umbrellaacademy.domain.model.QnA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

open class CardStackController(
    val scope: CoroutineScope,
    private val screenWidth: Float,
    internal val animationSpec: AnimationSpec<Float> = SpringSpec()
) {
    val right = Offset(screenWidth, 0f)
    val left = Offset(-screenWidth, 0f)
    val center = Offset(0f, 0f)

    var threshold = 0.0f

    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)
    val rotation = Animatable(0f)
    val scale = Animatable(0.8f)

    var onSwipeLeft: () -> Unit = {}
    var onSwipeRight: () -> Unit = {}

    fun swipeLeft() {
        scope.apply {
            launch {
                offsetX.animateTo(-screenWidth, animationSpec)

                onSwipeLeft()

                launch {
                    offsetX.snapTo(center.x)
                }

                launch {
                    offsetY.snapTo(0f)
                }

                launch {
                    rotation.snapTo(0f)
                }

                launch {
                    scale.snapTo(0.8f)
                }
            }

            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun swipeRight() {
        scope.apply {
            launch {
                offsetX.animateTo(screenWidth, animationSpec)

                onSwipeRight()

                launch {
                    offsetX.snapTo(center.x)
                }

                launch {
                    offsetY.snapTo(0f)
                }

                launch {
                    scale.snapTo(0.8f)
                }

                launch {
                    rotation.snapTo(0f)
                }
            }

            launch {
                scale.animateTo(1f, animationSpec)
            }
        }
    }

    fun returnCenter() {
        scope.apply {
            launch {
                offsetX.animateTo(center.x, animationSpec)
            }

            launch {
                offsetY.animateTo(center.y, animationSpec)
            }

            launch {
                rotation.animateTo(0f, animationSpec)
            }

            launch {
                scale.animateTo(0.8f, animationSpec)
            }
        }
    }
}

@Composable
fun rememberCardStackController(
    animationSpec: AnimationSpec<Float> = SpringSpec()
): CardStackController {
    val scope = rememberCoroutineScope()
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    return remember {
        CardStackController(
            scope = scope,
            screenWidth = screenWidth,
            animationSpec = animationSpec
        )
    }
}



fun Modifier.draggableStack(
    controller: CardStackController,
    thresholdConfig: (Float, Float) -> ThresholdConfig2,
    velocityThreshold: Dp = 125.dp
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val velocityThresholdPx = with(density) {
        velocityThreshold.toPx()
    }

    val thresholds = { a: Float, b: Float ->
        with(thresholdConfig(a, b)) {
            density.computeThreshold(a, b)
        }
    }

    controller.threshold = thresholds(controller.center.x, controller.right.x)

    Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDragEnd = {
                if (controller.offsetX.value <= 0f) {
                    if (controller.offsetX.value > -controller.threshold) {
                        controller.returnCenter()
                    } else {
                        controller.swipeLeft()
                    }
                } else {
                    if (controller.offsetX.value < controller.threshold) {
                        controller.returnCenter()
                    } else {
                        controller.swipeRight()
                    }
                }
            },
            onDrag = { change, dragAmount ->
                controller.scope.apply {
                    launch {
                        controller.offsetX.snapTo(controller.offsetX.value + dragAmount.x)
                        controller.offsetY.snapTo(controller.offsetY.value + dragAmount.y)

                        val targetRotation = normalize(
                            controller.center.x,
                            controller.right.x,
                            abs(controller.offsetX.value),
                            0f,
                            10f
                        )

                        controller.rotation.snapTo(targetRotation * -controller.offsetX.value.sign)

                        controller.scale.snapTo(
                            normalize(
                                controller.center.x,
                                controller.right.x / 3,
                                abs(controller.offsetX.value),
                                0.8f
                            )
                        )
                    }
                }
                change.consume()
            }
        )
    }
}

private fun normalize(
    min: Float,
    max: Float,
    v: Float,
    startRange: Float = 0f,
    endRange: Float = 1f
): Float {
    require(startRange < endRange) {
        "Start range is greater than end range"
    }

    val value = v.coerceIn(min, max)

    return (value - min) / (max - min) * (endRange + startRange) + startRange
}


@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    items: MutableList<QnA>,
    thresholdConfig: (Float, Float) -> ThresholdConfig2 = { _, _ -> FractionalThreshold(0.2f) },
    velocityThreshold: Dp = 125.dp,
    onSwipeLeft: (item: QnA) -> Unit = {},
    onSwipeRight: (item: QnA) -> Unit = {},
    onClick2: (item: QnA) -> Unit = {},
    onClick3: (item: QnA) -> Unit = {},
    onEmptyStack: (lastItem: QnA) -> Unit = {}
) {


    var i by remember {
        mutableIntStateOf(items.size - 1)
    }

    if (i == -1) {
        onEmptyStack(items.last())
    }

    val cardStackController = rememberCardStackController()

    cardStackController.onSwipeLeft = {
        onSwipeLeft(items[i])
        i--
    }

    cardStackController.onSwipeRight = {
        onSwipeRight(items[i])
        i--
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        val stack = createRef()

        Box(
            modifier = modifier
                .constrainAs(stack) {
                    top.linkTo(parent.top)
                }
                .draggableStack(
                    controller = cardStackController,
                    thresholdConfig = thresholdConfig,
                    velocityThreshold = velocityThreshold
                )
                .fillMaxHeight()
        ) {
            items.asReversed().forEachIndexed { index, item ->
                Card(
                    modifier = Modifier
                        .moveTo(
                            x = if (index == i) cardStackController.offsetX.value else 0f,
                            y = if (index == i) cardStackController.offsetY.value else 0f
                        )
                        .visible(visible = index == i || index == i - 1)
                        .graphicsLayer(
                            rotationZ = if (index == i) cardStackController.rotation.value else 0f,
                            scaleX = if (index < i) cardStackController.scale.value else 1f,
                            scaleY = if (index < i) cardStackController.scale.value else 1f
                        ),
                    item,
                    cardStackController,
                    onClick2,
                    onClick3
                )
            }
        }
    }
}

fun Modifier.moveTo(
    x: Float,
    y: Float
) = this.then(Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(x.roundToInt(), y.roundToInt())
    }
})

fun Modifier.visible(
    visible: Boolean = true
) = this.then(Modifier.layout{ measurable, constraints ->
    val placeable = measurable.measure(constraints)

    if (visible) {
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    } else {
        layout(0, 0) {}
    }
})

data class FractionalThreshold(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    private val fraction: Float
) : ThresholdConfig2 {
    override fun Density.computeThreshold(fromValue: Float, toValue: Float): Float {
        return lerp(fromValue, toValue, fraction)
    }
}

interface ThresholdConfig2 {
    /**
     * Compute the value of the threshold (in pixels), once the values of the anchors are known.
     */
    fun Density.computeThreshold(fromValue: Float, toValue: Float): Float
}

data class Item(
    val url: String? = null,
    val text: String = "",
    val subText: String = ""
)

@Composable
fun Card(
    modifier: Modifier = Modifier,
    item: QnA,
    cardStackController: CardStackController,
    onClick2: (item: QnA) -> Unit,
    onClick3: (item: QnA) -> Unit
) {
    Box(modifier = modifier) {

        AsyncImage(
            model = "https://images.unsplash.com/photo-1618641986557-1ecd230959aa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize()
        )

        Column(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.Black.copy(0.5f))
                .padding(10.dp)



        ) {
            Text(text = item.no.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 25.sp)

            Text(text = item.question, color = Color.White, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(50.dp))
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {


            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = {
                cardStackController.swipeLeft()
            }) {
                Text(text = "Did not apply to me at all", textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = modifier
                .fillMaxWidth(),
                onClick = {
                onClick2(item)
            }) {
                Text(text = "Applied to me to some degree, or some of the time", textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = modifier
                .fillMaxWidth(),
                onClick = {
                onClick3(item)
            }) {
                Text(text = "Applied to me to a considerable degree, or a good part of the time", textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = {
                cardStackController.swipeRight()
            }) {
                Text(text = "Applied to me very much, or most of the time", textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }

//        Row(
//            modifier = modifier
//                .align(Alignment.BottomStart)
//                .padding(10.dp)
//        ) {
//            IconButton(
//                modifier = modifier.padding(50.dp, 0.dp, 0.dp, 0.dp),
//                onClick = { cardStackController.swipeLeft() },
//            ) {
//                Icon(
//                    Icons.Default.Close, contentDescription = "", tint = Color.White, modifier =
//                    modifier
//                        .height(50.dp)
//                        .width(50.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            IconButton(
//                modifier = modifier.padding(0.dp, 0.dp, 50.dp, 0.dp),
//                onClick = { cardStackController.swipeRight() }
//            ) {
//                Icon(
//                    Icons.Default.ThumbUp, contentDescription = "", tint = Color.White, modifier =
//                    modifier
//                        .height(50.dp)
//                        .width(50.dp)
//                )
//            }
//        }
    }
}