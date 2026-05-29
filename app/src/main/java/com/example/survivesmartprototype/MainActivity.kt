package com.example.survivesmartprototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.survivesmartprototype.ui.theme.SurviveSmartPrototypeTheme

// ----------------------------------------------------
// MAIN ACTIVITY
// ----------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurviveSmartPrototypeTheme {
                SurviveSmartApp()
            }
        }
    }
}

// ----------------------------------------------------
// REFINED HIGHER-FIDELITY COLOURS
// ----------------------------------------------------
private val BgColor = Color(0xFFFFF8EF)
private val PanelColor = Color(0xFFFFFFFF)
private val NavColor = Color(0xFF2E7D5B)
private val WhiteBox = Color(0xFFFAFAFA)
private val BorderColor = Color(0xFFE5E7EB)
private val TextGray = Color(0xFF6B7280)

private val AccentBlue = Color(0xFF457B9D)
private val AccentOrange = Color(0xFFF4A261)
private val AccentGray = Color(0xFF9CA3AF)
private val AccentRed = Color(0xFFE76F51)
private val AccentGreen = Color(0xFF2E7D5B)

// ----------------------------------------------------
// ROUTES
// ----------------------------------------------------
sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Add : Screen("add", "Add")
    data object Summary : Screen("summary", "Summary")
    data object Habits : Screen("habits", "Habits")
    data object Settings : Screen("settings", "Settings")
    data object Details : Screen("details", "Details")
    data object Confirmation : Screen("confirmation", "Confirm")

    // Hamburger / side menu screens
    data object Profile : Screen("profile", "Profile")
    data object Alerts : Screen("alerts", "Alerts")
    data object Goals : Screen("goals", "Goals")
    data object WeeklyReport : Screen("weekly_report", "Weekly Report")
    data object Tips : Screen("tips", "Tips")
    data object AppInfo : Screen("app_info", "App Info")
}

private val bottomScreens = listOf(
    Screen.Home,
    Screen.Add,
    Screen.Summary,
    Screen.Habits,
    Screen.Settings
)

