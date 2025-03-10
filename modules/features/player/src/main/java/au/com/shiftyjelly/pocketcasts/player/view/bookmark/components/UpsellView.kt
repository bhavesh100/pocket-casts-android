package au.com.shiftyjelly.pocketcasts.player.view.bookmark.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import au.com.shiftyjelly.pocketcasts.analytics.SourceView
import au.com.shiftyjelly.pocketcasts.compose.AppTheme
import au.com.shiftyjelly.pocketcasts.compose.images.HorizontalLogoText
import au.com.shiftyjelly.pocketcasts.compose.images.SubscriptionBadgeDisplayMode
import au.com.shiftyjelly.pocketcasts.compose.images.SubscriptionBadgeForTier
import au.com.shiftyjelly.pocketcasts.compose.preview.ThemePreviewParameterProvider
import au.com.shiftyjelly.pocketcasts.models.type.Subscription.SubscriptionTier
import au.com.shiftyjelly.pocketcasts.player.view.bookmark.components.UpsellViewModel.UiState
import au.com.shiftyjelly.pocketcasts.ui.theme.Theme
import au.com.shiftyjelly.pocketcasts.localization.R as LR

@Composable
fun UpsellView(
    style: MessageViewColors,
    onClick: () -> Unit,
    sourceView: SourceView,
    modifier: Modifier = Modifier,
    viewModel: UpsellViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    when (state) {
        UiState.Loading -> Unit
        is UiState.Loaded -> {
            UpsellViewContent(
                style = style,
                state = state as UiState.Loaded,
                onClick = {
                    viewModel.onClick(sourceView)
                    onClick()
                },
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun UpsellViewContent(
    style: MessageViewColors,
    state: UiState.Loaded,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(state.tier.getContentDescription())
    MessageView(
        titleView = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clearAndSetSemantics { contentDescription = description }
            ) {
                HorizontalLogoText()
                Spacer(modifier = Modifier.width(8.dp))
                SubscriptionBadgeForTier(
                    tier = state.tier,
                    displayMode = SubscriptionBadgeDisplayMode.Colored
                )
            }
        },
        message = stringResource(LR.string.bookmarks_create_instructions),
        buttonTitle = getButtonTitle(state.tier, state.hasFreeTrial),
        buttonAction = onClick,
        style = style,
        modifier = modifier,
    )
}

private fun SubscriptionTier.getContentDescription() = when (this) {
    SubscriptionTier.PATRON -> LR.string.pocket_casts_patron
    SubscriptionTier.PLUS -> LR.string.pocket_casts_plus
    SubscriptionTier.UNKNOWN -> throw IllegalStateException("Unknown subscription tier")
}

@Composable
private fun getButtonTitle(
    tier: SubscriptionTier,
    hasFreeTrial: Boolean
) = if (hasFreeTrial) {
    stringResource(LR.string.profile_start_free_trial)
} else {
    stringResource(
        LR.string.upgrade_to,
        when (tier) {
            SubscriptionTier.PATRON -> stringResource(LR.string.pocket_casts_patron_short)
            SubscriptionTier.PLUS -> stringResource(LR.string.pocket_casts_plus_short)
            SubscriptionTier.UNKNOWN -> throw IllegalStateException("Unknown subscription tier")
        }
    )
}

@Preview
@Composable
private fun UpsellPreview(
    @PreviewParameter(ThemePreviewParameterProvider::class) themeType: Theme.ThemeType,
) {
    AppTheme(themeType) {
        UpsellViewContent(
            style = MessageViewColors.Default,
            state = UiState.Loaded(tier = SubscriptionTier.PATRON, hasFreeTrial = false),
            onClick = {},
        )
    }
}
