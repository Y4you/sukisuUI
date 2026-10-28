package me.bmax.apatch.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.bmax.apatch.R
import me.bmax.apatch.ui.component.miuix.StatusTagMiuix
import me.bmax.apatch.util.Version
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Close
import top.yukonga.miuix.kmp.icon.extended.Ok
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import top.yukonga.miuix.kmp.theme.MiuixTheme.isDynamicColor
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
fun SukiStatusCard(
    state: HomeUiState,
    onInstallClick: () -> Unit,
) {
    val isInstalled = state.conclusion == HomeConclusion.FULL_APATCH ||
            state.conclusion == HomeConclusion.KERNEL_PATCH_ONLY

    if (isInstalled) {
        SukiActiveStatusCard(state = state, onClick = onInstallClick)
    } else {
        SukiInactiveStatusCard(state = state, onClick = onInstallClick)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SukiActiveStatusCard(
    state: HomeUiState,
    onClick: () -> Unit,
) {
    val accentColor = if (isDynamicColor) colorScheme.primary else Color(0xFFFF9CA8)
    val tagBackground = if (isDynamicColor) colorScheme.tertiaryContainer else Color(0xFFFFD9E2)
    val tagContent = if (isDynamicColor) colorScheme.onTertiaryContainer else Color(0xFF5E1128)
    val managerVersion = remember { Version.getManagerVersion() }

    val workingMode = when (state.conclusion) {
        HomeConclusion.FULL_APATCH -> "APatch"
        HomeConclusion.KERNEL_PATCH_ONLY -> "Kernel"
        else -> "KP"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.defaultColors(
            color = if (isDynamicColor) colorScheme.secondaryContainer else Color(0xFFFFF0F3)
        ),
        onClick = onClick,
        showIndication = true,
        pressFeedbackType = PressFeedbackType.Tilt
    ) {
        Box {
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(27.dp, 31.dp)
                    .size(150.dp),
                imageVector = MiuixIcons.Ok,
                tint = accentColor.copy(alpha = if (isDynamicColor) 0.8f else 0.9f),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 188.dp)
                    .padding(22.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    StatusTagMiuix(
                        label = workingMode,
                        contentColor = if (isDynamicColor) colorScheme.onPrimary else Color.White,
                        backgroundColor = accentColor,
                    )
                    state.environment?.selinuxStatus?.let { selinux ->
                        StatusTagMiuix(
                            label = selinux.name,
                            contentColor = tagContent,
                            backgroundColor = tagBackground,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.home_working),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                    )
                    Text(
                        text = "KP: " + managerVersion.first + " (" + managerVersion.second + ")",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        color = colorScheme.onSurfaceVariantSummary,
                    )
                }
            }
        }
    }
}

@Composable
private fun SukiInactiveStatusCard(
    state: HomeUiState,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        showIndication = true,
        pressFeedbackType = PressFeedbackType.Tilt
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 188.dp)
                .padding(22.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = MiuixIcons.Close,
                    contentDescription = null,
                    tint = colorScheme.error,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = stringResource(R.string.home_click_to_install),
                    fontSize = 14.sp,
                    color = colorScheme.error,
                )
            }
            Text(
                text = stringResource(R.string.home_not_installed),
                modifier = Modifier.padding(vertical = 12.dp),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(
                    text = stringResource(R.string.install),
                    onClick = onClick,
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}

@Composable
fun SukiStatsRow(
    state: HomeUiState,
    onSuperuserClick: () -> Unit,
    onModuleClick: () -> Unit,
    onKpmClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Card(
            modifier = Modifier.weight(1f),
            onClick = onModuleClick,
            showIndication = true,
            pressFeedbackType = PressFeedbackType.Sink
        ) {
            Column(
                modifier = Modifier
                    .height(88.dp)
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = state.apmCount.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.module),
                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                    color = colorScheme.onSurfaceVariantSummary,
                    maxLines = 1,
                )
            }
        }
        Card(
            modifier = Modifier.weight(1f),
            onClick = onKpmClick,
            showIndication = true,
            pressFeedbackType = PressFeedbackType.Sink
        ) {
            Column(
                modifier = Modifier
                    .height(88.dp)
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = state.kpmCount.toString(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                )
                Text(
                    text = "KPM",
                    fontSize = MiuixTheme.textStyles.body2.fontSize,
                    color = colorScheme.onSurfaceVariantSummary,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun SukiInfoCard(state: HomeUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Device Info",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorScheme.onSurface,
            )
            state.environment?.let { env ->
                Text(
                    text = "Kernel: " + env.kernelRelease,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "Arch: " + env.primaryAbi,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = colorScheme.onSurfaceVariantSummary,
                )
                Text(
                    text = "Android: " + env.androidRelease + " (API " + env.androidApi + ")",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = colorScheme.onSurfaceVariantSummary,
                )
            }
        }
    }
}
