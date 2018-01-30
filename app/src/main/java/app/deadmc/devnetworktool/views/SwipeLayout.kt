package app.deadmc.devnetworktool.views

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.deadmc.devnetworktool.R

class SwipeLayout : ViewGroup {
    val TAG = "SwipeLayout"

    val DRAG_EDGE_LEFT = 0x1
    val DRAG_EDGE_RIGHT = 0x1 shl 1

    val defaultFling = 0
    val dragEdge = DRAG_EDGE_RIGHT

    var mainLayoutClosedLeft = 0
    var mainLayoutClosedRight = 0

    var secondaryPartWidth = 0
    var speed = 1.00f
    var swipeLayoutCallback = {}

    private var mMinDistRequestDisallowParent = 0

    @Volatile private var mIsScrolling = false
    private lateinit var mainView: View
    private lateinit var secondaryView: ViewGroup
    private var arrayListSecondaryViews = ArrayList<View>()
    private lateinit var dragHelper: ViewDragHelper
    private lateinit var gestureDetector: GestureDetectorCompat

    private val dragHelperCallback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            dragHelper.captureChildView(mainView, pointerId)
            return false
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            setAlpha(left)
            deleteItem(left)

            super.onViewPositionChanged(changedView, left, top, dx, dy)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var result = 0
            when (dragEdge) {
                DRAG_EDGE_RIGHT -> result = (Math.max(
                        Math.min((left*speed).toInt(), mainLayoutClosedLeft),
                        mainLayoutClosedLeft - secondaryView.width
                ))

                else -> result = child.left
            }

            //Log.e(TAG,"clampViewPositionHorizontal left = $left, dx = $dx, result = $result")
            return result
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            //Log.e(TAG,"getViewHorizontalDragRange")
            when (dragEdge) {
                DRAG_EDGE_RIGHT -> return Math.max(
                        Math.min(left, mainView.left),
                        mainView.left - secondaryView.width
                )
                else -> return child.left
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val partOpened = -mainView.left > secondaryPartWidth*0.3
            val fullyOpened = mainView.width*(-0.6) > mainView.left
            //Log.e(TAG,"fullyOpened "+mainView.width*(-0.85)+ " > "+mainView.left)
            //Log.e(TAG,"partOpened "+ -mainView.left+ " > "+secondaryPartWidth*0.3)
            super.onViewReleased(releasedChild, xvel, yvel)

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> {
                    if (partOpened) {
                        if (fullyOpened)
                            openFully(true)
                        else
                            openPart(true)
                    } else {
                        closePart(true)
                    }
                }
            }


