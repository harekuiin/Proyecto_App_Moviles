# Instrucciones para Copiar las Imágenes

El proyecto necesita las imágenes de los productos para mostrarlas correctamente. Las imágenes están en el proyecto React y deben copiarse a la carpeta `assets` del proyecto Android.

## Pasos para copiar las imágenes:

1. **Ubicación de las imágenes originales:**
   ```
   C:\Users\carva\OneDrive\Documentos\edutech\Pasteleria_Mil_Sabores\actualizado_a_react\public\img\
   ```

2. **Ubicación destino en el proyecto Android:**
   ```
   app\src\main\assets\img\
   ```

3. **Comando PowerShell para copiar todas las imágenes:**
   ```powershell
   Copy-Item -Path "C:\Users\carva\OneDrive\Documentos\edutech\Pasteleria_Mil_Sabores\actualizado_a_react\public\img\*" -Destination "app\src\main\assets\img\" -Recurse -Force
   ```

4. **O manualmente:**
   - Abre el explorador de archivos
   - Ve a: `C:\Users\carva\OneDrive\Documentos\edutech\Pasteleria_Mil_Sabores\actualizado_a_react\public\img\`
   - Selecciona todos los archivos de imagen
   - Cópialos a: `app\src\main\assets\img\` (en el proyecto Android)

## Imágenes necesarias:

Las siguientes imágenes son las que se usan en los productos:

### Imágenes principales:
- `torta_cuadrada_chocolate.jpg`
- `torta_cuadrada_frutas.png`
- `torta_circular_vainilla.jpg`
- `torta_circular_manjar.png`
- `mousse_chocolate.png`
- `tiramisu.png`
- `torta_sin_azucar_naranja.webp`
- `cheesecake_sin_azucar.jpg`
- `empanada.png`
- `tarta_santiago.webp`
- `brownie.webp`
- `pan.png`
- `torta_vegana_chocolate.png`
- `galleta_vegana_avena.jpg`
- `torta_cumpleanos.png`
- `torta_boda.webp`

### Imágenes miniaturas (opcionales, pero recomendadas):
- Todas las imágenes que empiezan con `tcc_mini_`, `tcf_mini_`, `tcv_mini_`, `tcm_mini_`, `m_mini_`, `t_mini_`, `tn_mini_`, `c_mini_`, `e_mini_`, `ts_mini_`, `b_mini_`, `p_mini_`, `tv_mini_`, `gv_mini_`, `tec_mini_`, `teb_mini_`

## Nota importante:

Después de copiar las imágenes, deberás:
1. Limpiar el proyecto en Android Studio: `Build` → `Clean Project`
2. Reconstruir el proyecto: `Build` → `Rebuild Project`
3. Ejecutar la aplicación nuevamente

Las imágenes deberían aparecer automáticamente en el catálogo de productos y en la pantalla de detalle.

