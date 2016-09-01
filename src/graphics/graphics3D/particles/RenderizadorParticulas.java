package graphics.graphics3D.particles;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import graphics.Shader;
import graphics.Transform;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjetos;

public class RenderizadorParticulas {

	private static final int NUM_FLOATS = 16;
	private static final int TAM_PARTICULA = NUM_FLOATS * 4;

	private final int vaoID, vboID;
	private final FloatBuffer buffer;

	private Shader shader;

	public RenderizadorParticulas() {
		vaoID = GL30.glGenVertexArrays();
		vboID = GL15.glGenBuffers();

		GL30.glBindVertexArray(vaoID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TAM_PARTICULA * GestorParticulas.MAX_PARTICULAS, GL15.GL_DYNAMIC_DRAW);

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, TAM_PARTICULA, 0);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, TAM_PARTICULA, 16);
		GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, TAM_PARTICULA, 32);
		GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, TAM_PARTICULA, 48);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);

		buffer = BufferUtils.createFloatBuffer(NUM_FLOATS * GestorParticulas.MAX_PARTICULAS);

		shader = new Shader("src/graphics/graphics3D/particles/particula.vert",
				"src/graphics/graphics3D/particles/particula.geom", "src/graphics/graphics3D/particles/particula.frag");
		shader.enlazar();
		shader.uniformMatrix4("projectionMatrix", Transform.obtenerMatrizProyeccion());
		shader.desenlazar();
	}

	public void render(final Particula[] particulas, final short numeroParticulas, final boolean additiveBlending) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, additiveBlending ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);

		shader.enlazar();
		final Vector3 eje = PoolObjetos.VECTOR3.solicitar().set(0, 0, 1);
		final Vector3 escalado = PoolObjetos.VECTOR3.solicitar();

		comenzar();
		for (int i = 0; i < numeroParticulas; i++) {
			final Particula particula = particulas[i];
			final float escala = particula.obtenerEscalado();

			buffer.put(obtenerTransformationViewMatrix(particula.obtenerPosicion(),
					escalado.set(escala, escala, escala), particula.obtenerRotacion(), eje).elementos);
		}
		acabar();

		GL30.glBindVertexArray(vaoID);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, numeroParticulas);
		GL30.glBindVertexArray(0);

		PoolObjetos.VECTOR3.devolver(eje);
		PoolObjetos.VECTOR3.devolver(escalado);

		shader.desenlazar();

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private final void comenzar() {
		buffer.clear();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
	}

	private final void acabar() {
		buffer.flip();
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private final Matrix4 obtenerTransformationViewMatrix(final Vector3 posicion, final Vector3 escalado,
			final float rotacion, final Vector3 eje) {
		final Matrix4 transMatrix = Matrix4.trasladar(posicion, null);
		final Matrix4 viewMatrix = Transform.obtenerViewMatrix();

		transMatrix.elementos[Matrix4.m00] = viewMatrix.elementos[Matrix4.m00];
		transMatrix.elementos[Matrix4.m01] = viewMatrix.elementos[Matrix4.m10];
		transMatrix.elementos[Matrix4.m02] = viewMatrix.elementos[Matrix4.m20];

		transMatrix.elementos[Matrix4.m10] = viewMatrix.elementos[Matrix4.m01];
		transMatrix.elementos[Matrix4.m11] = viewMatrix.elementos[Matrix4.m11];
		transMatrix.elementos[Matrix4.m12] = viewMatrix.elementos[Matrix4.m21];

		transMatrix.elementos[Matrix4.m20] = viewMatrix.elementos[Matrix4.m02];
		transMatrix.elementos[Matrix4.m21] = viewMatrix.elementos[Matrix4.m12];
		transMatrix.elementos[Matrix4.m22] = viewMatrix.elementos[Matrix4.m22];

		Matrix4.rotar(rotacion, eje, transMatrix);
		Matrix4.escalar(escalado, transMatrix);

		return Matrix4.multiplicar(viewMatrix, transMatrix, null);
	}
}
