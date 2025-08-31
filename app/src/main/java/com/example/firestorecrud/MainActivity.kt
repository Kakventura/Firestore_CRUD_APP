package com.example.firestorecrud

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firestorecrud.ui.theme.FirestoreCRUDTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("login") {
                        LoginScreen(
                            onLogin = { userName -> navController.navigate("home/$userName") },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            onRegisterComplete = { navController.navigate("login") },
                            onLoginClick = { navController.navigate("login") }
                        )
                    }
                    composable(
                        "home/{userName}",
                        arguments = listOf(navArgument("userName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val userName = backStackEntry.arguments?.getString("userName") ?: ""
                        HomeScreen(userName = userName, onLogout = {
                            navController.navigate("login") { popUpTo("home/{userName}") { inclusive = true } }
                        })
                    }
                }
            }
        }
    }
}



@Composable
fun RegisterScreen(
    onRegisterComplete: () -> Unit,
    onLoginClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var apelido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val db = Firebase.firestore

    val primaryColor = Color(0xFFFFB300)
    val textColor = Color.White
    val cardBackground = Color(0xFF1E1E1E)
    val labelColor = Color.Gray

    val LobsterTwo = FontFamily(
        Font(R.font.lobster_two_regular, weight = FontWeight.Normal),
        Font(R.font.lobster_two_bold, weight = FontWeight.Bold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // Conteúdo rolável central
        Column(
            modifier = Modifier
                .weight(1f) // ocupa espaço entre topo e footer
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "float")
            val offsetY by infiniteTransition.animateFloat(
                initialValue = -10f,
                targetValue = 10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetY"
            )

            Image(
                painter = painterResource(id = R.drawable.loginicon),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(240.dp)
                    .offset { IntOffset(0, offsetY.roundToInt()) }
                    .padding(bottom = 16.dp)
            )

            Text(
                "Registro",
                fontFamily = LobsterTwo,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFFB300),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = LobsterTwo,
                )
            }

            // Campos de formulário
            CustomDarkTextField(
                value = nome,
                onValueChange = { nome = it },
                label = "Nome",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                fontFamily = LobsterTwo
            )
            CustomDarkTextField(
                value = apelido,
                onValueChange = { apelido = it },
                label = "Nickname",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                fontFamily = LobsterTwo
            )
            CustomDarkTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                fontFamily = LobsterTwo
            )
            CustomDarkTextField(
                value = senha,
                onValueChange = { senha = it },
                label = "Senha",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                isPassword = !mostrarSenha,
                trailingIcon = {
                    IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                        Icon(
                            painter = painterResource(
                                id = if (mostrarSenha) R.drawable.visivel else R.drawable.invisivel
                            ),
                            contentDescription = "Toggle password visibility",
                            tint = labelColor
                        )
                    }
                },
                fontFamily = LobsterTwo
            )
            CustomDarkTextField(
                value = telefone,
                onValueChange = { input ->
                    // só mantém os dígitos, máximo 11
                    telefone = input.filter { it.isDigit() }.take(11)
                },
                label = "Telefone",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                fontFamily = LobsterTwo,
                visualTransformation = PhoneMaskTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (nome.isBlank() || apelido.isBlank() || email.isBlank() || senha.isBlank()) {
                        errorMessage = "Preencha todos os campos obrigatórios"
                        return@Button
                    }

                    val usuario = hashMapOf(
                        "nome" to nome,
                        "apelido" to apelido,
                        "email" to email,
                        "senha" to senha,
                        "telefone" to telefone
                    )

                    db.collection("banco")
                        .add(usuario)
                        .addOnSuccessListener { onRegisterComplete() }
                        .addOnFailureListener { e ->
                            errorMessage = "Erro ao cadastrar: ${e.message}"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = textColor
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "Cadastrar",
                    fontSize = 18.sp,
                    fontFamily = LobsterTwo,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onLoginClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = primaryColor
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, primaryColor)
            ) {
                Text(
                    "Já tem uma conta? Faça login",
                    fontSize = 16.sp,
                    fontFamily = LobsterTwo,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ======= FOOTER FIXO =======
        Footer()
    }
}