// ----------------------------------------------------
// ROOT APP
// ----------------------------------------------------
@Composable
fun SurviveSmartApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideDrawerContent(
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                    scope.launch { drawerState.close() }
                },
                onAlertsClick = {
                    navController.navigate(Screen.Alerts.route)
                    scope.launch { drawerState.close() }
                },
                onGoalsClick = {
                    navController.navigate(Screen.Goals.route)
                    scope.launch { drawerState.close() }
                },
                onWeeklyReportClick = {
                    navController.navigate(Screen.WeeklyReport.route)
                    scope.launch { drawerState.close() }
                },
                onTipsClick = {
                    navController.navigate(Screen.Tips.route)
                    scope.launch { drawerState.close() }
                },
                onAppInfoClick = {
                    navController.navigate(Screen.AppInfo.route)
                    scope.launch { drawerState.close() }
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            containerColor = BgColor,
            bottomBar = {
                BottomNavBar(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } },
                        onAddExpense = { navController.navigate(Screen.Add.route) },
                        onViewSummary = { navController.navigate(Screen.Summary.route) }
                    )
                }

                composable(Screen.Add.route) {
                    AddExpenseScreen(
                        innerPadding = innerPadding,
                        onViewSummary = { navController.navigate(Screen.Summary.route) },
                        onBack = { navController.navigate(Screen.Home.route) },
                        onSave = { navController.navigate(Screen.Confirmation.route) },
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Summary.route) {
                    BudgetSummaryScreen(
                        innerPadding = innerPadding,
                        onCategoryTap = { navController.navigate(Screen.Details.route) },
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Habits.route) {
                    HabitTrackerScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Details.route) {
                    ExpenseDetailsScreen(
                        innerPadding = innerPadding,
                        onBack = { navController.popBackStack() },
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Confirmation.route) {
                    ConfirmationScreen(
                        innerPadding = innerPadding,
                        onGoHome = { navController.navigate(Screen.Home.route) },
                        onViewSummary = { navController.navigate(Screen.Summary.route) },
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileSetupScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Alerts.route) {
                    SpendingAlertsScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Goals.route) {
                    SavingsGoalsScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.WeeklyReport.route) {
                    WeeklyReportScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.Tips.route) {
                    FinancialTipsScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }

                composable(Screen.AppInfo.route) {
                    AppInfoScreen(
                        innerPadding = innerPadding,
                        onOpenMenu = { scope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// REUSABLE COMPONENTS
// ----------------------------------------------------
@Composable
fun BottomNavBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavColor)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        bottomScreens.forEach { screen ->
            val selected = currentRoute == screen.route

            Text(
                text = screen.label,
                color = if (selected) Color.White else Color.White.copy(alpha = 0.75f),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp,
                modifier = Modifier
                    .background(
                        if (selected) Color.White.copy(alpha = 0.18f) else Color.Transparent,
                        RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun AppHeader(
    title: String,
    subtitle: String = "Helping students survive financially\nwhile building smarter spending habits",
    showMenu: Boolean = true,
    onOpenMenu: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showMenu) {
                IconButton(
                    onClick = onOpenMenu,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            AccentGreen.copy(alpha = 0.12f),
                            RoundedCornerShape(14.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Open Menu",
                        tint = AccentGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = TextGray,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PrototypeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentGreen,
            contentColor = Color.White
        ),
        modifier = modifier.height(48.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun WireframeCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PanelColor, RoundedCornerShape(18.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
fun LabeledInputRow(label: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2933)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(WhiteBox, RoundedCornerShape(12.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Enter $label",
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun LabeledDropdownRow(label: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2933)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(WhiteBox, RoundedCornerShape(12.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Select $label",
                fontSize = 12.sp,
                color = TextGray
            )

            Text(
                text = "▼",
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.sp)
    }
}

@Composable
fun SmallToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) AccentGreen else Color(0xFFE5E7EB),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (selected) Color.White else TextGray
        )
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    leftText: String,
    rightText: String,
    leftSelected: Boolean,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2933)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SmallToggleButton(
                text = leftText,
                selected = leftSelected,
                onClick = onLeftClick
            )

            SmallToggleButton(
                text = rightText,
                selected = !leftSelected,
                onClick = onRightClick
            )
        }
    }
}
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        color = Color(0xFF1F2933)
    )
}

// ----------------------------------------------------
// HOME
// ----------------------------------------------------
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit,
    onAddExpense: () -> Unit,
    onViewSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "SurviveSmart", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Welcome back, Student 👋",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your money overview",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Monthly Budget", color = TextGray, fontSize = 13.sp)
                    Text(
                        "R3000",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AccentGreen
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Remaining", color = TextGray, fontSize = 13.sp)
                    Text(
                        "R1200",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = AccentOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "Spent so far: R1800",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .background(AccentGreen, RoundedCornerShape(20.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Quick Actions")
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrototypeButton(
                    text = "+ Add\nExpense",
                    onClick = onAddExpense,
                    modifier = Modifier.weight(1f)
                )

                PrototypeButton(
                    text = "View\nSummary",
                    onClick = onViewSummary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Recent Expenses")
            Spacer(modifier = Modifier.height(12.dp))

            ExpensePreviewRow("Transport", "Taxi to campus", "R50")
            Spacer(modifier = Modifier.height(10.dp))
            ExpensePreviewRow("Food", "Lunch after class", "R120")
            Spacer(modifier = Modifier.height(10.dp))
            ExpensePreviewRow("Airtime", "Weekly data bundle", "R30")
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Habit Snapshot")
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥",
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        "3 day tracking streak",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1F2933)
                    )
                    Text(
                        "You have logged expenses consistently this week.",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
            }
        }
    }
}

@Composable
fun ExpensePreviewRow(
    category: String,
    description: String,
    amount: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = category,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF1F2933)
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = TextGray
            )
        }

        Text(
            text = amount,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = AccentRed
        )
    }
}
// ----------------------------------------------------
// ADD EXPENSE
// ----------------------------------------------------
@Composable
fun AddExpenseScreen(
    innerPadding: PaddingValues,
    onViewSummary: () -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Add Expense", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Budget context",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryValue("Budget", "R3000", AccentGreen)
                SummaryValue("Remaining", "R1200", AccentOrange)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Expense Form")
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Add the expense details below. This helps keep your summary accurate.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledInputRow("Amount")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledDropdownRow("Category")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputRow("Date")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputRow("Note")
            Spacer(modifier = Modifier.height(20.dp))

            PrototypeButton(
                text = "Save Expense",
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "✓",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Ready to record",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1F2933)
                    )

                    Text(
                        text = "After saving, your budget summary will update.",
                        fontSize = 13.sp,
                        color = TextGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrototypeButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.weight(1f)
            )

            PrototypeButton(
                text = "View Summary",
                onClick = onViewSummary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ----------------------------------------------------
// BUDGET SUMMARY
// ----------------------------------------------------
@Composable
fun BudgetSummaryScreen(
    innerPadding: PaddingValues,
    onCategoryTap: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Budget Summary", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Monthly overview",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryValue("Budget", "R3000", AccentGreen)
                SummaryValue("Spent", "R1800", AccentRed)
                SummaryValue("Left", "R1200", AccentOrange)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFE8F5EF), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Status: Within budget ✓",
                    color = AccentGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Spending Breakdown")
            Spacer(modifier = Modifier.height(12.dp))
            DonutChart()
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Category Totals")
            Spacer(modifier = Modifier.height(12.dp))

            CategoryTotalRow("Food", "Groceries and lunch", "R800", AccentBlue, onCategoryTap)
            Spacer(modifier = Modifier.height(10.dp))
            CategoryTotalRow("Transport", "Taxi and campus travel", "R500", AccentOrange, onCategoryTap)
            Spacer(modifier = Modifier.height(10.dp))
            CategoryTotalRow("Airtime", "Data and calls", "R500", AccentGray, onCategoryTap)
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Insight")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Food is currently your highest spending category. Try reviewing this before adding more expenses.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SummaryValue(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextGray
        )

        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun CategoryTotalRow(
    category: String,
    description: String,
    amount: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = category,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF1F2933)
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }

        Text(
            text = amount,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = color
        )
    }
}

