package ru.ermolnik.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    when (val newsState = state.value) {
        is NewsState.Error -> ErrorBlock { viewModel.onClickTryAgain() }
        is NewsState.FromBd -> {
            NewsWithRefresh(newsState.news, isRefreshing) { viewModel.refresh() }
            RepositoryLabel(stringResource(R.string.label_db))
        }
        is NewsState.FromNet -> {
            NewsBlock(newsState.news)
            RepositoryLabel(stringResource(R.string.label_net))
        }
        NewsState.Loading -> LoadingBlock()
    }
}

@Composable
private fun NewsWithRefresh(
    news: List<NewsViewState>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
    ) {
        NewsBlock(news = news)
    }
}

@Composable
private fun LoadingBlock() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun ErrorBlock(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally


    ) {
        Text(stringResource(R.string.error_title))
        Button(onClick = onClick) {
            Text(stringResource(R.string.error_button))
        }
    }
}

@Composable
private fun NewsBlock(news: List<NewsViewState>) {
    LazyColumn() {
        items(items = news) { item ->
            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center

                ) {
                    NewsTitle(item.title)
                    NewsBody(item.body)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.NewsTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally),
        textAlign = TextAlign.Center,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ColumnScope.NewsBody(body: String) {
    Text(
        text = body,
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally),
        textAlign = TextAlign.Center,
        fontSize = 18.sp
    )
}

@Composable
private fun RepositoryLabel(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, end = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .align(TopEnd)
                .rotate(45f)
                .background(Color.Green),

            text = text,
            color = Color.Magenta,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