// Máscara de telefone (##) #####-####
class PhoneMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(11)
        val formatted = StringBuilder()
        for (i in trimmed.indices) {
            when (i) {
                0 -> formatted.append("(").append(trimmed[i])
                1 -> formatted.append(trimmed[i]).append(") ")
                6 -> formatted.append("-").append(trimmed[i])
                else -> formatted.append(trimmed[i])
            }
        }
        return TransformedText(
            AnnotatedString(formatted.toString()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 1 -> offset + 1
                        offset <= 5 -> offset + 3
                        offset <= 10 -> offset + 4
                        else -> 14
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 2 -> offset - 1
                        offset <= 7 -> offset - 3
                        offset <= 12 -> offset - 4
                        else -> 11
                    }
                }
            }
        )
    }
}

//TELA PRINCIPAL DO USUARIO COM CADASTRO DAS INFORMAÇÕES
@Composable
fun HomeScreen(
    userName: String = "Usuário",
    onLogout: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var mostrarRegistros by remember { mutableStateOf(false) }
    val db = Firebase.firestore
    val banco = remember { mutableStateListOf<Map<String, Any>>() }
    val scrollState = rememberScrollState()

    val backgroundColor = Color(0xFF121212)
    val primaryColor = Color(0xFFFFB300)
    val textColor = Color.White
    val cardBackground = Color(0xFF1E1E1E)

    val LobsterTwo = FontFamily(
        Font(R.font.lobster_two_regular, weight = FontWeight.Normal),
        Font(R.font.lobster_two_bold, weight = FontWeight.Bold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // ======= TOPO FIXO =======
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // MENU
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = primaryColor
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = (-30).dp, y = 0.dp),
                    containerColor = cardBackground
                ) {
                    DropdownMenuItem(
                        text = { Text("Listar Registros", fontFamily = LobsterTwo, color = primaryColor) },
                        onClick = {
                            menuExpanded = false
                            db.collection("banco")
                                .get()
                                .addOnSuccessListener { result ->
                                    banco.clear()
                                    for (document in result) {
                                        banco.add(document.data)
                                    }
                                    mostrarRegistros = true
                                }
                        }
                    )
                    DropdownMenuItem(
                        onClick = { menuExpanded = false; onLogout() },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.logout),
                                    contentDescription = "Sair",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Sair", fontFamily = LobsterTwo, color = primaryColor)
                            }
                        }
                    )
                }
            }

            // LOGO ANIMADO
            val infiniteTransition = rememberInfiniteTransition(label = "float")
            val offsetY by infiniteTransition.animateFloat(
                initialValue = -10f,
                targetValue = 10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetY"
            )
            Image(
                painter = painterResource(id = R.drawable.analisar),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(240.dp)
                    .offset { IntOffset(0, offsetY.roundToInt()) }
                    .padding(bottom = 16.dp)
            )

            // TEXTO DE BOAS-VINDAS
            Text(
                "Bem-vindo, $userName!",
                fontFamily = LobsterTwo,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                textAlign = TextAlign.Center
            )
        }

        // ======= CONTEÚDO ROLÁVEL (APENAS REGISTROS) =======
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (mostrarRegistros) {
                banco.forEachIndexed { index, registro ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(cardBackground, shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            "Registro ${index + 1}",
                            color = primaryColor,
                            fontSize = 18.sp,
                            fontFamily = LobsterTwo,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Nome: ${registro["nome"]}", color = textColor, fontFamily = LobsterTwo)
                        Text("Apelido: ${registro["apelido"]}", color = textColor, fontFamily = LobsterTwo)
                        Text("Email: ${registro["email"]}", color = textColor, fontFamily = LobsterTwo)
                        Text("Senha: ${registro["senha"]}", color = textColor, fontFamily = LobsterTwo)
                        Text("Telefone: ${registro["telefone"]}", color = textColor, fontFamily = LobsterTwo)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Use o menu no canto superior direito para listar os registros",
                        color = Color.Gray,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = LobsterTwo
                    )
                }
            }
        }

        // ======= FOOTER FIXO =======
        Footer()
    }
}



