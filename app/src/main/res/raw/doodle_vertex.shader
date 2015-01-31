uniform mat4 MVP;

attribute vec3 aPosition;
attribute vec4 aColor;

varying vec4 vColor;

void main()
{
    gl_Position = MVP * vec4(aPosition.x, aPosition.y, aPosition.z, 1.0);
    vColor = aColor;
}