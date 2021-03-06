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
package de.javagl.rendering.interaction.camera;


import java.awt.Point;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import de.javagl.rendering.core.view.Camera;
import de.javagl.rendering.core.view.CameraUtils;
import de.javagl.rendering.core.view.Cameras;
import de.javagl.rendering.core.view.Rectangle;
import de.javagl.rendering.core.view.View;

/**
 * This class is a {@link CameraBehavior} that performs the movement of 
 * the {@link Camera} of a {@link View} in an Arcball style. <br> 
 */
class ArcballCameraBehavior implements CameraBehavior
{
    /**
     * A factor to compute the zoom from a mouse wheel rotation
     */
    private static final float ZOOMING_SPEED = 0.1f;
    
    /**
     * The view this behavior operates on
     */
    private final View view;

    /**
     * A {@link Camera} instance storing the initial camera configuration
     */
    private final Camera initialCamera;
    
    /**
     * The previous (mouse) position
     */
    private final Point previousPosition = new Point();

    /**
     * The Quaternion describing the rotation when dragging started
     */
    private final Quat4f dragStartRotation = new Quat4f(0, 0, 0, 1);

    /**
     * The Quaternion describing the current rotation
     */
    private final Quat4f currentRotation = new Quat4f(0, 0, 0, 1);

    /**
     * The position in 3D space where dragging started
     */
    private final Vector3f dragStartPosition = new Vector3f();

    /**
     * The current position in 3D space
     */
    private final Vector3f currentDragPosition = new Vector3f();

    /**
     * Creates a new ArcballCameraBehavior for the {@link Camera} in 
     * the given {@link View}
     * 
     * @param view The {@link View}
     */
    ArcballCameraBehavior(View view)
    {
        this.view = view;
        this.initialCamera = Cameras.create();
        Camera camera = view.getCamera();
        set(initialCamera, camera);
        reset();
    }
    
    /**
     * Set the given target {@link Camera} to have the same configuration as
     * the given source {@link Camera}
     * 
     * @param target The target {@link Camera}
     * @param source The source {@link Camera}
     */
    private static void set(Camera target, Camera source)
    {
        target.setEyePoint(source.getEyePoint());
        target.setViewPoint(source.getViewPoint());
        target.setUpVector(source.getUpVector());
        target.setFovDegY(source.getFovDegY());
    }
    
    @Override
    public void reset()
    {
        Camera camera = view.getCamera();
        set(camera, initialCamera);

        Matrix4f rotation = CameraUtils.computeRotation(camera);
        rotation.transpose();
        currentRotation.set(rotation);
    }
    

    @Override
    public void startRotate(Point point)
    {
        mapOnArcball(point.x, point.y, dragStartPosition);
        dragStartRotation.set(currentRotation);
    }

    @Override
    public void doRotate(Point point)
    {
        mapOnArcball(point.x, point.y, currentDragPosition);
        float dot = dragStartPosition.dot(currentDragPosition);
        Vector3f tmp = new Vector3f();
        tmp.cross(dragStartPosition, currentDragPosition);

        Quat4f q = new Quat4f(tmp.x, tmp.y, tmp.z, dot);
        currentRotation.mul(q, dragStartRotation);

        updateRotation();
    }

    /**
     * Update the eyePoint and upVector according to the current rotation
     */
    private void updateRotation()
    {
        Camera camera = view.getCamera();

        Matrix4f currentMatrix = new Matrix4f();
        currentMatrix.set(currentRotation);
        currentMatrix.transpose();
        
        float currentEyeToViewDistance = 
            CameraBehaviorUtils.computeEyeToViewDistance(camera);
        
        Vector3f initialEyeToView = 
            CameraBehaviorUtils.computeEyeToViewVector(initialCamera);
        initialEyeToView.normalize();
        initialEyeToView.scale(currentEyeToViewDistance);
        currentMatrix.transform(initialEyeToView);
        
        Vector3f upVector = camera.getUpVector();
        Vector3f initialUpVector = initialCamera.getUpVector();
        currentMatrix.transform(initialUpVector, upVector);
        camera.setUpVector(upVector);
        
        Point3f eyePoint = new Point3f();
        eyePoint.sub(camera.getViewPoint(), initialEyeToView);
        camera.setEyePoint(eyePoint);
    }

    /**
     * Maps the given point onto the arcball
     * 
     * @param x The x-screen coordinate of the point
     * @param y The y-screen coordinate of the point
     * @param mappedPoint The point on the arcball in 3D
     */
    private void mapOnArcball(int x, int y, Vector3f mappedPoint)
    {
        Vector2f temp = new Vector2f();
        Rectangle viewport = view.getViewport();
        temp.x = ((float)x / (viewport.getWidth() / 2)) - 1.0f;
        temp.y = -(((float)y / (viewport.getHeight() / 2)) - 1.0f);
        float length = temp.length();
        if (length > 1.0f)
        {
            mappedPoint.x = temp.x / length;
            mappedPoint.y = temp.y / length;
            mappedPoint.z = 0.0f;
        }
        else
        {
            mappedPoint.x = temp.x;
            mappedPoint.y = temp.y;
            mappedPoint.z = (float) Math.sqrt(1.0f - length);
        }
    }

    @Override
    public void startMovement(Point point)
    {
        previousPosition.setLocation(point);
    }

    @Override
    public void doMovement(Point point)
    {
        Camera camera = view.getCamera();
        
        Vector3f delta = new Vector3f();
        delta.x = previousPosition.x - point.x;
        delta.y = point.y - previousPosition.y;
        
        Rectangle viewport = view.getViewport();
        delta.x /= viewport.getWidth();
        delta.y /= viewport.getHeight();
        
        float eyeToViewDistance = 
            CameraBehaviorUtils.computeEyeToViewDistance(camera);
        delta.scale(eyeToViewDistance);

        Matrix4f currentMatrix = new Matrix4f();
        currentMatrix.setIdentity();
        currentMatrix.set(currentRotation);
        currentMatrix.transpose();
        currentMatrix.transform(delta);

        CameraBehaviorUtils.translate(camera, delta);
        
        previousPosition.setLocation(point);
    }
    
    @Override
    public void zoom(float amount)
    {
        Camera camera = view.getCamera();
        CameraBehaviorUtils.zoom(camera, amount * ZOOMING_SPEED);
    }

    @Override
    public void translateZ(float amount)
    {
        Camera camera = view.getCamera();
        CameraBehaviorUtils.translateZ(camera, amount * ZOOMING_SPEED);
    }


}
