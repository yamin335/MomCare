package com.mom2b.androidApp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun String.getMilliFromDate(): Long {
    var date: Date? = null
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    try {
        date = formatter.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return date?.time ?: Long.MAX_VALUE
}

val String.colorValue
    get() = Color.parseColor(this)

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) {
        hideKeyboard(View(this))
    } else {
        hideKeyboard(currentFocus as View)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}

fun File.asFilePart(name: String): MultipartBody.Part {
    val attachedFile = this.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, this.name, attachedFile)
}

//fun Uri.asFile(context: Application) = FileUtils.getFileFromUri(context, this)

fun Bitmap.toFile(context: Context): File {
    //create a file to write bitmap customerMenu
    val f = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
    f.createNewFile()

    //Convert bitmap to byte array
    val os = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, os)
    val bitmapdata = os.toByteArray()

    //write the bytes in file
    val fos = FileOutputStream(f)
    fos.write(bitmapdata)
    fos.flush()
    fos.close()

    return f
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            afterTextChanged.invoke(p0.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
        }
    })
}

fun List<View>.isValid(): Boolean {
    var isValid = true
    this.reversed().forEach { if (!it.isValid()) isValid = false }
    return isValid
}

@SuppressLint("NewApi")
fun Spinner.validate(
    validator: ((String?) -> Boolean)? = null,
    errorMsg: ((String) -> Int?)? = null
) {
    this.onItemSelected {
        val value =  it.toString()
        this.prompt = when {
            //value.isBlank() -> context.getString(R.string.error_field_empty)
            validator != null && !validator(value) -> this.context.getString(errorMsg?.let { it1 ->
                it1(value)
            } ?: 0 /*R.string.error_field_empty*/)
            else -> null
        }
    }
}

fun Spinner.onItemSelected(onItemSelected: (Any?) -> Unit) {
    this.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            onItemSelected.invoke("")
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected.invoke(parent?.selectedItem)
        }
    })
}

fun View.isValid(): Boolean {
    val valid = if (this.isShown) {
        when (this) {
            is TextInputLayout -> {
                when {
                    this.editText?.text?.isBlank() == true -> {
                        //this.error = this.context.getString(R.string.error_field_empty)
                        false
                    }
                    else -> this.error.isNullOrBlank()
                }
            }
            is RadioGroup -> if (this.checkedRadioButtonId != -1) true
            else {
                val lastChildPos = this.childCount - 1
                (this.getChildAt(lastChildPos) as RadioButton).error = "Please select option"
                false
            }
            is Spinner ->
                if (this.selectedItem != null && !TextUtils.isEmpty(this.selectedItem.toString())) true
                else {
                    (this.selectedView as? TextView)?.error = "Please ${this.adapter.getItem(0)}"
                    false
                }
            is ImageView -> {
                if (this.hasImage()) {
                    this.background = null
                    true
                } else {
                    //this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.colorRed))
                    false
                }
            }
            /* is CardForm -> {
                 when {
                     this.cardEditText?.text?.isBlank() == true -> {
                         this.setCardNumberError(context.getString(R.string.empty_field))
                         false
                     }
                     else -> {
                         this.setCardNumberError(null)
                         true
                     }
                 }
             }*/
            is NumberPicker -> {
                this.isValid()
            }
            else -> false
        }
    } else {
        true
    }
    /* if (!valid && this !is SearchableSpinner) this.requestFocus()*/
    return valid
}

fun Fragment.setWindowStatusColor(statusbarColor: Int) {
    val flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
    } else {
        0
    }
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH) {
        activity?.window?.attributes?.flags = activity?.window?.attributes?.flags?.or(flags)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val uiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = uiVisibility
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity?.window?.attributes?.flags = activity?.window?.attributes?.flags?.and(flags.inv())
        activity?.window?.statusBarColor = statusbarColor
    }
}

fun Fragment.gotoGoogleMap(lat: String?, long: String?, label: String?) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=$lat,$long($label)")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
        startActivity(mapIntent)
    }
}

fun Fragment.openYoutubeLink(link: String?) {
    try {
        link?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(requireContext(), "Trailer is not available!", Toast.LENGTH_SHORT).show()
    }

}

fun Fragment.shareTextIntent(text: String?) {
    text?.let {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}

@SuppressLint("NewApi") // Lint does not understand isAtLeastQ currently
fun DrawerLayout.shouldCloseDrawerFromBackPress(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // If we're running on Q, and this call to closeDrawers is from a key event
        // (for back handling), we should only honor it IF the device is not currently
        // in gesture mode. We approximate that by checking the system gesture insets
        return rootWindowInsets?.let {
            val systemGestureInsets = it.systemGestureInsets
            return systemGestureInsets.left == 0 && systemGestureInsets.right == 0
        } ?: false
    }
    // On P and earlier, always close the drawer
    return true
}


/**
 * Combines this [LiveData] with another [LiveData] using the specified [combiner] and returns the
 * result as a [LiveData].
 */
fun <A, B, Result> LiveData<A>.combine(
    other: LiveData<B>,
    combiner: (A, B) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other.value
        if (b != null) {
            result.postValue(combiner(a, b))
        }
    }
    result.addSource(other) { b ->
        val a = this@combine.value
        if (a != null) {
            result.postValue(combiner(a, b))
        }
    }
    return result
}

/**
 * Combines this [LiveData] with other two [LiveData]s using the specified [combiner] and returns
 * the result as a [LiveData].
 */
fun <A, B, C, Result> LiveData<A>.combine(
    other1: LiveData<B>,
    other2: LiveData<C>,
    combiner: (A, B, C) -> Result
): LiveData<Result> {
    val result = MediatorLiveData<Result>()
    result.addSource(this) { a ->
        val b = other1.value
        val c = other2.value
        if (b != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other1) { b ->
        val a = this@combine.value
        val c = other2.value
        if (a != null && c != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    result.addSource(other2) { c ->
        val a = this@combine.value
        val b = other1.value
        if (a != null && b != null) {
            result.postValue(combiner(a, b, c))
        }
    }
    return result
}

//fun String?.validMobileNumber() = MobileValidator.validate(this)

fun File?.convertToMultiPart(
    paramName: String,
    type: String = "image/*"
): MultipartBody.Part? {
    if (this == null) return null
    val requestFileCustomerImage = asRequestBody(type.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        paramName,
        paramName + System.currentTimeMillis(),
        requestFileCustomerImage
    )
}

//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val gson = Gson()
    val json = if (this is String) this else gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}

private const val CHROME_PACKAGE = "com.android.chrome"

fun ImageView?.hasImage(): Boolean {
    if (this == null) return false
    val drawable: Drawable? = drawable
    var hasImage = drawable != null
    if (hasImage && drawable is BitmapDrawable) {
        hasImage = drawable.bitmap != null
    }
    return hasImage
}