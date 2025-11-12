# RESUMEN: SEGURIDAD Y CRIPTOGRAFÍA (UF1)

## 1. CRIPTOGRAFÍA

### 1.1 Introducción
La criptografía es la ciencia que protege la información mediante técnicas de cifrado, garantizando confidencialidad, integridad, autenticidad y no repudio. Transforma datos legibles (texto plano) en datos ilegibles (texto cifrado) mediante algoritmos matemáticos.

**Objetivos principales:**
- Confidencialidad: solo destinatarios autorizados acceden a la información
- Integridad: detectar modificaciones no autorizadas
- Autenticidad: verificar identidad del emisor
- No repudio: el emisor no puede negar haber enviado el mensaje

### 1.2 Hash
Es una función matemática que transforma datos de cualquier tamaño en una cadena de longitud fija. Es unidireccional (irreversible).

**Características:**
- Determinista: mismo input genera mismo output
- Rápido de calcular
- Efecto avalancha: pequeños cambios generan hash completamente diferente
- Resistente a colisiones: difícil encontrar dos inputs con el mismo hash
- Unidireccional: imposible recuperar el dato original del hash

**Algoritmos comunes:**
- MD5 (128 bits) - obsoleto, vulnerable
- SHA-1 (160 bits) - obsoleto
- SHA-256 (256 bits) - seguro, recomendado
- SHA-512 (512 bits) - muy seguro
- bcrypt, scrypt, Argon2 - especializados para contraseñas

**Usos:**
- Almacenamiento de contraseñas
- Verificación de integridad de archivos
- Firma digital
- Blockchain

### 1.3 Huella Digital (Digital Fingerprint)
Es el resultado de aplicar una función hash a un documento o archivo. Sirve como identificador único que permite verificar que el contenido no ha sido alterado.

**Aplicaciones:**
- Verificar descargas de software
- Certificados digitales
- Control de versiones
- Detección de duplicados

### 1.4 Ejemplo de Criptografía
```
Texto plano: "HOLA MUNDO"
Cifrado César (desplazamiento 3): "KROD PXQGR"
Hash SHA-256: "a3c7e..."
```

---

## 2. CRIPTOGRAFÍA SIMÉTRICA

### 2.1 Introducción
Usa la **misma clave** para cifrar y descifrar. Es rápida y eficiente para grandes volúmenes de datos.

**Ventajas:**
- Muy rápida
- Menor carga computacional
- Ideal para cifrar archivos grandes

**Desventajas:**
- Distribución segura de la clave
- Necesita una clave por cada par de comunicación
- Si se compromete la clave, se compromete toda la comunicación

### 2.2 AES (Advanced Encryption Standard)
Estándar actual de cifrado simétrico adoptado por el gobierno de EE.UU. en 2001.

**Características:**
- Longitudes de clave: 128, 192 o 256 bits
- Cifrado por bloques de 128 bits
- Muy seguro y eficiente
- Usado en TLS/SSL, VPNs, WiFi (WPA2/WPA3)

**Modos de operación:**
- ECB (Electronic Codebook) - no recomendado
- CBC (Cipher Block Chaining)
- GCM (Galois/Counter Mode) - recomendado
- CTR (Counter)

### 2.3 DES (Data Encryption Standard)
Algoritmo histórico de cifrado simétrico.

**Características:**
- Clave de 56 bits (obsoleta, vulnerable a fuerza bruta)
- Bloques de 64 bits
- Reemplazado por 3DES (Triple DES) como solución temporal
- Actualmente obsoleto, reemplazado por AES

---

## 3. CRIPTOGRAFÍA ASIMÉTRICA

### 3.1 Introducción
Utiliza un **par de claves**: pública (se comparte) y privada (se mantiene secreta).

**Principio:**
- Cifrado con clave pública → descifrado con clave privada
- Cifrado con clave privada → descifrado con clave pública (firma digital)

**Ventajas:**
- No requiere canal seguro para intercambio de claves
- Facilita firma digital y autenticación
- Escalable

**Desventajas:**
- Más lenta que la criptografía simétrica
- Claves más largas
- Mayor carga computacional

### 3.2 RSA (Rivest-Shamir-Adleman)
Algoritmo asimétrico más utilizado, basado en la factorización de números primos grandes.

**Características:**
- Longitudes de clave: 1024, 2048, 4096 bits (2048 bits mínimo recomendado)
- Seguridad basada en dificultad de factorizar números grandes
- Usado en SSL/TLS, SSH, PGP, firma digital

**Proceso:**
1. Generación de claves: dos números primos grandes p y q
2. Cifrado: mensaje^e mod n
3. Descifrado: cifrado^d mod n

### 3.3 Clave Pública - Clave Simétrica (Cifrado Híbrido)
Combina lo mejor de ambos mundos:

1. Se usa **criptografía asimétrica** para intercambiar de forma segura una clave simétrica
2. Se usa **criptografía simétrica** (más rápida) para cifrar los datos

**Ejemplo: TLS/HTTPS**
- RSA o ECDH para intercambio de claves
- AES para cifrar la comunicación

### 3.4 Firma Digital
Mecanismo que garantiza autenticidad e integridad de un mensaje.

**Proceso:**
1. Se calcula el hash del mensaje
2. El hash se cifra con la clave privada del emisor (esto es la firma)
3. Se envía el mensaje + firma
4. El receptor descifra la firma con la clave pública del emisor
5. Se compara el hash descifrado con el hash del mensaje recibido