            invalidate()
        }
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        internal var hasDisallowed = false

        override fun onDown(e: MotionEvent): Boolean {
            mIsScrolling = false
            hasDisallowed = false
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            mIsScrolling = true
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mIsScrolling = true
            if (parent != null) {
                val shouldDisallow: Boolean

                if (!hasDisallowed) {
                    //shouldDisallow = distToClosestEdge >= mMinDistRequestDisallowParent
                    //shouldDisallow = distToClosestEdge <= 0
                    shouldDisallow = true
                    hasDisallowed = shouldDisallow
                } else {
                    shouldDisallow = true
                }

                // disallow parent to intercept touch event so that the layout will work
                // properly on RecyclerView or view that handles scroll gesture.
                //Log.e(TAG,"requestDisallowInterceptTouchEvent($shouldDisallow)")
                parent.requestDisallowInterceptTouchEvent(shouldDisallow)
            }

            return false
        }
    }

    var minFlingVelocity = defaultFling

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun init(context: Context?, attrs: AttributeSet?) {
        dragHelper = ViewDragHelper.create(this, 1f, dragHelperCallback)
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        gestureDetector = GestureDetectorCompat(context, gestureListener)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        gestureDetector.onTouchEvent(ev)
        dragHelper.processTouchEvent(ev)
        val settling = dragHelper.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = dragHelper.viewDragState == ViewDragHelper.STATE_IDLE && mIsScrolling

        val result = settling || idleAfterScrolled

        //Log.e(TAG,"onInterceptTouchEvent $result "+ev.toString())
        return result
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        dragHelper.processTouchEvent(event)

        return true
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onAttachedToWindow() {
        //Log.e(TAG,"onAttachedToWindow "+mainView.height)
        super.onAttachedToWindow()

        secondaryView.post{
            if (secondaryView != null) {
                secondaryPartWidth = 0
                val height = mainView.height
                secondaryView.layoutParams = ViewGroup.LayoutParams(secondaryView.width,height)
                for (i in 0 until secondaryView.childCount) {
                    arrayListSecondaryViews.add(secondaryView.getChildAt(i))
                    secondaryPartWidth += secondaryView.getChildAt(i).width
                    val width = secondaryView.getChildAt(i).width
                    secondaryView.getChildAt(i).layoutParams = LinearLayout.LayoutParams(width,height)

                    secondaryView.getChildAt(i).requestLayout()
                    secondaryView.getChildAt(i).invalidate()
                }
            }
            arrayListSecondaryViews.reverse()
            //secondaryView.invalidate()
        }
    }

    override fun onLayout(p0: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //Log.e(TAG,"onLayout"+mainView.height)
        for (index in 0 until childCount) {
            val child = getChildAt(index)

            var left: Int
            var right: Int
            var top: Int
            var bottom: Int
            bottom = 0
            top = bottom
            right = top
            left = right



            val minLeft = paddingLeft
            val maxRight = Math.max(r - paddingRight - l, 0)
            val minTop = paddingTop
            val maxBottom = Math.max(b - paddingBottom - t, 0)

            var measuredChildHeight = child.measuredHeight
            var measuredChildWidth = child.measuredWidth

            // need to take account if child size is match_parent
            val childParams = child.layoutParams
            var matchParentHeight = false
            var matchParentWidth = false

            if (childParams != null) {
                matchParentHeight = childParams.height == ViewGroup.LayoutParams.MATCH_PARENT || childParams.height == ViewGroup.LayoutParams.FILL_PARENT
                matchParentWidth = childParams.width == ViewGroup.LayoutParams.MATCH_PARENT || childParams.width == ViewGroup.LayoutParams.FILL_PARENT
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop
                childParams!!.height = measuredChildHeight
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft
                childParams!!.width = measuredChildWidth
            }

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> {
                    left = Math.max(r - measuredChildWidth - paddingRight - l, minLeft)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.max(r - paddingRight - l, minLeft)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_LEFT -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }
            }
            //Log.e(TAG,"child.layout($left, $top, $right, $bottom)")
            setInitValues()
            child.layout(left, top, right, bottom)

            /*
            if (child is ViewGroup) {
                for (index in 0 until child.childCount) {
                    val childInside = getChildAt(index)
                    childInside.layoutParams = LayoutParams(childInside.width,measuredChildHeight)
                }
            }
            */
        }
    }

    fun setInitValues() {
        mainLayoutClosedLeft = mainView.left
        mainLayoutClosedRight = mainView.right
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //Log.e(TAG,"onMeasure "+mainView.height)
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (childCount < 2) {
            throw RuntimeException("Layout must have two children")
        }

        val params = layoutParams

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var desiredWidth = 0
        var desiredHeight = 0

        // first find the largest child
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            desiredWidth = Math.max(child.measuredWidth, desiredWidth)
            desiredHeight = Math.max(child.measuredHeight, desiredHeight)
        }
        // create new measure spec using the largest child width
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode)
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode)

        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)


        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childParams = child.layoutParams

            if (childParams != null) {
                if (childParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumHeight = measuredHeight
                }

                if (childParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumWidth = measuredWidth
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            desiredWidth = Math.max(child.measuredWidth, desiredWidth)
            desiredHeight = Math.max(child.measuredHeight, desiredHeight)
        }


        val mainView = getChildAt(1)
        desiredHeight = mainView.measuredHeight
        desiredWidth = mainView.measuredWidth

        // taking accounts of padding
        desiredWidth += paddingLeft + paddingRight
        desiredHeight += paddingTop + paddingBottom

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth
        } else {
            if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = if (desiredWidth > measuredWidth) measuredWidth else desiredWidth
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight
        } else {
            if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = if (desiredHeight > measuredHeight) measuredHeight else desiredHeight
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount >= 2) {
            secondaryView = getChildAt(0) as ViewGroup
            mainView = getChildAt(1)
        }


    }

    fun openPart(animation: Boolean) {
        //Log.e(TAG, "openPart ")
        if (animation) {
            //Log.e(TAG, "finalLeft "+(mainView.right-secondaryPartWidth))
            dragHelper.smoothSlideViewTo(mainView, -secondaryPartWidth, mainView.top)
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun openFully(animation: Boolean) {
        //Log.e(TAG,"openFully")
        if (animation) {
            val leftValue = mainView.left
            dragHelper.smoothSlideViewTo(mainView, leftValue-secondaryView.width, mainView.top)
            ViewCompat.postInvalidateOnAnimation(this@SwipeLayout)
        } else {
            ViewCompat.postInvalidateOnAnimation(this)
        }


    }

    fun closePart(animation: Boolean) {
        //Log.e(TAG,"closePart")

        if (animation) {
            dragHelper.smoothSlideViewTo(mainView, 0, mainView.top)
            ViewCompat.postInvalidateOnAnimation(this@SwipeLayout)
        } else {
            ViewCompat.postInvalidateOnAnimation(this@SwipeLayout)
        }
    }

    fun setAlpha(left:Int) {
        var alpha = 1f
        when (dragEdge) {
            DRAG_EDGE_RIGHT -> {
                var width = mainView.width - secondaryPartWidth
                alpha = (width-(left+secondaryPartWidth)*(-2.0f))/width
            }
        }



        arrayListSecondaryViews.forEach {
            if (it.tag != null && it.tag.toString() == "hide") {
                it.alpha = alpha
            }
        }
    }

    fun deleteItem(left: Int) {
        //Log.e(TAG,"left $left")
        when (dragEdge) {
            DRAG_EDGE_RIGHT -> {
                if (left <= -width) {
                    swipeLayoutCallback()
                }
            }
        }
    }

    private fun pxToDp(px: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

}
