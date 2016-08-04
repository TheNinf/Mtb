package utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

public final class PoolObjetos {

	private static final HashMap<Class, ArrayList<Object>> disponibles = new HashMap<>();
	private static final Hashtable<Object, Long> tiempoDisponibles = new Hashtable<>();
	private static final int tiempoExpiracion = 6000;

	private static final ArrayList<Object> bloqueados = new ArrayList<>();

	private PoolObjetos() {

	}

	/**
	 * Este método devuelve un objeto disponible del pool perteneciente a la
	 * clase solicitada. Si no hay ninguno, se crea automáticamente (es muy
	 * importante que tenga la clase un constructor por defecto público, por lo
	 * que quizás tendrás que actualizar las clases que quieras que se usen en
	 * el pool. Si se te olvida, tranquilo que una excepción te lo recordará
	 * xD).
	 * 
	 * @param clase
	 *            -> Clase de la cual quieres el objeto.
	 * @return -> El objeto solicitado.
	 */
	public static final <T extends Object> T solicitar(final Class<T> clase) {
		try {
			T objeto = null;
			ArrayList objetos = disponibles.get(clase);
			if (objetos == null) {
				objeto = clase.newInstance();
				objetos = new ArrayList();
				bloqueados.add(objeto);
				disponibles.put(clase, objetos);
				return objeto;
			} else if (!objetos.isEmpty()) {
				objeto = (T) objetos.get(0);
				objetos.remove(objeto);
				tiempoDisponibles.remove(objeto);
				bloqueados.add(objeto);

				return objeto;
			}

			objeto = clase.newInstance();
			bloqueados.add(objeto);
			return objeto;

		} catch (final Exception e) {
			System.err.println(
					"Ha habido un problema al instanciar el objeto. Asegúrate de que tiene un constructor por defecto (sin parámetros y público)!");
			return null;
		}
	}

	/**
	 * Actualiza los objetos utilizables del pool. Si durante 9 segundos un
	 * objeto no es utilizado, este se elimina del pool.
	 */
	public static final void actualizar() {
		final long ahora = System.currentTimeMillis();
		if (!tiempoDisponibles.isEmpty()) {
			Object objeto;
			Enumeration e = tiempoDisponibles.keys();

			while (e.hasMoreElements()) {
				objeto = e.nextElement();
				if (ahora - tiempoDisponibles.get(objeto) >= tiempoExpiracion) {
					tiempoDisponibles.remove(objeto);
					final Class clase = objeto.getClass();
					final ArrayList lista = disponibles.get(clase);
					lista.remove(objeto);
					if (lista.isEmpty()) {
						disponibles.remove(clase);
					}
					objeto = null;
				}
			}
		}
	}

	/**
	 * Libera un objeto para así poder ser utilizado nuevamente por el pool.
	 * Este método es muy importante llamar (si no se hace es probable que hayan
	 * unos problemas bastante graves de rendimiento y de memoria si el método
	 * es muy solicitado).
	 * 
	 * @param objeto
	 *            -> Objeto que se quiere liberar. Obviamente, tiene que ser un
	 *            objeto que haya sido creado por el pool.
	 */
	public static final void liberar(final Object objeto) {
		bloqueados.remove(objeto);
		final ArrayList objetos = disponibles.get(objeto.getClass());
		objetos.add(objeto);
		tiempoDisponibles.put(objeto, System.currentTimeMillis());
	}

}