@Composable
fun DonutChart() {
    Box(
        modifier = Modifier
            .size(260.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val stroke = 36f
            val diameter = size.minDimension
            val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
            val chartSize = Size(diameter, diameter)

            drawArc(
                color = AccentBlue,
                startAngle = -90f,
                sweepAngle = 158.4f,
                useCenter = false,
                topLeft = topLeft,
                size = chartSize,
                style = Stroke(width = stroke, cap = StrokeCap.Butt)
            )
            drawArc(
                color = AccentOrange,
                startAngle = 68.4f,
                sweepAngle = 100.8f,
                useCenter = false,
                topLeft = topLeft,
                size = chartSize,
                style = Stroke(width = stroke, cap = StrokeCap.Butt)
            )
            drawArc(
                color = AccentGray,
                startAngle = 169.2f,
                sweepAngle = 100.8f,
                useCenter = false,
                topLeft = topLeft,
                size = chartSize,
                style = Stroke(width = stroke, cap = StrokeCap.Butt)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 40.dp)
                .background(WhiteBox)
                .border(1.dp, BorderColor)
                .padding(8.dp)
        ) {
            LegendItem(AccentBlue, "Food")
            LegendItem(AccentOrange, "Transport")
            LegendItem(AccentGray, "Airtime")
        }
    }
}

