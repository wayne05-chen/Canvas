package tw.edu.pu.csim.tcyang.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import tw.edu.pu.csim.tcyang.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestCanvas(
                        modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
}
@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    var CanvasWidth by remember { mutableStateOf(0f) }
    var CamvaHeight by remember { mutableStateOf(0f) }
    var ninionsX by remember { mutableStateOf(0f) }
    var ninionsY by remember { mutableStateOf(0f) }
    var gameOver by remember { mutableStateOf(false) }
    val minionsImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.minions)

    LaunchedEffect(key1 = Unit) {
        while (!gameOver) {
            delay(timeMillis = 16L) // 建議調小延遲（例如 16ms 約 60fps），移動會更加平滑

            ninionsX += 5f
            ninionsY += 10f

            // 當畫布寬高已取得，且小小兵超出畫布右方或下方時重置回原點
            if (CanvasWidth > 0f && CamvaHeight > 0f) {
                if (ninionsX > CanvasWidth || ninionsY > CamvaHeight) {
                    ninionsX = 0f
                    ninionsY = 0f
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // 修正：原本第二行誤寫為 CanvasWidth
            CanvasWidth = size.width
            CamvaHeight = size.height

            drawCircle(
                color = Color.Magenta,
                radius = 100f,
                center = Offset(x = 100f, y = 100f)
            )

            // 繪製藍色空心矩形，左上角 (300, 200)，寬度與高度 200
            drawRect(
                color = Color.Blue,
                topLeft = Offset(x = 300f, y = 200f),
                size = Size(width = 200f, height = 200f),
                style = Stroke(width = 5f) // 使用 style 控制外框樣式
            )

            drawLine(
                color = Color.Red,
                start = Offset(x = 200f, y = 800f),
                end = Offset(x = size.width, y = size.height),
                strokeWidth = 10f
            )

            val minionsWidth = minionsImage.width
            val minionsHeight = minionsImage.height

            // 將畫布起點移動至 ninionsX, ninionsY 後繪圖
            translate(left = ninionsX, top = ninionsY) {
                drawImage(minionsImage)
            }
        }

        Column {
            Text("畫布寬度 $CanvasWidth, 長度 $CamvaHeight")

            Button(
                onClick = { gameOver = !gameOver }
            ) {
                if (gameOver) {
                    Text("繼續遊戲")
                } else {
                    Text("停止遊戲")
                }
            }
        }
    }
}