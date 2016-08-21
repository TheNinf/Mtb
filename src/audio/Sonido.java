package audio;

import java.util.concurrent.ExecutorService;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import maths.Vector3;
import utils.PoolObjeto;

public class Sonido {

	// TODO hacer limpieza de VAOS, VBOS, SONIDOS, FRAMEBUFFERS, TEXTURAS
	private final String nombre;

	private final int source;
	private final int buffer;
	private int modeloDistancia;

	private static ExecutorService hiloOpenAL;
	private static boolean iniciado = false;

	private boolean sonando;

	public Sonido(final String nombre, final DatosWav datos) {
		this.nombre = nombre;

		this.source = AL10.alGenSources();
		this.buffer = AL10.alGenBuffers();

		sonando = false;

		AL10.alBufferData(buffer, datos.formato, datos.datos, datos.frecuencia);

		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);

		final Vector3 posicionyvel = PoolObjeto.VECTOR3.solicitar();
		ponerPosicionSonido(posicionyvel);
		ponerVelocidadSonido(posicionyvel);
		PoolObjeto.VECTOR3.devolver(posicionyvel);

		modeloDistancia = AL11.AL_EXPONENT_DISTANCE;
		GestorSonidos.agregarSonido(this);
	}

	public Sonido(final String nombre, final String ruta) {
		this(nombre, DatosWav.leerWavDesdeArchivo(ruta));
	}

	// TODO hacer algo con estas variables
	public Sonido(final String nombre, final String ruta, final int modeloDistancia, final float distMaxima,
			final float distReferencia, final float atenuacion) {
		this(nombre, ruta);

		this.modeloDistancia = modeloDistancia;
		AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, atenuacion);
		AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, distMaxima);
		AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, distReferencia);
		GestorSonidos.agregarSonido(this);
	}

	public static final void iniciar(final ExecutorService hiloOpenAL) {
		if (!iniciado) {
			Sonido.hiloOpenAL = hiloOpenAL;
			iniciado = true;
		}
	}

	public final void sonar() {
		if (estaSonando())
			return;
		GestorOpenAL.cambiarModeloDistancia(modeloDistancia);
		ejecutar(() -> AL10.alSourcePlay(source));
	}

	public final void parar() {
		if (estaSonando())
			ejecutar(() -> AL10.alSourceStop(source));
	}

	public final void pausar() {
		if (estaSonando())
			ejecutar(() -> AL10.alSourcePause(source));
	}

	// TODO supongo que esto no funcionará
	public final boolean estaSonando() {
		ejecutar(() -> {
			sonando = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
		});
		return sonando;
	}

	public final Sonido ponerVolumen(final float volumen) {
		ejecutar(() -> AL10.alSourcef(source, AL10.AL_GAIN, volumen));
		return this;
	}

	public final Sonido ponerTono(final float tono) {
		ejecutar(() -> AL10.alSourcef(source, AL10.AL_PITCH, tono));
		return this;
	}

	public final Sonido ponerPosicionSonido(final Vector3 posicion) {
		ejecutar(() -> AL10.alSource3f(source, AL10.AL_POSITION, posicion.x, posicion.y, posicion.z));
		return this;
	}

	public final Sonido ponerVelocidadSonido(final Vector3 velocidad) {
		ejecutar(() -> AL10.alSource3f(source, AL10.AL_VELOCITY, velocidad.x, velocidad.y, velocidad.z));
		return this;
	}

	public final String obtenerNombre() {
		return nombre;
	}

	public final void destruir() {
		ejecutar(() -> {
			AL10.alDeleteBuffers(buffer);
			AL10.alDeleteSources(source);
		});
	}

	private static final void ejecutar(final Runnable comando) {
		hiloOpenAL.execute(comando);
	}
}
