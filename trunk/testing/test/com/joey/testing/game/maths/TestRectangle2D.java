package com.joey.testing.game.maths;

import com.joey.testing.game.shapes.Ellipse2D;
import com.joey.testing.game.shapes.Rectangle2D;
import com.joey.testing.game.shapes.Vector2D;

import junit.framework.TestCase;

public class TestRectangle2D extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	public void testConstructorsWorksCorrectly() {
		Rectangle2D r = new Rectangle2D(1, 2, 3, 4);

		assertEquals(1.0f, r.x);
		assertEquals(2.0f, r.y);
		assertEquals(3.0f, r.sizeX);
		assertEquals(4.0f, r.sizeY);
	}

	public void testContainsPointWorksCorrectlyWhenBothInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.contains(5, 5));
	}

	public void testContainsPointWorksCorrectlyWhenOnlyXInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(-5, 5));
	}

	public void testContainsPointWorksCorrectlyWhenOnlyYInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(5, -5));
	}

	public void testContainsPointWorksCorrectlyWhenBothOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(0, 0));
	}

	public void testContainsPointWorksCorrectlyWhenXOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(0, 5));
	}

	public void testContainsPointWorksCorrectlyWhenYOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(5, 0));
	}
	public void testContainsPointWorksCorrectlyWhenBothOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(10, 10));
	}

	public void testContainsPointWorksCorrectlyWhenXOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(5, 10));
	}

	public void testContainsPointWorksCorrectlyWhenYOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(10, 5));
	}

	public void testIntersectsPointWorksCorrectlyWhenBothInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(5, 5));
	}

	public void testIntersectsPointWorksCorrectlyWhenOnlyXInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.intersects(-5, 5));
	}

	public void testIntersectsPointWorksCorrectlyWhenOnlyYInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.intersects(5, -5));
	}

	public void testIntersectsPointWorksCorrectlyWhenBothOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(0, 0));
	}

	public void testIntersectsPointWorksCorrectlyWhenXOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(0, 5));
	}

	public void testIntersectsPointWorksCorrectlyWhenYOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(5, 0));
	}
	public void testIntersectsPointWorksCorrectlyWhenBothOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(10, 10));
	}

	public void testIntersectsPointWorksCorrectlyWhenXOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(5, 10));
	}

	public void testIntersectsPointWorksCorrectlyWhenYOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(10, 5));
	}

	public void testContainsVectorWorksCorrectlyWhenBothInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.contains(new Vector2D(5, 5)));
	}

	public void testContainsVectorWorksCorrectlyWhenOnlyXInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(-5, 5)));
	}

	public void testContainsVectorWorksCorrectlyWhenOnlyYInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(5, -5)));
	}

	public void testContainsVectorWorksCorrectlyWhenBothOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(0, 0)));
	}

	public void testContainsVectorWorksCorrectlyWhenXOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(0, 5)));
	}

	public void testContainsVectorWorksCorrectlyWhenYOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(5, 0)));
	}
	public void testContainsVectorWorksCorrectlyWhenBothOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(10, 10)));
	}

	public void testContainsVectorWorksCorrectlyWhenXOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(5, 10)));
	}

	public void testContainsVectorWorksCorrectlyWhenYOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.contains(new Vector2D(10, 5)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenBothInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(5, 5)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenOnlyXInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.intersects(new Vector2D(-5, 5)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenOnlyYInsideRectangle() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertFalse(r.intersects(new Vector2D(5, -5)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenBothOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(0, 0)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenXOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(0, 5)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenYOnRectangleMin() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(5, 0)));
	}
	public void testIntersectsVectorWorksCorrectlyWhenBothOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(10, 10)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenXOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(5, 10)));
	}

	public void testIntersectsVectorWorksCorrectlyWhenYOnRectangleMax() {
		Rectangle2D r = new Rectangle2D(0, 0, 10, 10);
		assertTrue(r.intersects(new Vector2D(10, 5)));
	}

	public void testContainsRectangleWorksCorrectlyWhenNeitherToucherOrContains(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(200,200,100,100);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneFullyContainsOtherRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,25,50,50);

		assertTrue(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneIntersectsOtherRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(0,0,100,100);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneContainsP1OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,25,100,100);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneContainsP2OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(-25,25,100,100);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneContainsP3OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,-25,100,100);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneContainsP4OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(-25,-25,100,100);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneIntersectsP1OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(0,25,50,50);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneIntersectsP2OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,0,50,50);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneIntersectsP3OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(50,25,50,50);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testContainsRectangleWorksCorrectlyWhenOneIntersectsP4OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,50,50,50);

		assertFalse(r1.contains(r2));
		assertFalse(r2.contains(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenNeitherToucherOrContains(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(200,200,100,100);

		assertFalse(r1.intersects(r2));
		assertFalse(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneFullyContainsOtherRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,25,50,50);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectRectangleWorksCorrectlyWhenOneIntersectsOtherRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(0,0,100,100);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneContainsP1OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,25,100,100);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneContainsP2OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(-25,25,100,100);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneContainsP3OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,-25,100,100);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneContainsP4OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(-25,-25,100,100);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneIntersectsP1OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(0,25,50,50);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneIntersectsP2OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,0,50,50);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneIntersectsP3OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(50,25,50,50);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}

	public void testIntersectsRectangleWorksCorrectlyWhenOneIntersectsP4OfRectangle(){
		Rectangle2D r1 = new Rectangle2D(0,0,100,100);
		Rectangle2D r2 = new Rectangle2D(25,50,50,50);

		assertTrue(r1.intersects(r2));
		assertTrue(r2.intersects(r1));
	}
	
	/**
	 * Rectangle Ellipse Test
	 */
	public void testContainsRectangleWorksCorrectlyContainsEllipse(){
		Rectangle2D r = new Rectangle2D(0,0,100,100);
		Ellipse2D e = new Ellipse2D(80,50,25,25);

		assertTrue(r.intersects(e));
		assertTrue(e.intersects(r));
	}
}
