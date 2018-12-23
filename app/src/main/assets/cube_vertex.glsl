# version 300 es

in vec3 position;
in vec4 vColor;
uniform mat4 uMatrix;
out vec4 color;

void main(){

    gl_Position = uMatrix * vec4(position, 1.0);
    color = vColor;
}