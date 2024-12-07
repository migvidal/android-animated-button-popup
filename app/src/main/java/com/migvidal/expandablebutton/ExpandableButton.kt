package com.migvidal.expandablebutton

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.migvidal.expandablebutton.ui.theme.ExpandableButtonTheme

@Composable
fun ExpandableButton(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.small,
        color = Color.White,
    ) {
        val expandedState = remember { MutableTransitionState(false) }
        var iconVisible by remember { mutableStateOf(true) }

        if (!expandedState.currentState && !expandedState.targetState) {
            iconVisible = true
        }

        Card(
            modifier = Modifier.clickable {
                expandedState.targetState = true
                iconVisible = false
            },
            shape = MaterialTheme.shapes.small,
        ) {
            val iconAlpha: Float by animateFloatAsState(
                targetValue = if (iconVisible) 1f else 0f,
                animationSpec = tween(durationMillis = 100),
                label = "iconAlpha",
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(iconAlpha),
            )

            Popup(onDismissRequest = { expandedState.targetState = false }) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color.Transparent,
                    shadowElevation = 0.dp,
                ) {
                    AnimatedVisibility(
                        visibleState = expandedState,
                        enter = expandIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            )
                        ),
                        exit = shrinkOut(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                        ),
                    ) {
                        Card(
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            val contentAlpha by animateFloatAsState(
                                targetValue = if (expandedState.currentState) 1f else 0f,
                                label = "contentAlpha",
                                animationSpec = tween(durationMillis = 100)
                            )
                            Text(
                                modifier = Modifier
                                    .padding(32.dp)
                                    .alpha(contentAlpha),
                                text = "Content, content and more content"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ExpandableButtonPreview() {
    ExpandableButtonTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                ExpandableButton()
            }
        }
    }
}