**Garantiza:**
- Autenticidad: solo el dueño de la clave privada pudo firmar
- Integridad: cualquier modificación invalida la firma
- No repudio: el emisor no puede negar haberlo firmado

### 3.5 ECDSA (Elliptic Curve Digital Signature Algorithm)
Algoritmo de firma digital basado en curvas elípticas.

**Ventajas sobre RSA:**
- Claves más cortas con igual seguridad (256 bits ECDSA ≈ 3072 bits RSA)
- Más rápido
- Menor consumo de recursos

**Usos:**
- Bitcoin y criptomonedas
- TLS 1.3
- SSH moderno
- Dispositivos móviles y IoT

---

## 4. SPRING SECURITY

### 4.1 Introducción
Framework de seguridad para aplicaciones Spring que proporciona autenticación y autorización.

**Características principales:**
- Protección contra ataques comunes (CSRF, XSS, Session Fixation)
- Integración con múltiples mecanismos de autenticación
- Control de acceso basado en roles
- Soporte para OAuth2, JWT, LDAP, etc.

**Dependencia Maven:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 4.2 Permitir Rutas
Configuración de qué endpoints son públicos y cuáles requieren autenticación.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**", "/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

**Métodos principales:**
- `permitAll()`: acceso sin autenticación
- `authenticated()`: requiere estar autenticado
- `hasRole()`: requiere rol específico
- `hasAuthority()`: requiere autoridad específica

### 4.3 Configuración de Roles
Los roles definen qué puede hacer cada usuario.

```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails user = User.builder()
        .username("user")
        .password(passwordEncoder().encode("password"))
        .roles("USER")
        .build();
    
    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("admin"))
        .roles("ADMIN", "USER")
        .build();
    
    return new InMemoryUserDetailsManager(user, admin);
}
```

**Jerarquía de roles:**
- ROLE_USER: usuario básico
- ROLE_ADMIN: administrador
- ROLE_MODERATOR: moderador

### 4.4 OAuth2
Protocolo de autorización que permite acceso delegado sin compartir contraseñas.

**Flujo básico:**
1. Usuario solicita acceso a una aplicación
2. Aplicación redirige al proveedor OAuth2 (Google, GitHub, etc.)
3. Usuario se autentica y autoriza
4. Proveedor devuelve token de acceso
5. Aplicación usa el token para acceder a recursos

**Roles:**
- Resource Owner: usuario
- Client: aplicación que solicita acceso
- Authorization Server: emite tokens
- Resource Server: protege los recursos

**Configuración en Spring:**
```java
@Configuration
public class OAuth2Config {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.oauth2Login();
        return http.build();
    }
}
```

### 4.5 API REST Token (JWT)
JWT (JSON Web Token) es un estándar para crear tokens de acceso.

**Estructura JWT:**
```
header.payload.signature
```

- **Header**: tipo de token y algoritmo
- **Payload**: datos del usuario (claims)
- **Signature**: firma para verificar integridad

**Ventajas:**
- Stateless: no requiere almacenar sesiones en servidor
- Autocontenido: incluye información del usuario
- Portable: funciona entre diferentes dominios

### 4.6 Configuración JWT
```java
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hora
        
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

---

## 5. AUTHENTICATION TOKEN

### 5.1 API REST Token (Hibernate)
Implementación de autenticación basada en tokens con persistencia en base de datos usando Hibernate.

**Entidades:**
```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
}

@Entity
public class Token {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private LocalDateTime expiryDate;
    
    @ManyToOne
    private User user;
}
```

**Flujo:**
1. Login: validar credenciales y generar token
2. Guardar token en BD con fecha de expiración
3. En cada petición, validar token desde BD
4. Logout: invalidar/eliminar token

### 5.2 API REST Token
Implementación general de autenticación con tokens.

**Endpoints típicos:**

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // Validar credenciales
        // Generar token
        // Retornar token
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Authorization") String token) {
        // Validar refresh token
        // Generar nuevo access token
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        // Invalidar token
    }
}
```

**Filtro de autenticación:**
```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) {
        String token = extractToken(request);
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        filterChain.doFilter(request, response);
    }
}
```

---

## COMPARATIVA RÁPIDA

| Aspecto | Simétrica | Asimétrica |
|---------|-----------|------------|
| Claves | 1 clave compartida | Par de claves (pública/privada) |
| Velocidad | Muy rápida | Lenta |
| Longitud clave | 128-256 bits | 2048-4096 bits |
| Uso principal | Cifrado de datos | Intercambio de claves, firma |
| Ejemplos | AES, DES | RSA, ECDSA |

| Hash | Cifrado |
|------|---------|
| Unidireccional | Bidireccional |
| Longitud fija | Longitud variable |
| No usa clave | Usa clave(s) |
| Verificar integridad | Proteger confidencialidad |

---

## CONCEPTOS CLAVE PARA RECORDAR

1. **Hash ≠ Cifrado**: hash es irreversible, cifrado es reversible
2. **JWT es stateless**: no requiere almacenar sesiones
3. **Cifrado híbrido**: RSA para clave + AES para datos
4. **Firma digital**: hash + cifrado con clave privada
5. **Spring Security**: protección por defecto contra ataques comunes
6. **OAuth2**: autorización delegada, NO autenticación directa
7. **ECDSA > RSA**: misma seguridad con claves más cortas
8. **Tokens**: deben tener expiración y mecanismo de renovación