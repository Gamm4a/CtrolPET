const BASE_URL = "/api";

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

export async function obtenerPerfil(idDueno, token) {
    try {
        const response = await fetch(`/api/dueno/${idDueno}`, {
            method: "GET",
            headers: { "Authorization": "Bearer " + token }
        });
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
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(datosReserva)
        });
        return await response.json();
    } catch(error) {
        return { error: true };
    }
}