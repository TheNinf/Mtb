#version 400 core

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec3 textureCoords;

void main(void) {
	vec3 unprojected;
	mat4 inverseProjection = inverse(projectionMatrix);
	mat3 inverseModelView = transpose(mat3(viewMatrix));
	
	vec4 vertex = vec4(-1, 1, 1, 1);
	unprojected = (inverseProjection * vertex).xyz;
	textureCoords = inverseModelView * unprojected;
	gl_Position = vertex;
	EmitVertex();
	
	vertex.y = -1.0f;
	unprojected = (inverseProjection * vertex).xyz;
	textureCoords = inverseModelView * unprojected;
	gl_Position = vertex;
	EmitVertex();
	
	vertex.x = vertex.y = 1.0f;
	unprojected = (inverseProjection * vertex).xyz;
	textureCoords = inverseModelView * unprojected;
	gl_Position = vertex;
	EmitVertex();
	
	vertex.y = -1.0f;
	unprojected = (inverseProjection * vertex).xyz;
	textureCoords = inverseModelView * unprojected;
	gl_Position = vertex;
	EmitVertex();
	EndPrimitive();
}