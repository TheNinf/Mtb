#version 400 core

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in vec2 out_size[];
in vec4 out_color[];
in float out_tid[];

out vec4 color;
out vec2 texture_coords;
out float tid;

uniform mat4 pr_matrix;

void main(void) {
	tid = out_tid[0];
	color = out_color[0];
	
	vec4 offset = vec4(0, -out_size[0].y, 0, 0);
	gl_Position = pr_matrix * (offset + gl_in[0].gl_Position);
	texture_coords = vec2(0, 1);
	EmitVertex();
	
	offset.x = out_size[0].x;
	gl_Position = pr_matrix * (offset + gl_in[0].gl_Position);
	texture_coords.x = 1.0f;
	EmitVertex();
	
	texture_coords.x = texture_coords.y = 0.0f;
	gl_Position = pr_matrix * gl_in[0].gl_Position;
	EmitVertex();
	
	offset.y = 0;
	texture_coords.x = 1.0f;
	gl_Position = pr_matrix * (offset + gl_in[0].gl_Position);
	EmitVertex();
	EndPrimitive();
}