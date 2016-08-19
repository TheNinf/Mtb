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

import maths.Vec3;

public class GestorOpenAL {

	private static long servicio;
	private static long contexto;

	private static ExecutorService hilo;

	private GestorOpenAL() {
	}

	public static final void iniciar() {
		hilo = Executors.newSingleThreadExecutor();
		hilo.execute(() -> iniciarOpenAL());
	}

	private static final void iniciarOpenAL() {
		servicio = ALC10.alcOpenDevice((ByteBuffer) null);
		contexto = ALC10.alcCreateContext(servicio, (IntBuffer) null);

		final ALCCapabilities capacidadesServicio = ALC.createCapabilities(servicio);
		ALC10.alcMakeContextCurrent(contexto);
		AL.createCapabilities(capacidadesServicio);

		ponerPosicionJugador(new Vec3());
	}

	public static final void ponerPosicionJugador(final Vec3 posicion) {
		hilo.execute(() -> AL10.alListener3f(AL10.AL_POSITION, posicion.x, posicion.y, posicion.z));
	}

	public static final void cambiarModeloDistancia(final int modelo) {
		hilo.execute(() -> AL10.alDistanceModel(modelo));
	}

	public static final void sonar(final Sonido sonido) {
		hilo.execute(() -> AL10.alSourcePlay(sonido.obtenerSource()));
	}

	public static final void pausar(final Sonido sonido) {
		hilo.execute(() -> AL10.alSourcePause(sonido.obtenerSource()));
	}

	public static final void parar(final Sonido sonido) {
		hilo.execute(() -> AL10.alSourceStop(sonido.obtenerSource()));
	}

	public static final void terminar() {
		hilo.execute(() -> {
			GestorSonidos.destruir();
			ALC10.alcMakeContextCurrent(0);
			ALC10.alcDestroyContext(contexto);
			ALC10.alcCloseDevice(servicio);
		});
		hilo.shutdown();
	}

}
