package graphics.graphics3D.particles;

import entity.Camara;
import maths.Vector3;

public final class GestorParticulas {

	public static final short MAX_PARTICULAS = 9000;
	public static final short MITAD_PARTICULAS = MAX_PARTICULAS / 2;

	private static Particula[] particulasNBlend;
	private static Particula[] particulasABlend;

	private static short PARTICULAS_VIVAS_NBLEND = 0;
	private static short PARTICULAS_VIVAS_ABLEND = 0;

	private static RenderizadorParticulas renderizador;

	private GestorParticulas() {
	}

	public static final void iniciar() {
		new Thread(() -> {
			particulasNBlend = new Particula[MITAD_PARTICULAS];
			particulasABlend = new Particula[MITAD_PARTICULAS];

			for (short i = 0; i < MITAD_PARTICULAS; i++) {
				particulasNBlend[i] = new Particula();
				particulasABlend[i] = new Particula();
			}
		}).start();
		renderizador = new RenderizadorParticulas();
	}

	public static final void agregarParticula(final TexturaParticula textura, final Vector3 posicion,
			final Vector3 velocidad, final float rotacion, final float escalado, final float tiempoVida,
			final float gravedad) {
		if (textura.usaAdditiveBlending()) {
			if (PARTICULAS_VIVAS_ABLEND < MITAD_PARTICULAS)
				particulasABlend[PARTICULAS_VIVAS_ABLEND++].set(textura, posicion, velocidad, rotacion, escalado,
						tiempoVida, gravedad);
		} else {
			if (PARTICULAS_VIVAS_NBLEND < MITAD_PARTICULAS)
				particulasNBlend[PARTICULAS_VIVAS_NBLEND++].set(textura, posicion, velocidad, rotacion, escalado,
						tiempoVida, gravedad);
		}
	}

	public static final void dibujar(final Camara camara) {
		for (short i = 0; i < PARTICULAS_VIVAS_NBLEND; i++) {
			particulasNBlend[i].actualizar(camara);
		}

		for (short i = 0; i < PARTICULAS_VIVAS_ABLEND; i++) {
			particulasABlend[i].actualizar(camara);
		}

		ordenarPorLejania();

		renderizador.render(particulasNBlend, PARTICULAS_VIVAS_NBLEND, false);
		renderizador.render(particulasABlend, PARTICULAS_VIVAS_ABLEND, true);
	}

	private static final void ordenarPorLejania() {
		for (short i = 1; i < PARTICULAS_VIVAS_NBLEND; i++) {
			final Particula particula = particulasNBlend[i];
			if (particula.obtenerDistancia() > particulasNBlend[i - 1].obtenerDistancia()) {
				int posicion = i - 1;
				while (posicion != 0 && particulasNBlend[posicion].obtenerDistancia() < particula.obtenerDistancia()) {
					posicion--;
				}
				cambiar(particulasNBlend, posicion, i);
			}
		}
	}

	public static final void morir(final Particula particula) {
		if (particula.obtenerTextura().usaAdditiveBlending()) {
			cambiar(particulasABlend, indiceDe(particulasABlend, PARTICULAS_VIVAS_ABLEND, particula),
					PARTICULAS_VIVAS_ABLEND - 1);
			PARTICULAS_VIVAS_ABLEND--;
		} else {
			cambiar(particulasNBlend, indiceDe(particulasNBlend, PARTICULAS_VIVAS_NBLEND, particula),
					PARTICULAS_VIVAS_NBLEND - 1);
			PARTICULAS_VIVAS_NBLEND--;
		}
	}

	private static final void cambiar(final Particula[] particulas, final int i, final int j) {
		Particula temp = particulas[i];
		particulas[i] = particulas[j];
		particulas[j] = temp;
	}

	private static final int indiceDe(final Particula[] particulas, final int numParticulas,
			final Particula particula) {
		for (int i = 0; i < numParticulas; i++) {
			if (particulas[i].equals(particula)) {
				return i;
			}
		}

		return -1;
	}
}
