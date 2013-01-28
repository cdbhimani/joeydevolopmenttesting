
package com.esotericsoftware.spine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SkeletonTest extends ApplicationAdapter {
	SpriteBatch batch;
	float time;
	ShapeRenderer renderer;

	SkeletonData skeletonData;
	Skeleton skeleton;
	Animation animation;

	public void create () {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		Pixmap pixmap = new Pixmap(32, 32, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		final AtlasRegion fake = new AtlasRegion(new Texture(pixmap), 0, 0, pixmap.getWidth(), pixmap.getHeight());
		pixmap.dispose();
		TextureAtlas atlas = new TextureAtlas() {
			public AtlasRegion findRegion (String name) {
				return fake;
			}
		};

		SkeletonJson json = new SkeletonJson(atlas);
		skeletonData = json.readSkeletonData(Gdx.files.absolute(
				"C:/Users/joey.enfield/programming/Game Workarea/Test/spine-libgdx/example-skeleton.json"));
		animation = json.readAnimation(Gdx.files.internal(
				"C:/Users/joey.enfield/programming/Game Workarea/Test/spine-libgdx/example-animation.json"), skeletonData);

		skeleton = new Skeleton(skeletonData);
		// skeleton.setSkin("blue");
		skeleton.setToBindPose();

		Bone root = skeleton.getRootBone();
		root.x = 230;
		root.y = 10;
		root.scaleX = 1f;
		root.scaleY = 1f;
		skeleton.updateWorldTransform();
	}

	public void render () {
		time += Gdx.graphics.getDeltaTime();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setColor(Color.GRAY);

		if (animation != null) {
			animation.apply(skeleton, time, true);
		}
		skeleton.updateWorldTransform();
		skeleton.update(Gdx.graphics.getDeltaTime());
		skeleton.draw(batch);

		batch.end();

		skeleton.drawDebug(renderer);
	}

	public void resize (int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
	}

	public static void main (String[] args) throws Exception {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Skeleton Test";
		config.useGL20 = true;
		new LwjglApplication(new SkeletonTest(), config);
	}
}
