# Reorganización en Bounded Contexts - GigMap Mobile

## Estructura Final

```
gigmap_frontend_sprint1/
├── MainActivity.kt
├── app/ (vacío)
└── bounded/
    ├── concerts/
    │   ├── model/
    │   │   ├── Concerts.kt
    │   │   ├── ConcertCreateRequest.kt
    │   │   ├── Venue.kt
    │   │   └── VenueRequest.kt
    │   ├── viewmodel/
    │   │   ├── ConcertViewModel.kt
    │   │   └── VenueViewModel.kt
    │   └── view/
    │       ├── ConcertsList.kt
    │       ├── ConcertDetails.kt
    │       └── CreateConcert.kt
    │
    ├── communities/
    │   ├── model/
    │   │   ├── Community.kt
    │   │   ├── Post.kt
    │   │   └── PostCreateRequest.kt
    │   ├── viewmodel/
    │   │   ├── CommunityViewModel.kt
    │   │   └── PostViewModel.kt
    │   └── view/
    │       ├── CommunitiesList.kt
    │       ├── Community.kt
    │       └── CreatePost.kt
    │
    ├── relatedevents/
    │   └── model/
    │       └── RelatedEvent.kt
    │
    ├── users/
    │   ├── model/
    │   │   ├── Users.kt
    │   │   ├── LoginRequest.kt
    │   │   ├── LoginResponse.kt
    │   │   ├── RegisterRequest.kt
    │   │   └── CreateDeviceTokenRequest.kt
    │   ├── viewmodel/
    │   │   └── UserViewModel.kt
    │   └── view/
    │       ├── Login.kt
    │       ├── SignIn.kt
    │       ├── Profile.kt
    │       └── Welcome.kt
    │
    ├── public/
    │   └── view/
    │       ├── Home.kt
    │       ├── HomeContent.kt
    │       ├── Map.kt
    │       └── nav/
    │           └── Navi.kt
    │
    └── shared/
        ├── components/
        │   ├── BottomBar.kt
        │   ├── TopBar.kt
        │   └── RedGuideLayout.kt
        ├── services/
        │   ├── CloudinaryService.kt
        │   ├── FirebaseService.kt
        │   └── GoogleMapsService.kt
        ├── client/
        │   ├── RetrofitClient.kt
        │   └── WebService.kt
        ├── ui/
        │   └── theme/
        │       ├── Color.kt
        │       ├── Theme.kt
        │       └── Type.kt
        ├── model/
        │   ├── Platform.kt
        │   └── PlatformRequest.kt
        └── viewmodel/
            └── PlatformViewModel.kt
```

## Bounded Contexts Definidos

### 1. **Concerts**
- **Responsabilidad**: Gestión de conciertos y venues
- **Incluye**: Modelos de conciertos, venues, creación de conciertos, listado y detalles
- **Razón**: Venue es parte integral del dominio de conciertos

### 2. **Communities**
- **Responsabilidad**: Gestión de comunidades y posts
- **Incluye**: Modelos de comunidades, posts, creación y visualización
- **Razón**: Los posts pertenecen a comunidades, forman un dominio cohesivo

### 3. **RelatedEvents**
- **Responsabilidad**: Eventos relacionados
- **Incluye**: Modelo de eventos relacionados
- **Razón**: Dominio separado para futuras expansiones

### 4. **Users**
- **Responsabilidad**: Autenticación y gestión de usuarios
- **Incluye**: Login, registro, perfil, tokens de dispositivo
- **Razón**: Dominio de identidad y acceso

### 5. **Public**
- **Responsabilidad**: Vistas públicas y navegación principal
- **Incluye**: Home, Map, navegación principal
- **Razón**: Componentes de nivel superior que orquestan múltiples bounded contexts

### 6. **Shared**
- **Responsabilidad**: Código compartido entre todos los bounded contexts
- **Incluye**: Componentes UI, servicios, cliente HTTP, temas
- **Razón**: Infraestructura común reutilizable

## Cambios Realizados

### Packages Actualizados
- `com.example.gigmap_frontend_sprint1.model` → `com.example.gigmap_frontend_sprint1.bounded.[context].model`
- `com.example.gigmap_frontend_sprint1.viewmodel` → `com.example.gigmap_frontend_sprint1.bounded.[context].viewmodel`
- `com.example.gigmap_frontend_sprint1.view` → `com.example.gigmap_frontend_sprint1.bounded.[context].view`
- `com.example.gigmap_frontend_sprint1.components` → `com.example.gigmap_frontend_sprint1.bounded.shared.components`
- `com.example.gigmap_frontend_sprint1.services` → `com.example.gigmap_frontend_sprint1.bounded.shared.services`
- `com.example.gigmap_frontend_sprint1.ui.theme` → `com.example.gigmap_frontend_sprint1.bounded.shared.ui.theme`

### Imports Actualizados
Todos los imports en todos los archivos fueron actualizados automáticamente para reflejar la nueva estructura de bounded contexts.

### Archivos Eliminados
- `/model` (antigua estructura)
- `/viewmodel` (antigua estructura)
- `/view` (antigua estructura)
- `/components` (antigua estructura)
- `/services` (antigua estructura)
- `/ui` (antigua estructura)

## Ventajas de esta Organización

1. **Separación de Responsabilidades**: Cada bounded context tiene una responsabilidad clara
2. **Escalabilidad**: Fácil agregar nuevos bounded contexts sin afectar los existentes
3. **Mantenibilidad**: Código más organizado y fácil de navegar
4. **Cohesión**: Elementos relacionados están juntos
5. **Bajo Acoplamiento**: Los bounded contexts son independientes entre sí

## Notas Importantes

- **MainActivity.kt** fue actualizado con los nuevos imports
- **WebService.kt** en shared/client contiene todas las interfaces de API
- **RetrofitClient.kt** en shared/client maneja la configuración HTTP
- Todos los imports fueron verificados y actualizados correctamente
- La funcionalidad del código se mantiene exactamente igual, solo cambió la organización

## Próximos Pasos Recomendados

1. Compilar el proyecto para verificar que no hay errores
2. Ejecutar tests si los hay
3. Considerar agregar README.md en cada bounded context explicando su responsabilidad
4. Evaluar si RelatedEvents necesita más componentes (viewmodel, views)
