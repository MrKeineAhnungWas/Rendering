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

package de.javagl.rendering.geometry;

import java.nio.IntBuffer;

/**
 * This class offers a {@link MutableIntArray} view on an IntBuffer.
 */
final class IntBufferIntArray implements MutableIntArray
{
    /**
     * The buffer backing this IntArray
     */
    private final IntBuffer buffer;
    
    /**
     * Creates a new IntBufferIntArray, backed the given buffer.
     * The IntBufferIntArray will keep a reference to the 
     * given buffer. 
     * 
     * @param buffer The buffer
     */
    IntBufferIntArray(IntBuffer buffer)
    {
        this.buffer = buffer;
    }

    /**
     * Creates a new IntBufferIntArray, backed the given array. 
     * 
     * @param array The array
     */
    IntBufferIntArray(int array[])
    {
        this(array, 0, array.length);
    }
    
    /**
     * Creates a new IntIntArray, backed by the specified
     * portion of the given array. 
     * 
     * @param array The array
     * @param offset The offset inside the given array
     * @param size The size of this array
     */
    IntBufferIntArray(int array[], int offset, int size)
    {
        IntBuffer b = IntBuffer.wrap(array);
        b.position(offset);
        b.limit(offset+size);
        this.buffer = b.slice();
    }
    
    @Override
    public int getSize()
    {
        return buffer.capacity();
    }

    @Override
    public int get(int index)
    {
        return buffer.get(index);
    }
    
    @Override
    public void get(IntBuffer destination)
    {
        destination.put(buffer.slice());
    }

    @Override
    public void set(int index, int value)
    {
        buffer.put(index, value);
    }

    @Override
    public void set(IntBuffer source)
    {
        buffer.slice().put(source);
    }

    @Override
    public MutableIntArray subArray(int fromIndex, int toIndex)
    {
        int oldPosition = buffer.position();
        int oldLimit = buffer.limit();
        buffer.limit(toIndex);
        buffer.position(fromIndex);
        MutableIntArray result = new IntBufferIntArray(buffer.slice());
        buffer.position(oldPosition);
        buffer.limit(oldLimit);
        return result;
    }
    
}
