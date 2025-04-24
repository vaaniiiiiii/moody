package moody.ui.screen


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import moody.navigation.SetupNavGraph
import moody.ui.theme.MoodyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodyTheme {
                SetupNavGraph()
            }
        }
    }
}
