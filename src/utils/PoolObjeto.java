package utils;

import java.util.ArrayList;
import java.util.HashMap;

import maths.Matrix4;
import maths.Vector3;

public abstract class PoolObjeto<G> {

	private static final ArrayList<PoolObjeto<? extends Object>> pools = new ArrayList<>();
	private static final short tiempoExpiracion = 6000;

	public static final PoolObjeto<Vector3> VECTOR3 = new PoolObjeto<Vector3>() {
		@Override
		public Vector3 obtenerObjeto() {
			return new Vector3();
		}
	};

	public static final PoolObjeto<Matrix4> MATRIX4 = new PoolObjeto<Matrix4>() {
		@Override
		public Matrix4 obtenerObjeto() {
			return new Matrix4();
		}
	};

	private final HashMap<G, Long> disponibles;
	private final ArrayList<G> bloqueados;

	public PoolObjeto() {
		disponibles = new HashMap<>();
		bloqueados = new ArrayList<>();

		pools.add(this);
	}

	public static final void actualizarPools() {
		for (final PoolObjeto<? extends Object> pool : pools)
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
	}

	protected abstract G obtenerObjeto();
}
