# version 300 es

in vec3 position;
out vec3 vPosition;
uniform mat4 uMatrix;

void main() {
    gl_Position = uMatrix * vec4(position, 1.0);
    vPosition = position;
}