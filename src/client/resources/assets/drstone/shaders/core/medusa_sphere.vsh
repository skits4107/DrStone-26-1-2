#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;

void main() {
    gl_Position = vec4(Position.xy, 0.0, 1.0);
}