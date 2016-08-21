#version 400 core

layout (location = 0) in vec2 position;

out vec2 textureCoords;

void main(void) {
	gl_Position = vec4(position, 0.0f, 1.0f);
	textureCoords = position.xy * 0.5f + 0.5f;
}