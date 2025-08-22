package viajefeliz;

import java.util.HashMap;
import java.util.Map;

public class Paquete extends Servicio {
	protected HashMap <Integer, Servicio> servicios = new HashMap<Integer, Servicio>();
	protected double costoTotal;

	public Paquete(int codUnico, String fechaInicio, int cantidad) {
		super(codUnico, fechaInicio, cantidad);
	}

	public void agregarServicio(int codUnico, Servicio s) {
		/*
		 * Agrega un servicio a la lista.
		 */
		servicios.put(codUnico, s);
		
		/*
		 * Verfica si la cantPersonas servicio entrante es mayor que la que posee el paquete.
		 * Si lo es, lo actualiza.
		 */
		if (s.getCantidad() > this.cantidad) {
			this.cantidad = s.getCantidad();
		}
		
		/*
		 * Verfica si la fecha del servicio entrante es más reciente que la que posee el paquete.
		 * Si lo es, lo actualiza.
		 */
		
		if (this.fechaInicio == null) {
			this.fechaInicio = s.getFechaInicio();
		} else if (! fechaMenorQueOtra(this.fechaInicio, s.getFechaInicio())) {
			this.fechaInicio = s.getFechaInicio();
		}
	}
	
	public Servicio quitarServicio(int codServicio) {
		/*
		 * Elimina un servicio/paquete de la lista y lo retorna.
		 * Devuelve null si no se encontró el servicio.
		 */	
		return servicios.remove(codServicio);
	}
	
	@Override
	public double calcularCosto() {
		/*
		 * Reinicia el costoTotal y recorre la lista de servicios acumulando los costos.
		 */
		
		this.costoTotal = 0;
		
		// Inicia el recorrido.
		
		for (Map.Entry<Integer, Servicio> s : servicios.entrySet()) {
			this.costoTotal = this.costoTotal + s.getValue().calcularCosto();
		}
		return this.costoTotal;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Mostrando información de paquete: " + this.codUnico + ". Lista de servicios:\n");
		for (Map.Entry<Integer, Servicio> s : servicios.entrySet()) {
			str.append(" - - - - " + s.getValue().toString());
		}
		return str.toString();
	}
	
	@Override
	public String toStringTipo() {
		/*
		 * Recorre la lista de servicios y agrega al string el tipo de servicios
		 * que posee el paquete.
		 */
		
		// Sirve para conocer cual es el último servicio de la lista
		int cantServicios = servicios.size();

		StringBuilder str = new StringBuilder();
		for (Map.Entry<Integer, Servicio> s : servicios.entrySet()) {
			// Si es el ultimo servicio de la lista, no le agrega el punto y coma
			if (cantServicios == 1) {
				str.append(s.getValue().toStringTipo());
			} else { // Si lo es, lo agrega
				str.append(s.getValue().toStringTipo() + ", ");
			}
			cantServicios--;
		}
		
		return str.toString();
	}
}
