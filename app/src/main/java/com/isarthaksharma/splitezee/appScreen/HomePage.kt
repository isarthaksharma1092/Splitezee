package com.isarthaksharma.splitezee.appScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.viewModel.UserInfoViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomePage(
    modifier: Modifier,
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    goSetting: () -> Unit
) {
    val userProfile by userInfoViewModel.userProfile.collectAsState()
    Column(modifier) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Hi ${userProfile?.userName},",
                    style = MaterialTheme.typography.displaySmallEmphasized,
                    fontFamily = FontFamily(Font(R.font.doto, FontWeight.ExtraBold)),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable(onClick = { goSetting() }, enabled = true),
                    painter = if(userProfile?.userProfilePictureUrl == "null") painterResource(R.drawable.man_icon) else rememberAsyncImagePainter(userProfile?.userProfilePictureUrl),
                    contentDescription = "${userProfile?.userName}'s Picture",
                )
            }
        }

        // Total Spent Banner
        Card(
            elevation = CardDefaults.cardElevation(70.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xff375fad)),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        )
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Total Spending
                            Text(
                                text = "Total Spent",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .shadow(20.dp),
                                style = MaterialTheme.typography.labelMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                            Text(
                                text = "₹ 2000",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .shadow(20.dp),
                                style = MaterialTheme.typography.titleLargeEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                        }
                    }

                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp),
                        color = MaterialTheme.colorScheme.background
                    )

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            // Today
                            Text(
                                text = "Today",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .shadow(20.dp),
                                fontFamily = FontFamily(Font(R.font.doto)),
                                style = MaterialTheme.typography.labelMediumEmphasized,
                            )
                            Text(
                                text = "₹ 80",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .shadow(20.dp),
                                style = MaterialTheme.typography.titleLargeEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp),
                                color = MaterialTheme.colorScheme.background
                            )

                            // Monthly
                            Text(
                                text = "Monthly",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .shadow(20.dp),
                                style = MaterialTheme.typography.labelMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                                )

                            Text(
                                text = "₹ 100",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .shadow(20.dp),
                                style = MaterialTheme.typography.titleLargeEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                                )
                        }
                    }
                }
            }
        }
    }
}