#version 400 core

out vec4 out_color;

uniform sampler2D samplerTexture;
in vec2 textureCoords;

void main(void){
	vec4 color = texture(samplerTexture, textureCoords);
	float brightness = (color.r * 0.2126) + (color.g * 0.7152) + (color.b * 0.0722);
	out_color = vec4(brightness, brightness, brightness, color.a);
}