package maths;

public class Matrix4 {

	public static final byte m00 = 0 + 0 * 4;
	public static final byte m01 = 0 + 1 * 4;
	public static final byte m02 = 0 + 2 * 4;
	public static final byte m03 = 0 + 3 * 4;
	public static final byte m10 = 1 + 0 * 4;
	public static final byte m11 = 1 + 1 * 4;
	public static final byte m12 = 1 + 2 * 4;
	public static final byte m13 = 1 + 3 * 4;
	public static final byte m20 = 2 + 0 * 4;
	public static final byte m21 = 2 + 1 * 4;
	public static final byte m22 = 2 + 2 * 4;
	public static final byte m23 = 2 + 3 * 4;
	public static final byte m30 = 3 + 0 * 4;
	public static final byte m31 = 3 + 1 * 4;
	public static final byte m32 = 3 + 2 * 4;
	public static final byte m33 = 3 + 3 * 4;

	public float[] elementos;

	public Matrix4() {
		elementos = new float[16];
	}

	public Matrix4(final float diagonal) {
		elementos = new float[16];
		elementos[m00] = elementos[m11] = elementos[m22] = elementos[m33] = diagonal;
	}

	public final void ponerIdentidad() {
		elementos[m00] = elementos[m11] = elementos[m22] = elementos[m33] = 1.0f;
	}

	public static final Matrix4 perspectiva(final float fov, final float relacionAspecto, final float cerca,
			final float lejos, final Matrix4 destino) {
		final Matrix4 resultado = new Matrix4();

		final float q = (float) (1.0f / Math.tan(Math.toRadians(fov / 2.0f)));
		final float a = q / relacionAspecto;

		final float cml = cerca - lejos;
		final float b = (cerca + lejos) / cml;
		final float c = (2.0f * cerca * lejos) / cml;

		resultado.elementos[m00] = a;
		resultado.elementos[m11] = q;
		resultado.elementos[m22] = b;
		resultado.elementos[m32] = -1.0f;
		resultado.elementos[m23] = c;

		return destino == null ? resultado : multiplicar(destino, resultado, destino);
	}

	public static final Matrix4 ortografico(final float izquierda, final float derecha, final float abajo,
			final float arriba, final float cerca, final float lejos, final Matrix4 destino) {
		Matrix4 resultado = new Matrix4();

		resultado.elementos[m00] = 2.0f / (derecha - izquierda);
		resultado.elementos[m11] = 2.0f / (arriba - abajo);
		resultado.elementos[m22] = -2.0f / (cerca - lejos);
		resultado.elementos[m33] = 1.0f;

		return destino == null ? resultado : multiplicar(destino, resultado, destino);
	}

	public static final Matrix4 trasladar(final Vector3 posicion, final Matrix4 destino) {
		final Matrix4 resultado = new Matrix4(1.0f);

		resultado.elementos[m03] = posicion.x;
		resultado.elementos[m13] = posicion.y;
		resultado.elementos[m23] = posicion.z;

		return destino == null ? resultado : multiplicar(destino, resultado, destino);
	}

	public static final Matrix4 rotar(final float angulo, final Vector3 eje, final Matrix4 destino) {
		final Matrix4 resultado = new Matrix4();

		final float x = eje.x;
		final float y = eje.y;
		final float z = eje.z;
		final float radianes = (float) Math.toRadians(angulo);
		final float coseno = (float) Math.cos(radianes);
		final float seno = (float) Math.sin(radianes);
		final float unoMenosCoseno = 1.0f - coseno;

		resultado.elementos[m00] = x * unoMenosCoseno + coseno;
		resultado.elementos[m10] = y * x * unoMenosCoseno + z * seno;
		resultado.elementos[m20] = x * z * unoMenosCoseno - y * seno;

		resultado.elementos[m01] = x * y * unoMenosCoseno - z * seno;
		resultado.elementos[m11] = y * unoMenosCoseno + coseno;
		resultado.elementos[m21] = y * z * unoMenosCoseno + x * seno;

		resultado.elementos[m02] = x * z * unoMenosCoseno + y * seno;
		resultado.elementos[m12] = y * z * unoMenosCoseno - x * seno;
		resultado.elementos[m22] = z * unoMenosCoseno + coseno;

		resultado.elementos[m33] = 1.0f;

		return destino == null ? resultado : multiplicar(destino, resultado, destino);
	}

