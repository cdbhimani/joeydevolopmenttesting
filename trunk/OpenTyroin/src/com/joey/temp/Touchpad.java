package com.joey.temp;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ActorEvent;
import com.badlogic.gdx.scenes.scene2d.ActorEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ActorListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * A touch pad is a widget that functions as an on-screen joystick. The touch pad
 * has a radius that functions as the maximum distance from the center of the
 * widget that will cause {@link Actor#hit(float, float)} to return the touch pad.
 * The touch pad can also be configured with a given dead zone, similar to real
 * joysticks. A {@link TouchpadStyle} object may be passed to further customize
 * the look of the widget.
 * <p>
 * If a {@link TouchpadStyle} is set, the background {@link Drawable}'s minimun 
 * width and height will become this widget's preferred dimensions. Otherwise,
 * the preferred width and height will be diameter of the touchable area regardless
 * if the drawable's dimensions are smaller. Note that the knob may be drawn outside
 * of the widget's dimensions. Background is always stretched to fit.
 * <p>
 * As touch events are detected on the widget, it will provide notification to
 * any {@link ChangeListener}'s that are listening. A registered {@link
 * ChangeListener} may cancel the touch event (returning the touch position to
 * the center of the widget) by returning <code>true</code> from the listener's
 * {@link ChangeListener#changed(ChangeEvent, Actor)} method.
 * <p>
 * Movement readings can be retrieved either as x and y distances from the center
 * of the pad, or as percentages of x and y distance from center.
 * 
 * @author Josh Street
 */
public class Touchpad extends Widget {
   private TouchpadStyle style;
   
   private boolean touched;
   
   private float radius;
   private Circle padBounds;
   private float deadzone;
   private Circle padDZBounds;
   
   private Vector2 knobPosition;
   private Vector2 padValue;
   
   /**
    * Creates a new touch pad. Its width and height are determined by the size of
    * provided background {@link TextureRegion}. The range of movement detected
    * is controlled by the provided radius. The dead zone of the of pad is controlled
    * by the provided deadzone value. Both boundaries are centrally positioned
    * within the touch pad widget.
    * @param radius
    * The radius used to determine if the touch pad is "touched"
    * @param deadzone
    * The radius indicating the deadzone area of the touch pad
    * @param skin
    * The {@link Skin} to use
    */
   public Touchpad(float radius, float deadzone, Skin skin) {
      this(radius, deadzone, skin.get(TouchpadStyle.class));
   }
   
   /**
    * Creates a new touch pad. Its width and height are determined by the size of
    * provided background {@link TextureRegion}. The range of movement detected
    * is controlled by the provided radius. The dead zone of the of pad is controlled
    * by the provided deadzone value. Both boundaries are centrally positioned
    * within the touch pad widget.
    * @param radius
    * The radius used to determine if the touch pad is "touched"
    * @param deadzone
    * The radius indicating the deadzone area of the touch pad
    * @param skin
    * The {@link Skin} to use
    * @param styleName
    * The style within the {@link Skin} to use
    */
   public Touchpad(float radius, float deadzone, Skin skin, String styleName) {
      this(radius, deadzone, skin.get(styleName, TouchpadStyle.class));
   }
   
   /**
    * Creates a new touch pad. Its width and height are determined by the size of
    * provided background {@link TextureRegion}. The range of movement detected
    * is controlled by the provided radius. The dead zone of the of pad is controlled
    * by the provided deadzone value. Both boundaries are centrally positioned
    * within the touch pad widget.
    * @param radius
    * The radius used to determine if the touch pad is "touched"
    * @param deadzone
    * The radius indicating the deadzone area of the touch pad
    * @param style
    * The {@link TouchpadStyle}
    */
   public Touchpad(float radius, float deadzone, TouchpadStyle style) {
      if (radius < 0f)
         throw new IllegalArgumentException("radius may not be negative");
      this.radius = radius;
      
      if (deadzone >= this.radius || deadzone < 0)
         throw new IllegalArgumentException("deadzone must be less than radius and greater than 0");
      this.deadzone = deadzone;
      
      setStyle(style);
      setWidth(getPrefWidth());
      setHeight(getPrefHeight());
      
      addListener(new ActorListener() {
         
         @Override
         public boolean touchDown(ActorEvent event, float x, float y, int pointer, int button) {
            if (touched)
               return false;
            touched = true;
            calcPositionAndValue(x, y, event.getType());
            return true;
         }
         
         @Override
         public void touchDragged(ActorEvent event, float x, float y, int pointer) {
            calcPositionAndValue(x, y, event.getType());
         }
         
         @Override
         public void touchUp(ActorEvent event, float x, float y, int pointer, int button) {
            touched = false;
            calcPositionAndValue(x, y, event.getType());
         }
      });
      padBounds = new Circle(getWidth() / 2f, getHeight() / 2f, radius);
      padDZBounds = new Circle(getWidth() / 2f, getHeight() / 2f, deadzone);
      knobPosition = new Vector2(getWidth() /2f, getHeight() / 2f);
      padValue = new Vector2();
   }
   
   void calcPositionAndValue(float x, float y, Type type) {
      float oldPosX = knobPosition.x;
      float oldPosY = knobPosition.y;
      float oldValX = padValue.x;
      float oldValY = padValue.y;
      knobPosition.set(getWidth() / 2f, getHeight() / 2f);
      padValue.set(0f, 0f);
      switch (type) {
      case touchUp:
         break;
      default:
         if (!padDZBounds.contains(x, y)) {
            if (padBounds.contains(x, y)) {
               knobPosition.set(x, y);
               padValue.set(x - padBounds.x, y - padBounds.y).mul(1f / padBounds.radius);
            }
            else {
               Vector2 tmp = Vector2.tmp.set(x - padBounds.x, y - padBounds.y).nor();
               padValue.set(tmp);
               knobPosition.set(tmp.mul(padBounds.radius).add(padBounds.x, padBounds.y));
            }
         }
         break;
      }
      if ((oldValX != padValue.x || oldValY != padValue.y) && fire(new ChangeEvent())) {
         padValue.set(oldValX, oldValY);
         knobPosition.set(oldPosX, oldPosY);
      }
   }
   
   /**
    * Sets a new style to this touch pad. Setting style causes this widget
    * to call {@link Widget#invalidateHierarchy().
    * @param style
    * The new {@link TouchpadStyle} to be applied
    */
   public void setStyle(TouchpadStyle style) {
      if (style == null)
         throw new IllegalArgumentException("style cannot be null");
      this.style = style;
      invalidateHierarchy();
   }
   
   /**
    * Gets this touch pad's currently applied style. Changes to the return style
    * may not be visible until {@link Widget#invalidateHierarchy()} is called.
    * Also changes to this widget's sizing are not made until {@link Widget#pack()}
    * is called.
    * @return
    * The current applied {@link TouchpadStyle}
    */
   public TouchpadStyle getStyle() {
      return style;
   }
   
   @Override
   public Actor hit(float x, float y) {
      return padBounds.contains(x, y) ? this : null;
   }
   
   @Override
   public void layout() {
      // Recalc pad and deadzone bounds
      padBounds.set(getWidth() / 2f, getHeight() / 2f, radius);
      padDZBounds.set(getWidth()/ 2f, getHeight() / 2f, deadzone);
      // Recalc pad values and knob position
      knobPosition = new Vector2(getWidth() /2f, getHeight() / 2f);
      padValue = new Vector2();
   }

   @Override
   public void draw(SpriteBatch batch, float parentAlpha) {
      super.draw(batch, parentAlpha);
      
      final Drawable knob = style.knob;
      final Drawable bg = style.background;
      
      Color c = getColor();
      float x = getX();
      float y = getY();
      float w = getWidth();
      float h = getHeight();
      
      batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
      
      if (bg != null) {
         bg.draw(batch, x, y, w, h);
      }
      
      if (knob != null) {
         x += knobPosition.x - knob.getMinWidth() / 2f;
         y += knobPosition.y - knob.getMinHeight() / 2f;
         knob.draw(batch, x, y, knob.getMinWidth(), knob.getMinHeight());
      }
   }
   
   @Override
   public float getPrefWidth() {
      Drawable d = style.background;
      if (d != null)
         return d.getMinWidth();
      return 2 * radius;
   }
   
   @Override
   public float getPrefHeight() {
      Drawable d = style.background;
      if (d != null)
         return d.getMinHeight();
      return 2 * radius;
   }
   
   /**
    * Returns <code>true</code> if this touch pad is being touched, otherwise
    * <code>false</code>.
    */
   public boolean isTouched() {
      return touched;
   }
   
   /**
    * Set the radius of the circle where touch will be detected on the widget.
    * @param radius
    * The radius of the circle.
    */
   public void setRadius(float radius) {
      if (radius < 0f)
         throw new IllegalArgumentException("radius may not be negative");
      this.radius = radius;
      validate();
   }
   
   /**
    * Set the radius of the circle where any touches within this circle will
    * always measure 0
    * @param deadzone
    * The radius of the deadzone
    */
   public void setDeadzone(float deadzone) {
      if (deadzone >= this.radius || deadzone < 0)
         throw new IllegalArgumentException("deadzone must be less than radius and greater than 0");
      this.deadzone = deadzone;
      validate();
   }
   
   /**
    * Returns the x-position of the knob relative to the center of the widget.
    * @return
    * The x-position; positive direction is to the right
    */
   public float getKnobPositionX() {
      return knobPosition.x;
   }
   
   /**
    * Returns the y-position of the knob relative to the center of the widget.
    * @return
    * The y-position; positive direction is up
    */
   public float getKnobPositionY() {
      return knobPosition.y;
   }
   
   /**
    * Returns the percentage of x-position of the knob relative to the pad's
    * circular bounds
    * @return
    * The percentage of x-position; positive direction is to the right
    */
   public float getPadValueX() {
      return padValue.x;
   }
   
   /**
    * Returns the percentage of y-position of the knob relative to the pad's
    * circular bounds
    * @return
    * The percentage of y-position; positive direction is up
    */
   public float getPadValueY() {
      return padValue.y;
   }
   
   /**
    * The style for a {@link Touchpad}. Typically, both the background and
    * knob will be a texture or texture region.
    * <p>
    * The background {@link Drawable} will be used to calculate the preferred
    * width and height of the {@link Touchpad} this style applies to, if such
    * a background is supplied.
    * @see TextureRegion
    * @see Texture
    * @see Drawable
    * @see Touchpad
    * 
    * @author Josh Street
    */
   public static class TouchpadStyle {
      
      /**
       * The background for the touchpad, stretched in both x and y directions;
       * may be null if no background is needed, however the touchable radius
       * of the {@link Touchpad} this style applies to will be used to calculate
       * its preferred width and height instead.
       */
      public Drawable background;
      
      /**
       * The drawable for the knob. May be null if no knob texture is desired.
       * Typically will be a texture region. Is not stretched.
       */
      public Drawable knob;
      
      /**
       * Creates a new {@link TouchpadStyle} with no background or knob {@link
       * Drawable}s set
       */
      public TouchpadStyle() {

      }
      
      /**
       * Creates a new {@link TouchpadStyle}
       * @param background
       * The drawable used to draw a background, or <code>null</code>
       * if no background is desired
       * @param knob
       * The {@link Drawable} to use when drawing the knob of the {@link Touchpad}
       */
      public TouchpadStyle(Drawable background, Drawable knob) {
         this.background = background;
         this.knob = knob;
      }
      
      /**
       * Creates a new {@link TouchpadStyle} based on a existing one.
       * @param style
       * The style to copy from
       */
      public TouchpadStyle(TouchpadStyle style) {
         this.background = style.background;
         this.knob = style.knob;
      }
   }
}