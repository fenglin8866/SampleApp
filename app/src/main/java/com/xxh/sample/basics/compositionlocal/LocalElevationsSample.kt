package com.xxh.sample.basics.compositionlocal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xxh.sample.common.component.XDivider
import com.xxh.sample.common.component.XInput

@Composable
fun LocalElevationsSample() {

    var text by remember {
        mutableStateOf("")
    }

    var cardEle by remember {
        mutableIntStateOf(10)
    }

    XInput(text, onValueChange = { text = it }, doClick = {
        cardEle = text.toInt()
    })

    XDivider()
    // Calculate elevations based on the system theme
    val elevations = if (isSystemInDarkTheme()) {
        Elevations(3.dp, default = 3.dp)
    } else {
        Elevations(10.dp, default = 10.dp)
    }
    CompositionLocalProvider(LocalElevations provides elevations) {
        Card(
                backgroundColor = MaterialTheme.colors.onError,
                elevation = LocalElevations.current.card,
                modifier = Modifier
                        .size(115.dp)
                        .padding(4.dp)
        ) {
            Log.d("LocalElevationsSample", "CompositionLocalProvider1")
            // Content
            Text("测试", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }

    }
    Card(
            backgroundColor = MaterialTheme.colors.onError,
            elevation = LocalElevations.current.card,
            modifier = Modifier
                    .size(115.dp)
                    .padding(4.dp)
    ) {
        Log.d("LocalElevationsSample", "CompositionLocalProvider0")
        Text("测试", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }

    Row {
        CompositionLocalProvider(
                LocalElevations provides Elevations(
                        cardEle.dp,
                        default = cardEle.dp
                )
        ) {
            Log.d("LocalElevationsSample", "CompositionLocalProvider2")
            Card(
                    backgroundColor = MaterialTheme.colors.onError,
                    elevation = LocalElevations.current.card,
                    modifier = Modifier
                            .size(115.dp)
                            .padding(4.dp)
            ) {
                Log.d("LocalElevationsSample", "CompositionLocalProvider3")
                // Content
                Text(
                        "测试",
                        modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Red),
                        textAlign = TextAlign.Center
                )
            }

            Row(
                    modifier = Modifier
                            .background(Color.Green)
                            .size(115.dp)
                            .padding(LocalElevations.current.card)
            ) {
                Log.d("LocalElevationsSample", "CompositionLocalProvider3 Row")
                // Content
                Text("测试", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }

            Button(onClick = { /*TODO*/ }) {
                Log.d("LocalElevationsSample", "CompositionLocalProvider Button ")
                Text(text = "test")
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Log.d("LocalElevationsSample", "CompositionLocalProvider4")
                Column {
                    // Content
                    Text("测2", textAlign = TextAlign.Center)
                    Text("测3", textAlign = TextAlign.Center)
                    Text("测4", textAlign = TextAlign.Center)
                    Log.d("LocalElevationsSample", "CompositionLocalProvider5")
                }

                Box {
                    Text("测5", textAlign = TextAlign.Center)
                    Log.d("LocalElevationsSample", "CompositionLocalProvider6")
                }
            }

        }
        Box {
            Text("测6", textAlign = TextAlign.Center)
            Log.d("LocalElevationsSample", "CompositionLocalProvider7")
        }
    }
}


data class Elevations(val card: Dp = 2.dp, val default: Dp = 0.dp)

val LocalElevations = staticCompositionLocalOf {
    Log.d("LocalElevationsSample", "LocalElevations")
    Elevations()
}

/*
21:59:30.426  D  CompositionLocalProvider 0
21:59:30.458  D  CompositionLocalProvider1
21:59:30.458  D  LocalElevations
21:59:30.459  D  CompositionLocalProvider0
21:59:30.459  D  CompositionLocalProvider2
21:59:30.459  D  CompositionLocalProvider3
21:59:30.461  D  CompositionLocalProvider Button
21:59:30.461  D  CompositionLocalProvider4
21:59:30.462  D  CompositionLocalProvider5
21:59:30.462  D  CompositionLocalProvider6
21:59:30.462  D  CompositionLocalProvider7
21:59:30.472  D  CompositionLocalProvider 0 SideEffect

compositionLocalOf构建的LocalElevations：类似正常的可组合项，读取其current值的对象改变，但content内智能重组
在重组期间更改提供的值只会使读取其current值的内容无效
21:59:44.907  D  CompositionLocalProvider7
21:59:47.252  D  CompositionLocalProvider2
21:59:47.254  D  CompositionLocalProvider4
21:59:47.254  D  CompositionLocalProvider5
21:59:47.254  D  CompositionLocalProvider6
21:59:47.255  D  CompositionLocalProvider7

staticCompositionLocalOf构建的LocalElevations：CompositionLocalProvider作用域内所有可组合项全部重组。注意不是智能重组，是重新执行
与compositionLocalOf不同，Compose不会跟踪staticCompositionLocalOf的读取。更改该值会导致提供CompositionLocal的整个content lambda被重组，而不仅仅是在组合中读取current值的位置。
如果为CompositionLocal提供的值发生更改的可能性微乎其微或永远不会更改，使用staticCompositionLocalOf可提高性能。
22:10:25.625  D  CompositionLocalProvider7
22:10:26.649  D  CompositionLocalProvider2
22:10:26.652  D  CompositionLocalProvider3
22:10:26.660  D  CompositionLocalProvider Button
22:10:26.662  D  CompositionLocalProvider4
22:10:26.665  D  CompositionLocalProvider5
22:10:26.667  D  CompositionLocalProvider6
22:10:26.667  D  CompositionLocalProvider7
 */
