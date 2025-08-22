package viajefeliz;

import java.util.Map;

public class PaquetePersonalizado extends Paquete {
	
	private boolean estaAbonado;
	@SuppressWarnings("unused")
	private String fechaPago;

	public PaquetePersonalizado(int codUnico, String fechaInicio, int cantidad) {
		super(codUnico, fechaInicio, cantidad);
	}
	
	@Override
	public double calcularCosto() {
		/*
		 * Reinicia el costoTotal y recorre la lista de servicios acumulando los costos.
		 */
		
		this.costoTotal = 0;
		
		/*
		 *  Por cada paquete:
		 *  	- Recorre sus servicios y obtiene el costo.
		 *  Por cada servSimple:
		 *  	- Obtiene el costo.
		 */
		for (Map.Entry<Integer, Servicio> s : servicios.entrySet()) {
			double costoServicioActual = s.getValue().calcularCosto();
			this.costoTotal = this.costoTotal + costoServicioActual;
		}
		
		/*
		 * Calcula los descuentos
		 */
		
		if (servicios.size() == 2) {
			this.costoTotal = this.costoTotal - (this.costoTotal * 0.05);
		} else if (servicios.size() >= 3) {
			this.costoTotal = this.costoTotal - (this.costoTotal * 0.10);
		}
		return this.costoTotal;
	}
	
	public void confirmarPago(String fecha) {
		/*
		 * Lanza excepción si fechaPago < fechaInicio
		 */
		
		if (fechaMenorQueOtra(this.fechaInicio, fecha)) {
			throw new RuntimeException("¡La fecha de pago de la contratacion: " + this.codUnico + " es menor a la fecha de inicio.");
		}
		
		this.estaAbonado = true;
		this.fechaPago = fecha;
	}
	
	public boolean estaAbonado() {
		return this.estaAbonado;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Mostrando información de paquete personalizado: " + this.codUnico + ". Lista de servicios:\n");
		for (Map.Entry<Integer, Servicio> s : servicios.entrySet()) {
			str.append(" - - -  " + s.getValue().toString());
		}
		return str.toString();
	}
	
}
