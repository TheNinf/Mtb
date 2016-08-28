#version 400 core

layout (location = 0) out vec4 color;
layout (location = 1) out vec4 brightColor;

void main(void) {
	color = brightColor = vec4(1.0f, 0.0f, 1.0f, 0.5f);
}