package screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import data.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalFoundationApi::class)
class DatabaseScreen(val database: Database) : Screen {
    @Composable
    override fun Content() {
        // val decorator = ServiceLocator.getService<ICredentialDecoratorService>("CredentialDecorator")
        val state = rememberLazyListState()
        val scope = rememberCoroutineScope()

        val scrollTo = { index: Int ->
            scope.launch {
                state.animateScrollToItem(index, 0)
            }
        }

        BoxWithConstraints {
            if (this.maxWidth > 700.dp) {
                Row {
                    Groups(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(300.dp)
                            .background(Color.Blue.copy(alpha = 0.05f))
                    )
                    Credentials(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.Blue.copy(alpha = 0.025f))
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                    state = state,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
                ) {
                    item {
                        Groups(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(this@BoxWithConstraints.maxWidth)
                                .background(Color.Blue.copy(alpha = 0.05f))
                        ) {
                            scrollTo(1)
                        }
                    }
                    item {
                        Credentials(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(this@BoxWithConstraints.maxWidth)
                                .background(Color.Blue.copy(alpha = 0.025f))
                        ) {
                            scrollTo(0)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Groups(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
        Column(modifier) {
            onClick?.let {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ArrowCircleRight,
                        "To Credentials",
                        tint = Color(0xFFB3E5FC),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { it() }
                            .border(2.dp, Color(0xFF7AAFD3), CircleShape)
                    )
                }
            }

            Text("Groups")
        }
    }

    @Composable
    private fun Credentials(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
        Column(modifier) {
            onClick?.let {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ArrowCircleLeft,
                        contentDescription = "To Groups",
                        tint = Color(0xFFB3E5FC),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { it() }
                            .border(2.dp, Color(0xFF7AAFD3), CircleShape)
                    )
                }
            }
            Text("Credentials")
        }
    }
}