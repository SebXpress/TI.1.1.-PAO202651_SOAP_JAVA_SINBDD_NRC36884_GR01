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
builder.Services.AddScoped<EstadoAplicacion>();
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

app.UseHttpsRedirection();
app.UseAntiforgery();
app.MapStaticAssets();

app.MapRazorComponents<SistemaConversorUI.Web.Components.App>()
    .AddInteractiveServerRenderMode()
    .AddInteractiveWebAssemblyRenderMode()
    .AddAdditionalAssemblies(typeof(SistemaConversorUI.Shared.Vista.Index).Assembly);

app.Run();