# Pastelería Mil Sabores - Aplicación Móvil Android

Aplicación móvil Android desarrollada con Jetpack Compose para la pastelería "Mil Sabores", que celebra 50 años de tradición en la repostería chilena.

## 🚀 Características

- **Sistema de Autenticación**: Login y registro con validación de dominios
  - Admin: usuarios con dominio `@profesor.duoc.cl`
  - Usuarios regulares: `@duoc.cl` y `@gmail.com`
- **Catálogo de Productos**: Visualización de productos en grid con detalles
- **Base de Datos Room**: Almacenamiento local de productos y usuarios
- **Diseño Personalizado**: Colores y tipografías según la identidad de la pastelería

## 📋 Requisitos

- Android Studio Hedgehog | 2023.1.1 o superior
- JDK 11 o superior
- Android SDK 24 (mínimo) - Android 14 (API 36)
- Gradle 8.12.3

## 🛠️ Instalación

1. Clona el repositorio:
```bash
git clone <url-del-repositorio>
cd Proyecto_App_Moviles
```

2. Abre el proyecto en Android Studio

3. Android Studio sincronizará automáticamente las dependencias (Gradle Sync)

4. Espera a que termine la sincronización

5. Ejecuta la aplicación en un dispositivo o emulador

## ⚠️ Solución de Problemas

### Error del Daemon de Kotlin

Si encuentras el error "The daemon has terminated unexpectedly", sigue estos pasos:

1. **En Android Studio:**
   - Ve a `File` → `Invalidate Caches / Restart...`
   - Selecciona `Invalidate and Restart`

2. **Limpia los archivos de build:**
   ```powershell
   # En PowerShell (Windows)
   Remove-Item -Recurse -Force .gradle, build, app\build -ErrorAction SilentlyContinue
   ```

3. **Detén todos los daemons de Gradle:**
   ```powershell
   .\gradlew --stop
   ```

4. **Vuelve a sincronizar el proyecto** en Android Studio

### Si el problema persiste:

- Verifica que tengas al menos **8GB de RAM** disponible
- Asegúrate de tener **Java 11 o superior** instalado
- Verifica que Android Studio esté actualizado a la última versión

## 📱 Estructura del Proyecto

```
app/src/main/java/com/example/proyectologin006d_final/
├── data/
│   ├── dao/              # DAOs de Room
│   ├── database/         # Configuración de Room
│   ├── model/            # Modelos de datos (Producto, Usuario)
│   └── repository/       # Repositorios
├── ui/
│   ├── home/            # Pantalla principal
│   ├── login/           # Pantalla de login
│   ├── register/        # Pantalla de registro
│   ├── detalle/         # Detalle de producto
│   └── theme/           # Temas y colores
├── navigation/          # Navegación de la app
└── viewmodel/          # ViewModels
```

## 🎨 Colores y Tipografía

- **Fondo Principal**: Crema Pastel (#FFF5E1)
- **Acentos**: Rosa Suave (#FFC0CB) y Chocolate (#8B4513)
- **Texto Principal**: Marrón Oscuro (#5D4037)
- **Texto Secundario**: Gris Claro (#B0BEC5)

## 📦 Dependencias Principales

- Jetpack Compose
- Room Database 2.6.1
- Navigation Compose 2.7.7
- Material3
- Lifecycle ViewModel

## 🔐 Sistema de Autenticación

- **Dominios permitidos**: `@gmail.com`, `@duoc.cl`, `@profesor.duoc.cl`
- **Admin automático**: Usuarios con `@profesor.duoc.cl`
- Los usuarios se almacenan en Room Database

## 📝 Notas

- Los productos se inicializan automáticamente en la primera ejecución
- El carrito de compras se implementará en una fase posterior
- Las imágenes de productos están preparadas para agregarse posteriormente

## 👨‍💻 Desarrollo

Desarrollado con:
- Kotlin
- Jetpack Compose
- Room Database
- Material Design 3

