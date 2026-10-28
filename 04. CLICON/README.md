# Cliente consola SOAP

El inicio solicita solamente usuario y contraseña: `monster` / `monster69`.

La conexión se configura fuera del login. En la misma computadora que Payara no necesitas cambiar nada (localhost, puerto 8080). Para otro equipo, establece antes de ejecutar el cliente:

```powershell
$env:SOAP_HOST = "10.40.73.127"
$env:SOAP_PORT = "8080"
$env:SOAP_CONTEXTO = "WS_ConUni_Java_SOAP_GR01"
```

También se admiten propiedades JVM `-Dsoap.host`, `-Dsoap.port` y `-Dsoap.contexto`. Usa la IP vigente del equipo que ejecuta Payara. El protocolo y el token permanecen en SOAP.
