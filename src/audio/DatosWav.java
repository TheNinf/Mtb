package audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import utils.exceptions.WavDataException;

public final class DatosWav {

	public final int formato;
	public final int frecuencia;
	public final ByteBuffer datos;

	private DatosWav(final int formato, final int frecuencia, final ByteBuffer datos) {
		this.formato = formato;
		this.frecuencia = frecuencia;
		this.datos = datos;
	}

	public static final DatosWav leerWavDesdeArchivo(final String ruta) throws WavDataException {
		BufferedInputStream stream = null;
		byte[] datos = null;
		try {
			stream = new BufferedInputStream(new FileInputStream("src/audio/" + ruta));
			datos = new byte[stream.available()];
			stream.read(datos);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		int pointer = 0;
		final char[] textos = new char[4];
		textos[0] = (char) datos[pointer++];
		textos[1] = (char) datos[pointer++];
		textos[2] = (char) datos[pointer++];
		textos[3] = (char) datos[pointer++];

		if (!compararString(textos, "RIFF"))
			throw new WavDataException("No se ha encontrado en la cabecera del archivo la palaba 'RIFF'.");

		pointer += 4;
		textos[0] = (char) datos[pointer++];
		textos[1] = (char) datos[pointer++];
		textos[2] = (char) datos[pointer++];
		textos[3] = (char) datos[pointer++];

		if (!compararString(textos, "WAVE"))
			throw new WavDataException("No se ha encontrado la definición 'WAVE' en el archivo.");

		textos[0] = (char) datos[pointer++];
		textos[1] = (char) datos[pointer++];
		textos[2] = (char) datos[pointer++];
		textos[3] = (char) datos[pointer++];

		if (!compararString(textos, "fmt "))
			throw new WavDataException("No se ha encontrado la definición 'fmt ' en el archivo.");

		pointer += 4 + 2;
		ByteBuffer buffer = wrap(datos, pointer, 2);
		final int canales = buffer.getShort();
		pointer += 2;

		if (canales != 1 && canales != 2)
			throw new WavDataException("Número de canales incorrectos: " + canales + ". Debe ser 1 o 2");

		buffer = wrap(datos, pointer, 4);
		final int frecuencia = buffer.getInt();

		pointer += 4 + 4 + 2;
		buffer = wrap(datos, pointer, 2);
		final int bitsPorMuestra = buffer.getShort();
		pointer += 2;

		if (bitsPorMuestra != 8 && bitsPorMuestra != 16)
			throw new WavDataException(
					"Número de bits por muestra incorrectos: " + bitsPorMuestra + ". Debe ser 8 o 16.");

		textos[0] = (char) datos[pointer++];
		textos[1] = (char) datos[pointer++];
		textos[2] = (char) datos[pointer++];
		textos[3] = (char) datos[pointer++];

		if (!compararString(textos, "data"))
			throw new WavDataException("No se ha encontrado la definición 'data' en el archivo.");

		buffer = wrap(datos, pointer, 4);
		final int tamDatos = buffer.getInt();
		pointer += 4;

		final byte[] sonido = new byte[tamDatos];
		System.arraycopy(datos, pointer, sonido, 0, tamDatos);

		final ByteBuffer bufferSonido = BufferUtils.createByteBuffer(tamDatos);
		bufferSonido.put(sonido).flip();

		return new DatosWav(bitsPorMuestra == 8 ? (canales == 1 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_STEREO8)
				: (canales == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16), frecuencia, bufferSonido);
	}

	private static final ByteBuffer wrap(final byte[] datos, final int pointer, final int longitud) {
		return ByteBuffer.wrap(datos, pointer, longitud).order(ByteOrder.LITTLE_ENDIAN);
	}

	private static final boolean compararString(final char[] texto, final String string) {
		if (texto.length != string.length())
			return false;
		for (short i = 0; i < texto.length; i++)
			if (texto[i] != string.charAt(i))
				return false;
		return true;
	}
}
