# Spring Security JWT

JWT (JSON web token) 

JSON - значит это текст (набор символов)

header.payload.signature

Каждая часть - это тоже JSON (текст)

Header(заголовок) - JSON-объект, содержащий два элемента (два ключа)
{
  "alg":"HS256",
  "typ":"JWT"
}

Хранится информация о типе токена и используемый алгоритм хеширования (кодировки)

Payload (полезная нагрузка) - JSON-объект, который может содержать
какую-либо дополнительную информацию (например о пользователе)

Информация передается в виде так называемых "утверждений" (claims)
- Утверждения о пользователе (registered claims): Их значения стандартизированы и включают, например, 
идентификатор пользователя (subject), время выдачи токена (iat) и срок действия (exp)
- Public claims
- Private claims

Signature (подпись) - Это результат шифрования кодированной строки заголовка и полезой нагрузки.


Как осуществляется работа с JWT:
1) пользователь (фронт) присылает нам свой логин и пароль
2) приложение проверяет эти данные и если все ок, то должно отправить обратно JWT
3) создаем header - указываем какой алгоритм хеширования мы будем использовать (например, HS256)
4) создаем payload - добавляем в него username, дату создания и срок действия
5) объединяем вместе header.payload и кодируем на основе алгоритма Base64 (стандартный алгоритм для кодирования данных)
6) в нашем приложении есть некий секретный ключ для шифрования данных (это набор символов о котором знает ТОЛЬКО наш сервер)
7) с помощью этого ключа и с использование алгоритма шифрования (HS256)
8) получили некую signature (набор символов, созданные на базе header.payload в зашифрованном виде)
9) и далее все это вместе объединяем: header.payload.signature -> JWT -> отправляем в качестве ответа пользователю

Далее:
1) пользователь когда отправляет ЛЮБОЙ свой запрос в этом запросе в один из заголовков (authorization header)
добавляется тип Auth Type и добавляется токен 
2) Сервер получил это запрос.
3) Проверяет а есть ли заголовок authorization header, если есть - то вытаскивает из него JWT
4) Осуществляется проверка (валидация) токена
- проверка подлинности осуществляется следующим образом: берем первые две части header.payload и 
с помощью секретного ключа осуществляем шифрование - и сравниваем то что мы получили с signature
Если подпись совпадает - значит JWT подлинный.
- из payload берем срок действия и проверяем JWT все еще актуальный или уже устаревший 
5) на основе проверок мы получаем либо exception (по любому поводу) - доступ закрыт или все ок - выполняем запрос



Настройки Spring Security:

SecurityContextHolder -
SecurityContext - содержит объект Authentication
Authentication - представляет пользователя (principal) в том формате как понимает Spring Security
GrantedAuthority - разрешения пользователя (USER, ADMIN)
UserDetails - содержится необходимая информация для создания объекта Authentication
Содержит:
- имя пользователя
- пароль
- флаги: isAccountNonExpire, isAccountNonLocked, isEnable
- коллекцию прав (ролей) пользователя




### Ключевые объекты контекста Spring Security:

- **SecurityContextHolder**: В нем содержится информация о текущем контексте безопасности приложения, который включает в себя подробную информацию о пользователе (Principal), работающем в настоящее время с приложением. По умолчанию, SecurityContextHolder использует `ThreadLocal` для хранения такой информации, что означает, что контекст безопасности всегда доступен для методов, исполняющихся в том же самом потоке. Для изменения стратегии хранения информации можно воспользоваться статическим методом класса `SecurityContextHolder.setStrategyName(String strategy)`.
- **SecurityContext**: Содержит объект `Authentication` и в случае необходимости информацию системы безопасности, связанную с запросом от пользователя.
- **Authentication**: Представляет пользователя (Principal) с точки зрения Spring Security.
- **GrantedAuthority**: Отражает разрешения, выданные пользователю в масштабе всего приложения, такие разрешения (как правило, называются «роли»), например `ROLE_ANONYMOUS`, `ROLE_USER`, `ROLE_ADMIN`.
- **UserDetails**: Предоставляет необходимую информацию для построения объекта `Authentication` из DAO объектов приложения или других источников данных системы безопасности. Объект `UserDetails` содержит имя пользователя, пароль, флаги: `isAccountNonExpired`, `isAccountNonLocked`, `isCredentialsNonExpired`, `isEnabled` и Collection — прав (ролей) пользователя.
- **UserDetailsService**: Используется, чтобы создать объект `UserDetails` путем реализации единственного метода этого интерфейса:
  ```java
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
  ```

### Аутентификация

