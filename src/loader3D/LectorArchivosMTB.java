package loader3D;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class LectorArchivosMTB {

	private LectorArchivosMTB() {
	}

	// TODO hacer que en un archivo puedan haber + de un objeto
	public static final ModeloMTB leerObjetoMTB(final String ruta) {
		ModeloMTB modelo = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(ruta));
			byte[] buffer = new byte[stream.available()];
			stream.read(buffer);

			int pointer = 0;
			int numeroVertices = leerInt(buffer, pointer);
			pointer += 4;

			float[] posiciones = new float[numeroVertices * 3];
			leerFloats(buffer, posiciones, pointer);
			pointer += posiciones.length * 4;

			float[] normals = new float[numeroVertices * 3];
			leerFloats(buffer, normals, pointer);
			pointer += normals.length * 4;

			float[] tangentes = new float[numeroVertices * 3];
			leerFloats(buffer, tangentes, pointer);
			pointer += tangentes.length * 4;

			float[] bitangentes = new float[numeroVertices * 3];
			leerFloats(buffer, bitangentes, pointer);
			pointer += bitangentes.length * 4;

			float[] textureCoords = new float[numeroVertices * 2];
			leerFloats(buffer, textureCoords, pointer);
			pointer += textureCoords.length * 4;

			int numeroIndices = leerInt(buffer, pointer);
			pointer += 4;

			int[] indices = new int[numeroIndices];
			leerInts(buffer, indices, pointer);

			modelo = new ModeloMTB(posiciones, normals, tangentes, bitangentes, textureCoords, indices);

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return modelo;
	}

	private static final int leerInt(final byte[] origen, int pointer) {
		return ByteBuffer.wrap(origen, pointer, 4).getInt();
	}

	private static final void leerInts(final byte[] origen, final int[] destino, int pointer) {
		for (int i = 0; i < destino.length; i++) {
			destino[i] = leerInt(origen, pointer);
			pointer += 4;
		}
	}

	private static final float leerFloat(final byte[] origen, final int pointer) {
		return Float.intBitsToFloat(leerInt(origen, pointer));
	}

	private static final void leerFloats(final byte[] origen, final float[] destino, int pointer) {
		for (int i = 0; i < destino.length; i++) {
			destino[i] = leerFloat(origen, pointer);
			pointer += 4;
		}
	}

}
