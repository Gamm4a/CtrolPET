import { obtenerPerfil } from "../api.js";

const API_URL = "http://localhost:8080/api/dueno";
const token = localStorage.getItem("token");
const idDueno = localStorage.getItem("idDueno");

if (!token || !idDueno) {
    window.location.href = "/login.html";
}

// ------------------ PERFIL ------------------
async function cargarPerfil() {
    try {
        const dueno = await obtenerPerfil(idDueno, token);

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
let listaMascotas = [];

async function cargarMascotas() {
    try {
        const response = await fetch(`${API_URL}/${idDueno}/mascotas`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Error al cargar mascotas");
        const mascotas = await response.json();
        listaMascotas = mascotas;

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

document.getElementById("mascota-select").addEventListener("change", (e) => {
    const idMascota = e.target.value;
    const mascota = listaMascotas.find(m => m.id === idMascota);
    if (mascota) mostrarMascota(mascota);
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
        <td>${c.estado === "ACTIVA" ? `<button class="btn-cancelar" data-id="${c.id}">Cancelar</button>` : ""}</td>
      `;
            tbody.appendChild(tr);
        });

        document.querySelectorAll(".btn-cancelar").forEach(btn => {
            btn.addEventListener("click", () => cancelarCita(btn.dataset.id));
        });
    } catch (error) {
        console.error(error);
        alert("No se pudieron cargar las citas");
    }
}

async function cancelarCita(idReserva) {
    try {
        const response = await fetch(`${API_URL}/${idDueno}/citas/cancelar`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ idReserva })
        });
        if (!response.ok) throw new Error("Error al cancelar cita");
        await response.json();
        alert("Cita cancelada correctamente");
        cargarCitas();
    } catch (error) {
        console.error(error);
        alert("No se pudo cancelar la cita");
    }
}

// ------------------ LOGOUT ------------------
async function cerrarSesion() {
    try {
        const response = await fetch(`${API_URL}/logout`, {
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error("Error al cerrar sesión");
        localStorage.removeItem("token");
        localStorage.removeItem("idDueno");
        alert("Sesión cerrada correctamente");
        window.location.href = "/login.html";
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
