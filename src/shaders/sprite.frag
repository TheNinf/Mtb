#version 400 core

out vec4 out_color;

in vec4 color;
in float tid;
in vec2 texture_coords;

uniform sampler2D textures[32];

void main(void) {
	out_color = tid > 0 ? texture(textures[int(tid - 1.0f)], texture_coords) :
		color;
}