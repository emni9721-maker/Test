package app.zero.vault.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import app.zero.vault.model.AppThemeMode

private val MidnightColorScheme = darkColorScheme(
    primary = MidnightEmerald,
    onPrimary = MidnightBg,
    primaryContainer = MidnightSurfaceElevated,
    onPrimaryContainer = MidnightEmeraldHover,
    secondary = MidnightCyan,
    onSecondary = MidnightBg,
    background = MidnightBg,
    onBackground = MidnightTextPrimary,
    surface = MidnightSurface,
    onSurface = MidnightTextPrimary,
    surfaceVariant = MidnightCard,
    onSurfaceVariant = MidnightTextSecondary,
    outline = MidnightBorder,
    outlineVariant = MidnightBorderSubtle
)

private val AmoledColorScheme = darkColorScheme(
    primary = AmoledNeonEmerald,
    onPrimary = AmoledBg,
    primaryContainer = AmoledSurfaceElevated,
    onPrimaryContainer = AmoledNeonEmerald,
    secondary = MidnightCyan,
    onSecondary = AmoledBg,
    background = AmoledBg,
    onBackground = AmoledTextPrimary,
    surface = AmoledSurface,
    onSurface = AmoledTextPrimary,
    surfaceVariant = AmoledCard,
    onSurfaceVariant = AmoledTextSecondary,
    outline = AmoledBorder,
    outlineVariant = AmoledBorderStrong
)

@Composable
fun ZeroTheme(
    themeMode: AppThemeMode = AppThemeMode.MIDNIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = if (themeMode == AppThemeMode.AMOLED) AmoledColorScheme else MidnightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
