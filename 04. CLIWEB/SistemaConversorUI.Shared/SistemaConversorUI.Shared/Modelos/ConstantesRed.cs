using System;
using System.Collections.Generic;
using System.Text;

namespace ec.edu.monster.Modelos;

public static class ConstantesRed
{
    // cambiar 'localhost' por la IP local del seervidorWS
    public const string IP_WEB_SERVICES = "localhost";
    public const string UrlJavaSoap = $"http://{IP_WEB_SERVICES}:8080/WS_ConUni_Java_SOAP_GR01/WSConversorUnidades";
}