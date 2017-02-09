package com.softrice.rotatingcube;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by softrice on 17/2/8.
 */
public class RotatingCubeView extends View {

	private int colors[] = { 0xFFFAECA4 , 0xFFEEDC70 };

	private int viewWidth, viewHeight;
	private Camera camera;
	private Matrix matrix;
	private Paint paint;
	private RectF rectF;
	private ValueAnimator animator;
	private float value;

	public RotatingCubeView(Context context) {
		super(context);
		init();
	}

	public RotatingCubeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RotatingCubeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		camera = new Camera();
		matrix = new Matrix();
		paint = new Paint();
		paint.setAntiAlias(true);
		rectF = new RectF(-100,-100,100,100);

		animator = ValueAnimator.ofFloat(0,1).setDuration(20000);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				value = (float) animation.getAnimatedValue();
				invalidate();
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {

		float degree = value * 360;
		if(degree >= 0 && degree < 90 ) {
			drawCube(1,canvas);
			drawCube(0,canvas);
		}

		if(degree >= 90 && degree < 135) {
			drawCube(2,canvas);
			drawCube(1,canvas);
		}

		if(degree >= 135 && degree < 180) {
			drawCube(1,canvas);
			drawCube(2,canvas);
		}

		if(degree >= 180 && degree < 225) {
			drawCube(3,canvas);
			drawCube(2,canvas);
		}

		if(degree >= 225 && degree < 270) {
			drawCube(2,canvas);
			drawCube(3,canvas);
		}

		if(degree >= 270 && degree <= 360) {
			drawCube(3,canvas);
			drawCube(0,canvas);
		}

		camera.save();
		camera.rotateY(45);
		camera.rotateX(value * 360);
		camera.rotateY(90);
		camera.translate(100,0,100);
		camera.getMatrix(matrix);
		camera.restore();

		// control center
		matrix.preTranslate(-viewWidth / 2, -viewHeight / 2);
		matrix.postTranslate(viewWidth / 2, viewHeight / 2);

		canvas.save();
		canvas.concat(matrix);
		canvas.translate(viewWidth / 2, viewHeight / 2);
		paint.setColor(0xFFFDFDE3);
		canvas.drawRect(rectF, paint);
		canvas.restore();

	}

	/**
	 *
	 * @param position side of cube 0~3
	 * @param canvas
	 */
	void drawCube(int position,Canvas canvas) {
		camera.save();
		camera.rotateY(45);
		camera.rotateX(value * 360 + position % 2 * 90);
		switch (position) {
			case 0:
				break;
			case 1:
				camera.translate(0,100,100);
				break;
			case 2:
				camera.translate(0,0,200);
				break;
			case 3:
				camera.translate(0,100,-100);
				break;

		}
		camera.getMatrix(matrix);
		camera.restore();

		// control center
		matrix.preTranslate(-viewWidth / 2, -viewHeight / 2);
		matrix.postTranslate(viewWidth / 2, viewHeight / 2);

		canvas.save();
		canvas.concat(matrix);
		canvas.translate(viewWidth / 2, viewHeight / 2);
		paint.setColor(colors[position % 2]);
		canvas.drawRect(rectF, paint);
		canvas.restore();
	}



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = getMeasuredWidth();
		viewHeight = getMeasuredHeight();
	}

	@Override
	protected void onAttachedToWindow() {
		animator.start();
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		animator.cancel();
		super.onDetachedFromWindow();
	}


}
