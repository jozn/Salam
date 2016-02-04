package com.shamchat.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ZoomableImageView extends ImageView {
    float bmHeight;
    float bmWidth;
    float bottom;
    Context context;
    float height;
    PointF last;
    float[] f27m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    float maxScale;
    float minScale;
    int mode;
    float origHeight;
    float origWidth;
    float redundantXSpace;
    float redundantYSpace;
    float right;
    float saveScale;
    PointF start;
    float width;

    /* renamed from: com.shamchat.gallery.ZoomableImageView.1 */
    class C10841 implements OnTouchListener {
        C10841() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            ZoomableImageView.this.mScaleDetector.onTouchEvent(event);
            ZoomableImageView.this.matrix.getValues(ZoomableImageView.this.f27m);
            float x = ZoomableImageView.this.f27m[2];
            float y = ZoomableImageView.this.f27m[5];
            PointF curr = new PointF(event.getX(), event.getY());
            switch (event.getAction()) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    ZoomableImageView.this.last.set(event.getX(), event.getY());
                    ZoomableImageView.this.start.set(ZoomableImageView.this.last);
                    ZoomableImageView.this.mode = 1;
                    break;
                case Logger.SEVERE /*1*/:
                    ZoomableImageView.this.mode = 0;
                    int yDiff = (int) Math.abs(curr.y - ZoomableImageView.this.start.y);
                    if (((int) Math.abs(curr.x - ZoomableImageView.this.start.x)) < 3 && yDiff < 3) {
                        ZoomableImageView.this.performClick();
                        break;
                    }
                case Logger.WARNING /*2*/:
                    if (ZoomableImageView.this.mode == 2 || (ZoomableImageView.this.mode == 1 && ZoomableImageView.this.saveScale > ZoomableImageView.this.minScale)) {
                        float deltaX = curr.x - ZoomableImageView.this.last.x;
                        float deltaY = curr.y - ZoomableImageView.this.last.y;
                        float scaleHeight = (float) Math.round(ZoomableImageView.this.origHeight * ZoomableImageView.this.saveScale);
                        if (((float) Math.round(ZoomableImageView.this.origWidth * ZoomableImageView.this.saveScale)) < ZoomableImageView.this.width) {
                            deltaX = 0.0f;
                            if (y + deltaY > 0.0f) {
                                deltaY = -y;
                            } else if (y + deltaY < (-ZoomableImageView.this.bottom)) {
                                deltaY = -(ZoomableImageView.this.bottom + y);
                            }
                        } else if (scaleHeight < ZoomableImageView.this.height) {
                            deltaY = 0.0f;
                            if (x + deltaX > 0.0f) {
                                deltaX = -x;
                            } else if (x + deltaX < (-ZoomableImageView.this.right)) {
                                deltaX = -(ZoomableImageView.this.right + x);
                            }
                        } else {
                            if (x + deltaX > 0.0f) {
                                deltaX = -x;
                            } else if (x + deltaX < (-ZoomableImageView.this.right)) {
                                deltaX = -(ZoomableImageView.this.right + x);
                            }
                            if (y + deltaY > 0.0f) {
                                deltaY = -y;
                            } else if (y + deltaY < (-ZoomableImageView.this.bottom)) {
                                deltaY = -(ZoomableImageView.this.bottom + y);
                            }
                        }
                        ZoomableImageView.this.matrix.postTranslate(deltaX, deltaY);
                        ZoomableImageView.this.last.set(curr.x, curr.y);
                        break;
                    }
                case Logger.FINE /*5*/:
                    ZoomableImageView.this.last.set(event.getX(), event.getY());
                    ZoomableImageView.this.start.set(ZoomableImageView.this.last);
                    ZoomableImageView.this.mode = 2;
                    break;
                case Logger.FINER /*6*/:
                    ZoomableImageView.this.mode = 0;
                    break;
            }
            ZoomableImageView.this.setImageMatrix(ZoomableImageView.this.matrix);
            ZoomableImageView.this.invalidate();
            return true;
        }
    }

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public final boolean onScaleBegin(ScaleGestureDetector detector) {
            ZoomableImageView.this.mode = 2;
            return true;
        }

        public final boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = ZoomableImageView.this.saveScale;
            ZoomableImageView zoomableImageView = ZoomableImageView.this;
            zoomableImageView.saveScale *= mScaleFactor;
            if (ZoomableImageView.this.saveScale > ZoomableImageView.this.maxScale) {
                ZoomableImageView.this.saveScale = ZoomableImageView.this.maxScale;
                mScaleFactor = ZoomableImageView.this.maxScale / origScale;
            } else if (ZoomableImageView.this.saveScale < ZoomableImageView.this.minScale) {
                ZoomableImageView.this.saveScale = ZoomableImageView.this.minScale;
                mScaleFactor = ZoomableImageView.this.minScale / origScale;
            }
            ZoomableImageView.this.right = ((ZoomableImageView.this.width * ZoomableImageView.this.saveScale) - ZoomableImageView.this.width) - ((ZoomableImageView.this.redundantXSpace * 2.0f) * ZoomableImageView.this.saveScale);
            ZoomableImageView.this.bottom = ((ZoomableImageView.this.height * ZoomableImageView.this.saveScale) - ZoomableImageView.this.height) - ((ZoomableImageView.this.redundantYSpace * 2.0f) * ZoomableImageView.this.saveScale);
            float x;
            float y;
            if (ZoomableImageView.this.origWidth * ZoomableImageView.this.saveScale <= ZoomableImageView.this.width || ZoomableImageView.this.origHeight * ZoomableImageView.this.saveScale <= ZoomableImageView.this.height) {
                ZoomableImageView.this.matrix.postScale(mScaleFactor, mScaleFactor, ZoomableImageView.this.width / 2.0f, ZoomableImageView.this.height / 2.0f);
                if (mScaleFactor < 1.0f) {
                    ZoomableImageView.this.matrix.getValues(ZoomableImageView.this.f27m);
                    x = ZoomableImageView.this.f27m[2];
                    y = ZoomableImageView.this.f27m[5];
                    if (mScaleFactor < 1.0f) {
                        if (((float) Math.round(ZoomableImageView.this.origWidth * ZoomableImageView.this.saveScale)) < ZoomableImageView.this.width) {
                            if (y < (-ZoomableImageView.this.bottom)) {
                                ZoomableImageView.this.matrix.postTranslate(0.0f, -(ZoomableImageView.this.bottom + y));
                            } else if (y > 0.0f) {
                                ZoomableImageView.this.matrix.postTranslate(0.0f, -y);
                            }
                        } else if (x < (-ZoomableImageView.this.right)) {
                            ZoomableImageView.this.matrix.postTranslate(-(ZoomableImageView.this.right + x), 0.0f);
                        } else if (x > 0.0f) {
                            ZoomableImageView.this.matrix.postTranslate(-x, 0.0f);
                        }
                    }
                }
            } else {
                ZoomableImageView.this.matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                ZoomableImageView.this.matrix.getValues(ZoomableImageView.this.f27m);
                x = ZoomableImageView.this.f27m[2];
                y = ZoomableImageView.this.f27m[5];
                if (mScaleFactor < 1.0f) {
                    if (x < (-ZoomableImageView.this.right)) {
                        ZoomableImageView.this.matrix.postTranslate(-(ZoomableImageView.this.right + x), 0.0f);
                    } else if (x > 0.0f) {
                        ZoomableImageView.this.matrix.postTranslate(-x, 0.0f);
                    }
                    if (y < (-ZoomableImageView.this.bottom)) {
                        ZoomableImageView.this.matrix.postTranslate(0.0f, -(ZoomableImageView.this.bottom + y));
                    } else if (y > 0.0f) {
                        ZoomableImageView.this.matrix.postTranslate(0.0f, -y);
                    }
                }
            }
            return true;
        }
    }

    public ZoomableImageView(Context context, AttributeSet attr) {
        super(context, attr);
        this.matrix = new Matrix();
        this.mode = 0;
        this.last = new PointF();
        this.start = new PointF();
        this.minScale = 1.0f;
        this.maxScale = 4.0f;
        this.saveScale = 1.0f;
        super.setClickable(true);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.matrix.setTranslate(1.0f, 1.0f);
        this.f27m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new C10841());
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bmWidth = (float) bm.getWidth();
        this.bmHeight = (float) bm.getHeight();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = (float) MeasureSpec.getSize(widthMeasureSpec);
        this.height = (float) MeasureSpec.getSize(heightMeasureSpec);
        float scale = Math.min(this.width / this.bmWidth, this.height / this.bmHeight);
        this.matrix.setScale(scale, scale);
        setImageMatrix(this.matrix);
        this.saveScale = 1.0f;
        this.redundantYSpace = this.height - (this.bmHeight * scale);
        this.redundantXSpace = this.width - (this.bmWidth * scale);
        this.redundantYSpace /= 2.0f;
        this.redundantXSpace /= 2.0f;
        this.matrix.postTranslate(this.redundantXSpace, this.redundantYSpace);
        this.origWidth = this.width - (this.redundantXSpace * 2.0f);
        this.origHeight = this.height - (this.redundantYSpace * 2.0f);
        this.right = ((this.width * this.saveScale) - this.width) - ((this.redundantXSpace * 2.0f) * this.saveScale);
        this.bottom = ((this.height * this.saveScale) - this.height) - ((this.redundantYSpace * 2.0f) * this.saveScale);
        setImageMatrix(this.matrix);
    }
}
