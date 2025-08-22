package viajefeliz;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Cliente {
	private String dni;
	private String nombre;
	@SuppressWarnings("unused")
	private String direccion;
	HashMap<Integer, PaquetePersonalizado> listaServiciosAdquiridos = new HashMap<Integer, PaquetePersonalizado>();
	private int codContratacionActual = -1;	// -1 indica que no hay contrataciones vigentes.

	Cliente(String dni, String nombre, String direccion) {
		this.dni = dni;
		this.nombre = nombre;
		this.direccion = direccion;
	}
	
	public void iniciarContratacion(int codPaquete, PaquetePersonalizado pp) {
		/* 
		 * Verifica que no haya contrataciones en vigencia (si lo hay, en codContratacionActual debe haber
		 * un código de un paquete).
		 */
		if (codContratacionActual != -1) {
			throw new RuntimeException("Error al crear una contratación: Ya existe una en curso");
		}
		/* 
		 * Agrega un paquete donde se guardaran los servicios que se agreguen durante la contratación
		 * actual y también se guarda el codigo del paquete de la contratación vigente.
		 */
		this.codContratacionActual = codPaquete;
		listaServiciosAdquiridos.put(codPaquete, pp);
	}
	
	public void agregarServicio(int codServicio, Servicio s) {
		/*
		 * Busca el paquete personalizado de la contratacion actual y le agrega
		 *  un servicio (simple o paqPredefinido) al paquete de la contratacion actual.
		 */
		listaServiciosAdquiridos.get(codContratacionActual).agregarServicio(codServicio, s);
	}
	
	public Servicio quitarServicio(int codServicio) {
		/*
		 * Busca el servicio/paquete en la contratacion actual, lo elimina y lo trae
		 * Devuelve null si no se encontró el servicio.
		 */
		return listaServiciosAdquiridos.get(codContratacionActual).quitarServicio(codServicio);
	}
	
	public double calcularCostoPaquete(int codPaquete) {
		/*
		 * Verifica que el paquete esté en este cliente.
		 */
		if (! listaServiciosAdquiridos.containsKey(codPaquete)) {
			throw new RuntimeException("¡El paquete " + codPaquete + " no existe en este cliente!");
		}
		
		/*
		 * Realiza el cálculo del costo total del paquete pasado por parámetro.
		 */
		return listaServiciosAdquiridos.get(codPaquete).calcularCosto();
	}
	
	public double calcularCostoPaquete() {
		/*
		 * Solo funciona para calcular el costo de una contratación abierta.
		 */
		if (codContratacionActual == -1) {
			throw new RuntimeException("Error al calcular costo de contratación. No existe contratación abierta.");
		}
		/*
		 * Realiza el cálculo del costo total del paquete pasado por parámetro.
		 */
		return listaServiciosAdquiridos.get(codContratacionActual).calcularCosto();
	}
	
	public void confirmarPago(String fechaPago) {
		/*
		 * Marca como abonado el paquete actual (lanza excepcion si la fecha de pago es posterior
		 * a la fecha de inicio).
		 */
		listaServiciosAdquiridos.get(codContratacionActual).confirmarPago(fechaPago);
		
		/* 
		 * Borra el codigo del paquete de la contratación vigente para que se habilite a iniciar nuevas
		 * contrataciones.
		 */
		codContratacionActual = -1;
	}
	
	public LinkedList<Integer> listaPaquetesContratados() {
		LinkedList<Integer> historial = new LinkedList<Integer>();
		/*
		 * Recorre la lista contrataciones y añade los codigosUnicos al historial.
		 * Solo añade las contrataciones finalizadas u abonadas.
		 */
		for (Map.Entry<Integer, PaquetePersonalizado> s : listaServiciosAdquiridos.entrySet()) {
			if (s.getValue().estaAbonado()) {
				historial.add(s.getKey());
			}
		}		
		return historial;
	}
	
	public String contratacionesNoIniciadas(String fecha) {
		StringBuilder str = new StringBuilder();
		/*
		 * Recorre los paquetes contratados buscando contrataciones que aun no iniciaron
		 * desde la fecha pasada por parametro.
		 * Solo devuelve las contrataciones abonadas.
		 */
		for (Map.Entry<Integer, PaquetePersonalizado> s : listaServiciosAdquiridos.entrySet()) {
			if (s.getValue().estaAbonado() && s.getValue().noIniciadoDesdeFecha(fecha)) {
				str.append(
						this.nombre + " | " +
						s.getValue().getFechaInicio() + " | [" +
						s.getValue().toStringTipo() + "]");
			}
		}
		return str.toString();
	}
	
	public String contratacionesQueInicianEnFecha(String fecha) {
		StringBuilder str = new StringBuilder();
		/*
		 * Recorre los paquetes contratados buscando contrataciones que inicien en fecha
		 * Solo valido para contrataciones abonados o cerrados.
		 */
		for (Map.Entry<Integer, PaquetePersonalizado> s : listaServiciosAdquiridos.entrySet()) {
			if (s.getValue().estaAbonado() && s.getValue().iniciaEnFecha(fecha)) {
				str.append(s.getValue().toStringCodigo() + " - (" + this.dni + " " + this.nombre + ")");
			}
		}
		return str.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Mostrando información de cliente: " + this.dni + ", " + this.nombre +". Lista de servicios:\n");
		for (Map.Entry<Integer, PaquetePersonalizado> s : listaServiciosAdquiridos.entrySet()) {
			str.append(" - -  " + s.getValue().toString());
		}
		return str.toString();
	}
}
