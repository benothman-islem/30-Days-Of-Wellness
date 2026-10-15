package com.example.a30days

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a30days.ui.theme._30DaysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _30DaysTheme {
                WellnessScreen()
            }
        }
    }
}

private enum class IllustrationStyle {
    NATURE, WATER, JOURNAL, MOVEMENT, MINDFULNESS, NOURISHMENT, REST, CONNECTION
}

private data class WellnessTip(
    val title: String,
    val description: String,
    val detail: String,
    val style: IllustrationStyle
)

private val wellnessTips = listOf(
    WellnessTip("Prenez l’air pendant 15 minutes", "Une petite pause dehors peut changer le rythme de votre journée.", "Choisissez une rue calme ou un coin de verdure. Laissez votre téléphone dans votre poche et remarquez trois choses autour de vous.", IllustrationStyle.NATURE),
    WellnessTip("Buvez un grand verre d’eau au réveil", "Commencez la journée en prenant soin de vous, tout simplement.", "Posez un verre d’eau près de votre lit ce soir. Demain matin, buvez-le tranquillement avant votre café ou votre thé.", IllustrationStyle.WATER),
    WellnessTip("Notez trois choses positives", "Gardez une trace des petits moments qui vous ont fait du bien.", "Dans un carnet ou sur une feuille, écrivez trois choses que vous avez appréciées aujourd’hui. Rien n’est trop petit pour compter.", IllustrationStyle.JOURNAL),
    WellnessTip("Étirez-vous pendant cinq minutes", "Offrez un peu d’espace à votre corps après une longue journée.", "Étirez doucement les épaules, le dos et les jambes. Allez à votre rythme et arrêtez tout mouvement inconfortable.", IllustrationStyle.MOVEMENT),
    WellnessTip("Faites une pause pour respirer", "Quelques respirations lentes aident à revenir au moment présent.", "Inspirez doucement par le nez, puis expirez un peu plus longtemps. Répétez cinq fois sans chercher à contrôler parfaitement votre souffle.", IllustrationStyle.MINDFULNESS),
    WellnessTip("Préparez un repas coloré", "Ajoutez une nouvelle couleur et une touche de fraîcheur à votre assiette.", "Choisissez un fruit ou un légume de saison que vous aimez. Savourez-le sans vous presser, en prêtant attention aux goûts et aux textures.", IllustrationStyle.NOURISHMENT),
    WellnessTip("Créez un rituel du soir", "Un petit signal régulier aide à ralentir avant le coucher.", "Baissez les lumières, préparez une boisson chaude sans caféine ou lisez quelques pages. Gardez ce rituel simple et agréable.", IllustrationStyle.REST),
    WellnessTip("Envoyez un message à un proche", "Un mot attentionné peut illuminer la journée de quelqu’un.", "Pensez à une personne qui vous est chère et envoyez-lui un message sincère. Vous pouvez aussi lui proposer un moment ensemble.", IllustrationStyle.CONNECTION),
    WellnessTip("Écoutez un morceau en pleine attention", "Laissez la musique vous offrir une vraie parenthèse.", "Choisissez une chanson que vous aimez et écoutez-la jusqu’au bout, sans faire autre chose. Remarquez les sons que vous n’aviez jamais entendus.", IllustrationStyle.MINDFULNESS),
    WellnessTip("Rangez un petit espace", "Un coin plus clair peut rendre l’esprit un peu plus léger.", "Choisissez une seule surface, comme votre table ou votre bureau. Réglez un minuteur de dix minutes et arrêtez-vous quand il sonne.", IllustrationStyle.JOURNAL),
    WellnessTip("Marchez à votre rythme", "Quelques pas conscients comptent, quelle que soit la distance.", "Faites une courte promenade si vous le pouvez. Remarquez l’appui de vos pieds et choisissez une allure qui vous fait du bien.", IllustrationStyle.MOVEMENT),
    WellnessTip("Savourez une boisson sans écran", "Accordez-vous quelques minutes loin des notifications.", "Préparez votre boisson préférée et prenez le temps de la sentir et de la déguster. Laissez votre téléphone de côté pendant cette pause.", IllustrationStyle.WATER),
    WellnessTip("Découvrez un nouvel endroit près de chez vous", "La nouveauté peut se trouver juste au coin de la rue.", "Explorez un parc, une petite boutique ou un chemin que vous ne connaissez pas encore. Nul besoin d’aller loin pour changer d’air.", IllustrationStyle.NATURE),
    WellnessTip("Faites une pause loin des écrans", "Reposez vos yeux et votre attention pendant quelques instants.", "Éloignez-vous des écrans pendant quinze minutes. Regardez par la fenêtre, étirez-vous ou prenez simplement quelques respirations.", IllustrationStyle.REST),
    WellnessTip("Préparez une collation nourrissante", "Choisissez quelque chose qui vous plaît et vous donne de l’énergie.", "Assemblez une collation simple avec des ingrédients que vous aimez. Mangez quand vous avez faim et écoutez les besoins de votre corps.", IllustrationStyle.NOURISHMENT),
    WellnessTip("Faites quelque chose avec vos mains", "Une activité créative permet de ralentir et de s’amuser.", "Dessinez, cuisinez, jardinez ou bricolez pendant un petit moment. Le résultat importe moins que le plaisir de faire.", IllustrationStyle.JOURNAL),
    WellnessTip("Prenez quelques minutes au soleil", "Profitez de la lumière du jour si la météo le permet.", "Installez-vous près d’une fenêtre ou sortez un instant. Choisissez un endroit confortable et protégez-vous du soleil si nécessaire.", IllustrationStyle.NATURE),
    WellnessTip("Détendez vos épaules", "Repérez les tensions et laissez votre corps se relâcher.", "Haussez doucement les épaules vers les oreilles, puis relâchez-les en expirant. Répétez quelques fois sans forcer.", IllustrationStyle.MOVEMENT),
    WellnessTip("Écrivez une chose que vous voulez lâcher", "Déposez sur le papier une pensée qui prend trop de place.", "Écrivez librement pendant quelques minutes, sans vous juger. Vous pouvez garder la page, la plier ou la jeter : c’est votre espace.", IllustrationStyle.JOURNAL),
    WellnessTip("Prenez des nouvelles de vous-même", "Demandez-vous ce dont vous avez besoin, sans vous juger.", "Faites une pause et identifiez votre humeur, votre niveau d’énergie et un besoin du moment. Choisissez ensuite un petit geste adapté.", IllustrationStyle.MINDFULNESS),
    WellnessTip("Partagez un repas ou un thé", "Un moment simple à plusieurs nourrit aussi le lien.", "Proposez à un proche de partager un repas, un thé ou un appel. Une présence attentive suffit, même à distance.", IllustrationStyle.CONNECTION),
    WellnessTip("Préparez votre espace pour dormir", "Rendez votre chambre un peu plus propice au repos.", "Rangez ce qui vous gêne, tamisez la lumière et choisissez une température agréable. Faites uniquement les changements qui vous conviennent.", IllustrationStyle.REST),
    WellnessTip("Essayez une recette toute simple", "Cuisinez quelque chose de nouveau sans viser la perfection.", "Choisissez une recette courte avec quelques ingrédients. Prenez votre temps et adaptez-la selon vos goûts et ce que vous avez chez vous.", IllustrationStyle.NOURISHMENT),
    WellnessTip("Observez le ciel quelques minutes", "Levez les yeux et accordez-vous un instant de calme.", "Depuis une fenêtre ou dehors, remarquez les nuages, la lumière et les couleurs. Si le ciel est couvert, observez simplement les changements.", IllustrationStyle.NATURE),
    WellnessTip("Faites une pause sans rien planifier", "Vous avez le droit de ne pas optimiser chaque instant.", "Asseyez-vous confortablement pendant cinq minutes. Vous pouvez regarder autour de vous ou fermer les yeux, sans objectif à atteindre.", IllustrationStyle.REST),
    WellnessTip("Bougez comme cela vous fait plaisir", "Choisissez un mouvement libre, sans performance.", "Mettez une chanson, dansez, faites quelques pas ou étirez-vous. Adaptez l’activité à votre énergie et à vos capacités du jour.", IllustrationStyle.MOVEMENT),
    WellnessTip("Remerciez quelqu’un sincèrement", "Dites à une personne ce que vous appréciez chez elle.", "Pensez à un geste ou à une qualité qui vous a touché. Partagez votre gratitude dans un message ou lors d’une conversation.", IllustrationStyle.CONNECTION),
    WellnessTip("Faites une pause avec une tisane", "Un geste réconfortant pour marquer une transition dans la journée.", "Préparez une infusion que vous aimez et laissez-la refroidir à la bonne température. Prenez quelques gorgées en respirant calmement.", IllustrationStyle.WATER),
    WellnessTip("Choisissez votre moment préféré du mois", "Célébrez le chemin parcouru, même avec de petits pas.", "Repensez aux trente jours et notez le conseil que vous souhaitez garder. Continuez à votre manière, sans pression ni obligation.", IllustrationStyle.MINDFULNESS)
)

