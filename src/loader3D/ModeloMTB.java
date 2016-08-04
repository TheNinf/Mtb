package loader3D;

public class ModeloMTB {

	private float[] vertices, normals, tangentes, bitangentes, textureCoords;
	private int[] indices;

	public ModeloMTB(float[] vertices, float[] normals, float[] tangentes, float[] bitangentes, float[] textureCoords,
			int[] indices) {
		this.vertices = vertices;
		this.normals = normals;
		this.tangentes = tangentes;
		this.bitangentes = bitangentes;
		this.textureCoords = textureCoords;
		this.indices = indices;
	}

	public float[] obtenerVertices() {
		return vertices;
	}

	public float[] obtenerNormals() {
		return normals;
	}

	public float[] obtenerTangentes() {
		return tangentes;
	}

	public float[] obtenerBitangentes() {
		return bitangentes;
	}

	public float[] obtenerTextureCoords() {
		return textureCoords;
	}

	public int[] obtenerIndices() {
		return indices;
	}
}
