/*
 * www.javagl.de - Rendering
 * 
 * Copyright 2010-2016 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package de.javagl.rendering.core;

import java.util.Objects;


/**
 * Default implementation of a {@link Shader}.
 */
class DefaultShader implements Shader
{
    /**
     * The type of this shader
     */
    private final ShaderType type;
    
    /**
     * The source code of this shader
     */
    private final String source;

    /**
     * Creates a shader of the given type with the given
     * source code
     * 
     * @param type The type of the shader
     * @param source The source code of the shader
     */
    DefaultShader(ShaderType type, String source)
    {
        this.type = type;
        this.source = source;
    }

    @Override
    public ShaderType getType()
    {
        return type;
    }
    
    @Override
    public String getSource()
    {
        return source;
    }
    
    @Override
    public String toString()
    {
        return "DefaultShader[source="+source+"]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(source, type);
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null)
        {
            return false;
        }
        if (getClass() != object.getClass())
        {
            return false;
        }
        DefaultShader other = (DefaultShader)object;
        if (source == null)
        {
            if (other.source != null)
            {
                return false;
            }
        }
        else if (!source.equals(other.source))
        {
            return false;
        }
        if (type != other.type)
        {
            return false;
        }
        return true;
    }
    
}
