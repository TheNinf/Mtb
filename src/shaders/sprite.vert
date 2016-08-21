#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 size;
layout (location = 2) in vec4 color;
layout (location = 3) in float tid;

out vec2 out_size;
out vec4 out_color;
out float out_tid;

void main(void) {
	gl_Position = vec4(position, 1.0f);
	out_size = size;
	out_color = color;
	out_tid = tid;
}