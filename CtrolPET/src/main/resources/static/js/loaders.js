
export async function cargarServicios(idSelect) {
    const selectElement = document.getElementById(idSelect);
    if (!selectElement) return;

    try {
        const response = await fetch("/api/servicios");
        const servicios = await response.json();

        selectElement.innerHTML = '<option value="">Selecciona un servicio...</option>';
        servicios.forEach(s => {
            selectElement.innerHTML += `<option value="${s.idServicio}">${s.tipo}</option>`;
        });
    } catch (error) {
        console.error("Error al cargar servicios:", error);
    }
}

export async function cargarSucursales(idSelect) {
    const selectElement = document.getElementById(idSelect);
    if (!selectElement) return;

    try {
        const response = await fetch("/api/sucursales");
        const sucursales = await response.json();

        selectElement.innerHTML = '<option value="">Selecciona una sucursal...</option>';
        sucursales.forEach(s => {
            selectElement.innerHTML += `<option value="${s.idSucursal}">${s.nombre}</option>`;
        });
    } catch (error) {
        console.error("Error al cargar sucursales:", error);
    }
}