### README – Proyecto Mutant Detector 
API 
## Mutant Detector API   
API REST para detectar si un ADN pertenece a un mutante o a un humano.   
Basada en Spring Boot 3.5.8, Gradle, H2, Swagger y pruebas 
automatizadas con JUnit + Mockito + Jacoco. ------- 
## ✨ Objetivo del Proyecto 
El sistema determina si una secuencia de ADN (matriz NxN compuesta por 
A, T, C, G) corresponde a un **mutante**.   
Para ello analiza la matriz buscando **secuencias de cuatro letras 
iguales** en: - Horizontal → - Vertical ↓ - Diagonal ↘ - Diagonal inversa ↗ 
Además: - Guarda los resultados en BD (H2) - Permite consultar estadísticas - Valida el ADN - Maneja errores correctamente - Expone documentación automática con Swagger --- 

## Arquitectura del Proyecto 
src/ 
├─ main/java/org/mutants/mutants 
│ ├─ controller → Controladores REST (Mutant y Stats) 
│ ├─ service → Lógica de negocio (MutantService, StatsService) 
│ ├─ detector → Algoritmo detector 
│ ├─ entity → Entidad DnaRecord 
│ ├─ repository → Repositorio JPA 
│ ├─ dto → Objetos de transferencia (DnaRequest, StatsResponse) 
│ ├─ exception → Manejo global de errores (InvalidDnaException + Handler) 
│ └─ MutantsApplication → Clase principal 
│ 
└─ test/java/... → Tests unitarios + tests de controller --- 
 
## Cómo Ejecutar el Proyecto 
# 1 Compilar 

.\gradlew.bat clean build 

# 2 Ejecutar la API 
.\gradlew.bat bootRun 
o desde el JAR: 
java -jar build/libs/mutants-0.0.1-SNAPSHOT.jar 
La API queda disponible en: 
 
http://localhost:8080/ 

## Documentación Swagger 
Al iniciar la app, ingresar en: 
 
http://localhost:8080/swagger-ui/index.html 
Ahí se puede probar: 
• POST /mutant 
• GET /stats 

## Pruebas Automatizadas 
Las pruebas incluyen: 
✔ Tests del algoritmo detector 
✔ Tests de MutantService 
✔ Tests de StatsService 
✔ Tests del controlador /mutant 
✔ Tests del controlador /stats 
✔ Tests de errores (ADN inválido → 400) 
Ejecutar: 
.\gradlew.bat test 

## Cobertura con Jacoco 
Generar reporte: 
.\gradlew.bat test jacocoTestReport 
Abrir: 
build/reports/jacoco/test/html/index.html 
Cobertura esperada: 
• Detector → +90% 
• Servicios → 100% 
• Controladores → 100% 
• Excepciones → 100% 

## Endpoints 
 
# POST /mutant 
Request 
{ 
} 
"dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"] 
Respuestas posibles 
Código 
200 OK 
Significado 
Es mutante 
403 Forbidden Humano 
400 Bad Request ADN inválido 
Ejemplo de error 400: 
{ 
} 
"timestamp": "2025-11-26T13:31:20", 
"message": "La matriz de ADN debe ser NxN" 
# GET /stats 
Ejemplo: 
{ 
} 
"countMutantDna": 3, 
"countHumanDna": 5, 
"ratio": 0.6 

## Algoritmo Detector 
Se analiza cada celda de la matriz NxN en 4 direcciones: 
• Horizontal → si col <= n - 4 
• Vertical ↓ si row <= n - 4 
• Diagonal ↘ si (row <= n - 4 && col <= n - 4) 
• Diagonal ↗ si (row >= 3 && col <= n - 4) 
Si se encuentran 2 o más secuencias, el ADN se clasifica como mutante. 

## Base de Datos (H2️) 
La aplicación usa una BD en memoria con la tabla: 
dna_records 
id 
dna_hash 
is_mutant 
created_at 
Consola H2 accesible en: 
 
http://localhost:8080/h2-console 
Driver: org.h2.Driver 
URL: jdbc:h2:mem:mutantsdb 
User: sa 

## Manejo de Errores 
El sistema captura excepciones como: 
• ADN null 
• Matriz no NxN 
• Caracteres inválidos 
Mediante: 
• InvalidDnaException 
• GlobalExceptionHandler 
• ErrorResponse(timestamp, message) 

## Tecnologías Usadas 
• Java 17 
• Spring Boot 3.5.8 
• Spring Web 
• Spring Data JPA 
• H2 Database 
• Lombok 
• Swagger (springdoc-openapi 2.8.0) 
• Mockito + JUnit 5 
• Jacoco 
• Gradle 

## Autor 
Carla Fernández Gremoliche – 50894- 3k10 
UTN — Facultad Regional Mendoza 
Ingeniería en Sistemas 

## Diagrama de Secuencia 
<img width="1798" height="1701" alt="DiagramaDeSecuencia drawio" src="https://github.com/user-attachments/assets/33669071-26d7-4684-a8c6-57e11039fea2" />
