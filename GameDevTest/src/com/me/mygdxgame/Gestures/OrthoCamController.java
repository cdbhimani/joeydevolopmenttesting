package com.me.mygdxgame.Gestures;

import java.util.HashMap;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class OrthoCamController extends InputAdapter {
   final OrthographicCamera camera;
   final Vector3 curr = new Vector3();
   final Vector3 last = new Vector3(-1, -1, -1);   
   final Vector3 delta = new Vector3();
   private HashMap<Integer, Vector2> pointers;
   Vector2 v1 = new Vector2();
   Vector2 v2 = new Vector2();
   float distance;
   int finger_one_pointer = -1;
   int finger_two_pointer = -1;
   float initialDistance = 0f;
   
   public OrthoCamController(OrthographicCamera camera) {
      this.camera = camera;
      this.pointers = new HashMap<Integer,Vector2>();
      distance = 0f;
   }
   
   @Override public boolean touchDown(int x, int y, int pointer, int button) {
      if(pointers.size() == 0) {
         // no fingers down so assign v1
         finger_one_pointer = pointer;
         v1 = new Vector2(x,y);
         pointers.put(pointer, v1);
//         Log.e("IsoTiledMap","First finger down");
      } else if (pointers.size() == 1) {
         // figure out which finger is down
         if (finger_one_pointer == -1) {
            //finger two is still down
            finger_one_pointer = pointer;
            v1 = new Vector2(x,y);
            pointers.put(pointer,v1);
            initialDistance = v1.dst(pointers.get(finger_two_pointer));
//            Log.e("IsoTiledMap","First finger down (again)");
         } else {
            //finger one is still down
            finger_two_pointer = pointer;
            v2 = new Vector2(x,y);
            pointers.put(pointer, v2);
            initialDistance = v2.dst(pointers.get(finger_one_pointer));
//            Log.e("IsoTiledMap","Second finger down");
         }
      }
      return false;
   }

   @Override public boolean touchDragged (int x, int y, int pointer) {
      if (pointers.size() < 2) {
         // one finger (drag)
         camera.unproject(curr.set(x, y,0));
         if(!(last.x == -1 && last.y == -1 && last.z == -1)) {
            camera.unproject(delta.set(last.x, last.y, 0));         
            delta.sub(curr);
            camera.position.add(delta.x, delta.y, 0);
         }
         last.set(x, y, 0);
      } else {
         // two finger pinch (zoom)
         // now fingers are being dragged so measure the distance and apply zoom
         if (pointer == finger_one_pointer) {
            v1 = new Vector2(x,y);
            v2 = pointers.get(finger_two_pointer);
            pointers.put(pointer,v1);
         } else {
            v2 = new Vector2(x,y);
            v1 = pointers.get(finger_one_pointer);
            pointers.put(pointer,v2);
         }
         distance = v2.dst(v1);
         camera.zoom = initialDistance / distance;
//         Log.e("IsoTiledMap", "Camera zoom: " + camera.zoom);
      }
      return false;
   }
   
   @Override public boolean touchUp(int x, int y, int pointer, int button) {
	   System.out.println("HERE");
      if (pointer == finger_one_pointer) {
         finger_one_pointer = -1;
//         Log.e("IsoTiledMap", "Finger one removed");
      } else {
         finger_two_pointer = -1;
//         Log.e("IsoTiledMap", "Finger two removed");
      }
      pointers.remove(pointer);
      last.set(-1, -1, -1);
      return false;
   }
}