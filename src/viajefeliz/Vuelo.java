package viajefeliz;

public class Vuelo extends ServicioSimple {
	
	@SuppressWarnings("unused")
	private String pais;
	@SuppressWarnings("unused")
	private String ciudad;
	@SuppressWarnings("unused")
	private String fechaLlegada;
	private double tasa;	// En el diagrama aparece como tasaImpuestos

	public Vuelo(int codServicio, double costoBase, String fechaInicio, int cantidad, String pais, 
			String ciudad, String fechaLlegada, double tasa) {
		super(codServicio, costoBase, fechaInicio, cantidad);
		this.pais = pais;
		this.ciudad = ciudad;
		this.fechaLlegada = fechaLlegada;
		this.tasa = tasa;
	}

	/* Un servicio de vuelo es un(1) pasaje de avión (ida y vuelta). Se registra con país y ciudad de 
	 * destino, la fecha de salida y llegada, el costo (ida y vuelta) y la tasa de impuestos del 
	 * aeropuerto (porcentaje sobre el costo total).
	 */
	
	@Override
	public double calcularCosto() {
		return (costoBase +  costoBase * tasa)*this.cantidad;
	}

	@Override
	public String toStringTipo() {
		return "Vuelo";
	}

}
