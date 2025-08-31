# ![Typing SVG](https://readme-typing-svg.herokuapp.com/?color=fdd618&size=35&center=true&vCenter=true&width=1000&lines=Aplicativo+utilizando+Cloud+Firestore!)

## 📌 Descrição do Projeto
### 🔥 1° - Configuração do Firebase para o Projeto
- Abra o Android Studio e conecte sua conta na IDE;
- Acesse: Main Menu > Tools > 🔥Firebase;
- No "Assistent", você deverá realizar 3 configurações:

#### 👥 Authentication
- Procure a opção "👥Authentication" e selecione a opção "Authenticate with Google" e realize o primeiro passo o qual te redirecionará à página do Console Firebase;
- Caso não tenha um banco já criado, crie um com o mesmo nome do projeto;
- Faça as configurações que seja de seu agrado, como pro exemplo, "Ativar o Gemini no Firebase", entre outras;
- Volte à IDE e siga o segundo passo, importando as dependências para o funcionamento da função.
  
Pronto, o seu banco de dados foi criado e conectado ao projeto do Android Studio!

Caso já tenha um banco de dados criado, apenas clique nele quando for direcionado à tela do Console Firebase.

#### 📅 Realtime Database 
- Voltando ao "Assistent", procure a opção "📅 Realtime Database" e selecione a opção "Get started with Realtime Database";
- Retorne ao Console Firebase e vá até a sessão "Criação" na aba Categorias de Produtos";
- Selecione: Realtime Database > Criar Banco de Dados > Estados Unidos (us-central1) > Iniciar Modo Teste;
- Retorne à IDE e siga o segundo passo, importando as dependências para o funcionamento da função.
  
#### ☁️ Cloud Firestore 
- Ainda no Console do Firebase, vá novamente na sessão "Criação";
- Selecione: Firestore Database > Criar Banco de Dados > Edição Standard > Opção "nam5 (United States)" > Iniciar modo de Teste;
- Retorne à IDE e siga o segundo passo, importando as dependências para o funcionamento da função.

### Importações necessárias para o projeto
```
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database) //Para acionar a variável db = Firebase.Firestore
    implementation(libs.firebase.firestore) //Para acionar a variável db = Firebase.Firestore
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.8.0") //Para funcionar a conexão entre as páginas e os dados. Sujeita a mudança de versão.

```

### 👩‍💻 2° - Estruturação do Código
#### Class MainActivity.kt
Essa classe é a atividade principal do app Android:
- Inicializa o Firebase Firestore (db) para acessar dados na nuvem.
- Configura a interface usando Compose com Scaffold.
- Cria um navegador de telas (NavHost) para controlar a navegação entre as páginas existentes;
- Permite que, ao fazer logout, o usuário volte para a tela de login e limpe a tela anterior da pilha de navegação;
  
#### Fun LoginScreen.kt
Essa função é um composable do Jetpack Compose que cria a tela de login do aplicativo:
- Define variáveis de estado para armazenar o e-mail, senha, mensagem de erro e se a senha deve ser mostrada.
- Configura cores, fonte personalizada e estilo visual da tela.
- Exibe a logo animada do app com um efeito de flutuação.
- Mostra campos de texto personalizados para e-mail e senha, incluindo um botão para mostrar/ocultar a senha.
- Exibe mensagens de erro se algum campo estiver vazio ou se as credenciais forem inválidas.

#### Fun RegisterScreen.kt
Essa função é um composable do Jetpack Compose que cria a tela de registro/cadastro de usuários do app: 
- Define estados para os campos do formulário: nome, apelido, e-mail, senha, telefone, além de controlar visibilidade da senha e mensagens de erro.
- Valida os campos obrigatórios antes de cadastrar.
- Cadastra os dados no Firebase Firestore ao clicar em “Cadastrar” e chama onRegisterComplete() se tudo der certo.

#### Fun HomeScreen.kt
É a tela principal do usuário após o login:
- Exibe uma saudação personalizada com o nome do usuário.
- Possui um menu no canto superior direito com opções para fazer a consulta de todos os registros e sair da aplicação;

## 📲 Apresentação das telas 
<div style="display: flex; justify-content: center; flex-wrap: wrap; gap: 8px;">
  <img src="https://github.com/user-attachments/assets/ea577951-17b5-4ec9-b893-142eda8d5ee5" width="200" />
  <img src="https://github.com/user-attachments/assets/5f35abfa-e3a4-4c25-8fcc-c2579ff44e9b" width="200" />
  <img src="https://github.com/user-attachments/assets/1e9ccb7a-2719-4631-befc-26dfeb62d7a7" width="200" />
  <img src="https://github.com/user-attachments/assets/267e30df-d6af-4c6b-8470-3b9740ee19b5" width="200" />
</div>

## ☁️ Consulta dos Dados no Firestore Firebase
<img width="1365" height="597" alt="image" src="https://github.com/user-attachments/assets/656a8511-21dd-4153-bdb1-7a069bd2db2b" />

