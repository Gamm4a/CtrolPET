
async function cargarPerfil(params) {
    //se supone que tienes que verificar si existe el token para poder ver tu perfil
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = "/login" //si no, que se rediriga al login?
        return;
    }
    const nombre = document.getElementById('nombre-perfil');
    const correo = document.getElementById('correo-perfil');
    const telefono = document.getElementById('telefono-perfil');

    const response = await fetch('/api/perfil/{id}', {
        method: "GET",
       headers: { "Authorization": "Bearer " + token }
    });
   
    const resultado = await response.json();
    nombre.value = resultado.nombre;
    correo.value = resultado.correo;
    telefono.value = resultado.telefono;
   
    const mascotaSeleccionada = document.getElementById('mascota-select');

    resultado.mascotas.forEach(function(mascota) {
        const element = document.createElement('option');
        element.innerHTML = mascotaSeleccionada.nombre;
        mascotas.appendChild(element);
    });
}