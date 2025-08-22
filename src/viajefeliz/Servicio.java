package viajefeliz;

public abstract class Servicio {
	protected int codUnico;
	protected String fechaInicio;
	protected int cantidad;
	protected boolean estaConcretado = false;

	Servicio(int codUnico, String fechaInicio, int cantidad) {
		this.codUnico = codUnico;
		this.fechaInicio = fechaInicio;
		this.cantidad = cantidad;
	}
	
	public abstract double calcularCosto();
	
	public boolean noIniciadoDesdeFecha(String fecha) {
		return fechaMenorQueOtra(fecha, this.fechaInicio);
	}
	
	public boolean iniciaEnFecha(String fecha) {
		return this.fechaInicio.equals(fecha);
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public String getFechaInicio() {
		return this.fechaInicio;
	}
	
	/*
	 * Métodos de fecha
	 */
	
	public abstract String toStringTipo();
	
	public String toStringCodigo() {
		return String.valueOf(this.codUnico);
	}
	
	public static int[] stringAfecha(String f) {
		/* 
		 * Divide la fecha en un arreglo de strings por ej: 22-12-2003
		 * sacando los "-" quedando:
		 * {22, 12, 2003}
		 * Y luego convierte ese string a un arreglo de enteros
		 */
		String[] fecha = f.split("-");
		
		int[] fechaFinal = new int[] {
									Integer.parseInt(fecha[0]), 
									Integer.parseInt(fecha[1]), 
									Integer.parseInt(fecha[2])};
			
		return fechaFinal;
	}
	
	public static int diasEntre(String fecha1, String fecha2) {
		int dia1 = Integer.parseInt(fecha1.substring(8));
		int dia2 = Integer.parseInt(fecha2.substring(8));
		
		return dia2-dia1;
	}
	
	public static boolean fechaMenorQueOtra(String fecha1, String fecha2) {
		/*
		 * Pasa las fechas a un arreglo de entero, posiciones:
		 * 0: año
		 * 1: mes
		 * 2: dia
		 */
		int [] fecha1lista = stringAfecha(fecha1);
		int [] fecha2lista = stringAfecha(fecha2);
		
		// Año fecha 1 < Año fecha 2
		if (fecha1lista[0] < fecha2lista[0]) {
			return true;
		}
		
		else if (fecha1lista[0] == fecha2lista[0]) {
			// Año fecha 1 = Año fecha 2 | Mes fecha 1 < Mes fecha 2
			if (fecha1lista[1] < fecha2lista[1]) {
				return true;
			}
			
			else if (fecha1lista[1] == fecha2lista[1]) {
				// Mes fecha 1 = Mes fecha 2 | dia fecha 1 < dia fecha 2
				if (fecha1lista[2] < fecha2lista[2]) {
					return true;
				}
				
				// Dia fecha 1 = Dia fecha 2 (mismas fechas)
				if (fecha1lista[2] == fecha2lista[2]) {
					return true;
				}
			}
		}
		
		// Mes y Año fecha 1 = Mes y año fecha 2 | dia fecha 1 > dia fecha 2
		return false;
		
	}
}
