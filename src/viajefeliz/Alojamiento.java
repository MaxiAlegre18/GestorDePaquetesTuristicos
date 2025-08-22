	package viajefeliz;

public class Alojamiento extends ServicioSimple {
	@SuppressWarnings("unused")
	private String pais;
	@SuppressWarnings("unused")
	private String ciudad;
	private String fechaSalida;
	@SuppressWarnings("unused")
	private String hotel;
	private double costoTraslado;
	
	
	public Alojamiento(int codServicio, double costoBase, String fechaInicio, int cantidad, String pais, 
			String ciudad, String fechaSalida, String hotel, double costoTraslado) {
		super(codServicio, costoBase, fechaInicio, cantidad);
		
		if (cantidad > 5) {
			throw new RuntimeException("No se pueden contratar alojamientos con más de cinco personas.");
		}
		
		this.pais = pais;
		this.ciudad = ciudad;
		this.fechaSalida = fechaSalida;
		this.hotel = hotel;
		this.costoTraslado = costoTraslado;
	}

	/*
	 * El servicio de alojamiento se registra con nombre del hotel, país y ciudad de destino, la fecha
	 * de salida y llegada, el costo por día de alojamiento y costo de traslado al aeropuerto (incluye 
	 * ida y vuelta). El costo diario de las habitaciones se brinda con base doble y pueden alojarse 
	 * hasta 5 personas con un costo proporcional a la base doble.
	 * 
	 * En el caso de Alojamiento, se consideran los siguiente porcentajes:
	 * 		Hasta 2 personas: Costo Base
	 * 		Hasta 4 personas: Costo Base * 2
	 * 		5 personas: Costo Base * 2,5
	 */
	
	@Override
	public double calcularCosto() {
		double costoFinal = costoBase;
		
		if (cantidad > 2 && cantidad <= 4) {
			costoFinal *= 2;
		}
		
		else if (cantidad == 5) {
			costoFinal *= 2.5;
		}
		
		costoFinal *= diasEntre(this.fechaInicio, this.fechaSalida);
		costoFinal += costoTraslado;
		
		return costoFinal;
	}

	@Override
	public String toStringTipo() {
		return "Alojamiento";
	}

}
