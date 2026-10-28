"use strict";
const $ = id => document.getElementById(id);
const endpoint = new URL("WSConversorUnidades", new URL("./", location.href));
$("endpoint").value = endpoint.href; $("wsdl").href = endpoint.href + "?wsdl";
let token = null, refreshing = false;
const ns = "http://schemas.xmlsoap.org/soap/envelope/";
const escapeXml = value => String(value).replace(/[<>&"']/g, c => ({'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;',"'":'&apos;'}[c]));
const field = (node, name) => node.getElementsByTagNameNS('*', name)[0]?.textContent || '';
async function soap(operation, params = {}, session = null) {
  const body = Object.entries(params).map(([k,v]) => `<${k}>${escapeXml(v)}</${k}>`).join('');
  const controller = new AbortController(), timer = setTimeout(() => controller.abort(), 8000);
  try {
    const response = await fetch(endpoint, {method:'POST', headers:{'Content-Type':'text/xml; charset=utf-8',SOAPAction:'""','X-Cliente':'PANEL'}, signal:controller.signal,
      body:`<s:Envelope xmlns:s="${ns}" xmlns:w="http://ws.controladores.monster.edu.ec/"><s:Header>${session ? `<w:token>${escapeXml(session)}</w:token>` : ''}</s:Header><s:Body><w:${operation}>${body}</w:${operation}></s:Body></s:Envelope>`});
    const xml = new DOMParser().parseFromString(await response.text(),'application/xml');
    if (!response.ok || xml.getElementsByTagNameNS(ns,'Fault').length || xml.querySelector('parsererror')) throw new Error(field(xml,'faultstring') || 'Respuesta SOAP no válida');
    const result = xml.getElementsByTagNameNS('*','return')[0];
    if (!result) throw new Error('El servicio no devolvió un resultado SOAP.');
    return result;
  } finally { clearTimeout(timer); }
}
function element(tag, text, className) { const node = document.createElement(tag); node.textContent=text; if(className) node.className=className; return node; }
async function refresh() {
  if(refreshing) return; refreshing=true; $('refresh').disabled=true;
  try {
    const state=await soap('obtenerEstado');
    $('status').textContent='● Servicio activo'; $('status').className='pill online';
    const seconds=Number(field(state,'segundosActivo'));
    $('uptime').textContent=`${Math.floor(seconds/3600)} h ${Math.floor(seconds%3600/60)} min ${seconds%60} s`;
    $('started').textContent='Inicio: '+new Date(field(state,'inicio')).toLocaleString('es-EC');
    $('sessions').textContent=field(state,'sesiones'); $('conversions').textContent=field(state,'conversiones'); $('requests').textContent=field(state,'peticiones');
    $('clients').textContent=Array.from(state.getElementsByTagNameNS('*','porCliente')).map(c=>field(c,'nombre')+': '+field(c,'cantidad')).join(' · ');
    const magnitudes=Array.from(state.getElementsByTagNameNS('*','porMagnitud')), max=Math.max(1,...magnitudes.map(m=>Number(field(m,'cantidad'))));
    $('bars').replaceChildren(...magnitudes.map(m=>{ const bar=element('div','','bar'), title=element('div','','bar-title'), track=element('div','','track'), fill=element('div','','fill'); title.append(element('span',field(m,'nombre')),element('b',field(m,'cantidad'))); fill.style.width=(Number(field(m,'cantidad'))/max*100)+'%'; track.append(fill);bar.append(title,track);return bar;}));
    const events=Array.from(state.getElementsByTagNameNS('*','eventos'));
    $('events').replaceChildren(...events.map(e=>{const li=element('li','');li.append(element('time',new Date(field(e,'hora')).toLocaleTimeString('es-EC')),element('span',field(e,'cliente'),'badge'),element('strong',field(e,'operacion')),element('span',field(e,'exito')==='true'?'Correcto':'Error',field(e,'exito')==='true'?'success':'failed'));return li;}));
    if(!events.length) $('events').append(element('li','Esperando peticiones de los clientes…'));
    $('error').hidden=true; $('checked').textContent='Actualizado: '+new Date().toLocaleTimeString('es-EC');
  } catch(error) { $('status').textContent='● Sin respuesta'; $('status').className='pill offline'; $('error').textContent='No se pudo actualizar. Los datos visibles corresponden a la última respuesta. '+(error.name==='AbortError'?'Tiempo de espera agotado.':error.message);$('error').hidden=false; }
  finally { refreshing=false; $('refresh').disabled=false; }
}
const units={Longitud:['MILIMETRO','CENTIMETRO','METRO','KILOMETRO','YARDA'],Masa:['MILIGRAMO','GRAMO','KILOGRAMO','TONELADA','ONZA'],Temperatura:['CELSIUS','FAHRENHEIT','KELVIN','RANKINE']};
function fillUnits(){ for(const id of ['from','to']) $(id).replaceChildren(...units[$('magnitude').value].map(u=>{const option=element('option',u);option.value=u;return option;}));$('to').selectedIndex=1; }
$('magnitude').addEventListener('change',fillUnits); fillUnits();
$('login-form').addEventListener('submit',async e=>{e.preventDefault();$('login').disabled=true;try{token=(await soap('login',{usuario:$('user').value,contrasenia:$('password').value})).textContent;$('password').value='';$('login-form').hidden=true;$('conversion-form').hidden=false;$('logout').hidden=false;$('result').textContent='Sesión iniciada. Ya puedes convertir.';}catch(error){$('result').textContent=error.message;}finally{$('login').disabled=false;refresh();}});
$('logout').addEventListener('click',async()=>{$('logout').disabled=true;try{await soap('cerrarSesion',{token},token);token=null;$('login-form').hidden=false;$('conversion-form').hidden=true;$('logout').hidden=true;$('result').textContent='Sesión cerrada.';}catch(error){$('result').textContent=error.message;}finally{$('logout').disabled=false;refresh();}});
$('conversion-form').addEventListener('submit',async e=>{e.preventDefault();$('convert').disabled=true;try{const value=Number($('value').value);if(!Number.isFinite(value)) throw new Error('Ingresa un número finito.');const result=await soap('convertir'+$('magnitude').value,{valor:value,unidadInicial:$('from').value,unidadFinal:$('to').value},token);$('result').textContent=`${value} ${$('from').value} = ${result.textContent} ${$('to').value}`;}catch(error){$('result').textContent=error.message;}finally{$('convert').disabled=false;refresh();}});
$('copy').addEventListener('click',async()=>{try{await navigator.clipboard.writeText(endpoint.href);$('copy-result').textContent='Copiado.';}catch{$('endpoint').select();$('copy-result').textContent='Usa Ctrl+C para copiar.';}});
$('refresh').addEventListener('click',refresh); refresh(); setInterval(()=>{if(!document.hidden) refresh();},3000);
