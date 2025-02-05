package com.onesmock.Util.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.onesmock.R;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * 自定义布局组件
 * 
 * @author zihao
 * 
 */
public class MyGridLayout extends ViewGroup {
	int margin = 0;// 每个格子的水平和垂直间隔
	int colums = 0;
	int count = 0;

	GridAdatper adapter;

	@SuppressLint("Recycle")
	public MyGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.MyGridLayout);
			colums = a.getInteger(R.styleable.MyGridLayout_numColumns, 2);
			margin = (int) a.getInteger(R.styleable.MyGridLayout_itemMargin, 2);
		}
	}

	public MyGridLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyGridLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		count = getChildCount();
		if (count == 0) {
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);
			return;
		}

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int height = b - t;// 布局区域高度
		int width = r - l;// 布局区域宽度
		int rows =count % colums == 0 ? count / colums : count / colums + 1;// 行数
		if (count == 0)
			return;
		int gridW = (width - margin * (colums - 1)) / colums;// 格子宽度
		int gridH = (height - margin * rows) / rows;// 格子高度

		int left = 0;
		int top = margin;

		for (int i = 0; i < rows; i++) {// 遍历行
			for (int j = 0; j < colums; j++) {// 遍历每一行的元素
				View child = this.getChildAt(i * colums + j);
				if (child == null)
					return;
				left = j * gridW + j * margin;
				// 如果当前布局宽度和测量宽度不一样，就直接用当前布局的宽度重新测量
				if (gridW != child.getMeasuredWidth()
						|| gridH != child.getMeasuredHeight()) {
					child.measure(makeMeasureSpec(gridW, EXACTLY),
							makeMeasureSpec(gridH, EXACTLY));
				}
				child.layout(left, top, left + gridW, top + gridH);
			}
			top += gridH + margin;
		}
	}

	public interface GridAdatper {
		View getView(int index);

		int getCount();
	}

	/** 设置适配器 */
	public void setGridAdapter(GridAdatper adapter) {
		this.adapter = adapter;
		// 动态添加视图
		int size = adapter.getCount();
		for (int i = 0; i < size; i++) {
			addView(adapter.getView(i));
		}
	}

	public interface OnItemClickListener {
		void onItemClick(View v, int index);
	}

	/**
	 * 设置item点击事件
	 * 
	 * @param click
	 */
	public void setOnItemClickListener(final OnItemClickListener click) {
		if (this.adapter == null)
			return;
		for (int i = 0; i < adapter.getCount(); i++) {
			final int index = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					click.onItemClick(v, index);
				}
			});
		}
	}
}
