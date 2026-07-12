const BASE_URL = "/api";
import AuthService from "./services/auth.service.js";

export async function registrarDueno(datos) {
    try {
        const response = await fetch(`${BASE_URL}/registro`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(datos)
        });
        return await response.json();
    } catch(error) {
        return { error: true };
    }
}

export async function crearReservaSinLogin(datosReserva) {
    try {
        const response = await fetch(`${BASE_URL}/reservas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datosReserva)
        });
        return await response.json();
    } catch(error) {
        return { error: true };
    }
}

//creo que hay que mover esta funcion, se puede usar como utils
export function calcularEdad(fechaNacimiento) {
    const hoy = new Date();
    const nacimiento = new Date(fechaNacimiento);

    let edad = hoy.getFullYear() - nacimiento.getFullYear();
    const mes = hoy.getMonth() - nacimiento.getMonth();

    if (mes < 0 || (mes === 0 && hoy.getDate() < nacimiento.getDate())) {
        edad--;
    }
    return edad;
}

export async function obtenerPerfil(idDueno, token) {
    try {

        const response = await fetch(`${BASE_URL}/dueno/${idDueno}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${AuthService.getToken()}`,
                "Content-Type": "application/json"
            }
        })
        return await response.json();
    } catch(error) {
        return { error: true };
    }
}

export async function crearReservaConLogin(idDueno, datosReserva, token) {
    try {
        const response = await fetch(`${BASE_URL}/reservas/dueno/{id}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${AuthService.getToken()}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datosReserva)
        });
        return await response.json();
    } catch (error) {
        return {error: true};
    }
}

export async function obtenerMascotas(idDueno) {
    try {
        const response = await fetch(`${BASE_URL}/dueno/${idDueno}/mascotas`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${AuthService.getToken()}`
            }
        });
        return await response.json();
    } catch (error) {
        return { error: true };
    }
}

export async function obtenerCitas(idDueno) {
    try {
        const response = await fetch(`${BASE_URL}/dueno/${idDueno}/citas`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${AuthService.getToken()}`
            }
        });
        return await response.json();
    } catch (error) {
        return { error: true };
    }
}

export async function cancelarCita(idDueno, idReserva) {
    try {
        const response = await fetch(`${BASE_URL}/dueno/${idDueno}/citas/cancelar`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${AuthService.getToken()}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ idReserva: idReserva })
        });
        return await response.json();
    } catch (error) {
        return { error: true };
    }
}


export async function editarDueno(idDueno,duenoActualizado) {
    const response = await fetch(`${BASE_URL}/dueno/${idDueno}/editar`, {
        method: "PATCH",
        headers: {
            "Authorization": `Bearer ${AuthService.getToken()}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(duenoActualizado)
    });
    if (!response.ok) throw new Error("Error al editar perfil");

    const data = await response.json();
    alert("Perfil actualizado correctamente");
    return data;
}
