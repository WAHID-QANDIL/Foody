package org.wahid.foody.presentation.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.wahid.foody.R;

public class XStyleSwipeRefreshLayout extends FrameLayout {

    private static final String TEXT_SWIPE_TO_REFRESH = "Swipe to refresh";
    private static final String TEXT_RELEASE_TO_REFRESH = "Release to refresh";
    private static final String TEXT_REFRESHING = "Refreshing...";

    private static final int MAX_PULL_DISTANCE = 200;
    private static final int TRIGGER_DISTANCE = 150;

    private TextView refreshTextView;
    private View contentView;

    private float startY = 0f;
    private float currentPullDistance = 0f;
    private boolean isRefreshing = false;
    private boolean canRefresh = false;
    private boolean isPulling = false;

    private OnRefreshListener onRefreshListener;

    private float density;
    private int maxPullDistancePx;
    private int triggerDistancePx;

    public interface OnRefreshListener {
        void onRefresh();
    }

    public XStyleSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public XStyleSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XStyleSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        maxPullDistancePx = (int) (MAX_PULL_DISTANCE * density);
        triggerDistancePx = (int) (TRIGGER_DISTANCE * density);

        refreshTextView = new TextView(context);
        refreshTextView.setText(TEXT_SWIPE_TO_REFRESH);
        refreshTextView.setTextSize(14);
        refreshTextView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        refreshTextView.setGravity(Gravity.CENTER);
        refreshTextView.setAlpha(0f);

        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        textParams.topMargin = (int) (16 * density);
        refreshTextView.setLayoutParams(textParams);

        addView(refreshTextView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != refreshTextView) {
                contentView = child;
                break;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isRefreshing) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                isPulling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getY() - startY;
                if (deltaY > 0 && isContentAtTop()) {
                    isPulling = true;
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

@Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isRefreshing) {
            return super.onTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isPulling) {
                    float deltaY = ev.getY() - startY;
                    currentPullDistance = Math.min(deltaY * 0.5f, maxPullDistancePx);
                    updatePullProgress();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isPulling) {
                    if (canRefresh && onRefreshListener != null) {
                        startRefreshing();
                    } else {
                        resetPull();
                    }
                    isPulling = false;
                    performClick();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private boolean isContentAtTop() {
        if (contentView == null) {
            return true;
        }
        return !contentView.canScrollVertically(-1);
    }

    private void updatePullProgress() {
        // Calculate progress (0 to 1)
        float progress = Math.min(currentPullDistance / triggerDistancePx, 1f);

        // Update text alpha and position
        refreshTextView.setAlpha(progress);
        refreshTextView.setTranslationY(currentPullDistance * 0.3f);

        // Update content translation
        if (contentView != null) {
            contentView.setTranslationY(currentPullDistance);
        }

        // Update text based on whether we've pulled far enough
        canRefresh = currentPullDistance >= triggerDistancePx;
        if (canRefresh) {
            refreshTextView.setText(TEXT_RELEASE_TO_REFRESH);
            refreshTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        } else {
            refreshTextView.setText(TEXT_SWIPE_TO_REFRESH);
            refreshTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_secondary));
        }
    }

    private void startRefreshing() {
        isRefreshing = true;
        refreshTextView.setText(TEXT_REFRESHING);
        refreshTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        // Animate to a settled position
        ValueAnimator animator = ValueAnimator.ofFloat(currentPullDistance, triggerDistancePx * 0.5f);
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            if (contentView != null) {
                contentView.setTranslationY(value);
            }
            refreshTextView.setTranslationY(value * 0.3f);
        });
        animator.start();

        // Trigger the refresh callback
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    private void resetPull() {
        ValueAnimator animator = ValueAnimator.ofFloat(currentPullDistance, 0);
        animator.setDuration(250);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            currentPullDistance = value;

            if (contentView != null) {
                contentView.setTranslationY(value);
            }

            refreshTextView.setTranslationY(value * 0.3f);
            refreshTextView.setAlpha(value / triggerDistancePx);
        });
        animator.start();

        canRefresh = false;
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing && isRefreshing) {
            // Stop refreshing - animate back to top
            isRefreshing = false;
            resetPull();
        } else if (refreshing && !isRefreshing) {
            isRefreshing = true;
            refreshTextView.setText(TEXT_REFRESHING);
            refreshTextView.setAlpha(1f);
        }
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }
}