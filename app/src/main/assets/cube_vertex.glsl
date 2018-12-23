# version 300 es

uniform mat4 uMatrix;
in vec3 position;

void main(){

    gl_Position = uMatrix * vec4(position, 1.0);
}