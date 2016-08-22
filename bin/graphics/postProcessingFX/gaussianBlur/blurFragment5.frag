#version 150

out vec4 out_colour;
in vec2 blurTextureCoords[5];

uniform sampler2D originalTexture;

void main(void){
	
	out_colour = vec4(0.0);
	out_colour += texture(originalTexture, blurTextureCoords[0]) * 0.153388;
	out_colour += texture(originalTexture, blurTextureCoords[1]) * 0.221461;
	out_colour += texture(originalTexture, blurTextureCoords[2]) * 0.250301;
	out_colour += texture(originalTexture, blurTextureCoords[3]) * 0.221461;
	out_colour += texture(originalTexture, blurTextureCoords[4]) * 0.153388;

}