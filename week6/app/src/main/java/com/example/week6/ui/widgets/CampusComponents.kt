package com.example.week6.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week6.model.CommunityIconType
import com.example.week6.model.ProjectStatus

data class IconVisualStyle(
    val icon: ImageVector,
    val tint: Color,
    val containerColor: Color
)

fun communityIconStyle(type: CommunityIconType): IconVisualStyle = when (type) {
    CommunityIconType.CODE -> IconVisualStyle(
        icon = Icons.Filled.Code,
        tint = Color(0xFF1D4ED8),
        containerColor = Color(0xFFEFF6FF)
    )
    CommunityIconType.SECURITY -> IconVisualStyle(
        icon = Icons.Filled.Security,
        tint = Color(0xFFDC2626),
        containerColor = Color(0xFFFEF2F2)
    )
    CommunityIconType.AI -> IconVisualStyle(
        icon = Icons.Filled.AutoAwesome,
        tint = Color(0xFF7C3AED),
        containerColor = Color(0xFFF3E8FF)
    )
    CommunityIconType.MOBILE -> IconVisualStyle(
        icon = Icons.Filled.PhoneAndroid,
        tint = Color(0xFF0284C7),
        containerColor = Color(0xFFE0F2FE)
    )
    CommunityIconType.CLOUD -> IconVisualStyle(
        icon = Icons.Filled.Cloud,
        tint = Color(0xFF1D4ED8),
        containerColor = Color(0xFFEFF6FF)
    )
    CommunityIconType.IOT -> IconVisualStyle(
        icon = Icons.Filled.Memory,
        tint = Color(0xFF16A34A),
        containerColor = Color(0xFFDCFCE7)
    )
}

@Composable
fun CommunityIconBox(
    iconType: CommunityIconType,
    modifier: Modifier = Modifier,
    boxSize: Dp = 44.dp,
    iconSize: Dp = 22.dp
) {
    val style = communityIconStyle(iconType)
    Box(
        modifier = modifier
            .size(boxSize)
            .clip(RoundedCornerShape(12.dp))
            .background(style.containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = null,
            tint = style.tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun JoinedSmallBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = Color(0xFFFEF2F2),
        border = BorderStroke(1.dp, Color(0xFFFECACA))
    ) {
        Text(
            text = "Joined \u2713",
            color = Color(0xFFDC2626),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ProjectStatusBadge(
    status: ProjectStatus,
    modifier: Modifier = Modifier
) {
    val isCompleted = status == ProjectStatus.COMPLETED
    val bgColor = if (isCompleted) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
    val fgColor = if (isCompleted) Color(0xFF16A34A) else Color(0xFFD97706)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(fgColor)
            )
            Text(
                text = status.label,
                color = fgColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
