# Informe de SonarQube - Proyecto Parcial

## Datos del Proyecto

- **Nombre**: Microservicios E-commerce (Parcial 2.0)
- **Fecha**: 28 de junio de 2023
- **Versión**: 1.0

## Estado General

**Resultado**: ✅ APROBADO 

## Métricas Generales

| Métrica | Valor | Calificación |
|--------|-------|--------|
| Bugs | 0 | A |
| Vulnerabilidades | 0 | A |
| Code Smells | 3 | A |
| Cobertura | 82.5% | A |
| Código Duplicado | 1.2% | A |
| Mantenibilidad | A | A |
| Fiabilidad | A | A |
| Seguridad | A | A |

## Análisis por Microservicio

### ms-products

| Métrica | Valor | Nota |
|--------|-------|--------|
| Bugs | 0 | A |
| Vulnerabilidades | 0 | A |
| Code Smells | 2 | A |
| Cobertura | 84.3% | A |
| Código Duplicado | 0.8% | A |

### ms-orders

| Métrica | Valor | Nota |
|--------|-------|--------|
| Bugs | 0 | A |
| Vulnerabilidades | 0 | A |
| Code Smells | 1 | A |
| Cobertura | 80.7% | A |
| Código Duplicado | 1.6% | A |

## Problemas Encontrados

### Code Smells (3)

1. **Método muy largo** - El método `OrderServiceImpl.createOrder()` tiene 35 líneas cuando el máximo recomendado es 30
2. **Complejidad Cognitiva alta** - `ProductController.searchProducts()` tiene complejidad 6 (máximo recomendado: 5)
3. **Método sin usar** - `OrderServiceImpl.validateStock()` está definido pero no se usa en ninguna parte

## Cosas que podría mejorar

1. Dividir el método `createOrder()` en métodos más pequeños
2. Simplificar los if/else en `searchProducts()`
3. Borrar o usar el método `validateStock()`

## Conclusión

El proyecto está bastante bien estructurado y tiene buena calidad de código. La cobertura de pruebas es buena (más del 80%) y no hay bugs ni vulnerabilidades detectadas. Los pocos problemas encontrados son menores y fáciles de solucionar.

Para un proyecto académico, estos resultados son muy buenos y demuestran buenas prácticas de programación. Los patrones de diseño implementados (Builder y Circuit Breaker) también contribuyen a la buena calificación de mantenibilidad. 