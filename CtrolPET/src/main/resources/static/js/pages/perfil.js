
/* CODIGO QUE YA ESTABA
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
*/
const API_URL = "http://localhost:8080/api/dueno";
const token = localStorage.getItem("jwt");
const idDueno = localStorage.getItem("idDueno");

// ------------------ PERFIL ------------------
async function cargarPerfil() {
    try {
        const response = await fetch(`${API_URL}/${idDueno}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Error al cargar perfil");
        const dueno = await response.json();

        document.getElementById("nombre-perfil").value = dueno.nombre;
        document.getElementById("correo-perfil").value = dueno.correo;
        document.getElementById("telefono-perfil").value = dueno.telefono || "";
    } catch (error) {
        console.error(error);
        alert("No se pudo cargar el perfil");
    }
}

document.getElementById("btn-editar-perfil").addEventListener("click", async () => {
    const duenoActualizado = {
        nombre: document.getElementById("nombre-perfil").value,
        correo: document.getElementById("correo-perfil").value,
        telefono: document.getElementById("telefono-perfil").value
    };

    try {
        const response = await fetch(`${API_URL}/${idDueno}/editar`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(duenoActualizado)
        });
        if (!response.ok) throw new Error("Error al editar perfil");
        await response.json();
        alert("Perfil actualizado correctamente");
        cargarPerfil();
    } catch (error) {
        console.error(error);
        alert("No se pudo actualizar el perfil");
    }
});

// ------------------ MASCOTAS ------------------
async function cargarMascotas() {
    try {
        const response = await fetch(`${API_URL}/${idDueno}/mascotas`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Error al cargar mascotas");
        const mascotas = await response.json();

        const select = document.getElementById("mascota-select");
        select.innerHTML = "";
        mascotas.forEach(m => {
            const option = document.createElement("option");
            option.value = m.id;
            option.textContent = m.nombre;
            select.appendChild(option);
        });

        if (mascotas.length > 0) mostrarMascota(mascotas[0]);
    } catch (error) {
        console.error(error);
        alert("No se pudieron cargar las mascotas");
    }
}

function mostrarMascota(mascota) {
    document.getElementById("nombre-mascota-dashboard").textContent = mascota.nombre;
    document.getElementById("raza-mascota-dashboard").textContent = mascota.raza;
    document.getElementById("edad-mascota-dashboard").textContent = mascota.edad;
    document.getElementById("genero-mascota").textContent = mascota.genero;
    document.getElementById("color-mascota").textContent = mascota.color;
    document.getElementById("peso-mascota").textContent = mascota.peso;
    document.getElementById("distintivos-mascota").textContent = mascota.distintivos;
    document.getElementById("esterilizacion-mascota").textContent = mascota.esterilizado ? "Sí" : "No";
}

document.getElementById("mascota-select").addEventListener("change", async (e) => {
    const idMascota = e.target.value;
    // Aquí podrías buscar la mascota seleccionada en la lista cargada
});

// ------------------ CITAS ------------------
async function cargarCitas() {
    try {
        const response = await fetch(`${API_URL}/${idDueno}/citas`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Error al cargar citas");
        const citas = await response.json();

        const tbody = document.getElementById("tabla-citas-body");
        tbody.innerHTML = "";
        citas.forEach(c => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
        <td>${c.mascotaNombre}</td>
        <td>${c.fecha}</td>
        <td>${c.hora}</td>
        <td>${c.servicio}</td>
        <td>${c.veterinario}</td>
        <td>${c.costo}</td>
        <td>${c.estado}</td>
      `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error(error);
        alert("No se pudieron cargar las citas");
    }
}

// ------------------ LOGOUT ------------------
async function cerrarSesion() {
    try {
        const response = await fetch(`${API_URL}/logout`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Error al cerrar sesión");
        localStorage.removeItem("jwt");
        localStorage.removeItem("idDueno");
        alert("Sesión cerrada correctamente");
        window.location.href = "/index.html";
    } catch (error) {
        console.error(error);
        alert("No se pudo cerrar sesión");
    }
}

document.getElementById("btn-cerrar-sesion").addEventListener("click", cerrarSesion);
document.getElementById("btn-cerrar-sesion-2").addEventListener("click", cerrarSesion);

// ------------------ INICIALIZACIÓN ------------------
cargarPerfil();
cargarMascotas();
cargarCitas();



