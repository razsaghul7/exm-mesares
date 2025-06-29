# Microservicio de Productos

Este es el microservicio para gestionar productos de mi proyecto de e-commerce. Lo hice como parte del parcial 2.0.

## Lo que hace este microservicio

- CRUD completo de productos (crear, ver, actualizar, borrar)
- Búsqueda de productos por nombre con paginación
- Filtrado por categoría
- Verificación de stock

## Tecnologías que usé

- Spring Boot 3.x
- JPA para la base de datos
- MySQL
- Circuit Breaker con Resilience4j
- Validaciones
- Lombok para no escribir tanto código

## Cómo correr el microservicio

1. Necesitas tener Java 17 y Maven
2. La configuración de MySQL está en application.properties (usuario: root, contraseña: root)
3. Para ejecutarlo:

```bash
cd ms-products
mvn spring-boot:run
```

Se inicia en el puerto 8081.

## Endpoints disponibles

### Productos

- `POST /api/products` - Crear producto nuevo
- `GET /api/products` - Ver todos los productos (con paginación)
- `GET /api/products/{id}` - Ver un producto específico
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto
- `GET /api/products/search?name=keyword` - Buscar por nombre
- `GET /api/products/search?category=category` - Filtrar por categoría
- `GET /api/products/{id}/check-stock?quantity=n` - Verificar si hay stock suficiente

## Patrones de Diseño que implementé

### Patrón Builder

Usé el patrón Builder con la anotación `@Builder` de Lombok tanto en Product como en los DTOs. Así puedo crear objetos más fácilmente.

Ejemplo:

```java
Product product = Product.builder()
    .name("Smartphone")
    .description("Último modelo")
    .price(new BigDecimal("999.99"))
    .stock(100)
    .category("Electrónica")
    .build();
```

### Patrón Circuit Breaker

Implementé Circuit Breaker en `ProductServiceImpl` con Resilience4j. Lo usé en el método `checkStock` para simular una llamada externa que podría fallar.

Si ocurren muchos fallos, el circuito se abre y llama al método `checkStockFallback` para evitar que la aplicación se caiga. 