1. Пользователю будет предложено войти в систему, предоставив имя (логин или email) и пароль. Имя пользователя и пароль объединяются в экземпляр класса `UsernamePasswordAuthenticationToken` (экземпляр интерфейса `Authentication`) после чего он передается экземпляру `AuthenticationManager` для проверки.
2. В случае, если пароль не соответствует имени пользователя, будет выброшено исключение `BadCredentialsException` с сообщением “Bad Credentials”.
3. Если аутентификация прошла успешно, возвращается полностью заполненный экземпляр `Authentication`.
4. Для пользователя устанавливается контекст безопасности путем вызова метода `SecurityContextHolder.getContext().setAuthentication(…)`, куда передается объект, который вернул `AuthenticationManager`.


### SecurityContextHolder и SecurityContext

`SecurityContextHolder` используется для хранения деталей о текущем пользователе, что позволяет эти данные легко извлекать в любом месте приложения:

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication != null) {
    String currentUserName = authentication.getName();
    // использование имени пользователя
}
```

`SecurityContext` хранит в себе объект `Authentication`, который представляет текущего пользователя и его права в системе.

### UserDetails и UserDetailsService

`UserDetailsService` интерфейс используется для загрузки данных пользователя по имени пользователя.
Реализация этого интерфейса возвращает объект `UserDetails`, который Spring Security использует для построения объекта `Authentication`.

Пример реализации `UserDetailsService`:

```java
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }
}
```

## Аутентификация

Процесс аутентификации начинается с получения имени пользователя и пароля, которые затем преобразуются в `UsernamePasswordAuthenticationToken` и передаются `AuthenticationManager`:

```java
Authentication request = new UsernamePasswordAuthenticationToken(username, password);
Authentication result = authenticationManager.authenticate(request);
SecurityContextHolder.getContext().setAuthentication(result);
```

## Использование PasswordEncoder

### BCryptPasswordEncoder

`BCryptPasswordEncoder` является наиболее рекомендуемым `PasswordEncoder` за его способность эффективно защищать пароли с помощью bcrypt алгоритма:

```java
@Autowired
private PasswordEncoder passwordEncoder;

public void registerUser(String username, String rawPassword) {
    String encodedPassword = passwordEncoder.encode(rawPassword);
    User newUser = new User(username, encodedPassword);
    userRepository.save(newUser);
}
```

### DelegatingPasswordEncoder

`DelegatingPasswordEncoder` позволяет использовать несколько методов хеширования паролей одновременно, что удобно при переходе на новую систему хеширования:

```java
PasswordEncoder defaultEncoder = new BCryptPasswordEncoder();
Map<String, PasswordEncoder> encoders = new HashMap<>();
encoders.put("bcrypt", new BCryptPasswordEncoder());
encoders.put("noop", NoOpPasswordEncoder.getInstance());

PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
```

## Servlet Security и Filters

В Spring Security каждый `Filter` в цепочке фильтрации может изменять запрос или ответ, либо прерывать цепочку обработки. Очень важен порядок, в котором фильтры добавлены в цепочку:

```java
http.addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);
```


### Как работают фильтры в Spring Security

Фильтры в Spring Security играют ключевую роль в процессе обработки входящих запросов к вашему приложению.
Они обеспечивают не только аутентификацию и авторизацию, но и множество других функций безопасности, таких как CSRF-защита,
управление сессиями и многое другое.

Фильтры в Spring Security – это специализированные компоненты, которые встраиваются в стандартный цикл обработки запросов в веб-приложении на Java.
Каждый фильтр выполняет определенную задачу, связанную с безопасностью, и передает управление следующему фильтру в цепочке.
Все это происходит до того, как запрос достигает сервлета, который непосредственно обрабатывает бизнес-логику.

#### Классы и интерфейсы

Основным классом, с которым стоит ознакомиться, является `FilterChainProxy`. Этот класс отвечает за управление цепочкой фильтров.
Каждый фильтр реализует интерфейс `javax.servlet.Filter`.

##### Примеры ключевых фильтров:

1. **UsernamePasswordAuthenticationFilter** — обрабатывает формы аутентификации, извлекая имя пользователя и пароль из запроса.
2. **CsrfFilter** — предотвращает атаки межсайтовой подделки запросов (CSRF).
3. **LogoutFilter** — обрабатывает выход пользователя из системы.

#### Пример кода:

Давайте рассмотрим, как можно добавить кастомный фильтр, который будет логировать каждый входящий запрос:

```java
public class LoggingFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        LOG.info("Request URI is: {}", req.getRequestURI());
        chain.doFilter(request, response);
    }
}
```

#### Как добавить фильтр в конфигурацию:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Добавляем кастомный фильтр перед фильтром UsernamePasswordAuthenticationFilter
            .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers("/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .and()
            .logout();
    }
}
```

### Комментарии к примеру кода:

- **LoggingFilter** - этот фильтр логирует URI каждого запроса к серверу.
- В методе `configure` класса `SecurityConfig` фильтр `LoggingFilter` добавляется перед `UsernamePasswordAuthenticationFilter`, что позволяет ему обрабатывать запрос до начала процесса аутентификации.


