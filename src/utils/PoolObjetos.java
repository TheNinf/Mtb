package utils;

import java.util.ArrayList;
import java.util.HashMap;

import maths.Vector3;

public abstract class PoolObjetos<G> {

	private static final ArrayList<PoolObjetos<? extends Object>> pools = new ArrayList<>();
	private static final short tiempoExpiracion = 6000;

	public static final PoolObjetos<Vector3> VECTOR3 = new PoolObjetos<Vector3>() {
		@Override
		protected Vector3 obtenerObjeto() {
			return new Vector3();
		}
	};

	private final HashMap<G, Long> disponibles;
	private final ArrayList<G> bloqueados;

	private PoolObjetos() {
		disponibles = new HashMap<>();
		bloqueados = new ArrayList<>();

		pools.add(this);
	}

	public static final void actualizarPools() {
		for (final PoolObjetos<? extends Object> pool : pools)
			pool.actualizar();
	}

	public final G solicitar() {
		if (disponibles.isEmpty())
			return obtenerObjeto();

		final G objeto = (G) disponibles.keySet().toArray()[0];
		bloqueados.add(objeto);
		disponibles.remove(objeto);
		return objeto;
	}

	public final void devolver(final G objeto) {
		disponibles.put(objeto, System.currentTimeMillis());
		bloqueados.remove(objeto);
	}

	private final void actualizar() {
		final long ahora = System.currentTimeMillis();

		for (final G objeto : disponibles.keySet()) {
			final long tiempo = disponibles.get(objeto);
			if (ahora - tiempo >= tiempoExpiracion)
				disponibles.remove(objeto);
		}
		System.out.println(this + ", " + disponibles.keySet().size());
	}

	protected abstract G obtenerObjeto();
}
