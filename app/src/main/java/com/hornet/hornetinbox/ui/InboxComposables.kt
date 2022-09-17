@file:OptIn(ExperimentalFoundationApi::class)

package com.hornet.hornetinbox.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hornet.hornetinbox.Utils
import com.hornet.hornetinbox.models.Inbox

val PROFILE_IMAGE_SIZE = 70.dp

@Composable
fun InboxView(
    state: InboxViewState,
    handleNewInboxButtonClicked: () -> Unit,
    onLoadMoreClicked: () -> Unit) {
    when {
        state.hasError -> ErrorView(errorMessage = state.errorMessage)
        state.isLoading -> LoadingView()
        state.data.isEmpty() -> EmptyInboxState()
        else -> InboxContentView(
            inboxList = state.data,
            canLoadMore = state.canLoadMore,
            hasLoadedMore = state.hasLoadedMore,
            hasUpdatedContent = state.hasUpdatedContent,
            handleNewInboxButtonClicked = handleNewInboxButtonClicked,
            onLoadMoreClicked = onLoadMoreClicked
        )
    }
}

@Composable
fun ErrorView(errorMessage: String?) {
    val text = errorMessage ?: "It's not you, it's us :("
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Normal,
            )
        )
    }
}

@Composable
fun EmptyInboxState() {
    ErrorView(errorMessage = "You currently have no messages :)")
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.Cyan)
    }
}

@Composable
fun InboxContentView(
    inboxList: List<Inbox>,
    canLoadMore: Boolean,
    hasLoadedMore: Boolean,
    hasUpdatedContent: Boolean,
    handleNewInboxButtonClicked: () -> Unit,
    onLoadMoreClicked: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)) {
        InboxListView(
            inboxList = inboxList,
            canLoadMore = canLoadMore,
            onLoadMoreClicked = onLoadMoreClicked,
            hasLoadedMore = hasLoadedMore,
            hasUpdatedContent = hasUpdatedContent
        )
        AddInboxButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            handleNewInboxButtonClicked = handleNewInboxButtonClicked
        )
    }
}

@Composable
fun InboxListView(
    inboxList: List<Inbox>,
    canLoadMore: Boolean,
    hasLoadedMore: Boolean,
    hasUpdatedContent: Boolean,
    onLoadMoreClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        InboxListItems(
            inboxList = inboxList,
            hasLoadedMore = hasLoadedMore,
            hasUpdatedContent = hasUpdatedContent
        )

        AnimatedVisibility(modifier = Modifier
            .align(Alignment.BottomCenter)
            .height(40.dp),
            visible = canLoadMore) {
            LoadingMoreIndicator(
                modifier = Modifier,
                onLoadMoreClicked = onLoadMoreClicked
            )
        }
    }
}

@Composable
fun LoadingMoreIndicator(modifier: Modifier, onLoadMoreClicked: () -> Unit) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.textButtonColors(backgroundColor = Color.White),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        onClick = { onLoadMoreClicked.invoke() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Load More Items",
                style = MaterialTheme.typography.body2.copy(color = Color.Blue)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun InboxListItems(inboxList: List<Inbox>, hasLoadedMore: Boolean, hasUpdatedContent: Boolean) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(items = inboxList, key = { inbox -> inbox.userId }) { inbox ->
            Spacer(modifier = Modifier.height(4.dp))
            InboxCell(modifier = Modifier.animateItemPlacement(
                animationSpec = tween(
                    durationMillis = 700,
                    easing = LinearOutSlowInEasing,
                )
            ), inbox = inbox)
            Spacer(modifier = Modifier.height(4.dp))
            Divider()
        }
    }
    if (inboxList.isNotEmpty() && (hasLoadedMore || hasUpdatedContent)) {
        LaunchedEffect(inboxList) {
            if (hasUpdatedContent) listState.animateScrollToItem(0)
            if (hasLoadedMore) listState.animateScrollToItem(inboxList.lastIndex)
        }
    }
}

@Composable
fun SenderProfileImage(modifier: Modifier, senderInitials: Char, backgroundColor: Color) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = backgroundColor
            )
            .width(PROFILE_IMAGE_SIZE)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = senderInitials.toString().uppercase(),
            style = MaterialTheme.typography.body1.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center
            )
        )
    }
}
@Composable
fun InboxCell(modifier: Modifier, inbox: Inbox) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(PROFILE_IMAGE_SIZE),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SenderProfileImage(
            modifier = Modifier.fillMaxHeight(),
            senderInitials = inbox.userName.first(),
            backgroundColor = inbox.userImageBackground
        )
        Spacer(modifier = Modifier.width(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = inbox.userName,
                style = MaterialTheme.typography.body1.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Start,
                )
            )

            Text(
                text = Utils.getRelativeTime(inbox.lastMessageDate),
                style =  MaterialTheme.typography.body2.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Start
                )
            )
        }
    }
}

@Composable
fun AddInboxButton(modifier: Modifier, handleNewInboxButtonClicked: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = handleNewInboxButtonClicked,
        shape = CircleShape,
        backgroundColor = Color.Black,
        content = { Icon(Icons.Filled.Add, tint = Color.White, contentDescription = null) }
    )
}