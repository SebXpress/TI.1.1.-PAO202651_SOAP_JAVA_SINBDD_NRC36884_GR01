# Servidor visual SOAP y conexión por red local

El servidor conserva JAX-WS, XML, WSDL y el token en el encabezado SOAP. El panel consulta la operación SOAP `obtenerEstado`; no incorpora una API REST. Las métricas se guardan en memoria y se reinician al volver a desplegar.

## Ejecutar el servidor

1. Abre `WS_ConUni_Java_SOAP_GR01` en NetBeans con JDK 21 y Payara 7 (probado), o un servidor compatible con Jakarta y JAX-WS 4.
2. Compila con `mvn package` y despliega `target/WS_ConUni_Java_SOAP_GR01.war`. El contexto debe ser `/WS_ConUni_Java_SOAP_GR01`.
3. Abre el panel: `http://localhost:8080/WS_ConUni_Java_SOAP_GR01/`.
4. El WSDL está en `http://localhost:8080/WS_ConUni_Java_SOAP_GR01/WSConversorUnidades?wsdl`.

En una instalación nueva se puede desplegar con:

```powershell
C:\payara7\bin\asadmin.bat start-domain domain1
C:\payara7\bin\asadmin.bat deploy --contextroot WS_ConUni_Java_SOAP_GR01 .\WS_ConUni_Java_SOAP_GR01\target\WS_ConUni_Java_SOAP_GR01.war
```

Si ya está desplegado desde NetBeans, usa Run para actualizar esa aplicación, evitando registrar otra aplicación con el mismo contexto. El panel no inicia ni detiene Payara: está disponible mientras el servidor está ejecutándose.

## Clientes y credenciales

Todos los clientes solicitan solamente usuario y contraseña: `monster` / `monster69` (minúsculas).

La dirección de Payara se configura fuera del formulario:

- Web: sección `ServidorSoap` de `04. CLIWEB/SistemaConversorUI.Web/SistemaConversorUI.Web/appsettings.json` (localhost por defecto porque .NET y Payara se ejecutan en el mismo equipo).
- MAUI Android/Windows: `04. CLIMOVIL/SistemaConversorUI.Maui/SistemaConversorUI.Maui/appsettings.json`. Cambia `Host` a la IP vigente de Payara antes de compilar e instalar. La configuración va incluida en la aplicación.
- Consola: variables `SOAP_HOST`, `SOAP_PORT`, `SOAP_CONTEXTO`, o propiedades JVM `soap.host`, `soap.port`, `soap.contexto`. Valores por defecto: localhost, 8080, WS_ConUni_Java_SOAP_GR01.

El dashboard muestra tiempo activo, tokens vigentes (30 minutos), conversiones, peticiones por tipo de cliente, los últimos 60 eventos y barras por magnitud. Incluye prueba SOAP de login, conversión y cierre de sesión, además de la tabla de operaciones y enlace WSDL. El logo utiliza `imagen-front.jpg` de esta carpeta, copiada a los recursos web.

## Preparar la red

1. En el servidor ejecuta `ipconfig` y usa la IPv4 del adaptador Wi-Fi o Ethernet conectado a la misma red que los clientes.
2. Payara debe escuchar en todas las interfaces (`0.0.0.0`) o en esa IP, no únicamente en `127.0.0.1`. Comprueba el listener HTTP en la configuración de Payara.
3. Si el firewall bloquea el acceso, permite TCP 8080 para la red privada. Ejemplo opcional en PowerShell como administrador:

```powershell
New-NetFirewallRule -DisplayName "SOAP Java LAN 8080" -Direction Inbound -Action Allow -Protocol TCP -LocalPort 8080 -Profile Private -RemoteAddress LocalSubnet
```

4. Desde el otro dispositivo abre `http://192.168.1.10:8080/WS_ConUni_Java_SOAP_GR01/`. Debe mostrar **En línea**. Si no abre, revisa IP, puerto, firewall y aislamiento de dispositivos de la Wi-Fi.
5. Configura el cliente con esa misma IP. `localhost` en el teléfono apunta al teléfono. En un emulador Android, `10.0.2.2` puede usarse para acceder al equipo anfitrión.

El cliente web actual usa Blazor Interactive Server: las llamadas SOAP salen del equipo que ejecuta .NET. Para abrir la propia interfaz web desde otro dispositivo, publica también el host .NET en una interfaz accesible de la LAN y permite su puerto. Esto es independiente del puerto 8080 del servicio SOAP.

## Verificación manual

Abre el panel, inicia sesión en la prueba rápida y realiza una conversión. Verifica que aumenten el contador y la barra correspondiente, y que el evento aparezca en la lista. Cierra sesión para invalidar el token. Las comprobaciones Python locales no forman parte de la entrega Git.
