
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class AssetsHelper {
	public static int assumedWidth;
	public static int assumedHeight;
	public static BitmapFont font;
	public static TextureAtlas allTexture;
	
	static final String usesLdpi = "ldpi";
	static final String usesMdpi = "mdpi";
	static final String usesHdpi = "hdpi";
	static final String usesXdpi = "xdpi";
	
	static String usesDpi;

	public static void load () {
		assumedWidth=480;//landscape else 320
		assumedHeight=320;//landscape else 480
		fixAssetResolutions();
		
		loadAssets();
	}
	public static void dispose () {
		font.dispose();
		allTexture.dispose();
	}
	private static void loadAssets() {
//		if(usesDpi==usesLdpi){
//			font = new BitmapFont(Gdx.files.internal("data/ravenscroft12.fnt"), Gdx.files.internal("data/ravenscroft12.png"), false);
//		}else if(usesDpi==usesMdpi){
//			font = new BitmapFont(Gdx.files.internal("data/ravenscroft18.fnt"), Gdx.files.internal("data/ravenscroft18.png"), false);
//		}else if(usesDpi==usesHdpi){
//			font = new BitmapFont(Gdx.files.internal("data/ravenscroft24.fnt"), Gdx.files.internal("data/ravenscroft24.png"), false);
//		}else if(usesDpi==usesXdpi){
//			font = new BitmapFont(Gdx.files.internal("data/ravenscroft36.fnt"), Gdx.files.internal("data/ravenscroft36.png"), false);
//		}
//		//System.out.println("Graphic assets used :"+usesDpi);
//		allTexture = new TextureAtlas(Gdx.files.internal("images/"+usesDpi+"/pack"));
	}

	private static void fixAssetResolutions() {
		/**
		 * We assume landscape view and take width as a criteria
		 * Major width in consideration are 320, 480, 800, 960
		 * 
		 */
		float landscapeWidth=Gdx.graphics.getWidth();
		//float landscapeHeight=Gdx.graphics.getHeight();
		float screenDensity=Gdx.graphics.getDensity();
		/*
		System.out.println("density:"+screenDensity);
		System.out.println("width:"+landscapeWidth+" height:"+landscapeHeight);
		System.out.println("PpcX:"+Gdx.graphics.getPpcX()+" PpcY:"+Gdx.graphics.getPpcY());
		System.out.println("PpiX:"+Gdx.graphics.getPpiX()+" PpiY:"+Gdx.graphics.getPpiY());
		*/
		if(landscapeWidth<=320){
			if (screenDensity <= 1.00f) {
				usesDpi=usesLdpi;
			} else {
				usesDpi=usesMdpi;
			}
		}else if(landscapeWidth<=480){
			if (screenDensity < 1.00f) {
				usesDpi=usesLdpi;
			} else if (screenDensity == 1.00f) {
				usesDpi=usesMdpi;
			} else if (screenDensity >= 2.00f){
				usesDpi=usesXdpi;
			}else{
				usesDpi=usesHdpi;
			}
		}else if(landscapeWidth<=854){
			if (screenDensity <= 1.00f) {
				usesDpi=usesHdpi;
			}else{
				usesDpi=usesXdpi;
			}
		}else{
			if (screenDensity < 1.00f) {
				usesDpi=usesHdpi;
			} else{
				usesDpi=usesXdpi;
			}
		}
		
		if(usesDpi==usesLdpi){
			assumedWidth=320;
			assumedHeight=240;
		}else if(usesDpi==usesMdpi){
			assumedWidth=480;
			assumedHeight=320;
		}else if(usesDpi==usesHdpi){
			assumedWidth=800;
			assumedHeight=480;
		}else if(usesDpi==usesXdpi){
			assumedWidth=960;
			assumedHeight=640;
		}else{
			loadDefaults();
			//System.out.println("No valid DPI, using default.");
		}
	}
	public static float convertWidth(float value) {
		float retValue=0.0f;
		if(usesDpi==usesLdpi){
			retValue=(value/480)*320;
		}else if(usesDpi==usesMdpi){
			retValue=value;
		}else if(usesDpi==usesHdpi){
			retValue=(value/480)*800;
		}else if(usesDpi==usesXdpi){
			retValue=(value/480)*960;
		}
		return retValue;
	}
	public static float convertHeight(float value) {
		float retValue=0.0f;
		if(usesDpi==usesLdpi){
			retValue=(value/320)*240;
		}else if(usesDpi==usesMdpi){
			retValue=value;
		}else if(usesDpi==usesHdpi){
			retValue=(value/320)*480;
		}else if(usesDpi==usesXdpi){
			retValue=(value/320)*640;
		}
		return retValue;
	}
	
	public static Sound getSound(String sndName) {
		return(Gdx.audio.newSound(Gdx.files.internal("sounds/"+sndName)));
	}
	public static void loadDefaults() {
		/*
		 * change here to check all Art asset sets in desktop mode
		 */
		
		/*
		assumedWidth=320;
		assumedHeight=240;
		usesDpi=usesLdpi;
		*/
		
		/* use this Default
		assumedWidth=480;//landscape else 320
		assumedHeight=320;//landscape else 480
		usesDpi=usesMdpi;
		//*/
		
		/*
		assumedWidth=800;
		assumedHeight=480;
		usesDpi=usesHdpi;
		*/
		
//		/*
		assumedWidth=960;
		assumedHeight=640;
		usesDpi=usesXdpi;
//		*/
		
		loadAssets();
	}

}
