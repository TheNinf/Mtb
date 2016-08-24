#version 400 core

layout (location = 0) in vec3 position;

out vec3 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1);
	textureCoords = position;
}