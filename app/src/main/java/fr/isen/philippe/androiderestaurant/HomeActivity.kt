package fr.isen.philippe.androiderestaurant

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.philippe.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class RestaurantMenuItem {
    STARTER, MAIN, DESSERT
}

interface MenuInterface {
    fun itemPressed(itemType: RestaurantMenuItem)
}

class MainActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(this)
                }
            }
        }
    }

    override fun itemPressed(itemType: RestaurantMenuItem) {
        Toast.makeText(this, "Mon toast", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun Screen(menu: MenuInterface) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RestaurantMenuItem.values().forEach { menuItem ->
            ElevatedButton(onClick = { menu.itemPressed(menuItem) }) {
                Text(
                    text = stringResource(
                        id = when (menuItem) {
                            RestaurantMenuItem.STARTER -> R.string.entrées_btn
                            RestaurantMenuItem.MAIN -> R.string.entrées_btn
                            RestaurantMenuItem.DESSERT -> R.string.desserts_btn
                        }
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidERestaurantTheme {
        Screen(MainActivity())
    }
}