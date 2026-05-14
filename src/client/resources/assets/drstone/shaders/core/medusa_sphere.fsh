#version 120


layout(std140) uniform SphereCenter {
    vec4 sphereCenter; // xyz = center, w = padding
};

layout(std140) uniform SphereRadius {
    vec4 sphereRadius; // x = radius, yzw = padding
};
void main() {

}
