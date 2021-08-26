package com.lavyshyk.countrycity.ui.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lavyshyk.countrycity.R

class CustomStateBar(context: Context, attrs: AttributeSet?): ConstraintLayout(context, attrs) {

    private var mTvDistance: TextView? = null
    private var mTvDistanceId: Int? = null
    private var mIvLikeUp: ImageView? = null
    init {
        initView(context,attrs)
    }

   private fun initView(context: Context, attrs: AttributeSet?) {
       val view = inflate(context, R.layout.custom_state_bar,this)
       layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
       mTvDistance = view.findViewById(R.id.distance)

       attrs?.let {
           val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomStateBar,0,0)
           try {
//               mTvDistanceId = typeArray.getResourceId(R.styleable.CustomStateBar_text_distance, -1)
//               mTvDistanceId?.let { mTvDistance?.text = context.getString(it) }
           }catch (e: Exception){}
           finally {
               typeArray.recycle()
           }
       }
   }
}