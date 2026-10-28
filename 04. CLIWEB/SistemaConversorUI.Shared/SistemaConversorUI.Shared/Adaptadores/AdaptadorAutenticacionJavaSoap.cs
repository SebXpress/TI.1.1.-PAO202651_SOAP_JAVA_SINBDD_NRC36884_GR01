using System.Security;
using ec.edu.monster.Servicios;
﻿using System.Text;
using System.Xml.Linq;
using ec.edu.monster.Interfaces;
using ec.edu.monster.Modelos;

namespace ec.edu.monster.Adaptadores;

public class AdaptadorAutenticacionJavaSoap : IAdaptadorAutenticacion
{
    public TipoBackend Tipo => TipoBackend.JavaSoap;
    private readonly HttpClient _httpClient;
    private readonly EstadoAplicacion _estado;

    public AdaptadorAutenticacionJavaSoap(HttpClient httpClient, EstadoAplicacion estado)
    {
        _httpClient = httpClient;
        _estado = estado;
        _httpClient.DefaultRequestHeaders.Remove("X-Cliente");
        _httpClient.DefaultRequestHeaders.Add("X-Cliente", estado.Cliente);
    }

    public async Task<string> IniciarSesionAsync(string usuario, string contrasenia)
    {
        string soapXml = $@"<soapenv:Envelope xmlns:soapenv=""http://schemas.xmlsoap.org/soap/envelope/"" xmlns:ws=""http://ws.controladores.monster.edu.ec/"">
                               <soapenv:Header/>
                               <soapenv:Body>
                                  <ws:login>
                                     <usuario>{SecurityElement.Escape(usuario)}</usuario>
                                     <contrasenia>{SecurityElement.Escape(contrasenia)}</contrasenia>
                                  </ws:login>
                               </soapenv:Body>
                            </soapenv:Envelope>";

        var content = new StringContent(soapXml, Encoding.UTF8, "text/xml");
        var response = await _httpClient.PostAsync(_estado.UrlJavaSoap, content);

        if (!response.IsSuccessStatusCode)
            throw new Exception($"Error HTTP: {response.StatusCode}");

        string responseXml = await response.Content.ReadAsStringAsync();
        var doc = XDocument.Parse(responseXml);
        var tokenElement = doc.Descendants().FirstOrDefault(x => x.Name.LocalName == "return");

        if (tokenElement == null)
            throw new Exception("No se encontró el token en la respuesta SOAP.");

        return tokenElement.Value;
    }

    public async Task<string> CambiarContraseniaAsync(string contraseniaActual, string contraseniaNueva, string token)
    {
        string soapXml = $@"<soapenv:Envelope xmlns:soapenv=""http://schemas.xmlsoap.org/soap/envelope/"" xmlns:ws=""http://ws.controladores.monster.edu.ec/"">
                               <soapenv:Header><ws:token>{SecurityElement.Escape(token)}</ws:token></soapenv:Header>
                               <soapenv:Body>
                                  <ws:cambiarContrasenia>
                                     <contraseniaActual>{SecurityElement.Escape(contraseniaActual)}</contraseniaActual>
                                     <contraseniaNueva>{SecurityElement.Escape(contraseniaNueva)}</contraseniaNueva>
                                  </ws:cambiarContrasenia>
                               </soapenv:Body>
                            </soapenv:Envelope>";

        var request = new HttpRequestMessage(HttpMethod.Post, _estado.UrlJavaSoap)
        {
            Content = new StringContent(soapXml, Encoding.UTF8, "text/xml")
        };
        request.Headers.Add("SOAPAction", "\"\"");

        var response = await _httpClient.SendAsync(request);
        string responseXml = await response.Content.ReadAsStringAsync();

        if (!response.IsSuccessStatusCode)
        {
            var docFault = XDocument.Parse(responseXml);
            var faultString = docFault.Descendants().FirstOrDefault(x => x.Name.LocalName == "faultstring")?.Value;
            throw new Exception($"Error: {faultString ?? "No se pudo cambiar la contraseña"}");
        }

        var doc = XDocument.Parse(responseXml);
        var resultElement = doc.Descendants().FirstOrDefault(x => x.Name.LocalName == "return");
        return resultElement?.Value ?? "Contraseña actualizada correctamente";
    }
}