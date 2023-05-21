package com.example.mynotes.presentation.utils.items


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotes.R
import com.example.mynotes.domain.models.PersonDomain
import com.example.mynotes.presentation.ui.directions.common.DirectionType
import com.example.mynotes.presentation.utils.components.image.customColors
import com.example.mynotes.presentation.utils.components.text.MyText

@Composable
fun ItemPerson(
    person: PersonDomain,
    color: Color = MaterialTheme.customColors.textColor,
    onItemClicked: () -> Unit,
    onMenuMoreClicked: (Offset) -> Unit,
    iconId: Int = R.drawable.ic_person,
    iconStartVisibility: Boolean = false,
    iconEndVisibility: Boolean = false,
) {
    var offset by remember {
        mutableStateOf(Offset.Infinite)
    }
    ItemEmptyRow(
        verticalPading = 2.dp, onItemClicked = {
            onItemClicked()

        }) {
        Column(modifier = Modifier.padding(3.dp)) {
            MyText(
                text = person.name,
                color = color
            )
            if (person.phone.isNotEmpty()) MyText(
                text = "tel: " + person.phone,
                fontSize = 12.sp,
                color = MaterialTheme.customColors.subTextColor
            )
            if (person.address.isNotEmpty()) MyText(
                text = "manzil: " + person.address,
                fontSize = 12.sp,
                color = MaterialTheme.customColors.subTextColor
            )
        }

        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    val rect = layoutCoordinates.boundsInRoot()
                    offset = rect.bottomRight
                }
                .padding(start = 3.dp),
            onClick = {
                onMenuMoreClicked(offset)
            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "menu more",
                tint = color
            )
        }
    }
}
