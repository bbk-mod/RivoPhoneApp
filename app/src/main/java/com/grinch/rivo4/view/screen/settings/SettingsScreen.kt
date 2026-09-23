package com.grinch.rivo4.view.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.grinch.rivo4.R
import com.grinch.rivo4.controller.util.PreferenceManager
import com.grinch.rivo4.controller.util.openLink
import com.grinch.rivo4.view.components.RivoDialog
import com.grinch.rivo4.view.components.RivoDialogAction
import com.grinch.rivo4.view.components.RivoDivider
import com.grinch.rivo4.view.components.RivoExpressiveCard
import com.grinch.rivo4.view.components.RivoListItem
import com.grinch.rivo4.view.components.RivoSwitchListItem
import com.grinch.rivo4.view.theme.RivoMaterialShapes
import com.grinch.rivo4.view.theme.rememberRivoMorphShape
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {
    val prefs = koinInject<PreferenceManager>()
    val settingsState by prefs.settingsChanged.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Personalization & Display
            item {
                RivoExpressiveCard(
                    title = "Personalization & Display",
                    icon = Icons.Outlined.Palette
                ) {
                    RivoListItem(
                        headline = "Theme & Appearance",
                        supporting = "Material You, color palette, AMOLED dark mode & animations",
                        leadingIcon = Icons.Outlined.Palette,
                        onClick = { navigator.navigate(InterfaceScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = "Navigation Bar",
                        supporting = "Floating bar style, blur effect, roundness & tab layout",
                        leadingIcon = Icons.Outlined.Dock,
                        onClick = { navigator.navigate(BottomNavScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = "Avatars & Contact Cards",
                        supporting = "11 avatar shapes, contact photos, initials & cards",
                        leadingIcon = Icons.Outlined.AccountCircle,
                        onClick = { navigator.navigate(AvatarSettingsScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.settings_sound_vibration_headline),
                        supporting = stringResource(R.string.settings_sound_vibration_supporting),
                        leadingIcon = Icons.Outlined.VolumeUp,
                        onClick = { navigator.navigate(SoundVibrationScreenDestination) }
                    )
                }
            }

            // 2. Calling & Behavior
            item {
                RivoExpressiveCard(
                    title = "Calling & Behavior",
                    icon = Icons.Outlined.Phone
                ) {
                    RivoListItem(
                        headline = stringResource(R.string.settings_call_settings_headline),
                        supporting = stringResource(R.string.settings_call_settings_supporting),
                        leadingIcon = Icons.Outlined.SimCard,
                        onClick = { navigator.navigate(CallAccountsScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.settings_swipe_actions_title),
                        supporting = stringResource(R.string.settings_swipe_actions_supporting),
                        leadingIcon = Icons.Outlined.Swipe,
                        onClick = { navigator.navigate(SwipeActionsScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.call_recordings_title),
                        supporting = "Auto-recording, Shizuku internal audio & saved recordings",
                        leadingIcon = Icons.Outlined.FiberManualRecord,
                        onClick = { navigator.navigate(CallRecordingsScreenDestination()) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.settings_call_analytics_title),
                        supporting = stringResource(R.string.settings_call_analytics_supporting),
                        leadingIcon = Icons.Outlined.Analytics,
                        onClick = { navigator.navigate(CallAnalyticsScreenDestination()) }
                    )
                }
            }

            // 3. Call Protection & Security
            item {
                RivoExpressiveCard(
                    title = "Call Protection & Security",
                    icon = Icons.Outlined.Security
                ) {
                    val appLockEnabled = remember(settingsState) { prefs.isAppLockEnabled() }
                    RivoListItem(
                        headline = "App Lock",
                        supporting = if (appLockEnabled) "Enabled (Face, Fingerprint, PIN)" else "Protect app with biometrics or PIN",
                        leadingIcon = Icons.Outlined.Lock,
                        onClick = { navigator.navigate(AppLockScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = "Private Storage",
                        supporting = "Secret dialpad vault • Stored only in app memory",
                        leadingIcon = Icons.Outlined.FolderShared,
                        onClick = { navigator.navigate(PrivateContactsScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.settings_blocked_numbers_headline),
                        supporting = stringResource(R.string.settings_blocked_numbers_supporting),
                        leadingIcon = Icons.Outlined.Block,
                        onClick = { navigator.navigate(BlockedNumbersScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.fake_call_title),
                        supporting = stringResource(R.string.fake_call_subtitle),
                        leadingIcon = Icons.Outlined.PhoneCallback,
                        onClick = { navigator.navigate(FakeCallSchedulerScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = "Permissions & App Setup",
                        supporting = "Review granted permissions and system capabilities",
                        leadingIcon = Icons.Outlined.VerifiedUser,
                        onClick = { navigator.navigate(PermissionsChecklistScreenDestination) }
                    )
                }
            }

            // 4. Contacts & Data
            item {
                RivoExpressiveCard(
                    title = stringResource(R.string.settings_contacts_management_title),
                    icon = Icons.Outlined.ManageAccounts
                ) {
                    RivoListItem(
                        headline = stringResource(R.string.settings_contact_management_headline),
                        supporting = stringResource(R.string.settings_contact_management_supporting),
                        leadingIcon = Icons.Outlined.ManageAccounts,
                        onClick = { navigator.navigate(ContactManagementScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    val isContactManagementCardEnabled = remember(settingsState) { prefs.isContactManagementCardEnabled() }
                    RivoSwitchListItem(
                        headline = stringResource(R.string.settings_contact_management_card),
                        supporting = stringResource(R.string.settings_contact_management_card_supporting),
                        leadingIcon = Icons.Outlined.Info,
                        checked = isContactManagementCardEnabled,
                        onCheckedChange = { prefs.setContactManagementCardEnabled(it) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.settings_manage_visibility),
                        supporting = stringResource(R.string.settings_manage_visibility_supporting),
                        leadingIcon = Icons.Outlined.Visibility,
                        onClick = { navigator.navigate(ContactVisibilityScreenDestination) }
                    )
                    RivoDivider(Modifier.padding(horizontal = 16.dp))
                    RivoListItem(
                        headline = stringResource(R.string.settings_backup_restore_headline),
                        supporting = stringResource(R.string.settings_backup_restore_supporting),
                        leadingIcon = Icons.Outlined.Backup,
                        onClick = { navigator.navigate(BackupRestoreScreenDestination) }
                    )
                }
            }
        }
    }
}
