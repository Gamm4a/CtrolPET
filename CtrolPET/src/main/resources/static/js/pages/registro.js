document.getElementById('btn-enviar-registro').addEventListener('click', async function (e) {
    e.preventDefault();

    // Paso 1
    const nombre = document.getElementById('nombre').value;
    const apellidoPaterno = document.getElementById('apellidoPaterno').value;
    const apellidoMaterno = document.getElementById('apellidoMaterno').value;
    const telefono = document.getElementById('telefono').value;
    const correo = document.getElementById('correo').value;
    const contrasenia = document.getElementById('contrasenia').value;
    const fechaNacimiento = document.getElementById('fechaNacimiento').value;

    // Paso 2 
    const calle = document.getElementById('calle').value;
    const colonia = document.getElementById('colonia').value;
    const ciudad = document.getElementById('ciudad').value;
    const estado = document.getElementById('estado').value;
    const codigoPostal = document.getElementById('codigoPostal').value;
    const numeroCasa = document.getElementById('numeroCasa').value;

    // Paso 3 
    const nombreMascota = document.getElementById('nombre-mascota').value;
    const tipoMascota = document.getElementById('tipo-mascota').value;
    const razaMascota = document.getElementById('raza-mascota').value;
    const edadMascota = document.getElementById('fechaNacimientoMascota').value;


    if (!nombre || !apellidoPaterno || !apellidoMaterno || !telefono || !correo || !contrasenia || !fechaNacimiento) {
        alert('Por favor completa todos los campos del Paso 1');
        document.getElementById('switch-paso1').checked = true;
        return;
    }

    if (!calle || !colonia || !ciudad || !estado || !codigoPostal) {
        alert('Por favor completa todos los campos del Paso 2 (Dirección)');
        document.getElementById('switch-paso2').checked = true;
        return;
    }

    if (!nombreMascota || !tipoMascota || !razaMascota || !edadMascota) {
        alert('Por favor completa todos los campos del Paso 3 (Mascota)');
        document.getElementById('switch-paso3').checked = true;
        return;
    }

    const response = await fetch('/api/registro', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            duenoDTO: {
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                correo,
                contrasenia,
                fechaNacimiento: fechaNacimiento ? fechaNacimiento + "T00:00:00Z" : null,
                telefono,
                mascotas: [{
                    nombre: nombreMascota,
                    especie: tipoMascota,
                    raza: razaMascota,
                    fechaNacimiento: edadMascota ? edadMascota + "T00:00:00Z" : null,
                    fotoUrl: ponerFoto(tipoMascota)
                }],
                direccion: {
                    calle,
                    colonia,
                    ciudad,
                    estado,
                    codigoPostal,
                    numeroCasa: numeroCasa
                }
            }
        })
    });

    if (response.ok) {
        document.getElementById('switch-paso5').checked = true;
    } else {
        const mensaje = document.getElementById('mensaje-registro');
        mensaje.innerHTML = "Ocurrió un error, verifica tus datos";
    }
});



function ponerFoto(especie){
    let fotoUrl;
    switch (especie) {
        case 'Perro':
            fotoUrl = "https://res.cloudinary.com/drsldzlnp/image/upload/v1783920205/ymz5xw3drltbkbm2fko4.png"
            break;
        case 'Gato':
            fotoUrl = "https://res.cloudinary.com/drsldzlnp/image/upload/v1783920192/hkulag3kx6vslcd1ci7n.png"
            break;
        case 'Roedor':
            fotoUrl = "https://res.cloudinary.com/drsldzlnp/image/upload/v1783920217/g7m5miju67c5fojzyxyx.png"
            break;
        case 'Ave':
            fotoUrl = "https://res.cloudinary.com/drsldzlnp/image/upload/v1783920237/xknjatwvsfijkqqfb026.png"
            break;

    }

    return fotoUrl;

}