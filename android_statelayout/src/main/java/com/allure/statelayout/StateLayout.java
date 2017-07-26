package com.allure.statelayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * 状态布局管理
 * Created by Allure on 2017/7/24.
 */
public class StateLayout extends FrameLayout {

    private  View loadingView;
    private  View emptyView;
    private  View contentView;
    private  View errorView;

   protected  static final int LAYOUT_NONE = -1;

    public static final int STATE_LOADING = 0x10;
    public static final int STATE_ERROR = 0x11;
    public static final int STATE_EMPTY = 0x12;
    public static final int STATE_CONTENT = 0x13;

    private int displayState = -1;
    private int loadingLayoutId;
    private int errorLayoutId;
    private int emptyLayoutId;
    private int contentLayoutId;

    private OnStateChangeListener stateChangeListener;


    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttrs(context, attrs);
    }

    private void setupAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StateLayout);
        loadingLayoutId = typedArray.getResourceId(R.styleable.StateLayout_state_loading, LAYOUT_NONE);
        errorLayoutId = typedArray.getResourceId(R.styleable.StateLayout_state_empty, LAYOUT_NONE);
        emptyLayoutId = typedArray.getResourceId(R.styleable.StateLayout_state_content, LAYOUT_NONE);
        contentLayoutId = typedArray.getResourceId(R.styleable.StateLayout_state_error, LAYOUT_NONE);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("XStateController can only host 1 elements");
        } else {
            if (loadingLayoutId != LAYOUT_NONE) {
                loadingView = inflate(getContext(), loadingLayoutId, null);
                addView(loadingView);
            }
            if (errorLayoutId != LAYOUT_NONE) {
                errorView = inflate(getContext(), errorLayoutId, null);
                addView(errorView);
            }
            if (emptyLayoutId != LAYOUT_NONE) {
                emptyView = inflate(getContext(), emptyLayoutId, null);
                addView(emptyView);
            }
            if (contentLayoutId != LAYOUT_NONE) {
                contentView = inflate(getContext(), contentLayoutId, null);
                addView(contentView);
            }

            if (contentView == null) {
                if (childCount == 1) {
                    contentView = getChildAt(0);
                }
            }
            if (contentView == null) {
                throw new IllegalStateException("contentView can not be null");
            }

            for (int index = 0; index < getChildCount(); index++) {
                getChildAt(index).setVisibility(GONE);
            }

            if (loadingView != null) {
                setDisplayState(STATE_LOADING);
            } else {
                setDisplayState(STATE_CONTENT);
            }
        }

    }


    public void setDisplayState(int newState) {
        int oldState = displayState;

        if (newState != oldState) {

            switch (newState) {
                case STATE_LOADING:
                    notifyStateChange(oldState, newState, loadingView);
                    break;
                case STATE_ERROR:
                    notifyStateChange(oldState, newState, errorView);
                    break;
                case STATE_EMPTY:
                    notifyStateChange(oldState, newState, emptyView);
                    break;
                case STATE_CONTENT:
                    notifyStateChange(oldState, newState, contentView);
                    break;
            }

        }
    }

    private View getDisplayView(int oldState) {
        if (oldState == STATE_LOADING) return loadingView;
        if (oldState == STATE_ERROR) return errorView;
        if (oldState == STATE_EMPTY) return emptyView;
        return contentView;
    }


    private void notifyStateChange(int oldState, int newState, View enterView) {
        if (enterView != null) {

            displayState = newState;

            if (oldState != -1) {
                getStateChangeListener().onStateChange(oldState, newState);
                getStateChangeListener().animationState(getDisplayView(oldState), enterView);
            } else {
                enterView.setVisibility(VISIBLE);
                enterView.setAlpha(1);
            }
        }
    }


    public int getState() {
        return displayState;
    }

    public void showContent() {
        setDisplayState(STATE_CONTENT);
    }

    public void showEmpty() {
        setDisplayState(STATE_EMPTY);
    }

    public void showError() {
        setDisplayState(STATE_ERROR);
    }

    public void showLoading() {
        setDisplayState(STATE_LOADING);
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public View getErrorView() {
        return errorView;
    }

    public View getContentView() {
        return contentView;
    }

    public StateLayout setLoadingView(View loadingView) {
        if (this.loadingView != null) {
            removeView(this.loadingView);
        }
        this.loadingView = loadingView;
        addView(this.loadingView);
        this.loadingView.setVisibility(GONE);
        return this;
    }

    public StateLayout setErrorView(View errorView) {
        if (this.errorView != null) {
            removeView(this.errorView);
        }
        this.errorView = errorView;
        addView(this.errorView);
        this.errorView.setVisibility(GONE);
        return this;
    }

    public StateLayout setEmptyView(View emptyView) {
        if (this.emptyView != null) {
            removeView(this.emptyView);
        }
        this.emptyView = emptyView;
        addView(this.emptyView);
        this.emptyView.setVisibility(GONE);
        return this;
    }

    public StateLayout setContentView(View contentView) {
        if (this.contentView != null) {
            removeView(this.contentView);
        }
        this.contentView = contentView;
        addView(this.contentView);
        this.contentView.setVisibility(GONE);
        return this;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new SavedState(parcelable);
        savedState.state = this.displayState;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.displayState = savedState.state;
        setDisplayState(this.displayState);
    }


    static class SavedState extends BaseSavedState {
        int state;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            try {
                state = source.readInt();
            } catch (IllegalArgumentException e) {
                state = STATE_LOADING;
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

    }

    public void setStateChangeListener(OnStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public OnStateChangeListener getStateChangeListener() {
        if (stateChangeListener == null) {
            stateChangeListener = getDefaultStateChangeListener();
        }
        return stateChangeListener;
    }

    private OnStateChangeListener getDefaultStateChangeListener() {
        return new SimpleStateChangeListener();
    }

    public interface OnStateChangeListener {
        void onStateChange(int oldState, int newState);

        void animationState(View exitView, View enterView);
    }

    public static class SimpleStateChangeListener implements OnStateChangeListener {

        @Override
        public void onStateChange(int oldState, int newState) {

        }

        @Override
        public void animationState(final View exitView, final View enterView) {
            AnimatorSet set = new AnimatorSet();
            final ObjectAnimator enter = ObjectAnimator.ofFloat(enterView, View.ALPHA, 1f);
            ObjectAnimator exit = ObjectAnimator.ofFloat(exitView, View.ALPHA, 0f);


            set.playTogether(enter, exit);
            set.setDuration(400);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    enterView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    exitView.setAlpha(1);
                    exitView.setVisibility(GONE);
                    checkView(enterView);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            set.start();
        }

        private void checkView(View enterView) {
            int visibleChild = 0;
            FrameLayout parent = (FrameLayout) enterView.getParent();
            int childCount = parent.getChildCount();
            for (int index = 0; index < childCount; index++) {
                if (parent.getChildAt(index).getVisibility() == VISIBLE) {
                    visibleChild++;
                }
            }
            if (visibleChild < 1) {
                enterView.setVisibility(VISIBLE);
                enterView.setAlpha(1);
            }
        }
    }
}
