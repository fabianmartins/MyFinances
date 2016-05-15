package br.com.crosscut.android.myfinances.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

	private Paint linePaint;

	public MyTextView(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		init();
	}

	public MyTextView(Context context) {
		super(context);
		init();
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		//Resources myResources = getResources();
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(Color.WHITE);
		this.setTextColor(Color.WHITE);
		this.setEllipsize(TruncateAt.END);
	}

	@Override
	public void onDraw(Canvas canvas) {
		linePaint.setColor(this.getCurrentTextColor());

		// Left
		canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
		// Top
		canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
		// Right
	    canvas.drawLine(getMeasuredWidth(), 0, getMeasuredWidth(), getMeasuredHeight(), linePaint);
	    // Down
		canvas.drawLine(0 ,getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);
		canvas.save();
		super.onDraw(canvas);
		canvas.restore();
	}

}
