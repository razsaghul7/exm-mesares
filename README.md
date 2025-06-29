# Proyecto de Microservicios - E-commerce

Este es mi proyecto para el parcial. Implementé un sistema básico de e-commerce con dos microservicios usando Spring Boot:

1. **Microservicio de Productos** - Para gestionar todo lo relacionado con productos
2. **Microservicio de Órdenes** - Para manejar las órdenes de compra

## Estructura del Proyecto

```
PARCIAL 2.0/
├── ms-products/         # Microservicio de productos
├── ms-orders/           # Microservicio de órdenes
└── README.md            # Este archivo
```

## Tecnologías que usé

- Spring Boot 3.x
- JPA para la persistencia
- MySQL como base de datos
- OpenFeign para comunicación entre servicios
- Resilience4j para implementar Circuit Breaker
- Validaciones con anotaciones de Jakarta
- Lombok para reducir código repetitivo

## Cómo ejecutar el proyecto

### Requisitos

- Java 17
- Maven
- MySQL

### Pasos para ejecutar

1. Primero hay que iniciar el microservicio de Productos:

```bash
cd ms-products
mvn spring-boot:run
```

2. Después iniciamos el microservicio de Órdenes:

```bash
cd ms-orders
mvn spring-boot:run
```

El servicio de Productos corre en el puerto 8081 y el de Órdenes en el 8082.

## Endpoints de la API

### Microservicio de Productos

- `POST /api/products` - Crear producto nuevo
- `GET /api/products` - Ver todos los productos (con paginación)
- `GET /api/products/{id}` - Ver un producto por ID
- `PUT /api/products/{id}` - Actualizar un producto
- `DELETE /api/products/{id}` - Eliminar un producto
- `GET /api/products/search?name=keyword` - Buscar productos por nombre
- `GET /api/products/search?category=category` - Filtrar por categoría
- `GET /api/products/{id}/check-stock?quantity=n` - Verificar stock

### Microservicio de Órdenes

- `POST /api/orders` - Crear orden nueva
- `GET /api/orders` - Ver todas las órdenes (con paginación)
- `GET /api/orders/{id}` - Ver una orden por ID
- `GET /api/orders/search?customer=name` - Buscar órdenes por cliente
- `GET /api/orders/search?status=PENDING` - Filtrar por estado
- `GET /api/orders/search?customer=name&status=PENDING` - Filtrar por cliente y estado
- `PATCH /api/orders/{id}/status?status=SHIPPED` - Actualizar estado

## Patrones de Diseño Implementados

### Patrón Builder

Implementé el patrón Builder usando la anotación `@Builder` de Lombok en las clases de entidad y DTO. Lo usé porque facilita crear objetos complejos sin tener que usar constructores con muchos parámetros.

Ejemplo de cómo lo uso en el código:

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

Implementé Circuit Breaker con Resilience4j en ambos microservicios:

- En Productos: en el método `checkStock()` para simular una llamada externa
- En Órdenes: en el método `createOrder()` para manejar fallos cuando se comunica con el servicio de Productos

Cuando el circuito se abre (después de varios fallos), se llama a un método alternativo para evitar que los errores se propaguen.

## Pruebas

Para probar la API incluí colecciones de Postman en cada carpeta de microservicio. Solo hay que importarlas en Postman y ejecutar las peticiones.

## Notas adicionales

- Agregué validaciones para todos los campos importantes
- Implementé manejo global de excepciones
- La comunicación entre microservicios está protegida con Circuit Breaker
