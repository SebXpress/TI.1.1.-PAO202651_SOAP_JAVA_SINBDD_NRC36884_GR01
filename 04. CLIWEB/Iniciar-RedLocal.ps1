param([switch]$SegundoPlano)
$ErrorActionPreference = 'Stop'
$proyecto = Join-Path $PSScriptRoot 'SistemaConversorUI.Web/SistemaConversorUI.Web/SistemaConversorUI.Web.csproj'
$ocupado = Get-NetTCPConnection -State Listen -LocalPort 5293 -ErrorAction SilentlyContinue
if ($ocupado) {
    throw 'El puerto 5293 ya esta ocupado. Deten el cliente web en Visual Studio antes de ejecutar este archivo.'
}
if ($SegundoPlano) {
    dotnet build $proyecto --nologo
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo compilar el cliente web.' }
    $carpetaWeb = Split-Path $proyecto
    $ejecutableWeb = Join-Path $carpetaWeb 'bin/Debug/net10.0/SistemaConversorUI.Web.exe'
    $logWeb = Join-Path $carpetaWeb 'bin/lan-web.log'
    $errorWeb = Join-Path $carpetaWeb 'bin/lan-web-error.log'
    $entornoAnterior = $env:ASPNETCORE_ENVIRONMENT
    $httpAnterior = $env:RedLocal__PermitirHttp
    try {
        $env:ASPNETCORE_ENVIRONMENT = 'Development'
        $env:RedLocal__PermitirHttp = 'true'
        $procesoWeb = Start-Process -FilePath $ejecutableWeb -WorkingDirectory $carpetaWeb -ArgumentList '--urls', 'http://0.0.0.0:5293' -WindowStyle Hidden -RedirectStandardOutput $logWeb -RedirectStandardError $errorWeb -PassThru
        $procesoWeb.Id | Set-Content (Join-Path $carpetaWeb 'bin/lan-web.pid')
        $listo = $false
        for ($intento = 0; $intento -lt 20; $intento++) {
            $procesoWeb.Refresh()
            if ($procesoWeb.HasExited) { throw "El cliente web se cerro. Revisa $errorWeb" }
            try {
                $respuestaWeb = Invoke-WebRequest 'http://127.0.0.1:5293/login' -UseBasicParsing -TimeoutSec 2
                if ($respuestaWeb.StatusCode -eq 200) { $listo = $true; break }
            } catch { Start-Sleep -Milliseconds 500 }
        }
        if (!$listo) { throw "No se pudo confirmar el inicio. Revisa $logWeb" }
        Write-Host "Cliente web activo en segundo plano (PID $($procesoWeb.Id))."
        Write-Host 'Puedes cerrar esta ventana. Para detenerlo ejecuta Detener-RedLocal.ps1.'
    } finally {
        $env:ASPNETCORE_ENVIRONMENT = $entornoAnterior
        $env:RedLocal__PermitirHttp = $httpAnterior
    }
}
Write-Host 'Enlaces para otros dispositivos (elige la IP de Wi-Fi o Ethernet de tu red):'
Get-NetIPAddress -AddressFamily IPv4 | Where-Object {
    $_.IPAddress -ne '127.0.0.1' -and $_.IPAddress -notlike '169.254.*'
} | ForEach-Object { Write-Host ("  {0}: http://{1}:5293/login" -f $_.InterfaceAlias, $_.IPAddress) }
if ($SegundoPlano) { exit 0 }
Write-Host 'Manten esta ventana abierta. Ctrl+C detiene el cliente web.'
dotnet run --project $proyecto --launch-profile RedLocal
exit $LASTEXITCODE
