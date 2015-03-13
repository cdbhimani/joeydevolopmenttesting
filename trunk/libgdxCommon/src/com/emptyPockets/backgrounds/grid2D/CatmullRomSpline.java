package com.emptyPockets.backgrounds.grid2D;

import java.util.Vector;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** @author Xoppa */
public class CatmullRomSpline {
	/**
	 * Calculates the catmullrom value for the given position (t).
	 * 
	 * @param out
	 *            The Vector to set to the result.
	 * @param t
	 *            The position (0<=t<=1) on the spline
	 * @param points
	 *            The control points
	 * @param continuous
	 *            If true the b-spline restarts at 0 when reaching 1
	 * @param tmp
	 *            A temporary vector used for the calculation
	 * @return The value of out
	 */
	public static Vector2 calculate(final Vector2 out, final float t, final Vector2[] points, final boolean continuous, final Vector2 tmp) {
		final int n = continuous ? points.length : points.length - 3;
		float u = t * n;
		int i = (t >= 1f) ? (n - 1) : (int) u;
		u -= i;
		return calculate(out, i, u, points, continuous, tmp);
	}

	/**
	 * Calculates the catmullrom value for the given span (i) at the given
	 * position (u).
	 * 
	 * @param out
	 *            The Vector to set to the result.
	 * @param i
	 *            The span (0<=i<spanCount) spanCount = continuous ?
	 *            points.length : points.length - degree
	 * @param u
	 *            The position (0<=u<=1) on the span
	 * @param points
	 *            The control points
	 * @param continuous
	 *            If true the b-spline restarts at 0 when reaching 1
	 * @param tmp
	 *            A temporary vector used for the calculation
	 * @return The value of out
	 */
	public static Vector2 calculate(final Vector2 out, final int i, final float u, final Vector2[] points, final boolean continuous, final Vector2 tmp) {
		final int n = points.length;
		final float u2 = u * u;
		final float u3 = u2 * u;
		out.set(points[i]).mul(1.5f * u3 - 2.5f * u2 + 1.0f);
		if (continuous || i > 0)
			out.add(tmp.set(points[(n + i - 1) % n]).mul(-0.5f * u3 + u2 - 0.5f * u));
		if (continuous || i < (n - 1))
			out.add(tmp.set(points[(i + 1) % n]).mul(-1.5f * u3 + 2f * u2 + 0.5f * u));
		if (continuous || i < (n - 2))
			out.add(tmp.set(points[(i + 2) % n]).mul(0.5f * u3 - 0.5f * u2));
		return out;
	}

	/**
	 * Calculates the derivative of the catmullrom spline for the given position
	 * (t).
	 * 
	 * @param out
	 *            The Vector to set to the result.
	 * @param t
	 *            The position (0<=t<=1) on the spline
	 * @param points
	 *            The control points
	 * @param continuous
	 *            If true the b-spline restarts at 0 when reaching 1
	 * @param tmp
	 *            A temporary vector used for the calculation
	 * @return The value of out
	 */
	public static Vector2 derivative(final Vector2 out, final float t, final Vector2[] points, final boolean continuous, final Vector2 tmp) {
		final int n = continuous ? points.length : points.length - 3;
		float u = t * n;
		int i = (t >= 1f) ? (n - 1) : (int) u;
		u -= i;
		return derivative(out, i, u, points, continuous, tmp);
	}

	/**
	 * Calculates the derivative of the catmullrom spline for the given span (i)
	 * at the given position (u).
	 * 
	 * @param out
	 *            The Vector to set to the result.
	 * @param i
	 *            The span (0<=i<spanCount) spanCount = continuous ?
	 *            points.length : points.length - degree
	 * @param u
	 *            The position (0<=u<=1) on the span
	 * @param points
	 *            The control points
	 * @param continuous
	 *            If true the b-spline restarts at 0 when reaching 1
	 * @param tmp
	 *            A temporary vector used for the calculation
	 * @return The value of out
	 */
	public static Vector2 derivative(final Vector2 out, final int i, final float u, final Vector2[] points, final boolean continuous, final Vector2 tmp) {
		/*
		 * catmull'(u) = 0.5 *((-p0 + p2) + 2 * (2*p0 - 5*p1 + 4*p2 - p3) * u +
		 * 3 * (-p0 + 3*p1 - 3*p2 + p3) * u * u)
		 */
		final int n = points.length;
		final float u2 = u * u;
		// final float u3 = u2 * u;
		out.set(points[i]).mul(-u * 5 + u2 * 4.5f);
		if (continuous || i > 0)
			out.add(tmp.set(points[(n + i - 1) % n]).mul(-0.5f + u * 2 - u2 * 1.5f));
		if (continuous || i < (n - 1))
			out.add(tmp.set(points[(i + 1) % n]).mul(0.5f + u * 4 - u2 * 4.5f));
		if (continuous || i < (n - 2))
			out.add(tmp.set(points[(i + 2) % n]).mul(-u + u2 * 1.5f));
		return out;
	}

