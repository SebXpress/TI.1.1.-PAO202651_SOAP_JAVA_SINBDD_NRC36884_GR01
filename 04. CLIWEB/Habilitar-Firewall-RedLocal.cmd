@echo off
net session >nul 2>&1
if errorlevel 1 (
    echo Haz clic derecho en este archivo y selecciona Ejecutar como administrador.
    pause
    exit /b 1
)
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0Habilitar-Firewall-RedLocal.ps1"
pause
