# SpaceNews - Aplicación de Noticias Espaciales


## Descripción

SpaceNews es una aplicación Android que proporciona las últimas noticias e información sobre el espacio, misiones espaciales, descubrimientos astronómicos y avances tecnológicos en la exploración espacial. La aplicación consume una API REST para obtener artículos actualizados sobre el espacio y los presenta en una interfaz de usuario intuitiva y atractiva.

## Características

- **Listado de Noticias Paginado**: Visualización eficiente de artículos con carga paginada.
- **Búsqueda de Artículos**: Filtrado de noticias por términos de búsqueda.
- **Detalles de Artículos**: Vista detallada de cada noticia con información completa.
- **Experiencia de Usuario Fluida**: Transiciones suaves y estados de carga informativos.

## Arquitectura

La aplicación está construida siguiendo los principios de Clean Architecture y el patrón MVVM (Model-View-ViewModel), lo que proporciona una separación clara de responsabilidades y facilita el mantenimiento y las pruebas.

### Capas de la Arquitectura

- **Presentación (UI)**
  - Fragments y Activities
  - ViewModels
  - Adapters

- **Dominio**
  - Casos de Uso (UseCases)
  - Modelos de Dominio

- **Datos**
  - Repositorios
  - Fuentes de Datos (API)
  - Modelos de Datos

### Tecnologías y Bibliotecas

- **Kotlin**: Lenguaje de programación principal.
- **Jetpack Components**:
  - ViewModel
  - LiveData
  - Navigation Component
  - Paging 3: Para la carga paginada de artículos.
  - ViewBinding: Para interactuar con las vistas de manera segura.

- **Coroutines**: Para operaciones asíncronas y manejo de concurrencia.
- **Flow**: Para flujos de datos reactivos.
- **Dagger Hilt**: Para la inyección de dependencias.
- **Retrofit**: Para las llamadas a la API REST.
- **OkHttp**: Cliente HTTP para Retrofit.
- **Coil**: Para la carga eficiente de imágenes.
- **Jetpack Compose**: Para algunas vistas modernas (implementación parcial).

## Implementación de Paginación

La aplicación utiliza la biblioteca Paging 3 de Android Jetpack para implementar la carga paginada de artículos, lo que mejora significativamente el rendimiento y la experiencia del usuario al:

- Cargar solo los datos necesarios cuando se requieren.
- Manejar automáticamente los estados de carga y error.
- Proporcionar una experiencia de desplazamiento fluida.
- Optimizar el uso de recursos del dispositivo.

## Requisitos

- Android 8.0 (API 26) o superior
- Conexión a Internet para obtener los datos más recientes