###  Классы фильтров

#### а. **UsernamePasswordAuthenticationFilter**

Этот фильтр обрабатывает аутентификационные запросы типа `POST` с параметрами `username` и `password`.
Он создает объект `UsernamePasswordAuthenticationToken` и передает его `AuthenticationManager` для аутентификации.

Пример конфигурации:

```java
http.addFilterBefore(new UsernamePasswordAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
```

#### б. **BasicAuthenticationFilter**

Этот фильтр обрабатывает HTTP Basic Authentication. Он извлекает заголовок `Authorization` из запроса и передает его `AuthenticationManager` для аутентификации.

Пример конфигурации:

```java
http.addFilterBefore(new BasicAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
```

#### в. **FilterChainProxy**

Этот фильтр является основной точкой входа в цепочку фильтров Spring Security. Он вызывает все остальные фильтры в заданном порядке.

Пример конфигурации (обычно не требуется, так как он автоматически добавляется в контекст безопасности):

```java
http.addFilterBefore(new FilterChainProxy(), BasicAuthenticationFilter.class);
```

#### г. Пользовательские фильтры

Вы также можете создавать собственные фильтры, расширяя класс `GenericFilterBean` или реализуя интерфейс `javax.servlet.Filter`.
Эти фильтры могут выполнять различные действия, например, проверять наличие аутентификации, устанавливать права доступа и т. д.

Пример пользовательского фильтра:

```java
public class CustomFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // выполнение действий перед передачей запроса дальше по цепочке
        chain.doFilter(request, response); // передача запроса следующему фильтру
        // выполнение действий после обработки запроса другими фильтрами
    }
}
```

### 3. Порядок выполнения фильтров

Порядок выполнения фильтров очень важен, так как каждый фильтр может влиять на запрос или ответ перед его передачей следующему фильтру.
Фильтры обрабатываются в том порядке, в котором они добавлены в цепочку.

### Пример конфигурации

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(new CustomFilter(), BasicAuthenticationFilter.class)
        .addFilterBefore(new UsernamePasswordAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new BasicAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic();
}
```

В этом примере фильтры добавляются в цепочку и определяется порядок их выполнения. Сначала будет выполнен пользовательский фильтр, затем фильтр аутентификации по имени пользователя и паролю, а затем фильтр базовой аутентификации. После этого выполняется авторизация запросов.


### Основные фильтры

В список фильтров Spring Security входят многие элементы, каждый из которых играет свою роль в обеспечении безопасности приложения.
Но некоторые из них являются ключевыми в большинстве конфигураций, так как они выполняют основные функции безопасности, такие как аутентификация, авторизация и защита от атак.

Вот несколько из самых важных фильтров:

1. **SecurityContextPersistenceFilter** – этот фильтр управляет контекстом безопасности для каждого запроса. Он обеспечивает, что информация о текущем пользователе (аутентификация и возможно его права) сохраняется в начале обработки запроса и очищается в конце.

2. **UsernamePasswordAuthenticationFilter** – обрабатывает формы входа в систему, где пользователи вводят имя пользователя и пароль. Этот фильтр пытается аутентифицировать запросы с учетными данными.

3. **BasicAuthenticationFilter** – предоставляет поддержку для HTTP Basic Authentication, что является простым методом передачи учетных данных через заголовки HTTP.

4. **BearerTokenAuthenticationFilter** – обрабатывает аутентификацию с использованием токенов JWT или других механизмов Bearer Token, часто используемых в REST API.

5. **LogoutFilter** – обеспечивает возможность выхода из системы. При получении определенного запроса на выход он уничтожает сессию пользователя.

6. **CsrfFilter** – защищает от атак типа Cross-Site Request Forgery (CSRF), проверяя наличие специального токена CSRF в каждом запросе на изменение данных.

7. **ExceptionTranslationFilter** – перехватывает исключения доступа (Spring Security AccessDeniedException и AuthenticationException), инициируя процесс аутентификации или информируя пользователя о нехватке прав.

8. **FilterSecurityInterceptor** – последний фильтр в цепочке, который использует конфигурацию доступа к URL или методам для принятия решения о предоставлении доступа текущему пользователю. Это ядро авторизации Spring Security.

9. **OAuth2LoginAuthenticationFilter** и **OAuth2AuthorizationRequestRedirectFilter** – обеспечивают поддержку OAuth 2.0, позволяя пользователям аутентифицироваться через внешние сервисы (например, Google, Facebook).

Эти фильтры в совокупности обеспечивают основную функциональность любой защищенной системы на основе Spring Security, обрабатывая все аспекты безопасности от аутентификации до авторизации и защиты от веб-атак.


 