package graphics;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import maths.Matrix4;
import maths.Vector2;
import maths.Vector3;
import utils.UtilidadesArchivo;

public class Shader {

	private final int shaderID;

	private final Map<String, Integer> variables;

	public Shader(final String vShader, final String fShader) {
		shaderID = leerShader(vShader, fShader);
		variables = new HashMap<>();
	}

	public Shader(final String vShader, final String gShader, final String fShader) {
		shaderID = leerShader(vShader, gShader, fShader);
		variables = new HashMap<>();
	}

	public final void uniformMatrix4(final String nombre, final Matrix4 matriz) {
		comprobarVariable(nombre);
		GL20.glUniformMatrix4fv(variables.get(nombre), false, matriz.elementos);
	}

	public final void uniformVector3(final String nombre, final Vector3 vector) {
		comprobarVariable(nombre);
		GL20.glUniform3f(variables.get(nombre), vector.x, vector.y, vector.z);
	}

	public final void uniformVector2(final String nombre, final Vector2 vector) {
		comprobarVariable(nombre);
		GL20.glUniform2f(variables.get(nombre), vector.x, vector.y);
	}

	public final void uniformInt(final String nombre, final int valor) {
		comprobarVariable(nombre);
		GL20.glUniform1i(variables.get(nombre), valor);
	}

	public final void uniformArrayInt(final String nombre, final int[] valores) {
		comprobarVariable(nombre);
		GL20.glUniform1iv(variables.get(nombre), valores);
	}

	public final void uniformFloat(final String nombre, final float valor) {
		comprobarVariable(nombre);
		GL20.glUniform1f(variables.get(nombre), valor);
	}

	public final void uniformMatrix3(final String nombre, final float[] valores) {
		comprobarVariable(nombre);
		GL20.glUniformMatrix3fv(variables.get(nombre), false, valores);
	}

	private final void comprobarVariable(final String nombre) {
		if (!variables.containsKey(nombre))
			variables.put(nombre, GL20.glGetUniformLocation(shaderID, nombre));
	}

	public final void enlazar() {
		GL20.glUseProgram(shaderID);
	}

	public final void desenlazar() {
		GL20.glUseProgram(0);
	}

	private final int leerShader(final String vShader, final String fShader) {
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

		GL20.glDetachShader(id, vID);
		GL20.glDetachShader(id, fID);
		GL20.glDeleteShader(vID);
		GL20.glDeleteShader(fID);

		return id;
	}

	private final int leerShader(final String vShader, final String gShader, final String fShader) {
		final int id = GL20.glCreateProgram();
		final int vID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		final int gID = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
		final int fID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vID, UtilidadesArchivo.leerArchivo(vShader));
		GL20.glCompileShader(vID);
		if (GL20.glGetShaderi(vID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(vID, 500));
			System.exit(-1);
		}

		GL20.glShaderSource(gID, UtilidadesArchivo.leerArchivo(gShader));
		GL20.glCompileShader(gID);
		if (GL20.glGetShaderi(gID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(gID, 500));
			System.exit(-1);
		}

		GL20.glShaderSource(fID, UtilidadesArchivo.leerArchivo(fShader));
		GL20.glCompileShader(fID);
		if (GL20.glGetShaderi(fID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(fID, 500));
			System.exit(-1);
		}

		GL20.glAttachShader(id, vID);
		GL20.glAttachShader(id, gID);
		GL20.glAttachShader(id, fID);

		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);

		GL20.glDetachShader(id, vID);
		GL20.glDetachShader(id, gID);
		GL20.glDetachShader(id, fID);
		GL20.glDeleteShader(vID);
		GL20.glDeleteShader(gID);
		GL20.glDeleteShader(fID);

		return id;
	}

	@Override
	protected final void finalize() {
		GL20.glDeleteProgram(shaderID);
		variables.clear();
	}
}
