package com.me.mygdxgame.spatialPartitioning;

public class EntityCollider {
	public static boolean collide(Entity2D i, Entity2D j)
	{
		 // displacement from i to j 
        float y = (j.old_y - i.old_y); 
        float x = (j.old_x - i.old_x); 

        // distance squared 
        float d2 = x * x + y * y; 

        // dividing by 0 is bad 
        if (d2 == 0.) 
            return false; 

        // if d^2 < (30)^2, the disks have collided 
        if (d2 < 900.) { 
            float kii, kji, kij, kjj; 

            kji = (x * i.old_vx + y * i.old_vy) / d2; // k of j due to i 
            kii = (x * i.old_vy - y * i.old_vx) / d2; // k of i due to i 
            kij = (x * j.old_vx + y * j.old_vy) / d2; // k of i due to j 
            kjj = (x * j.old_vy - y * j.old_vx) / d2; // k of j due to j 
                        
            // set velocity of i 
            i.vy = kij * y + kii * x; 
            i.vx = kij * x - kii * y; 

            // set velocity of j 
            j.vy = kji * y + kjj * x; 
            j.vx = kji * x - kjj * y; 

            return true; 
        } 
        return false;

	}

}
