package com.joey.aitesting.game;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class VehicleControler implements KeyListener {

    public Vehicle vehicle;
    
    public VehicleControler(){
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) {
	
	if(vehicle ==  null)return;
	switch(e.getKeyChar()){
	    case 'q': vehicle.maxForce +=1;break;
	    case 'a': vehicle.maxForce -=1;break;
	    
	    case 'w': vehicle.maxSpeed +=1;break;
	    case 's': vehicle.maxSpeed -=1;break;
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
    }

}
