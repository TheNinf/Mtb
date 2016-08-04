package graphics;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import maths.Mat4;
import maths.Vec2;
import utils.UtilidadesArchivo;

public class Shader {

	private final int shaderID;

	private final Map<String, Integer> variables;

	public Shader(final String vShader, final String fShader) {
		this.shaderID = leerShader(vShader, fShader);
		variables = new HashMap<>();
	}

	public void enlazar() {
		GL20.glUseProgram(shaderID);
	}

	public void desenlazar() {
		GL20.glUseProgram(0);
	}

	public void uniformMat4(final String nombre, final Mat4 matriz) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniformMatrix4fv(variables.get(nombre), false, matriz.elementos);
	}

	public void uniformVec2(final String nombre, final Vec2 vector) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniform2f(variables.get(nombre), vector.x, vector.y);
	}

	public void uniformVec2(final String nombre, final float x, final float y) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniform2f(variables.get(nombre), x, y);
	}

	public void uniformInt(final String nombre, final int valor) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniform1i(variables.get(nombre), valor);
	}

	public void uniformArrayInt(final String nombre, final int[] valores) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniform1iv(variables.get(nombre), valores);
	}

	public void uniformFloat(final String nombre, final float valor) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniform1f(variables.get(nombre), valor);
	}

	public void uniformMat3(final String nombre, final float[] valores) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
		GL20.glUniform3fv(variables.get(nombre), valores);
	}

	private int leerShader(final String vShader, final String fShader) {
		final int id = GL20.glCreateProgram();
		final int vID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		final int fID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vID, UtilidadesArchivo.leerArchivo(vShader));
		GL20.glCompileShader(vID);
		if (GL20.glGetShaderi(vID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(vID, 500));
			System.exit(-1);
		}

		GL20.glShaderSource(fID, UtilidadesArchivo.leerArchivo(fShader));
		GL20.glCompileShader(fID);
		if (GL20.glGetShaderi(fID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(fID, 500));
			System.exit(-1);
		}

		GL20.glAttachShader(id, vID);
		GL20.glAttachShader(id, fID);

		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);

		GL20.glDeleteShader(vID);
		GL20.glDeleteShader(fID);

		return id;
	}
}
