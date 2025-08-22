package viajefeliz;

public class Alquiler extends ServicioSimple {
	@SuppressWarnings("unused")
	private String pais;
	@SuppressWarnings("unused")
	private String ciudad;
	private double garantia;
	private String fechaDevolucion;

	Alquiler(int codServicio, double costoBase, String fechaInicio, int cantidad, String pais, 
			String ciudad, double garantia, String fechaDevolucion) {
		super(codServicio, costoBase, fechaInicio, cantidad);
		
		if (cantidad > 10) {
			throw new RuntimeException("No se pueden alquilar vehículos para más de diez personas.");
		}
		
		this.pais = pais;	
		this.ciudad = ciudad;
		this.garantia = garantia;
		this.fechaDevolucion = fechaDevolucion;
	}

	/*
	 * El alquiler del transporte se registra con país y ciudad de alquiler, la fecha de salida y 
	 * devolución del automóvil, costo por día de alquiler e importe a depositar como garantía. El 
	 * costo del transporte variará si son 4 o más personas con un límite de 10 personas por alquiler 
	 * de un transporte.
	 */
	
	@Override
	public double calcularCosto() {
		int cantDias = diasEntre(this.fechaInicio, this.fechaDevolucion);
		if (this.cantidad <= 4) {
			return this.costoBase * cantDias  + this.garantia;
		} else if (this.cantidad <= 8) {
			return (this.costoBase * cantDias + this.garantia) * 2;
		} else { // para 9 o 10 personas
			return (this.costoBase * cantDias + this.garantia) * 3;
		}
	}

	@Override
	public String toStringTipo() {
		return "Alquiler";
	}

}
