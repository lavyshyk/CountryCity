package com.lavyshyk.countrycity.ui.ext

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavyshyk.countrycity.COUNTRY_NAME_KEY_FOR_DIALOG
import com.lavyshyk.countrycity.R
import io.reactivex.rxjava3.disposables.CompositeDisposable

val DIALOG_WIDTH_DELTA_7: Float = 0.7F
lateinit var dis: CompositeDisposable

fun Activity.showAlertDialog() {
    val alertDialog = MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.hi_app))
        .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
    alertDialog.show()
}

private fun createDialog(activity: Activity): Dialog {
    val dialog = Dialog(activity)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        activity.window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
            activity.window?.decorView?.windowInsetsController.let { controller ->
                activity.window?.let {
                    WindowInsetsControllerCompat(it, it.decorView).apply {
//                    this.hide(WindowInsetsCompat.Type.statusBars())
//                    this.hide(WindowInsetsCompat.Type.navigationBars())
                        this.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            activity.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }



    return dialog
}

private fun Activity.withButtonContent(
    title: String?,
): Pair<Dialog, View> {
    val dialog = createDialog(this)
    dialog.setCanceledOnTouchOutside(false)
    val contentView = LayoutInflater.from(this)
        .inflate(R.layout.dialog_search, null)

    val tvTitle: TextView = contentView.findViewById(R.id.title_dialog)
    title?.let {
        tvTitle.text = it
        tvTitle.visibility = View.VISIBLE
    }

    return Pair(dialog, contentView)
}

private fun setContentView(dialog: Dialog, contentView: View) {
    dialog.setContentView(contentView)
    val window = dialog.window
    window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
    val resources = dialog.context
        .resources
    val params = contentView.layoutParams as FrameLayout.LayoutParams
    params.width = ((resources.displayMetrics.widthPixels * DIALOG_WIDTH_DELTA_7).toInt())
    contentView.layoutParams = params
}

fun Activity.showDialogQuickSearch(
    title: String?,
    @StringRes leftButtonTextId: Int,
    leftClickListener: View.OnClickListener?,
    @StringRes rightButtonTextId: Int,
    rightClickListener: View.OnClickListener?, bundle: Bundle
): Dialog {
    val (dialog, contentView) = withButtonContent(title)

    val btnLeft: Button = contentView.findViewById(R.id.mButtonNo)
    btnLeft.setText(leftButtonTextId)
    btnLeft.setOnClickListener {
        dialog.dismiss()
        leftClickListener?.onClick(it)
        dis.clear()

    }
    val btnRight: Button = contentView.findViewById(R.id.mButtonYes)
    btnRight.setText(rightButtonTextId)
    val mEditText: AppCompatEditText = contentView.findViewById(R.id.mETNameCountry)
    var tt = ""
    mEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            if (text.length > 2) {
                btnRight.isEnabled = true
            }
            tt = text
        }
    })
    btnRight.isEnabled = false
    btnRight.setOnClickListener {
        bundle.putString(COUNTRY_NAME_KEY_FOR_DIALOG, tt)

        rightClickListener?.onClick(it)
        dialog.dismiss()
        dis.clear()
    }

    setContentView(dialog, contentView)
    if (!this.isFinishing) {
        dialog.show()
        dis = CompositeDisposable()
    }
    return dialog
}

//fun query(t: String): MutableList<CountryDataDetailDto> {
//    var result = mutableListOf<CountryDataDetailDto>()
//    dis.add(
//        retrofitService.getInfoAboutCountry(t)
////            .debounce(300, TimeUnit.MILLISECONDS)
//            .map { it.transformToCountryDetailDto() }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                result = it
//            }, {
//                it.printStackTrace()
//            })
//    )
//    return result
//}