@Composable
private fun WellnessScreen() {
    var completedDays by remember { mutableStateOf(emptySet<Int>()) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Header(completedCount = completedDays.size)
            }
            itemsIndexed(wellnessTips) { index, tip ->
                WellnessTipCard(
                    day = index + 1,
                    tip = tip,
                    isCompleted = index + 1 in completedDays,
                    onToggleCompleted = {
                        completedDays = if (index + 1 in completedDays) {
                            completedDays - (index + 1)
                        } else {
                            completedDays + (index + 1)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun Header(completedCount: Int) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "VOTRE PAUSE BIEN-ÊTRE",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "30 jours pour\nvous faire du bien",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            lineHeight = 38.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Un petit geste par jour. À votre rythme.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "VOTRE PARCOURS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Text(
                text = "$completedCount / 30 jours",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = completedCount / 30f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "30 idées simples pour cultiver votre équilibre",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WellnessTipCard(
    day: Int,
    tip: WellnessTip,
    isCompleted: Boolean,
    onToggleCompleted: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "JOUR ${day.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                if (isCompleted) {
                    Text(
                        text = "FAIT ✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = tip.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(11.dp))
            DayIllustration(
                style = tip.style,
                day = day,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(164.dp)
                    .clip(RoundedCornerShape(17.dp))
            )
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = tip.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 21.sp,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = tip.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 21.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onToggleCompleted,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = if (isCompleted) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    ) {
                        Text(
                            text = if (isCompleted) "Marquer comme à refaire" else "J’ai pris ce moment",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            if (!expanded) {
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = "Toucher pour découvrir le conseil",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun DayIllustration(
    style: IllustrationStyle,
    day: Int,
    modifier: Modifier = Modifier
) {
    val palettes = listOf(
        listOf(Color(0xFFE9DDF8), Color(0xFFD9C8F1)),
        listOf(Color(0xFFDDEAF1), Color(0xFFC5DDE5)),
        listOf(Color(0xFFF4E4D6), Color(0xFFECD2C2)),
        listOf(Color(0xFFE4EAD9), Color(0xFFD0DFC5)),
        listOf(Color(0xFFF1DFE9), Color(0xFFE7CBDB)),
        listOf(Color(0xFFF4EBCB), Color(0xFFE8D99F))
    )
    val colors = palettes[(day - 1) % palettes.size]

    Canvas(
        modifier = modifier.background(Brush.linearGradient(colors))
    ) {
        val w = size.width
        val h = size.height
        val ink = Color(0xFF493C63)
        val accent = Color(0xFF856BB6)
        val light = Color(0xFFFFFBF5)

        when (style) {
            IllustrationStyle.NATURE -> {
                drawCircle(Color(0xFFF6D995), w * 0.10f, Offset(w * 0.78f, h * 0.28f))
                val hill = Path().apply {
                    moveTo(0f, h * 0.72f)
                    cubicTo(w * 0.25f, h * 0.43f, w * 0.53f, h * 0.88f, w, h * 0.55f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(hill, Color(0xFF879C86))
                for (i in 0..6) {
                    val x = w * (0.10f + i * 0.13f)
                    drawLine(Color(0xFF476E58), Offset(x, h * 0.78f), Offset(x - 9f, h * 0.39f), 5f)
                    drawLine(Color(0xFF476E58), Offset(x, h * 0.77f), Offset(x + 13f, h * 0.48f), 5f)
                    drawOval(Color(0xFF789B69), topLeft = Offset(x - 22f, h * 0.42f), size = Size(27f, 12f))
                    drawOval(Color(0xFF9DB77C), topLeft = Offset(x + 2f, h * 0.51f), size = Size(27f, 12f))
                }
            }
            IllustrationStyle.WATER -> {
                drawCircle(Color(0xFFFFFFFF).copy(alpha = 0.48f), w * 0.28f, Offset(w * 0.5f, h * 0.52f))
                drawRoundRect(ink.copy(alpha = 0.88f), Offset(w * 0.40f, h * 0.24f), Size(w * 0.21f, h * 0.51f), androidx.compose.ui.geometry.CornerRadius(20f))
                drawRoundRect(Color(0xFFB8DCE8), Offset(w * 0.415f, h * 0.49f), Size(w * 0.18f, h * 0.24f), androidx.compose.ui.geometry.CornerRadius(12f))
                drawLine(light, Offset(w * 0.45f, h * 0.31f), Offset(w * 0.45f, h * 0.43f), 5f, cap = StrokeCap.Round)
                drawCircle(Color(0xFF8BB7C4), w * 0.035f, Offset(w * 0.72f, h * 0.36f))
                drawCircle(Color(0xFF8BB7C4), w * 0.022f, Offset(w * 0.26f, h * 0.65f))
            }
            IllustrationStyle.JOURNAL -> {
                drawRoundRect(Color(0xFFB99073), Offset(w * 0.27f, h * 0.20f), Size(w * 0.48f, h * 0.61f), androidx.compose.ui.geometry.CornerRadius(18f))
                drawRoundRect(light, Offset(w * 0.31f, h * 0.24f), Size(w * 0.39f, h * 0.53f), androidx.compose.ui.geometry.CornerRadius(12f))
                for (i in 0..3) {
                    val y = h * (0.37f + i * 0.085f)
                    drawLine(Color(0xFFC8B9D8), Offset(w * 0.37f, y), Offset(w * 0.64f, y), 3f, cap = StrokeCap.Round)
                }
                drawLine(accent, Offset(w * 0.74f, h * 0.72f), Offset(w * 0.84f, h * 0.31f), 9f, cap = StrokeCap.Round)
                drawCircle(Color(0xFFE8C782), w * 0.035f, Offset(w * 0.75f, h * 0.68f))
            }
            IllustrationStyle.MOVEMENT -> {
                drawCircle(Color(0xFFF6D995), w * 0.09f, Offset(w * 0.50f, h * 0.27f))
                drawLine(ink, Offset(w * 0.50f, h * 0.40f), Offset(w * 0.50f, h * 0.66f), 12f, cap = StrokeCap.Round)
                drawLine(ink, Offset(w * 0.50f, h * 0.47f), Offset(w * 0.32f, h * 0.34f), 10f, cap = StrokeCap.Round)
                drawLine(ink, Offset(w * 0.50f, h * 0.47f), Offset(w * 0.68f, h * 0.32f), 10f, cap = StrokeCap.Round)
                drawLine(ink, Offset(w * 0.50f, h * 0.65f), Offset(w * 0.35f, h * 0.83f), 11f, cap = StrokeCap.Round)
                drawLine(ink, Offset(w * 0.50f, h * 0.65f), Offset(w * 0.67f, h * 0.82f), 11f, cap = StrokeCap.Round)
                drawArc(accent.copy(alpha = 0.45f), 205f, 130f, false, Offset(w * 0.34f, h * 0.10f), Size(w * 0.32f, h * 0.66f), style = Stroke(width = 4f, cap = StrokeCap.Round))
            }
            IllustrationStyle.MINDFULNESS -> {
                for (i in 3 downTo 1) {
                    drawCircle(
                        color = accent.copy(alpha = 0.12f + i * 0.07f),
                        radius = w * (0.09f + i * 0.075f),
                        center = Offset(w * 0.5f, h * 0.51f),
                        style = Stroke(width = 4f)
                    )
                }
                drawCircle(Color(0xFFFFF7EA), w * 0.10f, Offset(w * 0.5f, h * 0.51f))
                drawCircle(accent, w * 0.025f, Offset(w * 0.5f, h * 0.51f))
                drawCircle(Color(0xFFFFF7EA).copy(alpha = 0.85f), 6f, Offset(w * 0.24f, h * 0.31f))
                drawCircle(Color(0xFFFFF7EA).copy(alpha = 0.85f), 4f, Offset(w * 0.76f, h * 0.67f))
            }
            IllustrationStyle.NOURISHMENT -> {
                drawRoundRect(Color(0xFFFFF8EA), Offset(w * 0.25f, h * 0.28f), Size(w * 0.50f, h * 0.48f), androidx.compose.ui.geometry.CornerRadius(45f))
                drawCircle(Color(0xFFE58C6A), w * 0.085f, Offset(w * 0.41f, h * 0.49f))
                drawCircle(Color(0xFF8AAB77), w * 0.09f, Offset(w * 0.58f, h * 0.46f))
                drawCircle(Color(0xFFF0C85D), w * 0.08f, Offset(w * 0.50f, h * 0.62f))
                drawOval(Color(0xFF63855F), topLeft = Offset(w * 0.55f, h * 0.31f), size = Size(w * 0.10f, h * 0.06f))
                drawLine(Color(0xFFB99073), Offset(w * 0.19f, h * 0.77f), Offset(w * 0.81f, h * 0.77f), 5f, cap = StrokeCap.Round)
            }
            IllustrationStyle.REST -> {
                drawCircle(Color(0xFFFFF1C9), w * 0.16f, Offset(w * 0.51f, h * 0.49f))
                drawCircle(colors[0], w * 0.14f, Offset(w * 0.57f, h * 0.43f))
                drawCircle(light.copy(alpha = 0.9f), 5f, Offset(w * 0.30f, h * 0.32f))
                drawCircle(light.copy(alpha = 0.9f), 4f, Offset(w * 0.73f, h * 0.39f))
                drawCircle(light.copy(alpha = 0.9f), 6f, Offset(w * 0.68f, h * 0.68f))
                drawRoundRect(ink.copy(alpha = 0.70f), Offset(w * 0.30f, h * 0.70f), Size(w * 0.40f, h * 0.10f), androidx.compose.ui.geometry.CornerRadius(30f))
            }
            IllustrationStyle.CONNECTION -> {
                drawCircle(Color(0xFFE9A5A2), w * 0.12f, Offset(w * 0.43f, h * 0.47f))
                drawCircle(Color(0xFF8C79B6), w * 0.12f, Offset(w * 0.58f, h * 0.47f))
                drawCircle(Color(0xFFF1C96D), w * 0.10f, Offset(w * 0.50f, h * 0.62f))
                drawLine(Color(0xFF68866C), Offset(w * 0.50f, h * 0.73f), Offset(w * 0.50f, h * 0.90f), 5f, cap = StrokeCap.Round)
                drawLine(Color(0xFF68866C), Offset(w * 0.50f, h * 0.83f), Offset(w * 0.40f, h * 0.76f), 4f, cap = StrokeCap.Round)
                drawLine(Color(0xFF68866C), Offset(w * 0.50f, h * 0.83f), Offset(w * 0.61f, h * 0.75f), 4f, cap = StrokeCap.Round)
                drawCircle(Color(0xFFFFF8EA).copy(alpha = 0.7f), 5f, Offset(w * 0.23f, h * 0.28f))
                drawCircle(Color(0xFFFFF8EA).copy(alpha = 0.7f), 4f, Offset(w * 0.77f, h * 0.68f))
            }
        }
    }
}
