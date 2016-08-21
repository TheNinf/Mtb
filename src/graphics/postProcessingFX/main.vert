#version 400 core

layout (location = 0) in vec4 position;

out vec2 textureCoords;

void main(void) {
	gl_Position = position;
	textureCoords = position.xy * 0.5f + 0.5f;
}