// ----------------------------------------------------
// HABITS
// ----------------------------------------------------
@Composable
fun HabitTrackerScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Habit Tracker", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Your habit streak",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥",
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "3 days in a row",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )

                    Text(
                        text = "Keep logging daily to build better spending habits.",
                        fontSize = 13.sp,
                        color = TextGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Daily Habits")
            Spacer(modifier = Modifier.height(12.dp))

            HabitRow("Logged expenses today", true)
            Spacer(modifier = Modifier.height(10.dp))
            HabitRow("Stayed within budget", true)
            Spacer(modifier = Modifier.height(10.dp))
            HabitRow("Avoided unnecessary spending", false)
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Weekly Progress")
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Progress",
                    color = TextGray,
                    fontSize = 13.sp
                )

                Text(
                    text = "60%",
                    color = AccentGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .background(AccentGreen, RoundedCornerShape(20.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Great job! You are staying consistent this week.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Reflection")
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Small daily actions can make budgeting easier over time. Try checking this screen every evening.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun HabitRow(
    text: String,
    completed: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(
                    if (completed) AccentGreen else Color(0xFFE5E7EB),
                    RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (completed) "✓" else "",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = if (completed) Color(0xFF1F2933) else TextGray
        )
    }
}
// ----------------------------------------------------
// SETTINGS
// ----------------------------------------------------
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    var notificationsOn by rememberSaveable { mutableStateOf(true) }
    var darkModeOn by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Settings", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Personalise your experience",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "App Preferences",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Adjust how SurviveSmart reminds and supports you.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Preferences")
            Spacer(modifier = Modifier.height(12.dp))

            SettingsToggleRow(
                title = "Notifications",
                leftText = "ON",
                rightText = "OFF",
                leftSelected = notificationsOn,
                onLeftClick = { notificationsOn = true },
                onRightClick = { notificationsOn = false }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsToggleRow(
                title = "Dark Mode",
                leftText = "OFF",
                rightText = "ON",
                leftSelected = !darkModeOn,
                onLeftClick = { darkModeOn = false },
                onRightClick = { darkModeOn = true }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Account and Support")
            Spacer(modifier = Modifier.height(12.dp))

            SettingsOptionRow("👤", "Account", "View budget profile and student preferences")
            Spacer(modifier = Modifier.height(12.dp))

            SettingsOptionRow("❓", "Help", "Get support on using SurviveSmart features")
            Spacer(modifier = Modifier.height(12.dp))

            SettingsOptionRow("ℹ️", "App Info", "View prototype and application details")
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✓",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = AccentGreen
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Settings updated successfully",
                    color = TextGray,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun SettingsOptionRow(
    icon: String,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AccentGreen.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1F2933)
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextGray,
                lineHeight = 16.sp
            )
        }

        Text(
            text = ">",
            fontWeight = FontWeight.Bold,
            color = TextGray
        )
    }
}
// ----------------------------------------------------
// EXPENSE DETAILS
// ----------------------------------------------------
@Composable
fun ExpenseDetailsScreen(
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Expense Details", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Expense overview",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Food Expense",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Review the details of the selected transaction below.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Selected Expense")
            Spacer(modifier = Modifier.height(14.dp))

            ExpenseDetailRow("Category", "Food")
            Spacer(modifier = Modifier.height(10.dp))

            ExpenseDetailRow("Amount", "R120")
            Spacer(modifier = Modifier.height(10.dp))

            ExpenseDetailRow("Date", "12 April 2026")
            Spacer(modifier = Modifier.height(10.dp))

            ExpenseDetailRow("Time", "14:32")
            Spacer(modifier = Modifier.height(10.dp))

            ExpenseDetailRow("Payment Method", "Cash")
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Additional Notes")
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Bought groceries after class before going back to residence.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        PrototypeButton(
            text = "Back",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ExpenseDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextGray
        )

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2933)
        )
    }
}

// ----------------------------------------------------
// CONFIRMATION
// ----------------------------------------------------
@Composable
fun ConfirmationScreen(
    innerPadding: PaddingValues,
    onGoHome: () -> Unit,
    onViewSummary: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(title = "Expense Saved", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(40.dp))

        WireframeCard {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            AccentGreen.copy(alpha = 0.15f),
                            RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Expense recorded successfully",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1F2933),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your expense has been added to the budget summary and weekly tracking report.",
                    fontSize = 13.sp,
                    color = TextGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            SectionTitle("Next Actions")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "• Return to the home dashboard",
                fontSize = 13.sp,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "• Review your updated spending summary",
                fontSize = 13.sp,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "• Continue building healthy spending habits",
                fontSize = 13.sp,
                color = Color(0xFF1F2933)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrototypeButton(
                text = "Home",
                onClick = onGoHome,
                modifier = Modifier.weight(1f)
            )

            PrototypeButton(
                text = "View Summary",
                onClick = onViewSummary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ----------------------------------------------------
// PROFILE / BUDGET SETUP
// ----------------------------------------------------
@Composable
fun ProfileSetupScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Profile Setup", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Student profile",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Budget preferences",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Set up your monthly limits so SurviveSmart can give better spending guidance.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Student Budget Setup")
            Spacer(modifier = Modifier.height(14.dp))

            LabeledInputRow("Monthly Allowance")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputRow("Weekly Limit")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledDropdownRow("Main Category")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledInputRow("Savings Target")
            Spacer(modifier = Modifier.height(18.dp))

            PrototypeButton(
                text = "Save Profile",
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("User Snapshot")
            Spacer(modifier = Modifier.height(12.dp))

            ProfileSnapshotRow("Student Type", "University Student")
            Spacer(modifier = Modifier.height(10.dp))

            ProfileSnapshotRow("Preferred Tracking", "Daily")
            Spacer(modifier = Modifier.height(10.dp))

            ProfileSnapshotRow("Budget Style", "Structured")
        }
    }
}

