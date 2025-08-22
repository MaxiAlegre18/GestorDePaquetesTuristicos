package viajefeliz;

public class Excursion extends ServicioSimple {
	@SuppressWarnings("unused")
	private String lugarSalida;
	private boolean esDiaCompleto;
	
	
	public Excursion(int codServicio, double costoBase, String fechaInicio, int cantidad, 
			String lugarSalida, boolean esDiaCompleto) {
		super(codServicio, costoBase, fechaInicio, cantidad);

		if (cantidad > 4) {
			throw new RuntimeException("No se pueden contratar excursiones para más de cuatro personas.");
		}
		
		this.lugarSalida = lugarSalida;
		this.esDiaCompleto = esDiaCompleto;
	}
	
	/*
	 * La contratación de excursiones que ofrece la empresa registra la fecha y lugar de salida, la 
	 * cantidad de personas y su costo por persona. El costo se diferencia si es excursión de medio 
	 * día o día entero y se hace un descuento del 5% si son 2 personas y del 10% si son 3 o 4. 
	 * 
	 * Para las excursiones, el costo base es el valor de media excursión y se lo multiplica por la 
	 * cantidad de personas.
	 * Si la excursión es completa, el costo se multiplica por 2 y luego por la cantidad de personas.
	 */

	@Override
	public double calcularCosto() {
		double costoFinal = 0;
		/*
		 * Cálculo del coste dependiendo si es diaCompleto o no.
		 */
		if (! esDiaCompleto) {
			costoFinal = this.costoBase * cantidad;
		} else {
			costoFinal = (this.costoBase * 2) * cantidad;
		}
		/*
		 * Cálculo de los descuentos.
		 */
		if (cantidad == 2) {
			costoFinal = costoFinal - (costoFinal * 0.05);
		} else if (cantidad >= 3) {
			costoFinal = costoFinal - (costoFinal * 0.10);
		}
		return costoFinal;
	}

	@Override
	public String toStringTipo() {
		return "Excursion";
	}

}
