#version 400 core

layout (location = 0) out vec4 color;

in DATA
{
	vec4 position;
	vec2 uv;
	float tid;
	vec4 color;
} fs_in;

uniform vec2 lightPosition;
uniform sampler2D textures[32];

void main(void){
	if(fs_in.color.a == 0) {
		color = vec4(0);
		return;
	}

	float intensity = 0.5f / length(fs_in.position.xy - lightPosition);
	vec4 texColor = fs_in.color;
	color = fs_in.tid > 0 ? 
		fs_in.color * texture(textures[int(fs_in.tid - 1)], fs_in.uv)
			 : fs_in.color;
}