@Composable
fun LoginScreen(
    onLogin: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mostrarSenha by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val db = Firebase.firestore

    val backgroundColor = Color(0xFF121212)
    val primaryColor = Color(0xFFFFB300)
    val textColor = Color.White
    val cardBackground = Color(0xFF1E1E1E)
    val labelColor = Color.Gray

    val LobsterTwo = FontFamily(
        Font(R.font.lobster_two_regular, weight = FontWeight.Normal),
        Font(R.font.lobster_two_bold, weight = FontWeight.Bold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize() // ocupa toda a altura da tela
            .background(backgroundColor)
    ) {
        // Conteúdo central ocupa todo o espaço restante
        Column(
            modifier = Modifier
                .weight(1f) // preenche o espaço restante
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()), // rolagem se necessário
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo animado
            val infiniteTransition = rememberInfiniteTransition(label = "float")
            val offsetY by infiniteTransition.animateFloat(
                initialValue = -10f,
                targetValue = 10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetY"
            )

            Image(
                painter = painterResource(id = R.drawable.loginicon),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(240.dp)
                    .offset { IntOffset(0, offsetY.roundToInt()) }
                    .padding(bottom = 16.dp)
            )

            Text(
                "Login",
                fontFamily = LobsterTwo,
                fontWeight = FontWeight.Bold,
                fontSize = 44.sp,
                color = primaryColor,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFFB300),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = LobsterTwo,
                )
            }

            // Campos de texto
            CustomDarkTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                fontFamily = LobsterTwo
            )

            CustomDarkTextField(
                value = senha,
                onValueChange = { senha = it },
                label = "Senha",
                backgroundColor = cardBackground,
                textColor = textColor,
                labelColor = labelColor,
                isPassword = !mostrarSenha,
                trailingIcon = {
                    IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                        Icon(
                            painter = painterResource(
                                id = if (mostrarSenha) R.drawable.visivel else R.drawable.invisivel
                            ),
                            contentDescription = "Toggle password visibility",
                            tint = labelColor
                        )
                    }
                },
                fontFamily = LobsterTwo
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Entrar
            Button(
                onClick = {
                    if (email.isBlank() || senha.isBlank()) {
                        errorMessage = "Preencha todos os campos"
                        return@Button
                    }

                    db.collection("banco")
                        .whereEqualTo("email", email)
                        .whereEqualTo("senha", senha)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                errorMessage = "Credenciais inválidas"
                            } else {
                                val nomeUsuario =
                                    documents.documents[0].getString("apelido") ?: email
                                onLogin(nomeUsuario)
                            }
                        }
                        .addOnFailureListener { exception ->
                            errorMessage = "Erro ao fazer login: ${exception.message}"
                            Log.w("Login", "Erro ao verificar login", exception)
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "Entrar",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = LobsterTwo,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = primaryColor
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, primaryColor)
            ) {
                Text(
                    "Não tem conta? Cadastre-se",
                    fontSize = 16.sp,
                    fontFamily = LobsterTwo,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Footer fixo na parte inferior
        Footer()
    }
}


//TEMA ESCURO
@Composable
fun CustomDarkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    backgroundColor: Color,
    textColor: Color,
    labelColor: Color,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    fontFamily: FontFamily? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {    val primaryDarkColor = Color(0xFFFFB300)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = labelColor) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = primaryDarkColor,
            focusedLabelColor = labelColor,
            unfocusedLabelColor = labelColor,
            focusedIndicatorColor = primaryDarkColor,
            unfocusedIndicatorColor = Color.Gray
        ),
        trailingIcon = trailingIcon
    )
}

@Composable
fun Footer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Karinne Angelo – 3°Desenvolvimento de Sistemas (AMS) | © 2025." +
                    "\nTodos os direitos reservados.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}



