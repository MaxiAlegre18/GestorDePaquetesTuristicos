package viajefeliz;

public abstract class ServicioSimple extends Servicio {
	protected double costoBase;

	public ServicioSimple(int codUnico, double costoBase, String fechaInicio, int cantidad) {
		super(codUnico, fechaInicio, cantidad);
		this.costoBase = costoBase;
	}

	public abstract double calcularCosto();
	
	@Override
	public String toString() {
		return ("Servicio codigo: " + this.codUnico + 
				" " + toStringTipo() +
				" | costoBase: " + this.costoBase + 
				" | cantPersonas: " + this.cantidad + 
				"\n");
	}
	
	public abstract String toStringTipo();

}
