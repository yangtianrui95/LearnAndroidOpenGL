# version 300 es

in vec3 position;
out vec3 vPosition;
out vec4 vSpecular;

uniform mat4 uMatrix;
uniform mat4 uTransformMatrix;
uniform vec3 uLocation;
uniform vec3 uCamera;
uniform float uShininess;

// 散射光照计算
void pointLight(
        inout vec4 specular, // 散射光计算结果
        in vec3 lightLocation, // 光源位置
        in vec4 lightSpecular // 镜面光强度
        ) {
    vec3 newNormal = (uTransformMatrix * vec4(position, 1)).xyz;
    newNormal = normalize(newNormal);
    // 表面点到光源位置的距离
    vec3 vp = normalize(lightLocation - (uTransformMatrix * vec4(position, 1)).xyz);
    vp = normalize(vp);

    // 计算观察点到顶点的单位向量
    vec3 eye = normalize(uCamera - (uTransformMatrix * vec4(position, 1)).xyz);

    // 计算半角向量
    vec3 halfv = normalize(vp + eye);

    // 计算点乘结果,光照粗糙度
    float result = max(0.0, dot(newNormal, halfv));
    specular = result * lightSpecular;
}

void main() {
    gl_Position = uMatrix * vec4(position, 1.0);
    vPosition = position;

    // 计算光照
    vec4 specular = vec4(0.0, 0.0, 0.0, 0.0);
    pointLight(specular, uLocation, vec4(0.8, 0.8, 0.8, 1));
    vSpecular = specular;
}


