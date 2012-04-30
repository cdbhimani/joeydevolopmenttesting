package com.joey.aitesting.game.shapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WorldWrapper {

	public static void main(String input[]) throws InterruptedException{
	
		final Rectangle2D reg = new Rectangle2D(110,130,190,210);
		final Rectangle2D world = new Rectangle2D(100,100,200,200);
		final Rectangle2D hold[] = new Rectangle2D[4];
		hold[0] = new Rectangle2D(0,0,5,5);
		hold[1] = new Rectangle2D(0,0,5,5);
		hold[2] = new Rectangle2D(0,0,5,5);
		hold[3] = new Rectangle2D(0,0,5,5);
		
		
		final int num = getOverlapRegions(reg, world, hold);
		
		System.out.println("Found : "+num);
		for(int i = 0; i < num; i++){
			//System.out.println(hold[i]);
		}
		
		
		float dx = -50;
		float dy = -50;
		reg.translate(dx,dy);
		world.translate(dx,dy);
		hold[0].translate(dx,dy);
		hold[1].translate(dx,dy);
		hold[2].translate(dx,dy);
		hold[3].translate(dx,dy);
		
		float scale = 3;
		reg.scale(scale);
		world.scale(scale);
		hold[0].scale(scale);
		hold[1].scale(scale);
		hold[2].scale(scale);
		hold[3].scale(scale);
		
		
		
		JPanel panel = new JPanel(){
			
			public void fillRectangle(Graphics2D g, Rectangle2D r){
				g.fillRect((int)r.x1, (int)r.y1, (int)r.getWidth(), (int)r.getHeight());
			}
			
			public void drawRectangle(Graphics2D g, Rectangle2D r){
				g.drawRect((int)r.x1, (int)r.y1, (int)r.getWidth(), (int)r.getHeight());
			}
			@Override
			protected void paintComponent(Graphics g1) {
				setBackground(Color.white);
				// TODO Auto-generated method stub
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D)g1;
				
				g.setColor(Color.ORANGE);
				fillRectangle(g, world);
				
				g.setColor(Color.PINK);
				fillRectangle(g, reg);
				
				g.setColor(Color.red);
				drawRectangle(g, hold[0]);
				
				g.setColor(Color.green);
				drawRectangle(g, hold[1]);
				
				g.setColor(Color.BLUE);
				drawRectangle(g, hold[2]);
				
				g.setColor(Color.CYAN);
				drawRectangle(g, hold[3]);

				
			}
			
		};
		
		
		JFrame f = new JFrame();
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(panel);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		
		f.setVisible(true);
		
		
		while(true){
			Thread.sleep(100);
			reg.translate(0, 1);
			System.out.println(reg);
			getOverlapRegions(reg, world, hold);
			f.repaint();
			
			if(reg.y1 > world.y2){
				float width = reg.getHeight();
				reg.y1 = world.y1-width;
				reg.y2 = reg.y1+width;
			}
		}
	}
	/**
	Flags for overlap
	*/
	public static int kMinusX = 1;
	public static int kMinusY = 2;
	public static int kPlusX = 4;
	public static int kPlusY = 8;
	
	public static  int getOverlapRegions( Rectangle2D reg, Rectangle2D world, Rectangle2D rst[])
	{
		int flag = testOverlapFlag(reg, world);
		if(kMinusX == flag){
			rst[0].x1 = world.x1;
			rst[0].y1 = reg.y1;
			rst[0].x2 = reg.x2;
			rst[0].y2 = reg.y2;
			
			rst[1].x1 = reg.x1+world.getWidth();
			rst[1].y1 = reg.y1;
			rst[1].x2 = world.x2;
			rst[1].y2 = reg.y2;
			return 2;
		} else if(kPlusX == flag){
			rst[0].x1 = reg.x1;
			rst[0].y1 = reg.y1;
			rst[0].x2 = world.x2;
			rst[0].y2 = reg.y2;
			
			rst[1].x1 = world.x1;
			rst[1].y1 = reg.y1;
			rst[1].x2 = reg.x2-world.getWidth();
			rst[1].y2 = reg.y2;
			return 2;			
		} else if(kMinusY == flag){
			rst[0].x1 = reg.x1;
			rst[0].y1 = reg.y1+world.getHeight();
			rst[0].x2 = reg.x2;
			rst[0].y2 = world.y2;
			
			rst[1].x1 = reg.x1;
			rst[1].y1 = world.y1;
			rst[1].x2 = reg.x2;
			rst[1].y2 = reg.y2;
			return 2;
		} else if(kPlusY == flag){
			rst[0].x1 = reg.x1;
			rst[0].y1 = world.y1;
			rst[0].x2 = reg.x2;
			rst[0].y2 = reg.y2-world.getHeight();
			
			rst[1].x1 = reg.x1;
			rst[1].y1 = reg.y1;
			rst[1].x2 = reg.x2;
			rst[1].y2 = world.y2;
			return 2;
		}else{
			rst[0].set(reg);
			return 1;
		}
	}
	 
	/**
	The overlap check itself
	*/
	public static  int testOverlapFlag( Rectangle2D reg, Rectangle2D world)
	{
		int flags = 0;
		boolean x1 = world.x1 < reg.x1 && world.x2 > reg.x1;
		boolean x2 = world.x1 < reg.x2 && world.x2 > reg.x2;
		boolean y1 = world.y1 < reg.y1 && world.y2 > reg.y1;
		boolean y2 = world.y1 < reg.y2 && world.y2 > reg.y2;
		
		if((!x1 && !x2) || (!y1 && !y2)){
			return flags;
		}
		if(!x1 || !x2){
			if(x1&&!x2){
				flags |= kPlusX;
			}else{
				flags |= kMinusX;
			}
		}
		if(!y1 || !y2){
			if(y1&&!y2){
				flags |= kPlusY;
			}else{
				flags |= kMinusY;
			}
		}
		return flags;
	}
}