@Composable
fun ProfileSnapshotRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextGray
        )

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2933)
        )
    }
}

// ----------------------------------------------------
// ALERTS
// ----------------------------------------------------
@Composable
fun SpendingAlertsScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Spending Alerts", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Today’s alerts",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "3 reminders need attention",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "These alerts help you notice spending patterns before they become a problem.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        AlertCard(
            icon = "🍔",
            title = "Food budget warning",
            message = "Food spending has reached 80% of your monthly category budget.",
            action = "Review food purchases",
            color = AccentOrange
        )

        Spacer(modifier = Modifier.height(12.dp))

        AlertCard(
            icon = "📝",
            title = "Expense log reminder",
            message = "You have not logged an expense today. Keeping records daily improves accuracy.",
            action = "Add today’s expenses",
            color = AccentBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        AlertCard(
            icon = "🚕",
            title = "Transport increase",
            message = "Transport costs are higher than usual this week compared to your normal spending pattern.",
            action = "Check weekly report",
            color = AccentRed
        )
    }
}

@Composable
fun AlertCard(
    icon: String,
    title: String,
    message: String,
    action: String,
    color: Color
) {
    WireframeCard {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1F2933)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message,
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = action,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}
// ----------------------------------------------------
// GOALS
// ----------------------------------------------------
@Composable
fun SavingsGoalsScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Savings Goals", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Savings overview",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "R610 saved so far",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "You are making steady progress towards your short-term student goals.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        SavingsGoalCard(
            title = "Emergency Transport Fund",
            target = "Target: R500",
            saved = "Saved: R200",
            progress = 0.4f,
            progressText = "40%",
            color = AccentOrange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SavingsGoalCard(
            title = "Monthly Data Budget",
            target = "Target: R300",
            saved = "Saved: R150",
            progress = 0.5f,
            progressText = "50%",
            color = AccentBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        SavingsGoalCard(
            title = "Campus Food Reserve",
            target = "Target: R400",
            saved = "Saved: R260",
            progress = 0.65f,
            progressText = "65%",
            color = AccentGreen
        )

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Savings Tip")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try moving a small amount into savings at the start of the week, not only when money is left over.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SavingsGoalCard(
    title: String,
    target: String,
    saved: String,
    progress: Float,
    progressText: String,
    color: Color
) {
    WireframeCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1F2933)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = target,
                    fontSize = 12.sp,
                    color = TextGray
                )

                Text(
                    text = saved,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }

            Text(
                text = progressText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth()
                .background(Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .background(color, RoundedCornerShape(20.dp))
            )
        }
    }
}

// ----------------------------------------------------
// WEEKLY REPORT
// ----------------------------------------------------
@Composable
fun WeeklyReportScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Weekly Report", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "This week",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "R540 spent",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AccentRed
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Food was your highest spending category this week.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Weekly Breakdown")
            Spacer(modifier = Modifier.height(12.dp))

            WeeklyBreakdownRow("Food", "R260", 0.48f, AccentBlue)
            Spacer(modifier = Modifier.height(10.dp))

            WeeklyBreakdownRow("Transport", "R180", 0.33f, AccentOrange)
            Spacer(modifier = Modifier.height(10.dp))

            WeeklyBreakdownRow("Airtime", "R100", 0.19f, AccentGreen)
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Habit Summary")
            Spacer(modifier = Modifier.height(12.dp))

            HabitSummaryRow("Expense logging", "5 / 7 days", 0.71f, AccentGreen)
            Spacer(modifier = Modifier.height(12.dp))

            HabitSummaryRow("Stayed within budget", "4 / 7 days", 0.57f, AccentOrange)
            Spacer(modifier = Modifier.height(12.dp))

            HabitSummaryRow("Savings progress", "Improved", 0.65f, AccentBlue)
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Recommendation")
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Focus on food and transport spending next week. These two categories used most of your weekly budget.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun WeeklyBreakdownRow(
    label: String,
    amount: String,
    progress: Float,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2933)
            )

            Text(
                text = amount,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .background(color, RoundedCornerShape(20.dp))
            )
        }
    }
}

