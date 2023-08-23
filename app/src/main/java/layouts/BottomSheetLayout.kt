package layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayout (btmSheetState: ModalBottomSheetState,
                       cornerRadius: Dp = 10.dp,
                       onCamera:()->Unit,
                       onGallery:()->Unit){

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        modifier = Modifier.navigationBarsPadding(),
        sheetState = btmSheetState,
        sheetShape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
        sheetContent = {
      Column(
          Modifier.padding(10.dp)
      ) {
          Button(
              modifier = Modifier.fillMaxWidth(),
              onClick = {
                  coroutineScope.launch {
                      btmSheetState.hide()
                  }
                  onCamera()
              }) {
              Text("Take Photo")
          }

          Button(
              modifier = Modifier.fillMaxWidth(),
              onClick = {
                  coroutineScope.launch {
                      btmSheetState.hide()
                  }
                  onGallery()
              }){
              Text("Pick From Gallery")
          }
      }
    }){}
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun BottomSheetPreview()
{
    val test = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    BottomSheetLayout(test,10.dp,{},{})
}
