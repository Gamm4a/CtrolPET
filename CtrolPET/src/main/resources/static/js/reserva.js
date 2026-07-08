//Reserva con login

const formularioLogueado = document.querySelector(".formulario-reserva-logueado")
formularioLogueado.addEventListener('submit', async function (e) {
    e.preventDefault();

    const mascota = document.getElementById('mascota').value;
    const sucursal = document.getElementById('sucursal').value;
    const servicio = document.getElementById('servicio').value;
    const fecha = document.getElementById('fecha').value;
    const horaCombinada = document.getElementById('hora').value;
    const idEmpleado = ""
   
    if( !mascota || !sucursal || !servicio || !fecha || !horaCombinada){
         alert("Por favor de completar todos los campos")
            return;
    }
    //en este se necesita mandar el token 
      const response = await fetch('/api/reservas/dueno/{id}', {
        method: "POST",
        headers: {"Content-Type": "application/json" 
                   // "Autorization:  + token"
        },
        body: JSON.stringify({
            //se supone que aqui va el dto de la reserva pero tiene cosas que el html no
            idEmpleado: "",
            fecha: fecha,
            estado: "PENDIENTE",
            idSucursal: sucursal,
            dueno: "",
            mascota: mascota,
            //pide el objeto completo , pero en el html solo da el id
            servicios: {}

      })
    })
       const resultado = await response.json();
       const mensaje = document.getElementById('mensaje-reserva-logueado')
       if(response.ok){
          mensaje.innerHTML = "Reserva creada con éxito" ;
       }else{
          mensaje.innerHTML ="Ocurrió un error, verifica tus datos" ;
     }

    
//Reserva sin login
const formulario = document.querySelector(".formulario-reserva-sin-login")
formulario.addEventListener('submit',async function(e){
   
    const nombre = document.getElementById('nombre').value;
    const telefono = document.getElementById('telefono').value;
    const nombreMascota = document.getElementById('nombre-mascota').value;
    const tipoMascota = document.getElementById('tipo-mascota').value;
    const razaMascota = document.getElementById('raza-mascota').value;
    const sucursalSinLogin = document.getElementById('sucursalSinLogin').value;
    const servicioSinLogin = document.getElementById('servicioSinLogin').value;
    const fechaSinLogin = document.getElementById('fechaSinLogin').value;
    const horaSinLogin = document.getElementById('horaSinLogin').value;

    if(!nombre || !telefono || !nombreMascota || !tipoMascota || !razaMascota || !sucursalSinLogin
        || !servicioSinLogin || !fechaSinLogin || !horaSinLogin){
            alert("Por favor de completar todos los campos")
            return;
        }
        const response = await fetch('/api/registro', {
        method: "POST",
        headers: {"Content-Type": "application/json" },
        body: JSON.stringify({
            //son los valores del html , el dto de la reserva es diferente , habra que ver como ponerlo
            nombre:nombre,
            telefono:telefono,
            nombreMascota:nombreMascota,
            tipoMascota:tipoMascota,
            razaMascota:razaMascota,
            idSucursal:sucursalSinLogin,
            idServicio:servicioSinLogin,
            fecha:fechaSinLogin,
            hora:horaSinLogin

        })
    }) 
    
    const resultado = await response.json();
    const mensaje = document.getElementById('mensaje-reserva')
    if(response.ok){
        mensaje.innerHTML = "Reserva creada con éxito" ;
    }else{
         mensaje.innerHTML ="Ocurrió un error, verifica tus datos" ;
    }
        
})

})