@Composable
fun HabitSummaryRow(
    label: String,
    value: String,
    progress: Float,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2933)
            )

            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth()
                .background(Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .background(color, RoundedCornerShape(20.dp))
            )
        }
    }
}
// ----------------------------------------------------
// FINANCIAL TIPS
// ----------------------------------------------------
@Composable
fun FinancialTipsScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "Financial Tips", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "Smart money habits",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Small changes can help you save",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "These tips are based on common student spending problems like food, transport and impulse buying.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        TipCard("🍱", "Pack lunch twice a week", "Carrying lunch from home can reduce unnecessary food spending on campus.")

        Spacer(modifier = Modifier.height(12.dp))

        TipCard("📊", "Check your budget summary", "Review your spending before making non-essential purchases.")

        Spacer(modifier = Modifier.height(12.dp))

        TipCard("🚕", "Set a transport limit", "Plan your weekly transport spending so that it does not surprise you later.")
    }
}

@Composable
fun TipCard(
    icon: String,
    title: String,
    text: String
) {
    WireframeCard {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(AccentGreen.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1F2933)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = text,
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ----------------------------------------------------
// APP INFO
// ----------------------------------------------------
@Composable
fun AppInfoScreen(
    innerPadding: PaddingValues,
    onOpenMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AppHeader(title = "App Info", onOpenMenu = onOpenMenu)

        Spacer(modifier = Modifier.height(18.dp))

        WireframeCard {
            Text(
                text = "About SurviveSmart",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Student finance support prototype",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "SurviveSmart is designed to help students track expenses, understand spending habits and make better budget decisions.",
                fontSize = 13.sp,
                color = TextGray,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Prototype Details")
            Spacer(modifier = Modifier.height(12.dp))

            ProfileSnapshotRow("Prototype Version", "Summative refinement")
            Spacer(modifier = Modifier.height(10.dp))

            ProfileSnapshotRow("Prototype Type", "Refined mobile prototype")
            Spacer(modifier = Modifier.height(10.dp))

            ProfileSnapshotRow("Target Users", "University students")
        }

        Spacer(modifier = Modifier.height(14.dp))

        WireframeCard {
            SectionTitle("Main Features")
            Spacer(modifier = Modifier.height(12.dp))

            FeatureRow("Expense logging")
            Spacer(modifier = Modifier.height(8.dp))

            FeatureRow("Budget tracking")
            Spacer(modifier = Modifier.height(8.dp))

            FeatureRow("Habit tracking")
            Spacer(modifier = Modifier.height(8.dp))

            FeatureRow("Spending alerts")
            Spacer(modifier = Modifier.height(8.dp))

            FeatureRow("Savings goals")
            Spacer(modifier = Modifier.height(8.dp))

            FeatureRow("Weekly reports")
        }
    }
}

@Composable
fun FeatureRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(AccentGreen, RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF1F2933)
        )
    }
}
// ----------------------------------------------------
// SIDE DRAWER
// ----------------------------------------------------
@Composable
fun SideDrawerContent(
    onProfileClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onWeeklyReportClick: () -> Unit,
    onTipsClick: () -> Unit,
    onAppInfoClick: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxSize()
            .background(WhiteBox)
            .padding(18.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "SurviveSmart",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentGreen
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Student budgeting and habit tracking",
            fontSize = 13.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        DrawerMenuItem("👤", "Profile Setup", onProfileClick)
        Spacer(modifier = Modifier.height(10.dp))

        DrawerMenuItem("🚨", "Spending Alerts", onAlertsClick)
        Spacer(modifier = Modifier.height(10.dp))

        DrawerMenuItem("🎯", "Savings Goals", onGoalsClick)
        Spacer(modifier = Modifier.height(10.dp))

        DrawerMenuItem("📊", "Weekly Report", onWeeklyReportClick)
        Spacer(modifier = Modifier.height(10.dp))

        DrawerMenuItem("💡", "Financial Tips", onTipsClick)
        Spacer(modifier = Modifier.height(10.dp))

        DrawerMenuItem("ℹ️", "App Info", onAppInfoClick)

        Spacer(modifier = Modifier.weight(1f))

        PrototypeButton(
            text = "Close Menu",
            onClick = onClose,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DrawerMenuItem(
    icon: String,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFF7F7F7),
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    AccentGreen.copy(alpha = 0.12f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2933)
        )
    }
}