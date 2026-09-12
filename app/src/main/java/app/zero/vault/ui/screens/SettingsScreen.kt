package app.zero.vault.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.zero.vault.model.AppThemeMode
import app.zero.vault.model.User

@Composable
fun SettingsScreen(
    user: User,
    themeMode: AppThemeMode,
    onBack: () -> Unit,
    onThemeChange: (AppThemeMode) -> Unit,
    onUpdatePin: (String) -> Unit
) {
    var wifiOnly by remember { mutableStateOf(user.storageSettings.backupOverWifiOnly) }
    var autoCompress by remember { mutableStateOf(user.storageSettings.autoCompress) }
    var autoBackup by remember { mutableStateOf(user.storageSettings.autoBackupEnabled) }
    var showPinDialog by remember { mutableStateOf(false) }
    var newPinInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    text = "Vault & Cloud Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("settings_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Appearance / Theme Section
            item {
                SettingsSectionHeader(title = "Appearance & Display")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Theme Palette",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FilterChip(
                                selected = themeMode == AppThemeMode.MIDNIGHT,
                                onClick = { onThemeChange(AppThemeMode.MIDNIGHT) },
                                label = { Text("Midnight Deep Navy") },
                                modifier = Modifier.weight(1f).testTag("theme_chip_midnight")
                            )
                            FilterChip(
                                selected = themeMode == AppThemeMode.AMOLED,
                                onClick = { onThemeChange(AppThemeMode.AMOLED) },
                                label = { Text("AMOLED Pure Black") },
                                modifier = Modifier.weight(1f).testTag("theme_chip_amoled")
                            )
                        }
                    }
                }
            }

            // 2. Vault Security Section
            item {
                SettingsSectionHeader(title = "Vault Security & Encryption")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Vault Security PIN", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Current: ${user.vaultPin.replace(Regex("."), "•")}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Button(
                                onClick = { showPinDialog = true },
                                modifier = Modifier.testTag("change_pin_button")
                            ) {
                                Text("Change PIN")
                            }
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("AES-256 GCM Hardware Lock", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Private photos never touch server unencrypted", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Icon(Icons.Default.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // 3. Cloud Sync & Backup Section
            item {
                SettingsSectionHeader(title = "Sync & Storage Management")
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Automatic Cloud Backup", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Continuous zero-loss background sync", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(checked = autoBackup, onCheckedChange = { autoBackup = it })
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Backup Over Wi-Fi Only", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Save cellular mobile data on large RAW uploads", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(checked = wifiOnly, onCheckedChange = { wifiOnly = it })
                        }

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Auto-Compress Media", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Disabled to preserve pristine uncompressed RAW", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(checked = autoCompress, onCheckedChange = { autoCompress = it })
                        }
                    }
                }
            }
        }
    }

    if (showPinDialog) {
        Dialog(onDismissRequest = { showPinDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Update 4-Digit Security PIN", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { if (it.length <= 4) newPinInput = it },
                        label = { Text("New PIN") },
                        modifier = Modifier.fillMaxWidth().testTag("new_pin_input"),
                        singleLine = true
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showPinDialog = false }) { Text("Cancel") }
                        Button(
                            onClick = {
                                if (newPinInput.length == 4) {
                                    onUpdatePin(newPinInput)
                                    showPinDialog = false
                                    newPinInput = ""
                                }
                            },
                            enabled = newPinInput.length == 4,
                            modifier = Modifier.testTag("save_pin_button")
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}
