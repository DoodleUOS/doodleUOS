uniform mat4 MVP;

attribute vec3 aPosition;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;

void main()
{
    gl_Position = MVP * vec4(aPosition.x, aPosition.y, aPosition.z, 1.0);
    vTexCoord = aTexCoord;
}