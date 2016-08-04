#version 400 core

layout (location = 0) in vec2 position;

out vec2 textureCoords;

void main(void) {
	gl_Position = vec4(position, 0, 1);
	textureCoords = position / 2 + 0.5;
}