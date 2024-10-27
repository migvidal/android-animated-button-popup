package com.migvidal.expandablebutton

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun ExpandableButton(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = { expanded = !expanded }
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) togetherWith
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }, label = "size transform"
        ) { targetExpanded ->
            if (targetExpanded) {
                Text(text = "Hello")
            } else {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}

@Composable
fun OptionsPopup(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.small,
        color = Color.White
    ) {
        Card(
            modifier = Modifier.clickable { expanded = true },
            shape = MaterialTheme.shapes.small
        ) {
            val iconAlpha by animateFloatAsState(
                targetValue = if (expanded) 0f else 1f, animationSpec = tween(durationMillis = 100),
                label = "iconAlpha"
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(iconAlpha)
            )

            Popup(onDismissRequest = { expanded = false }) {
                Surface(shape = MaterialTheme.shapes.small, color = Color.Transparent) {
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandIn(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow)),
                        exit = shrinkOut(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow)),
                    ) {
                        Card {
                            val contentAlpha by animateFloatAsState(
                                targetValue = if (expanded) 1f else 0f,
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