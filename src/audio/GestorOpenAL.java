package audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import maths.Vector3;

public class GestorOpenAL {

	private static long servicio;
	private static long contexto;

	private static ExecutorService hiloOpenAL;

	private GestorOpenAL() {
	}

	public static final void iniciar() {
		hiloOpenAL = Executors.newSingleThreadExecutor();
		ejecutar(() -> iniciarOpenAL());

		Sonido.iniciar(hiloOpenAL);
	}

	private static final void iniciarOpenAL() {
		servicio = ALC10.alcOpenDevice((ByteBuffer) null);
		contexto = ALC10.alcCreateContext(servicio, (IntBuffer) null);

		final ALCCapabilities capacidadesServicio = ALC.createCapabilities(servicio);
		ALC10.alcMakeContextCurrent(contexto);
		AL.createCapabilities(capacidadesServicio);

		ponerPosicionJugador(new Vector3());
	}

	public static final void ponerPosicionJugador(final Vector3 posicion) {
		ejecutar(() -> AL10.alListener3f(AL10.AL_POSITION, posicion.x, posicion.y, posicion.z));
	}

	public static final void cambiarModeloDistancia(final int modelo) {
		ejecutar(() -> AL10.alDistanceModel(modelo));
	}

	private static final void ejecutar(final Runnable comando) {
		hiloOpenAL.execute(comando);
	}

	public static final void terminar() {
		GestorSonidos.destruir();
		ejecutar(() -> {
			ALC10.alcMakeContextCurrent(0);
			ALC10.alcDestroyContext(contexto);
			ALC10.alcCloseDevice(servicio);
		});
		hiloOpenAL.shutdown();
	}

}
