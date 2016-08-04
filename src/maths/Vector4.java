package maths;

public class Vector4 {

	public float x, y, z, w;

	public Vector4(final float x, final float y, final float z, final float w) {
		set(x, y, z, w);
	}

	public Vector4() {
		this(0, 0, 0, 0);
	}

	public static final Vector4 sumar(final Vector4 derecha, final Vector4 izquierda, final Vector4 destino) {
		final float x = derecha.x + izquierda.x;
		final float y = derecha.y + izquierda.y;
		final float z = derecha.z + izquierda.z;
		final float w = derecha.w + izquierda.w;

		if (destino == null)
			return new Vector4(x, y, z, w);

		destino.set(x, y, z, w);
		return destino;
	}

	public static final Vector4 restar(final Vector4 derecha, final Vector4 izquierda, final Vector4 destino) {
		final float x = derecha.x - izquierda.x;
		final float y = derecha.y - izquierda.y;
		final float z = derecha.z - izquierda.z;
		final float w = derecha.w - izquierda.w;

		if (destino == null)
			return new Vector4(x, y, z, w);

		destino.set(x, y, z, w);
		return destino;
	}

	public static final Vector4 multiplicar(final Vector4 derecha, final Vector4 izquierda, final Vector4 destino) {
		final float x = derecha.x * izquierda.x;
		final float y = derecha.y * izquierda.y;
		final float z = derecha.z * izquierda.z;
		final float w = derecha.w * izquierda.w;

		if (destino == null)
			return new Vector4(x, y, z, w);

		destino.set(x, y, z, w);
		return destino;
	}

	public static final Vector4 dividir(final Vector4 derecha, final Vector4 izquierda, final Vector4 destino) {
		final float x = derecha.x / izquierda.x;
		final float y = derecha.y / izquierda.y;
		final float z = derecha.z / izquierda.z;
		final float w = derecha.w / izquierda.w;

		if (destino == null)
			return new Vector4(x, y, z, w);

		destino.set(x, y, z, w);
		return destino;
	}

	private final void set(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

}
