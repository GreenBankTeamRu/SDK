<h1 align="center">SDK для интеграции с API Сбербанка</h1>

<h2>Сборка библиотеки </h2>
Для сборки джарников необходимо запусть команду
```bash
./gradlew :build-src:clean :build-src:buildInstantPayment :build-src:buildH2h
```
Собранные fatJar будут лежать по пути build-src/build/libs.
Их можно подключить себе в проект, либо перенести отдельные модули в свой код.

<h2>🔧 Подготовка к работе</h2>

<ul>
  <li><strong>Требования:</strong> Java 1.8+</li>
  <li><strong>Сертификаты:</strong>
    <ul>
      <li>Добавьте банковский сертификат в хранилище JDK:
        <pre><code>keytool -importcert -alias &lt;alias&gt; -file &lt;cert_path&gt; -keystore &lt;path_to_cacerts&gt; -storepass changeit</code></pre>
      </li>
      <li>Или укажите путь к сертификату при создании <code>HttpClientFactory</code></li>
    </ul>
  </li>
</ul>

<h2> Подключение зависимостей</h2>

<h3>Gradle</h3>
<pre><code class="language-groovy">repositories {
    flatDir {
        dirs("/path/to/SDK/") 
    }
}
 
dependencies {   
    implementation("ru.sberbank.sbbol.sberbusinessapi:sdk-instantpayment:release-1.0.0-SNAPSHOT")
    implementation("ru.sberbank.sbbol.sberbusinessapi:sdk-h2h:release-1.0.0-SNAPSHOT")
}</code></pre>

<h3>Maven</h3>
<pre><code class="language-xml">&lt;repositories&gt;
    &lt;repository&gt;
        &lt;id&gt;local-sdk-repo&lt;/id&gt;
        &lt;url&gt;file:///path/to/SDK/&lt;/url&gt;
    &lt;/repository&gt;
&lt;/repositories&gt;
 
&lt;dependencies&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;ru.sberbank.sbbol.sberbusinessapi&lt;/groupId&gt;
        &lt;artifactId&gt;sdk-instantpayment&lt;/artifactId&gt;
        &lt;version&gt;release-1.0.0-SNAPSHOT&lt;/version&gt;
    &lt;/dependency&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;ru.sberbank.sbbol.sberbusinessapi&lt;/groupId&gt;
        &lt;artifactId&gt;sdk-h2h&lt;/artifactId&gt;
        &lt;version&gt;release-1.0.0-SNAPSHOT&lt;/version&gt;
    &lt;/dependency&gt;
&lt;/dependencies&gt;</code></pre>

<h2> Модуль авторизации</h2>

<h3>Инициализация клиента</h3>
<pre><code class="language-java">HttpClientFactory httpClientFactory = HttpClientFactory.of()
        .host("https://auth-server.url")
        .customCertPath("/path/to/cert.p12")
        .customCertPassword("password")
        .build();

AuthorizationApiClient authClient = new AuthorizationApiClientImpl(httpClientFactory);</code></pre>

<h3>Основные методы</h3>
<table>
  <tr>
    <th>Метод</th>
    <th>Описание</th>
  </tr>
  <tr>
    <td><code>getAccessToken()</code></td>
    <td>Получение токена доступа</td>
  </tr>
  <tr>
    <td><code>getRefreshToken()</code></td>
    <td>Обновление токена</td>
  </tr>
  <tr>
    <td><code>changeClientSecret()</code></td>
    <td>Смена секрета клиента</td>
  </tr>
  <tr>
    <td><code>revokeToken()</code></td>
    <td>Отзыв токена</td>
  </tr>
  <tr>
    <td><code>getUserInfo()</code></td>
    <td>Информация о пользователе</td>
  </tr>
</table>

<h2> Модуль моментальных платежей</h2>

<h3>Создание платежа</h3>
<pre><code class="language-java">InstantPaymentApi paymentsApi = new InstantPaymentApiImpl(httpClientFactory);

PaymentInvoiceRequest request = PaymentInvoiceRequest.builder()
        .amount(100.50f)
        .payeeAccount("40702810...")
        .build();

PaymentInvoiceResponse response = paymentsApi.createPaymentInvoice(accessToken, request);</code></pre>

<h3>Генерация URL для подписания</h3>
<pre><code class="language-java">String url = paymentsApi.buildPaymentUrl(
    externalId, 
    "https://callback.url", 
    CryptoprofileType.SMS, 
    "https://bank-host", 
    false
);</code></pre>

<h2>🖥️ Модуль H2H</h2>

<h3>Работа с платежами</h3>
<pre><code class="language-java">H2hApi h2hApi = new H2hApiImpl(httpClientFactory);

// Создание платежного поручения
FintechPayment payment = h2hApi.createPayment(accessToken, paymentRequest);

// Получение статуса
FintechPaymentDocState state = h2hApi.getPaymentDocState(accessToken, externalId);</code></pre>

<h3>Управление сертификатами</h3>
<table>
  <tr>
    <th>Метод</th>
    <th>Описание</th>
  </tr>
  <tr>
    <td><code>certificateRequest()</code></td>
    <td>Запрос нового сертификата</td>
  </tr>
  <tr>
    <td><code>activateCert()</code></td>
    <td>Активация сертификата</td>
  </tr>
  <tr>
    <td><code>getCertState()</code></td>
    <td>Проверка статуса сертификата</td>
  </tr>
</table>

<h2>✨ Дополнительные возможности</h2>
<ul>
  <li>Генерация PKCE-кодов для OAuth</li>
  <li>Работа с зарплатными ведомостями</li>
  <li>Получение выписок по счетам</li>
</ul>

<p><strong>Тестовые сертификаты:</strong><br>
Используйте <code>russiantrustedca.pem</code> для тестового окружения.</p>

📚 [Документация по интеграции с API Сбербанка (HTML)](docs/Документация_по_интеграции_с_API_Сбербанка.html)

<style>
  pre {
    background-color: #f6f8fa;
    border-radius: 6px;
    padding: 16px;
  }
  table {
    border-collapse: collapse;
    width: 100%;
  }
  th, td {
    border: 1px solid #dfe2e5;
    padding: 6px 13px;
  }
  th {
    background-color: #f6f8fa;
  }
  blockquote {
    border-left: 4px solid #dfe2e5;
    color: #6a737d;
    padding: 0 1em;
    margin: 0;
  }
</style>