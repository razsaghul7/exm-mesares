# Microservicio de Órdenes

Este es el microservicio que maneja las órdenes de compra para mi proyecto de e-commerce del parcial. Se comunica con el microservicio de productos para verificar stock.

## Lo que hace este microservicio

- Crear órdenes con varios productos a la vez
- Ver detalles de las órdenes
- Buscar órdenes por cliente o estado
- Cambiar el estado de las órdenes (pendiente, enviada, entregada, etc.)

## Tecnologías que usé

- Spring Boot 3.x
- JPA para la base de datos
- MySQL
- OpenFeign para llamar al otro microservicio
- Circuit Breaker con Resilience4j
- Validaciones
- Lombok para ahorrar código

## Cómo correr el microservicio

1. Necesitas Java 17 y Maven
2. La configuración de MySQL está en application.properties (usuario: root, contraseña: root)
3. IMPORTANTE: Primero debes iniciar el microservicio de Productos
4. Luego ejecutas:

```bash
cd ms-orders
mvn spring-boot:run
```

Se inicia en el puerto 8082.

## Endpoints disponibles

### Órdenes

- `POST /api/orders` - Crear orden nueva
- `GET /api/orders` - Ver todas las órdenes (con paginación)
- `GET /api/orders/{id}` - Ver una orden específica
- `GET /api/orders/search?customer=name` - Buscar por cliente
- `GET /api/orders/search?status=PENDING` - Filtrar por estado
- `GET /api/orders/search?customer=name&status=PENDING` - Filtrar por cliente y estado
- `PATCH /api/orders/{id}/status?status=SHIPPED` - Cambiar estado de una orden

## Patrones de Diseño que implementé

### Patrón Builder

Usé el patrón Builder con la anotación `@Builder` de Lombok en las clases Order y los DTOs. Así es más fácil crear objetos complejos.

Ejemplo:

```java
Order order = Order.builder()
    .customer("Juan Pérez")
    .date(LocalDateTime.now())
    .status(OrderStatus.PENDING)
    .total(new BigDecimal("1299.99"))
    .build();
```

### Patrón Circuit Breaker

Implementé Circuit Breaker en `OrderServiceImpl` con Resilience4j. Lo usé en el método `createOrder` para que no falle todo el sistema si el microservicio de Productos no responde.

Si el microservicio de Productos falla muchas veces, el circuito se abre y llama al método `createOrderFallback` para que la aplicación siga funcionando. 