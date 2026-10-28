$ErrorActionPreference = 'Stop'
$carpetaWeb = Join-Path $PSScriptRoot 'SistemaConversorUI.Web/SistemaConversorUI.Web'
$archivoPid = Join-Path $carpetaWeb 'bin/lan-web.pid'
if (!(Test-Path -LiteralPath $archivoPid)) { Write-Host 'No hay un inicio LAN registrado.'; exit }
$pidWeb = [int](Get-Content -LiteralPath $archivoPid)
$procesoWeb = Get-Process -Id $pidWeb -ErrorAction SilentlyContinue
$ejecutableWeb = [IO.Path]::GetFullPath((Join-Path $carpetaWeb 'bin/Debug/net10.0/SistemaConversorUI.Web.exe'))
if ($procesoWeb -and $procesoWeb.Path -eq $ejecutableWeb) {
    Stop-Process -Id $pidWeb
    Write-Host 'Cliente web LAN detenido.'
} elseif ($procesoWeb) {
    throw 'El PID corresponde a otro proceso; no se detuvo.'
}
Remove-Item -LiteralPath $archivoPid
