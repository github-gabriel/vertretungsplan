import{i as c,a as u,m,l as E,b as s,h as i,c as r,d as l,R as d,T as o,e as g}from"./utility-6HeZRMOR.js";/* empty css             */const n=["ANWESEND","ABWESEND"],a=document.getElementById("dropdown"),h=[{title:"Stundenpläne",id:"timetable-entries"},{title:"Vertretungspläne",id:"substitution-plan"}];function y(){let t=a.selectedIndex;const e=JSON.stringify({attendance:n[t]});s(function(){window.alert("Dein Status wurde erfolgreich auf "+n[t]+" geändert.")},i,"post",`${d}${o}/attendance-status`,e,{"Content-Type":"application/json"})}function T(){const t={method:"get",maxBodyLength:1/0,url:d+"/current-day",withCredentials:!0};return l.request(t)}function b(){s(function(t){r(t,document.getElementById("timetable-entries-head"),document.getElementById("timetable-entries-body"),["Stunde","Klasse","Fach","Lehrer","Tag"])},i,"get",d+o+"/timetable-entries")}function f(){s(function(t){r(t,document.getElementById("substitution-plan-head"),document.getElementById("substitution-plan-body"),["Klasse","Stunde","Art","Lehrer","Vertreter","Fach"])},i,"get",d+o+"/substitution-plan")}function p(){for(let t=0;t<n.length;t++){const e=document.createElement("option");e.text=n[t],e.value=n[t],a.appendChild(e)}a.addEventListener("change",y),T().then(t=>{const e=t.data.currentDay.charAt(0)+t.data.currentDay.slice(1).toLowerCase();document.getElementById("day").textContent="🗓️Heute ist "+e+", der "+new Date(t.data.currentDateTime).toLocaleDateString()}).catch(t=>{i(t)}),l.request({method:"get",maxBodyLength:1/0,url:d+o+"/attendance-status",withCredentials:!0}).then(t=>{const e=n.indexOf(t.data.attendance);a.value=n[e],a.selectedIndex=e}).catch(t=>{i(t)})}function C(){c([g]),u(h),p(),f(),b(),m(),document.getElementById("logout-img").addEventListener("click",E)}window.onload=C;
