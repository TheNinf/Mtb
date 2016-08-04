package audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class Sonido {

	// TODO hacer limpieza de VAOS, VBOS, SONIDOS, FRAMEBUFFERS, TEXTURAS
	private final String nombre;
	private final int source;
	private final int buffer;
	private int modeloDistancia;

	public Sonido(final String nombre, final DatosWav datos) {
		this.nombre = nombre;

		this.source = AL10.alGenSources();
		this.buffer = AL10.alGenBuffers();

		AL10.alBufferData(buffer, datos.formato, datos.datos, datos.frecuencia);

		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		ponerPosicionSonido(0, 0, 0);
		ponerVelocidadSonido(0, 0, 0);

		modeloDistancia = AL11.AL_EXPONENT_DISTANCE;
		GestorSonidos.agregarSonido(this);
	}

	public Sonido(final String nombre, final String ruta) {
		this.nombre = nombre;

		this.source = AL10.alGenSources();
		this.buffer = AL10.alGenBuffers();

		DatosWav datos = DatosWav.leerWavDesdeArchivo(ruta);
		AL10.alBufferData(buffer, datos.formato, datos.datos, datos.frecuencia);
		datos = null;

		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		ponerPosicionSonido(0, 0, 0);
		ponerVelocidadSonido(0, 0, 0);

		modeloDistancia = AL11.AL_EXPONENT_DISTANCE;
		GestorSonidos.agregarSonido(this);
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

	public final void sonar() {
		if (estaSonando())
			return;
		GestorOpenAL.cambiarModeloDistancia(modeloDistancia);
		GestorOpenAL.sonar(this);
	}

	public final void parar() {
		if (estaSonando())
			GestorOpenAL.parar(this);
	}

	public final void pausar() {
		if (estaSonando())
			GestorOpenAL.pausar(this);
	}

	// TODO supongo que esto no funcionará
	public final boolean estaSonando() {
		return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public final Sonido ponerVolumen(final float volumen) {
		AL10.alSourcef(source, AL10.AL_GAIN, volumen);
		return this;
	}

	public final Sonido ponerTono(final float tono) {
		AL10.alSourcef(source, AL10.AL_PITCH, tono);
		return this;
	}

	public final Sonido ponerPosicionSonido(final float x, final float y, final float z) {
		AL10.alSource3f(source, AL10.AL_POSITION, x, y, z);
		return this;
	}

	public final Sonido ponerVelocidadSonido(final float x, final float y, final float z) {
		AL10.alSource3f(source, AL10.AL_VELOCITY, x, y, z);
		return this;
	}

	public final String obtenerNombre() {
		return nombre;
	}

	public final void destruir() {
		AL10.alDeleteBuffers(buffer);
		AL10.alDeleteSources(source);
	}

	public final int obtenerSource() {
		return source;
	}

}
