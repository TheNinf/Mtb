package maths;

public class Mat4 {

	public float[] elementos;

	public Mat4() {
		elementos = new float[16];
	}

	public static Mat4 copiarDatos(final Mat4 otra) {
		Mat4 resultado = new Mat4();
		System.arraycopy(otra.elementos, 0, resultado.elementos, 0, 16);
		return resultado;
	}

	public Mat4(final float diagonal) {
		this();
		elementos[0 + 0 * 4] = diagonal;
		elementos[1 + 1 * 4] = diagonal;
		elementos[2 + 2 * 4] = diagonal;
		elementos[3 + 3 * 4] = diagonal;
	}

	public static Mat4 identidad() {
		return new Mat4(1.0f);
	}

	public static Mat4 lookAt(Vec3 camara, Vec3 objeto, Vec3 up) {
		Mat4 resultado = identidad();
		Vec3 cam = new Vec3(camara.x, camara.y, camara.z);
		Vec3 zaxis = (cam.restar(objeto)).normalizar();
		Vec3 xaxis = (zaxis.cross(up)).normalizar();
		Vec3 yaxis = xaxis.cross(zaxis);

		resultado.elementos[0 + 0 * 4] = xaxis.x;
		resultado.elementos[1 + 0 * 4] = yaxis.x;
		resultado.elementos[2 + 0 * 4] = zaxis.x;

		resultado.elementos[0 + 1 * 4] = xaxis.y;
		resultado.elementos[1 + 1 * 4] = yaxis.y;
		resultado.elementos[2 + 1 * 4] = zaxis.y;

		resultado.elementos[0 + 2 * 4] = xaxis.z;
		resultado.elementos[1 + 2 * 4] = yaxis.z;
		resultado.elementos[2 + 2 * 4] = zaxis.z;

		return resultado.multiplicar(trasladar(new Vec3(-camara.x, -camara.y, -camara.z)));
	}

	public static Mat4 ortografico(final float izquierda, final float derecha, final float abajo, final float arriba,
			final float cerca, final float lejos) {
		final Mat4 resultado = new Mat4();

		resultado.elementos[0 + 0 * 4] = 2.0f / (derecha - izquierda);
		resultado.elementos[1 + 1 * 4] = 2.0f / (arriba - abajo);
		resultado.elementos[2 + 2 * 4] = -2.0f / (cerca - lejos);
		resultado.elementos[3 + 3 * 4] = 1;

		// resultado.elementos[0 + 3 * 4] = (izquierda + derecha) / (izquierda -
		// derecha);
		// resultado.elementos[1 + 3 * 4] = (abajo + arriba) / (abajo - arriba);
		// resultado.elementos[2 + 3 * 4] = (lejos + cerca) / (lejos - cerca);

		return resultado;
	}

	public static Mat4 perspectiva(final float fov, final float relacionAspecto, final float cerca, final float lejos) {
		final Mat4 resultado = new Mat4();

		final float q = (float) (1.0f / Math.tan(Math.toRadians(0.5f * fov)));
		final float a = q / relacionAspecto;

		final float b = (cerca + lejos) / (cerca - lejos);
		final float c = (2.0f * cerca * lejos) / (cerca - lejos);

		resultado.elementos[0 + 0 * 4] = a;
		resultado.elementos[1 + 1 * 4] = q;
		resultado.elementos[2 + 2 * 4] = b;
		resultado.elementos[3 + 2 * 4] = -1.0f;
		resultado.elementos[2 + 3 * 4] = c;

		return resultado;
	}

	public static Mat4 trasladar(final Vec3 posicion) {
		final Mat4 resultado = new Mat4(1.0f);

		resultado.elementos[0 + 3 * 4] = posicion.x;
		resultado.elementos[1 + 3 * 4] = posicion.y;
		resultado.elementos[2 + 3 * 4] = posicion.z;

		return resultado;
	}

