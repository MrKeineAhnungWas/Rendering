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

package de.javagl.rendering.core.jogl;

import com.jogamp.opengl.GL3;

import de.javagl.rendering.core.Renderer;
import de.javagl.rendering.core.gl.GLDataBuffer;
import de.javagl.rendering.core.gl.GLFrameBuffer;
import de.javagl.rendering.core.gl.GLGraphicsObject;
import de.javagl.rendering.core.gl.GLProgram;
import de.javagl.rendering.core.gl.GLRenderedObject;
import de.javagl.rendering.core.gl.GLRenderer;
import de.javagl.rendering.core.gl.GLTexture;
import de.javagl.rendering.core.handling.DataBufferHandler;
import de.javagl.rendering.core.handling.FrameBufferHandler;
import de.javagl.rendering.core.handling.GraphicsObjectHandler;
import de.javagl.rendering.core.handling.ProgramHandler;
import de.javagl.rendering.core.handling.RenderedObjectHandler;
import de.javagl.rendering.core.handling.TextureHandler;


/**
 * Implementation of a {@link Renderer} using JOGL
 */
public class JOGLRenderer implements GLRenderer
{
    /**
     * The handler for rendered objects
     */
    private final JOGLRenderedObjectHandler renderedObjectHandler;
    
    /**
     * Creates a new JOGLRenderer
     */
    public JOGLRenderer()
    {
        this.renderedObjectHandler = new JOGLRenderedObjectHandler();
    }
    
    /**
     * Set the current GL instance
     * 
     * @param gl The current GL instance
     */
    void setGL(GL3 gl)
    {
        renderedObjectHandler.setGL(gl);
    }
    
    
    @Override
    public RenderedObjectHandler<GLRenderedObject> getRenderedObjectHandler()
    {
        return renderedObjectHandler;
    }



    @Override
    public ProgramHandler<GLProgram> getProgramHandler()
    {
        return renderedObjectHandler.getProgramHandler();
    }


    @Override
    public GraphicsObjectHandler<GLGraphicsObject> getGraphicsObjectHandler()
    {
        return renderedObjectHandler.getGraphicsObjectHandler();
    }


    @Override
    public DataBufferHandler<GLDataBuffer> getDataBufferHandler()
    {
        return renderedObjectHandler.getGraphicsObjectHandler().getDataBufferHandler();
    }


    @Override
    public TextureHandler<GLTexture> getTextureHandler()
    {
        return renderedObjectHandler.getTextureHandler();
    }

    
    
    @Override
    public FrameBufferHandler<GLFrameBuffer> getFrameBufferHandler()
    {
        return renderedObjectHandler.getTextureHandler().getFrameBufferHandler();
    }
    
}


