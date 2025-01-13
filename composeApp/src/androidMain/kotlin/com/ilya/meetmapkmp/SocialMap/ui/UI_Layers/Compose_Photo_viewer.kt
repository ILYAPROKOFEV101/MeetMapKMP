import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.size.Precision

@Composable
fun compose_photo_viewer(
    imageUrls: List<String>,
    navController: NavController,
    context: android.content.Context)
{
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(imageUrls) { url ->
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val state = rememberTransformableState{zoomChange, panChang, rotationChange->
                        scale = (scale * zoomChange).coerceIn(1f, 7f)
                        val extraWidth = (scale - 1) * constraints.maxWidth
                        val extraHeight = (scale - 1) * constraints.maxHeight

                        val maxX = extraWidth / 2
                        val maxY= extraHeight / 2

                        offset = Offset(
                            x = (offset.x + scale + panChang.x).coerceIn(-maxX, maxX),
                            y = (offset.y + scale + panChang.y).coerceIn(-maxY, maxY)
                        )
                        offset += panChang
                    }
                    Image(
                        painter = rememberImagePainter(
                            data = url,
                            builder = {
                                precision(Precision.EXACT)
                                // Добавьте другие параметры запроса по мере необходимости
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                            .transformable(state),

                        )
                }
            }
        }
    }
}