#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>

layout(std140) uniform Sphere {
        vec3 sphereCenter;
        float sphereRadius;
        vec4 cameraPos;
};

uniform sampler2D DepthSampler;

out vec4 fragColor;

float stepSize = 0.1;

float sphere_sample(in vec3 p, vec3 center){
    float dist = length(p - center);
    float distFromShell = abs(dist - sphereRadius); // 0 at shell surface

    float density = exp(-distFromShell*1.1);

    density = density;
    return density*0.02;
}

void main() {

        vec2 ndc = (gl_FragCoord.xy / ScreenSize) * 2.0 - 1.0;
        vec4 clipPos = vec4(ndc, -1.0, 1.0);
        float fovScale = 1.0 / ProjMat[1][1];
        vec3 viewDir = vec3(ndc.x * (ScreenSize.x / ScreenSize.y) * fovScale, ndc.y * fovScale, -1.0);
        vec3 rayDir = normalize(mat3(inverse(ModelViewMat)) * viewDir);

        vec3 rayOrigin = cameraPos.xyz;

        vec3 toSphere = sphereCenter - rayOrigin;
        float distToSphere = length(toSphere);
        float startT = max(0.0, distToSphere - sphereRadius * 1.5);
        float endT = distToSphere + sphereRadius * 1.5;


        float rawDepth = texture(DepthSampler, gl_FragCoord.xy /ScreenSize).r;
        float near = ProjMat[3][2] / (ProjMat[2][2] - 1.0);
        float far  = ProjMat[3][2] / (ProjMat[2][2] + 1.0);
        float linearDepth = near * far / (far - rawDepth * (far - near));
        float visualDepth = linearDepth / 64.0;

        float total = 0.0;

        for(float i=startT; i < endT; i+=stepSize){
            total += sphere_sample(rayOrigin+rayDir*i, sphereCenter);
        }

        total = clamp(total, 0.0, 1.0);

        fragColor = vec4(0.0, total, 0.0, total);

}

