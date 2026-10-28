using ec.edu.monster.Adaptadores;
using ec.edu.monster.Interfaces;
using ec.edu.monster.Modelos;
using ec.edu.monster.Servicios;
using SistemaConversorUI.Web.Components;
using SistemaConversorUI.Web.Client;




var builder = WebApplication.CreateBuilder(args);

// 1. Servicios base de Blazor
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents().AddInteractiveWebAssemblyComponents();


builder.Services.AddAntiforgery();
builder.Services.AddHttpClient(); // Requerido por los Adaptadores SOAP

// 2. SERVICIOS CORE DE LA APLICACIÓN
builder.Services.AddScoped<EstadoAplicacion>(_ => {
    var estado = new EstadoAplicacion();
    estado.ConfigurarServidor(builder.Configuration["ServidorSoap:Host"] ?? "localhost",
        builder.Configuration.GetValue<int?>("ServidorSoap:Puerto") ?? 8080,
        builder.Configuration["ServidorSoap:Contexto"] ?? "WS_ConUni_Java_SOAP_GR01");
    return estado;
});
builder.Services.AddScoped<FabricaEstrategiaBackend>();

// 3. REGISTRO DE ADAPTADORES DE AUTENTICACIÓN
builder.Services.AddScoped<IAdaptadorAutenticacion, AdaptadorAutenticacionJavaSoap>();

// 4. REGISTRO DE ADAPTADORES DE CONVERSIÓN
builder.Services.AddScoped<IAdaptadorConversion, AdaptadorConversionJavaSoap>();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseWebAssemblyDebugging();
}
else
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    app.UseHsts();
}

// La demostración LAN usa HTTP sin redirigir a un certificado de localhost.
// Fuera del entorno de desarrollo se conserva la redirección HTTPS.
if (!(app.Environment.IsDevelopment() && builder.Configuration.GetValue<bool>("RedLocal:PermitirHttp")))
{
    app.UseHttpsRedirection();
}
app.UseAntiforgery();
app.MapStaticAssets();

app.MapRazorComponents<SistemaConversorUI.Web.Components.App>()
    .AddInteractiveServerRenderMode()
    .AddInteractiveWebAssemblyRenderMode()
    .AddAdditionalAssemblies(typeof(SistemaConversorUI.Shared.Vista.Index).Assembly);

app.Run();