	public static final Matrix4 escalar(final Vector3 escalado, final Matrix4 destino) {
		final Matrix4 resultado = new Matrix4();

		resultado.elementos[m00] = escalado.x;
		resultado.elementos[m11] = escalado.y;
		resultado.elementos[m22] = escalado.z;
		resultado.elementos[m33] = 1.0f;

		return destino == null ? resultado : multiplicar(destino, resultado, destino);
	}

	public static Matrix4 lookAt(final Vector3 camara, final Vector3 posicion, final Vector3 up,
			final Matrix4 destino) {
		final Matrix4 resultado = new Matrix4();

		final Vector3 zaxis = Vector3.restar(camara, posicion, null).normalizar();
		final Vector3 xaxis = Vector3.cross(zaxis, up).normalizar();
		final Vector3 yaxis = Vector3.cross(xaxis, zaxis);

		resultado.elementos[m00] = xaxis.x;
		resultado.elementos[m10] = yaxis.x;
		resultado.elementos[m20] = zaxis.x;

		resultado.elementos[m01] = xaxis.y;
		resultado.elementos[m11] = yaxis.y;
		resultado.elementos[m21] = zaxis.y;

		resultado.elementos[m02] = xaxis.z;
		resultado.elementos[m12] = yaxis.z;
		resultado.elementos[m22] = zaxis.z;

		return destino == null
				? multiplicar(resultado, trasladar(new Vector3(-camara.x, -camara.y, -camara.z), null), resultado)
				: multiplicar(destino,
						multiplicar(resultado, trasladar(new Vector3(-camara.x, -camara.y, -camara.z), null), null),
						destino);
	}

	public static final Matrix4 multiplicar(final Matrix4 src, final Matrix4 otra, final Matrix4 destino) {
		final float[] datos = new float[16];

		float suma = 0.0f;
		for (byte y = 0; y < 4; y++) {
			final int y1 = (y << 2);
			for (byte x = 0; x < 4; x++) {
				for (byte e = 0; e < 4; e++) {
					suma += src.elementos[x + (e << 2)] * otra.elementos[e + y1];
				}
				datos[x + y1] = suma;
				suma = 0;
			}
		}

		if (destino == null) {
			final Matrix4 resultado = new Matrix4();
			resultado.elementos = datos;
			return resultado;
		}

		destino.elementos = datos;
		return destino;
	}

	public static final Vector3 multiplicar(final Matrix4 src, final Vector3 vector, final Vector3 destino) {
		final float x = src.elementos[m00] * vector.x + src.elementos[m01] * vector.y + src.elementos[m02] * vector.z
				+ src.elementos[m03];
		final float y = src.elementos[m10] * vector.x + src.elementos[m11] * vector.y + src.elementos[m12] * vector.z
				+ src.elementos[m13];
		final float z = src.elementos[m20] * vector.x + src.elementos[m21] * vector.y + src.elementos[m22] * vector.z
				+ src.elementos[m23];

		if (destino == null)
			return new Vector3(x, y, z);

		destino.x = x;
		destino.y = y;
		destino.z = z;
		return destino;
	}

	public static final Vector4 multiplicar(final Matrix4 src, final Vector4 vector, final Vector4 destino) {
		final float x = src.elementos[m00] * vector.x + src.elementos[m01] * vector.y + src.elementos[m02] * vector.z
				+ src.elementos[m03] * vector.w;
		final float y = src.elementos[m10] * vector.x + src.elementos[m11] * vector.y + src.elementos[m12] * vector.z
				+ src.elementos[m13] * vector.w;
		final float z = src.elementos[m20] * vector.x + src.elementos[m21] * vector.y + src.elementos[m22] * vector.z
				+ src.elementos[m23] * vector.w;
		final float w = src.elementos[m30] * vector.x + src.elementos[m31] * vector.y + src.elementos[m32] * vector.z
				+ src.elementos[m33] * vector.w;

		if (destino == null)
			return new Vector4(x, y, z, w);

		destino.x = x;
		destino.y = y;
		destino.z = z;
		destino.w = w;
		return destino;
	}

	@Override
	public final String toString() {
		StringBuilder string = new StringBuilder();
		for (byte i = 0; i < 16; i++)
			string.append(i % 4 == 0 ? "\n" + elementos[i] : elementos[i]);
		return string.toString();
	}

}
