# CtrolPET

#  Sistema de Reservas y Agenda - Veterinaria (Ember´s Enterprise)

##  Introducción
Este proyecto implementa una plataforma web para la **gestión de citas en una clínica veterinaria**.  
El sistema permite a los administradores configurar la disponibilidad de servicios (consultas médicas, vacunaciones, cirugías, estética animal, etc.), mientras que los clientes pueden consultar horarios y reservar en tiempo real.

---

##  Funcionalidades Principales
- **[Catálogo de servicios](ca://s?q=Catalogo_de_servicios_veterinaria)**: consultas, vacunaciones, cirugías, estética animal.
- **[Configuración de horarios](ca://s?q=Configurar_horarios_veterinaria)**: disponibilidad por día y rango de horas.
- **[Bloqueo automático](ca://s?q=Bloqueo_de_horarios_reservados)**: evita doble reservación.
- **[Reservaciones](ca://s?q=Reservaciones_veterinaria)**: con datos del cliente, mascota, fecha y servicio.
- **[Panel administrativo](ca://s?q=Panel_administrativo_veterinaria)**: vista de agenda en lista o calendario.
- **[Portal del cliente](ca://s?q=Portal_cliente_veterinaria)**: consulta de disponibilidad y gestión de citas.
- **[Historial de citas](ca://s?q=Historial_de_reservaciones_veterinaria)**: por cliente y mascota.
- **[Filtros de búsqueda](ca://s?q=Filtros_de_busqueda_reservaciones)**: por fecha, servicio y estado.

---

##  Casos de Uso

###  Backoffice / Panel Administrativo
- **[Crear servicio](ca://s?q=Crear_servicio_veterinaria)**: registrar consulta, vacunación, cirugía, etc.
- **[Configurar horarios](ca://s?q=Configurar_horarios_veterinaria)**: definir disponibilidad semanal.
- **[Bloquear fechas](ca://s?q=Bloquear_fechas_veterinaria)**: vacaciones o mantenimiento.
- **[Ver agenda](ca://s?q=Ver_agenda_veterinaria)**: citas organizadas por día/semana.
- **[Confirmar o cancelar](ca://s?q=Confirmar_o_cancelar_reservacion)**: gestión de estados de citas.
- **[Reservación manual](ca://s?q=Reservacion_manual_veterinaria)**: registro de citas telefónicas o presenciales.
- **[Historial por cliente](ca://s?q=Historial_por_cliente_veterinaria)**: seguimiento de pacientes frecuentes.
- **[Resumen de ocupación](ca://s?q=Resumen_ocupacion_veterinaria)**: métricas de uso de servicios.

###  Storefront / Portal del Cliente
- **[Explorar servicios](ca://s?q=Explorar_servicios_veterinaria)**: descripción, duración y costo.
- **[Consultar disponibilidad](ca://s?q=Consultar_disponibilidad_veterinaria)**: ver horarios libres.
- **[Hacer reservación](ca://s?q=Hacer_reservacion_veterinaria)**: seleccionar fecha y hora.
- **[Ver mis citas](ca://s?q=Ver_mis_reservaciones_veterinaria)**: próximas y pasadas.
- **[Cancelar cita](ca://s?q=Cancelar_reservacion_veterinaria)**: liberar horario reservado.
- **[Registro e inicio de sesión](ca://s?q=Registro_e_inicio_de_sesion_veterinaria)**: cuenta vinculada al historial de mascotas.

---

##  Tecnologías Utilizadas
- **Frontend**: Html/Css  
- **Backend**: Java  
- **Base de Datos**: NoSQL (MongoDB)   

---

##  Instalación y Uso
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Gamm4a/CtrolPET.git
