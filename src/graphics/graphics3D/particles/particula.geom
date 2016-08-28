#version 400 core

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

uniform mat4 projectionMatrix;

in mat4 transformationViewMatrix[];

void main(void) {
	mat4 totalMatrices = projectionMatrix * transformationViewMatrix[0];
	
	gl_Position = totalMatrices * vec4(-0.5f, 0.5f, 0.0f, 1.0f);
	EmitVertex();
	
	gl_Position = totalMatrices * vec4(-0.5f, -0.5f, 0.0f, 1.0f);
	EmitVertex();
	
	gl_Position = totalMatrices * vec4(0.5f, 0.5f, 0.0f, 1.0f);
	EmitVertex();
	
	gl_Position = totalMatrices * vec4(0.5f, -0.5f, 0.0f, 1.0f);
	EmitVertex();
	EndPrimitive();
}