	public static Mat4 rotar(final float angulo, final int ejeX, final int ejeY, final int ejeZ) {
		final Mat4 resultado = new Mat4(1.0f);

		final float radianes = (float) Math.toRadians(angulo);
		final float coseno = (float) Math.cos(radianes);
		final float seno = (float) Math.sin(radianes);
		final float unoMenosCoseno = 1.0f - coseno;

		resultado.elementos[0 + 0 * 4] = ejeX * unoMenosCoseno + coseno;
		resultado.elementos[1 + 0 * 4] = ejeY * ejeX * unoMenosCoseno + ejeZ * seno;
		resultado.elementos[2 + 0 * 4] = ejeX * ejeZ * unoMenosCoseno - ejeY * seno;

		resultado.elementos[0 + 1 * 4] = ejeX * ejeY * unoMenosCoseno - ejeZ * seno;
		resultado.elementos[1 + 1 * 4] = ejeY * unoMenosCoseno + coseno;
		resultado.elementos[2 + 1 * 4] = ejeY * ejeZ * unoMenosCoseno + ejeX * seno;

		resultado.elementos[0 + 2 * 4] = ejeX * ejeZ * unoMenosCoseno + ejeY * seno;
		resultado.elementos[1 + 2 * 4] = ejeY * ejeZ * unoMenosCoseno - ejeX * seno;
		resultado.elementos[2 + 2 * 4] = ejeZ * unoMenosCoseno + coseno;

		return resultado;
	}

	public static Mat4 escalar(final Vec3 escalado) {
		final Mat4 resultado = new Mat4(1.0f);

		resultado.elementos[0 + 0 * 4] = escalado.x;
		resultado.elementos[1 + 1 * 4] = escalado.y;
		resultado.elementos[2 + 2 * 4] = escalado.z;

		return resultado;
	}

	public Mat4 multiplicar(final Mat4 matriz) {
		float[] datos = new float[16];

		for (byte y = 0; y < 4; y++) {
			for (byte x = 0; x < 4; x++) {
				float suma = 0;
				for (int e = 0; e < 4; e++) {
					suma += elementos[x + e * 4] * matriz.elementos[e + y * 4];
				}
				datos[x + y * 4] = suma;
			}
		}

		elementos = datos;

		return this;
	}

	public Vec3 multiplicar(final Vec3 vector) {
		return new Vec3(
				elementos[0 + 0 * 4] * vector.x + elementos[0 + 1 * 4] * vector.y + elementos[0 + 2 * 4] * vector.z
						+ elementos[0 + 3 * 4],
				elementos[1 + 0 * 4] * vector.x + elementos[1 + 1 * 4] * vector.y + elementos[1 + 2 * 4] * vector.z
						+ elementos[1 + 3 * 4],
				elementos[2 + 0 * 4] * vector.x + elementos[2 + 1 * 4] * vector.y + elementos[2 + 2 * 4] * vector.z
						+ elementos[2 + 3 * 4]);
	}

	public Vec4 multiplicar(final Vec4 vector) {
		return new Vec4(
				elementos[0 + 0 * 4] * vector.x + elementos[0 + 1 * 4] * vector.y + elementos[0 + 2 * 4] * vector.z
						+ elementos[0 + 3 * 4] * vector.w,
				elementos[1 + 0 * 4] * vector.x + elementos[1 + 1 * 4] * vector.y + elementos[1 + 2 * 4] * vector.z
						+ elementos[1 + 3 * 4] * vector.w,
				elementos[2 + 0 * 4] * vector.x + elementos[2 + 1 * 4] * vector.y + elementos[2 + 2 * 4] * vector.z
						+ elementos[2 + 3 * 4] * vector.w,
				elementos[3 + 0 * 4] * vector.x + elementos[3 + 1 * 4] * vector.y + elementos[3 + 2 * 4] * vector.z
						+ elementos[3 + 3 * 4] * vector.w);
	}
}