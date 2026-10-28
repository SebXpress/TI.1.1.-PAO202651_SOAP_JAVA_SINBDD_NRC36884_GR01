#Requires -RunAsAdministrator
$ErrorActionPreference = 'Stop'
# Solo la subred local y el perfil privado; no expone la consola administrativa de Payara.
foreach ($puerto in @(5293, 8080)) {
    $nombre = "MonsterSOAP-LAN-TCP-$puerto"
    if (Get-NetFirewallRule -Name $nombre -ErrorAction SilentlyContinue) {
        Set-NetFirewallRule -Name $nombre -Enabled True -Direction Inbound -Action Allow -Profile Private -RemoteAddress LocalSubnet
    } else {
        New-NetFirewallRule -Name $nombre -DisplayName "Monster SOAP LAN TCP $puerto" -Enabled True -Direction Inbound -Action Allow -Protocol TCP -LocalPort $puerto -Profile Private -RemoteAddress LocalSubnet | Out-Null
    }
    Write-Host "Permitido TCP $puerto desde la subred local en redes privadas."
}
Write-Host 'Configuracion terminada. No es necesario desactivar el firewall.'
