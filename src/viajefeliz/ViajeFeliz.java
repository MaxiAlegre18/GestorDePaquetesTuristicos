package viajefeliz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class ViajeFeliz implements IViajeFeliz{
	
	protected String cuit;
	protected HashMap <String, Cliente> clientes = new HashMap<String, Cliente>();
	protected HashMap <Integer, Servicio> catalogo = new HashMap<Integer, Servicio>();
	private int contadorServiciosCreados = 0;	// Sirve para la generación del código único
	
	public ViajeFeliz(String cuit) {
		this.cuit = cuit;
	}

	@Override
	public void registrarCliente(String dni, String nombre, String direccion) {
		if (clientes.containsKey(dni)) {
			throw new RuntimeException("¡El cliente ya se encuentra registrado en el sistema!");
		} else {
			Cliente nuevo = new Cliente(dni, nombre, direccion);
			clientes.put(dni, nuevo);
		}
	}

	// SERVICIO: Vuelo
	
	@Override
	public int crearServicio(double costoBase, String fechaInicio, int cantidad, String pais, String ciudad,
			String fechaLlegada, double tasa) {
		int codigoUnico = generarCodigoUnico();
		Vuelo vuelo = new Vuelo (codigoUnico, costoBase, fechaInicio, cantidad, pais, ciudad, 
				fechaLlegada, tasa);
		catalogo.put(codigoUnico, vuelo);
		return codigoUnico;
	}
	
	// SERVICIO: Alojamiento

	@Override
	public int crearServicio(double costoBase, String fechaInicio, int cantidad, String pais, String ciudad,
			String fechaLSalida, String hotel, double costoTraslado) {
		int codigoUnico = generarCodigoUnico();
		Alojamiento alojamiento = new Alojamiento (codigoUnico, costoBase, fechaInicio, cantidad, 
				pais, ciudad, fechaLSalida, hotel, costoTraslado);
		catalogo.put(codigoUnico, alojamiento);
		return codigoUnico;
	}

	// SERVICIO: Alquiler
	
	@Override
	public int crearServicio(double costoBase, String fechaInicio, int cantidad, String pais, String ciudad,
			double garantia, String fechaDevolucion) {
		int codigoUnico = generarCodigoUnico();
		Alquiler alquiler = new Alquiler (codigoUnico, costoBase, fechaInicio, cantidad, 
				pais, ciudad, garantia, fechaDevolucion);
		catalogo.put(codigoUnico, alquiler);
		return codigoUnico;
	}
	
	// SERVICIO: Excursión

	@Override
	public int crearServicio(double costoBase, String fechaInicio, int cantidad, String lugarSalida,
			boolean esDiaCompleto) {
		int codigoUnico = generarCodigoUnico();
		Excursion excursion = new Excursion (codigoUnico, costoBase, fechaInicio, cantidad, 
				lugarSalida, esDiaCompleto);
		catalogo.put(codigoUnico, excursion);
		return codigoUnico;
	}

	@Override
	public int[] crearPaquetesPredefinidos(int cantPaquetes, int[] codigosDeServicios) {
		/*
		 * Crea un arreglo de codigos de paquetes con el largo de la cantidad de paquetes
		 */
		int[] codigosDePaquetes = new int[cantPaquetes];
		
		/*
		 * Recorre cantPaquetes y crea un paquete en cada iteracion.
		 */
		for (int i=0; i<cantPaquetes; i++) {
			int codigoPaquete = generarCodigoUnico();
			Paquete p = new Paquete(codigoPaquete, null, 0);
			// Agrega el codigo del paquete actual al arreglo
			codigosDePaquetes[i] = codigoPaquete;
			// Agerga el paquete actual al catalogo de la empresa
			catalogo.put(codigoPaquete, p);
			/*
			 * Recorre los servicios y los agrega al paquete actual
			 */
			for (int j=0; j<codigosDeServicios.length; j++) {
				p.agregarServicio(codigosDeServicios[j], catalogo.get(codigosDeServicios[j]));
			}
		}
		
		/*
		 * Elimina los servicios del catalogo
		 */
		for (int i=0; i<codigosDeServicios.length; i++) {
			catalogo.remove(codigosDeServicios[i]);
		}
		return codigosDePaquetes;
	}

	@Override
	public int iniciarContratacion(String dni, int codServicio) {
		/*
		 * Lanza excepciones si el dni o el cod servicio es inexistente:
		 */
		
		verificarExistencia(dni, codServicio);
		
		/* Se crea un paquete personalizado el cual erá la contratación vigente. Dicho paquete 
		 * adquirirá a medida de que se vayan agregando servicios, la fecha del que primero
		 * inicie y la cantidad de personas del que más tenga.
		 */
		int codigoPaquete = generarCodigoUnico();
		PaquetePersonalizado pp = new PaquetePersonalizado(codigoPaquete, null, 0);
		
		// Se obtiene el cliente pasado por parámetro:
		Cliente c = clientes.get(dni);
		
		// Se inicia la contratación desde el cliente.
		c.iniciarContratacion(codigoPaquete, pp);
		
		// Se agrega el servicio pasado por parámetro:
		agregarServicioAContratacion(dni, codServicio);
		return codigoPaquete;
	}

	@Override
	public void agregarServicioAContratacion(String dni, int codServicio) {
		/*
		 * Lanza excepciones si el dni o el cod servicio es inexistente:
		 */
		
		verificarExistencia(dni, codServicio);
		
		/*
		 * Agrega un servicio a la contratacion vigente de un cliente y luego la elimina del catálogo
		 * de la empresa
		 */
		clientes.get(dni).agregarServicio(codServicio, catalogo.get(codServicio));
		catalogo.remove(codServicio);
	}

	@Override
	public void quitarServicioDeContratacion(String dni, int codServicio) {
		/*
		 * Lanza excepción si el dni es inexistente:
		 */
		
		verificarExistencia(dni);
		
		/*
		 * Busca el servicio o paquete en el cliente y lo elimina.
		 * Si eliminado=null quiere decir que no se encontró el servicio y lanza excepción.
		 */
		Servicio eliminado = clientes.get(dni).quitarServicio(codServicio);
		if (eliminado == null) {
			throw new RuntimeException("¡El paquete codigo: " + codServicio + " no pertenece al cliente DNI: " + dni + "!");
		}
		/*
		 * Si es un servicioPaquete, lo retorna al catálogo. Sino, lo elimina.
		 */
		if (eliminado instanceof Paquete) {
			catalogo.put(codServicio, eliminado);
		} else {
			eliminado = null;
		}
	}

	@Override
	public double calcularCostoDePaquetePersonalizado(String dni, int codPaquetePersonalizado) {
		/*
		 * Lanza excepción si el dni es inexistente:
		 */
		
		verificarExistencia(dni);
		
		/*
		 * Realiza el cálculo del costo total del paquete pasado por parámetro.
		 */
		return clientes.get(dni).calcularCostoPaquete(codPaquetePersonalizado);
	}

	@Override
	public double pagarContratacion(String dni, String fechaPago) {
		double costo = calcularCostoDePaquetePersonalizado(dni);
		clientes.get(dni).confirmarPago(fechaPago);
		return costo;
	}

	@Override
	public List<Integer> historialDeContrataciones(String dni) {
		/*
		 * Lanza excepción si el dni es inexistente:
		 */
		
		verificarExistencia(dni);
		
		/*
		 * Busca el cliente por su dni y pide su historial de contrataciones.
		 */
		return clientes.get(dni).listaPaquetesContratados();
	}

	@Override
	public String contratacionesSinIniciar(String fecha) {
		/*
		 * Recorre los clientes buscando las contrataciones que aun no se iniciaron
		 * desde la fecha pasada por parámetro.
		 */
		StringBuilder str = new StringBuilder();
		Iterator <String> itmap = clientes.keySet().iterator();
		
		while (itmap.hasNext()) {
			/*
			 * Busca si el cliente actual tiene contrataciones que cumpla con lo pedido.
			 * Sino las tiene, no la agrega a la lista.
			 */
			String contratacion = clientes.get(itmap.next()).contratacionesNoIniciadas(fecha);
			if (! contratacion.isEmpty()) {
				str.append(contratacion + "\n");
			}
		}
		
		return str.toString();
	}

	@Override
	public List<String> contratacionesQueInicianEnFecha(String fecha) {
		/*
		 * Recorre los clientes buscando contrataciones que inicien en fecha.
		 */
		LinkedList<String> str = new LinkedList<String>();
		Iterator <String> itmap = clientes.keySet().iterator();
		
		while (itmap.hasNext()) {
			/*
			 * Busca si el cliente actual tiene contrataciones que inician en fecha.
			 * Si el cliente no tiene ninguna contratación que cumpla, no la agrega a la lista.
			 */
			String contratacion = clientes.get(itmap.next()).contratacionesQueInicianEnFecha(fecha);
			if (! contratacion.isEmpty()) {
				str.add(contratacion);
			}
		}
		return str;
	}

	@Override
	public Set<Integer> obtenerCodigosCatalogo() {
		
		Set<Integer> codigos = new HashSet<Integer>();
		for (Map.Entry<Integer, Servicio> s : catalogo.entrySet()) {
			codigos.add(s.getKey());
		}
		return codigos;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\n###################################################################\n"
				+ "Mostrando información de empresa: " + this.cuit + ". Lista de clientes:\n"
				+ "###################################################################\n");
		for (Map.Entry<String, Cliente> c : clientes.entrySet()) {
			str.append("\n - " + c.getValue().toString());
		}
		str.append("\n######################################################################\n "
				+ "Mostrando información de empresa: " + this.cuit + ". Catalogo disponible:\n"
				+ "######################################################################\n");
		for (Map.Entry<Integer, Servicio> s : catalogo.entrySet()) {
			str.append("\n - " + s.getValue().toString());
		}
		return str.toString();
	}
	
	private double calcularCostoDePaquetePersonalizado(String dni) {
		/*
		 * Lanza excepción si el dni es inexistente:
		 */
		
		verificarExistencia(dni);
		
		/*
		 * Realiza el cálculo del costo total del paquete pasado por parámetro.
		 */
		return clientes.get(dni).calcularCostoPaquete();
	}
	
	private int generarCodigoUnico() {
		int codigoUnico = contadorServiciosCreados + 1;
		contadorServiciosCreados++;
		return codigoUnico;
	}

	private void verificarExistencia(String dni, int codServicio) {
		if (! clientes.containsKey(dni)) {
			throw new RuntimeException("¡El cliente no se encuentra registrado en el sistema!");
		} else if (! catalogo.containsKey(codServicio)) {
			throw new RuntimeException("¡El servicio no se encuentra registrado en el sistema!");
		}
	}
	
	private void verificarExistencia(String dni) {
		if (! clientes.containsKey(dni)) {
			throw new RuntimeException("¡El cliente no se encuentra registrado en el sistema!");
		}
	}
}
