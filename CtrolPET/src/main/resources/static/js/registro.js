// Enviar el formulario cuando se haga clic en "Continuar" del paso 3
document.getElementById('btn-enviar-registro').addEventListener('click', async function (e) {
    e.preventDefault();
    // Validar campos requeridos de los pasos anteriores
    const form = document.getElementById('form-registro-completo');

    // Validar paso 1
    const nombre = document.getElementById('nombre').value;
    const telefono = document.getElementById('telefono').value;
    const correo = document.getElementById('correo').value;
    const contrasenia = document.getElementById('contrasenia').value;

    // Validar paso 2
    const nombreMascota = document.getElementById('nombre-mascota').value;
    const tipoMascota = document.getElementById('tipo-mascota').value;
    const razaMascota = document.getElementById('raza-mascota').value;
    const edadMascota = document.getElementById('edad-mascota').value;
    const sexoMascota = document.querySelector('input[name="sexoMascota"]:checked');
    const esterilizado = document.querySelector('input[name="esterilizadoMascota"]:checked');

    const servicio = document.getElementById('servicio').value;
    const fecha = document.getElementById('fecha').value;
    const hora = document.getElementById('hora').value;

    if (!nombre || !telefono || !correo || !contrasenia) {
        alert('Por favor completa todos los campos del Paso 1 (Tu Información)');
        document.getElementById('switch-paso1').checked = true;
        return;
    }

    if (!nombreMascota || !tipoMascota || !razaMascota || !edadMascota || !sexoMascota || !esterilizado) {
        alert('Por favor completa todos los campos del Paso 2 (Información de la mascota)');
        document.getElementById('switch-paso2').checked = true;
        return;
    }
    //la reserva pide no null estos datos pero si en el registro no quieres cita , se va mandar como null
    const reservaDTO = servicio ? {
        idSucursal: "", //no hay un select para sucursal en el html
        idServicio: servicio,
        fecha: fecha,
        hora: hora,
        estado: "PENDIENTE"
    } : null

    const response = await fetch('/api/registro', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            duenoDTO: {
                nombre: nombre,
                apellidoPaterno: "", //falta esto en el html
                apellidoMaterno: "", //falta esto en el html
                correo: correo,
                contrasenia: contrasenia,
                fechaNacimiento: "", //falta esto en el html
                telefono: telefono,
                mascotas: [{
                    nombre: nombreMascota,
                    especie: tipoMascota,//el dto pide especie pero el html es ="tipoMascota"
                    fechaNacimiento: "" //falta esto en el html
                }],
                direccion: {
                    calle: "", //falta esto en el html
                    colonia: "",//falta esto en el html
                    ciudad: "",//falta esto en el html
                    estado: "",//falta esto en el html
                    codigoPostal: "",//falta esto en el html
                    numeroCasa: ""//falta esto en el html
                }
            },
            reservaDTO
        })
    });
    const resultado = await response.json();
    //si el usuario llega hasta el paso 4 , es que el registro fue exitoso
    if (response.ok) {
        document.getElementById('switch-paso4').checked=true;
    } else {
        const mensaje = document.getElementById('mensaje-registro')
        mensaje.innerHTML = "Ocurrió un error, verifica tus datos";
    }
})