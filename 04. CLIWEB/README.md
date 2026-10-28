# Cliente web y clientes SOAP desde otros equipos

`localhost` significa el equipo de quien abre el enlace. Nunca compartas un enlace con localhost con otro computador o teléfono.

## En el equipo servidor

1. Inicia Payara y despliega el servidor SOAP en el puerto 8080.
2. Detén la ejecución anterior del cliente web en Visual Studio.
3. Para dejarlo funcionando independientemente del editor, abre `Iniciar-Web-LAN.cmd` con doble clic. Compila, inicia el proceso en segundo plano y comprueba `/login`. Puedes cerrar la ventana al terminar. Después de reiniciar Windows, vuelve a ejecutarlo. Para apagarlo usa `Detener-RedLocal.ps1`. Los registros quedan en `SistemaConversorUI.Web/SistemaConversorUI.Web/bin/lan-web.log` y `lan-web-error.log`.

Alternativa en primer plano:
3. Desde la raíz del repositorio, ejecuta en PowerShell:

```powershell
& '.\04. CLIWEB\Iniciar-RedLocal.ps1'
```

El script muestra las IP disponibles y arranca el cliente web en `0.0.0.0:5293`. Mantén esa ventana abierta. También puedes seleccionar el perfil **RedLocal** en Visual Studio. Los perfiles existentes http y https permiten ahora el acceso HTTP LAN por 5293. En desarrollo, estos perfiles evitan la redirección al certificado HTTPS de localhost; en producción se conserva HTTPS.

4. Una sola vez, haz clic derecho en `Habilitar-Firewall-RedLocal.cmd` y selecciona **Ejecutar como administrador**. Crea dos reglas para TCP 5293 y 8080, solo para el perfil privado y la subred local. No desactiva el firewall ni abre el puerto administrativo 4848. La red Wi-Fi debe tener perfil privado.

## En el equipo de tu compañero

Ambos equipos deben estar conectados a la misma red, con comunicación entre dispositivos permitida.

- **Cliente web:** abre `http://IP_DEL_SERVIDOR:5293/`. No necesita instalar .NET, Java ni el proyecto; basta el navegador. El equipo servidor mantiene ejecutándose .NET y Payara.
- **Consola, Android o escritorio MAUI:** configura previamente la dirección de Payara en las variables de entorno de consola o en `appsettings.json` de MAUI; el login pide solamente usuario y contraseña.
- **Panel SOAP:** abre `http://IP_DEL_SERVIDOR:8080/WS_ConUni_Java_SOAP_GR01/`.

El login web solicita solamente usuario y contraseña (`monster` / `monster69`). La dirección de Payara está en la sección `ServidorSoap` de `SistemaConversorUI.Web/SistemaConversorUI.Web/appsettings.json`. La interfaz web actual funciona mediante Blazor Server: el navegador se conecta a .NET en 5293 y .NET llama al servicio SOAP en 8080. Los clientes nativos se conectan directamente a SOAP. Todos mantienen el protocolo SOAP para autenticación y conversiones.

No compartas `0.0.0.0`: solo indica que la aplicación escucha en las interfaces del equipo. Obtén la IP Wi-Fi o Ethernet con `ipconfig`. Puede cambiar al reconectarte a la red. Evita la IP de adaptadores virtuales o VPN si tu compañero está conectado por Wi-Fi.

## Si todavía no conecta

Ejecuta desde el computador de tu compañero:

```powershell
Test-NetConnection IP_DEL_SERVIDOR -Port 5293
Test-NetConnection IP_DEL_SERVIDOR -Port 8080
```

- Si falla 5293, verifica que el cliente web esté ejecutándose con el perfil RedLocal y que se haya aplicado la regla del firewall.
- Si abre el sitio pero falla el login, verifica IP, puerto 8080, despliegue SOAP y credenciales.
- Si ambos puertos fallan con los procesos activos y las reglas aplicadas, revisa que estén en la misma red. Algunas redes de universidad, invitados o públicas aíslan los dispositivos. En ese caso usa una red privada que permita comunicación entre equipos.
- Si abre desde el servidor usando su propia IP, eso confirma la escucha local, pero no demuestra que el firewall permita la conexión desde otro equipo. La prueba final debe hacerse desde el dispositivo de tu compañero.

## Dirección de entrada y errores de Google

Tanto `/` como `/login` muestran el inicio de sesión: `http://IP_DEL_SERVIDOR:5293/login`. Escribe el enlace en la barra de direcciones del navegador, no en el buscador, sin barras invertidas (`\`) ni comillas. Para la prueba LAN usa HTTP 5293; HTTPS 7025 corresponde al desarrollo local y no forma parte del inicio LAN.

Los errores de `play.google.com/log` corresponden a una petición a Google y no demuestran que falle el servicio SOAP. Para diagnosticar nuestra aplicación, comprueba el resultado TCP de 5293 desde el equipo cliente y el error de la navegación al enlace de arriba. Ping puede responder aunque el puerto TCP esté cerrado o no exista un proceso escuchando.