	public Vector2[] controlPoints;
	public boolean continuous;
	public int spanCount;
	private Vector2 tmp;
	private Vector2 tmp2;
	private Vector2 tmp3;

	public CatmullRomSpline() {
	}

	public CatmullRomSpline(final Vector2[] controlPoints, final boolean continuous) {
		set(controlPoints, continuous);
	}

	public CatmullRomSpline set(final Vector2[] controlPoints, final boolean continuous) {
		if (tmp == null)
			tmp = controlPoints[0].cpy();
		if (tmp2 == null)
			tmp2 = controlPoints[0].cpy();
		if (tmp3 == null)
			tmp3 = controlPoints[0].cpy();
		this.controlPoints = controlPoints;
		this.continuous = continuous;
		this.spanCount = continuous ? controlPoints.length : controlPoints.length - 3;
		return this;
	}

	public Vector2 valueAt(Vector2 out, float t) {
		final int n = spanCount;
		float u = t * n;
		int i = (t >= 1f) ? (n - 1) : (int) u;
		u -= i;
		return valueAt(out, i, u);
	}

	/** @return The value of the spline at position u of the specified span */
	public Vector2 valueAt(final Vector2 out, final int span, final float u) {
		return calculate(out, continuous ? span : (span + 1), u, controlPoints, continuous, tmp);
	}

	public Vector2 derivativeAt(Vector2 out, float t) {
		final int n = spanCount;
		float u = t * n;
		int i = (t >= 1f) ? (n - 1) : (int) u;
		u -= i;
		return derivativeAt(out, i, u);
	}

	/** @return The derivative of the spline at position u of the specified span */
	public Vector2 derivativeAt(final Vector2 out, final int span, final float u) {
		return derivative(out, continuous ? span : (span + 1), u, controlPoints, continuous, tmp);
	}

	/** @return The span closest to the specified value */
	public int nearest(final Vector2 in) {
		return nearest(in, 0, spanCount);
	}

	/**
	 * @return The span closest to the specified value, restricting to the
	 *         specified spans.
	 */
	public int nearest(final Vector2 in, int start, final int count) {
		while (start < 0)
			start += spanCount;
		int result = start % spanCount;
		float dst = in.dst2(controlPoints[result]);
		for (int i = 1; i < count; i++) {
			final int idx = (start + i) % spanCount;
			final float d = in.dst2(controlPoints[idx]);
			if (d < dst) {
				dst = d;
				result = idx;
			}
		}
		return result;
	}

	public float approximate(Vector2 v) {
		return approximate(v, nearest(v));
	}

	public float approximate(final Vector2 in, int start, final int count) {
		return approximate(in, nearest(in, start, count));
	}

	public float approximate(final Vector2 in, final int near) {
		int n = near;
		final Vector2 nearest = controlPoints[n];
		final Vector2 previous = controlPoints[n > 0 ? n - 1 : spanCount - 1];
		final Vector2 next = controlPoints[(n + 1) % spanCount];
		final float dstPrev2 = in.dst2(previous);
		final float dstNext2 = in.dst2(next);
		Vector2 P1, P2, P3;
		if (dstNext2 < dstPrev2) {
			P1 = nearest;
			P2 = next;
			P3 = in;
		} else {
			P1 = previous;
			P2 = nearest;
			P3 = in;
			n = n > 0 ? n - 1 : spanCount - 1;
		}
		float L1Sqr = P1.dst2(P2);
		float L2Sqr = P3.dst2(P2);
		float L3Sqr = P3.dst2(P1);
		float L1 = (float) Math.sqrt(L1Sqr);
		float s = (L2Sqr + L1Sqr - L3Sqr) / (2f * L1);
		float u = MathUtils.clamp((L1 - s) / L1, 0f, 1f);
		return (n + u) / spanCount;
	}

	public float locate(Vector2 v) {
		return approximate(v);
	}

	public float approxLength(int samples) {
		float tempLength = 0;
		for (int i = 0; i < samples; ++i) {
			tmp2.set(tmp3);
			valueAt(tmp3, (i) / ((float) samples - 1));
			if (i > 0)
				tempLength += tmp2.dst(tmp3);
		}
		return tempLength;
	}
}