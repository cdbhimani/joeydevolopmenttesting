package com.joey.aitesting.game.shapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.sun.awt.AWTUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WorldWrapper {

	public static void main(String input[]) throws InterruptedException{
		final int recCount[] = new int[1];
		final Rectangle2D reg = new Rectangle2D(60,60,140,140);
		final Rectangle2D world = new Rectangle2D(100,100,200,200);
		final Rectangle2D hold[] = new Rectangle2D[4];
		hold[0] = new Rectangle2D(0,0,5,5);
		hold[1] = new Rectangle2D(0,0,5,5);
		hold[2] = new Rectangle2D(0,0,5,5);
		hold[3] = new Rectangle2D(0,0,5,5);
		
		
		recCount[0] = getOverlapRegions(reg, world, hold);
		
		System.out.println("Found : "+recCount[0]);
		for(int i = 0; i < recCount[0]; i++){
			System.out.println(hold[i]);
		}
		
		
		float dx = -80;
		float dy = -80;
		reg.translate(dx,dy);
		world.translate(dx,dy);
		hold[0].translate(dx,dy);
		hold[1].translate(dx,dy);
		hold[2].translate(dx,dy);
		hold[3].translate(dx,dy);
		
		float scale = 1;
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
				if(recCount[0] > 0)
					drawRectangle(g, hold[0]);
				
				g.setColor(Color.green);
				if(recCount[0] > 1)
					drawRectangle(g, hold[1]);
				
				g.setColor(Color.BLUE);
				if(recCount[0] > 2)
					drawRectangle(g, hold[2]);
				
				g.setColor(Color.CYAN);
				if(recCount[0] > 3)
					drawRectangle(g, hold[3]);

				
			}
			
		};
		
		
		JFrame f = new JFrame();
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(panel);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(800, 600);
		f.setSize(300, 300);
		
		f.setVisible(true);
		AWTUtilities.setWindowOpacity(f, 0.2f);
		
		while(true)
		{
			Thread.sleep(100);
			reg.translate(2, 5);
			System.out.println(reg);
			recCount[0]=getOverlapRegions(reg, world, hold);
			f.repaint();
			
			if(reg.y1 > world.y2){
				float height = reg.getHeight();
				reg.y1 = world.y1-height;
				reg.y2 = reg.y1+height;
			}
			
			if(reg.x1 > world.x2){
				float width = reg.getWidth();
				reg.x1 = world.x1-width;
				reg.x2 = reg.x1+width;
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
		}else if((kPlusY^kPlusX) == flag){
			rst[0].x1 = reg.x1;
			rst[0].y1 = reg.y1;
			rst[0].x2 = world.x2;
			rst[0].y2 = world.y2;
			
			rst[1].x1 = world.x1;
			rst[1].y1 = world.y1;
			rst[1].x2 = reg.x2-world.getWidth();
			rst[1].y2 = reg.y2-world.getHeight();
			
			rst[2].x1 = world.x1;
			rst[2].y1 = reg.y1;
			rst[2].x2 = reg.x2-world.getWidth();
			rst[2].y2 =  world.y2;
			
			rst[3].x1 = reg.x1;
			rst[3].y1 = world.y1;
			rst[3].x2 = world.x2;
			rst[3].y2 = reg.y2-world.getHeight();
			return 4;
			
		}else if((kMinusY^kMinusX) == flag){
			rst[0].x1 = world.x1;
			rst[0].y1 = world.y1;
			rst[0].x2 = reg.x2;
			rst[0].y2 = reg.y2;
			
			rst[1].x1 = reg.x1+world.getWidth();
			rst[1].y1 = reg.y1+world.getHeight();
			rst[1].x2 = world.x2;
			rst[1].y2 = world.y2;
			
			rst[2].x1 = reg.x1+world.getWidth();
			rst[2].y1 = world.y1;
			rst[2].x2 = world.x2;
			rst[2].y2 = reg.y2;
			
			rst[3].x1 = world.x1;
			rst[3].y1 = reg.y1+world.getHeight();
			rst[3].x2 = reg.x2;
			rst[3].y2 = world.y2;
			return 4;
		}else if((kPlusY^kMinusX) == flag){
			System.out.println("HERE");
			rst[0].x1 = world.x1;
			rst[0].y1 = reg.y1;
			rst[0].x2 = reg.x2;
			rst[0].y2 = world.y2;
			
			rst[1].x1 = reg.x1+world.getWidth();
			rst[1].y1 = world.y1;
			rst[1].x2 = world.x2;
			rst[1].y2 = reg.y2-world.getHeight();
			
			rst[2].x1 = reg.x1+world.getWidth();
			rst[2].y1 = reg.y1;
			rst[2].x2 = world.x2;
			rst[2].y2 =  world.y2;
			
			rst[3].x1 = world.x1;
			rst[3].y1 = world.y1;
			rst[3].x2 = reg.x2;
			rst[3].y2 = reg.y2-world.getHeight();
			return 4;
			
		}else if((kMinusY^kPlusX) == flag){
			rst[0].x1 = reg.x1;
			rst[0].y1 = world.y1;
			rst[0].x2 = world.x2;
			rst[0].y2 = reg.y2;
			
			rst[1].x1 = world.x1;
			rst[1].y1 = reg.y1+world.getHeight();
			rst[1].x2 = reg.x2-world.getWidth();
			rst[1].y2 = world.y2;
			
			rst[2].x1 = world.x1;
			rst[2].y1 = world.y1;
			rst[2].x2 = reg.x2-world.getWidth();
			rst[2].y2 = reg.y2;
			
			rst[3].x1 = reg.x1;
			rst[3].y1 = reg.y1+world.getHeight();
			rst[3].x2 = world.x2;
			rst[3].y2 = world.y2;
			return 4;
		}
		boolean debug = false;
		if(debug){
			System.out.println("Minus X: "+leftPad(Integer.toBinaryString(flag), Integer.SIZE, '0'));		
			System.out.println("Minus X: "+leftPad(Integer.toBinaryString(kMinusX), Integer.SIZE, '0'));
			System.out.println("Minus Y: "+leftPad(Integer.toBinaryString(kMinusY), Integer.SIZE, '0'));
			System.out.println("Plus  X: "+leftPad(Integer.toBinaryString(kPlusX), Integer.SIZE, '0'));
			System.out.println("Plus  Y: "+leftPad(Integer.toBinaryString(kPlusY), Integer.SIZE, '0'));
		}
		rst[0].set(reg);
		return 1;
	}
	
	public static String leftPad(String val, int length, char c){
		StringBuilder build = new StringBuilder();
		int num = length-val.length();
		for(int i = 0; i < num; i++){
			build.append(c);
		}
		build.append(val);
		return build.toString();
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
