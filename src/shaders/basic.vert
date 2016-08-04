#version 400 core 

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 uvs;
layout (location = 2) in vec4 color;
layout (location = 3) in float tid;

uniform mat4 vw_matrix = mat4(1.0f);
uniform mat4 ml_matrix = mat4(1.0f);
uniform mat4 pr_matrix;

out DATA
{
	vec4 position;	
	vec2 uv;
	float tid;
	vec4 color;
} vs_out;

void main(void) 
{
	vec4 worldPosition = vs_out.position = ml_matrix * position;
	gl_Position = pr_matrix * vw_matrix * worldPosition;
	vs_out.color = color;
	vs_out.uv = uvs;
	vs_out.tid = tid;
}