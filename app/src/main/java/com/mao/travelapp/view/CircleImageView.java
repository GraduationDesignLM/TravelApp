package com.mao.travelapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 圆形ImageView
 * 
 * @author 麦灿标
 * */
public class CircleImageView extends ImageView {

	private Context mContext;
	
	private Rect rect;
	private Paint paint;
	
	private Drawable mPreDrawable;
	private Bitmap mBitmap;
	
	public CircleImageView(Context context) {
		this(context, null);
	}
	
	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		init();
	}
	
	private void init() {
		rect = new Rect();
		paint = new Paint();
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		Drawable mDrawable = getDrawable();
//		if(mDrawable != null) {
//			int width = mDrawable.getIntrinsicWidth();
//			int height = mDrawable.getIntrinsicHeight();
//			int diameter = width > height ? height : width;
//			setMeasuredDimension(diameter, diameter);
//		} else {
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		}
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		Drawable mDrawable = getDrawable();
		if(mDrawable != null && mDrawable != mPreDrawable) {
			mPreDrawable = mDrawable;
			//只创建一次
			mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
			mBitmap = toRoundBitmap(mBitmap);
			mBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), getHeight(), false);
		}
		if(mBitmap != null) {
			rect.left = 0;
			rect.top = 0;
			rect.right = getWidth();
			rect.bottom = getHeight();
			paint.reset();
			
			canvas.drawBitmap(mBitmap, null, rect, paint);
		} else {
			super.onDraw(canvas);
		}
	}


	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}
}
