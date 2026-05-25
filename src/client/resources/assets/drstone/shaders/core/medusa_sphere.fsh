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
        

vec4 shell_sample(vec3 p, float r, vec3 center, vec3 color, float fallOff, float densityScale, vec3 pointColor){
    float dist = length(p - center);
    float distFromShell = abs(dist - r); // 0 at shell surface

    float shellDistInRadii = max(0.0, 1.0 - (distFromShell / r));

    float density = exp(-distFromShell*fallOff) * densityScale;

    return vec4(mix(pointColor, color, shellDistInRadii), density);
}

vec4 medusa_sample(vec3 p){
    vec4 outerSphere = shell_sample(
        p,
        sphereRadius,
        sphereCenter,
        vec3(0.0, 1.0, 0.0),
        1.1,
        0.2,
        vec3(0.0)
    );

    vec4 midSphere = shell_sample(
        p,
        sphereRadius * (2.0 / 3.0),
        sphereCenter,
        vec3(0.9, 1.0, 0.9),
        1.5,
        0.1,
        outerSphere.rgb
    );

    vec4 innerSphere = shell_sample(
        p,
        sphereRadius / 3.0,
        sphereCenter,
        vec3(0.0, 0.25, 0.0),
        1.05,
        0.5,
        midSphere.rgb
    );

    float alpha = outerSphere.a + midSphere.a + innerSphere.a;

    vec3 color =
    outerSphere.rgb * outerSphere.a +
    midSphere.rgb * midSphere.a +
    innerSphere.rgb * innerSphere.a;

    color /= max(alpha, 0.0001);

    return vec4(color, alpha);
}

void main() {

        vec2 ndc = (gl_FragCoord.xy / ScreenSize) * 2.0 - 1.0;
        vec4 clipPos = vec4(ndc, -1.0, 1.0);
        float fovScale = 1.0 / ProjMat[1][1];
        vec3 viewDir = vec3(ndc.x * (ScreenSize.x / ScreenSize.y) * fovScale, ndc.y * fovScale, -1.0);
        vec3 rayDir = normalize(mat3(inverse(ModelViewMat)) * viewDir);

        vec3 toSphere = sphereCenter - cameraPos.xyz;
        float distToSphere = length(toSphere);


        float tClosest = dot(toSphere, rayDir);

        float distSqToRay = dot(toSphere, toSphere) - tClosest * tClosest;


        float radiusBuffer = max(1.4, -0.0375*sphereRadius+2.375) * sphereRadius;
        float effectiveRadius = sphereRadius + radiusBuffer;

        if (distSqToRay > effectiveRadius * effectiveRadius) {
            return; // this pixel ray cannot matter
        }


        float startT = max(0.0, distToSphere - sphereRadius * 1.5);
        float endT = distToSphere + sphereRadius * 1.5;

        float rawDepth = texture(DepthSampler, gl_FragCoord.xy /ScreenSize).r;
        float near = ProjMat[3][2] / (ProjMat[2][2] - 1.0);
        float far  = ProjMat[3][2] / (ProjMat[2][2] + 1.0);
        float linearDepth = near * far / (far - rawDepth * (far - near));



        vec4 total = vec4(0.0, 0.0, 0.0, 0.0);

        for(float i=startT; i < endT; i+=stepSize){
            if (i > linearDepth){
                break;
            }
            vec4 point = medusa_sample(cameraPos.xyz + rayDir * i);

            float a = point.a * stepSize;

            total.rgb += point.rgb * a;
            total.a += a;
        }


        total.rgb /= max(total.a, 0.0001);
        total = clamp(total, 0.0, 1.0);

        fragColor = total;//vec4(0.0, total, 0.0, total);

}

