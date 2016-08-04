package audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public final class DatosWav {

	public final int formato;
	public final int frecuencia;
	public final ByteBuffer datos;

	private DatosWav(final AudioInputStream entradaAudio) {

		AudioFormat formato = entradaAudio.getFormat();
		final int canales = formato.getChannels(), bitsPorMuestra = formato.getSampleSizeInBits();
		this.formato = bitsPorMuestra == 8 ? (canales == 1 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_STEREO8)
				: (canales == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16);
		this.frecuencia = (int) formato.getSampleRate();
		final int bytesPorSegundo = formato.getFrameSize();

		final int totalBytes = (int) (entradaAudio.getFrameLength() * bytesPorSegundo);
		byte[] arrayDatos = new byte[totalBytes];
		this.datos = BufferUtils.createByteBuffer(totalBytes);

		try {
			entradaAudio.read(arrayDatos, 0, totalBytes);
			this.datos.put(arrayDatos);
			this.datos.flip();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		arrayDatos = null;
		formato = null;
	}

	public static final DatosWav leerWavDesdeArchivo(final String archivo) {
		DatosWav resultado = null;

		BufferedInputStream entrada = null;
		AudioInputStream entradaAudio = null;
		try {
			entrada = new BufferedInputStream(DatosWav.class.getResourceAsStream(archivo));
			entradaAudio = AudioSystem.getAudioInputStream(entrada);
			resultado = new DatosWav(entradaAudio);
		} catch (Exception e) {

		} finally {
			try {
				if (entrada != null) {
					entrada.close();
					entrada = null;
				}
				if (entradaAudio != null) {
					entradaAudio.close();
					entradaAudio = null;
				}
			} catch (final Exception e) {

			}
		}
		return resultado;
	}
}
