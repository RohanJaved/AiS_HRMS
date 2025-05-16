package com.appinsnap.aishrm.util

import android.app.Activity
import android.app.ActivityManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.core.util.PatternsCompat
import androidx.fragment.app.FragmentActivity
import com.appinsnap.aishrm.ui.activities.mainactivity.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/*import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import coil.load
import com.eshaafi.patient.consultation.BuildConfig
import com.eshaafi.patient.consultation.MyApplication
import com.eshaafi.patient.consultation.R
import com.eshaafi.patient.consultation.base.ErrorResponse
import com.eshaafi.patient.consultation.data.Repository
import com.eshaafi.patient.consultation.models.DeviceTokenPost
import com.eshaafi.patient.consultation.network.NetworkApi
import com.eshaafi.patient.consultation.ui.auth.AuthActivity
import com.eshaafi.patient.consultation.ui.auth.phone_no.models.RefreshTokenPost
import com.eshaafi.patient.consultation.ui.bottom_dialogues.add_medium.AddMediumFragment
import com.eshaafi.patient.consultation.ui.main.MainActivity
import com.eshaafi.patient.consultation.ui.main.book_appointment.models.JazzCashPaymentPost
import com.eshaafi.patient.consultation.ui.video_call.models.JoinMeetingParams
import com.eshaafi.patient.consultation.ui.video_call.models.MeetingResponse
import com.eshaafi.patient.consultation.util.Constants.Companion.UNABLE_TO_RESOLVE_HOST
import com.eshaafi.patient.consultation.util.extensions.toObject
import com.eshaafi.patient.consultation.work.NotifyWork
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await*/


object AppUtils {
    fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun isEmpty(editText: EditText): Boolean {
        var isEmptyResult = false
        if (editText.text.isNotEmpty()) {
            isEmptyResult = true
        }
        return isEmptyResult
    }

    fun isTextViewEmpty(textView: TextView): Boolean {
        var istvEmpty = false
        if (textView.text.isNotEmpty()) {
            istvEmpty = true
        }
        return istvEmpty
    }

    fun isEqualsTwoString(text1:String, text2:String): Boolean{
        var stringEquals = false
        if (text1.toString() == text2.toString()){
            stringEquals = true
        }
        return stringEquals
    }

    fun isChecked(button: RadioButton): Boolean{
        var isButtonChecked = false
        if (button.isChecked){
            isButtonChecked = true
        }
        return isButtonChecked
    }

    fun saveToGallery(context: Context, bitmap: Bitmap, albumName: String) {
        val filename = "${System.currentTimeMillis()}.png"
        val write: (OutputStream) -> Boolean = {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/$albumName")
            }

            context.contentResolver.let {
                it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                    it.openOutputStream(uri)?.let(write)
                }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + albumName
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, filename)
            write(FileOutputStream(image))
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun getBitmapFromView(view: View, defaultColor: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(defaultColor)
        view.draw(canvas)
        return bitmap
    }

    fun takeScreenshotForView(view: View): Bitmap? {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )
        view.layout(
            view.x.toInt(),
            view.y.toInt(),
            view.x.toInt() + view.measuredWidth,
            view.y.toInt() + view.measuredHeight
        )
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    fun getSinglePermisstion(context: Context, permission:String,
                            requestPermissionLauncher: ActivityResultLauncher<String>): ActivityResultLauncher<String>{
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
//                Toast.makeText(context, "do you work", Toast.LENGTH_SHORT).show()
                return requestPermissionLauncher
            }
            shouldShowRequestPermissionRationale(context as Activity,permission) -> {
                // your custom UI
                requestPermissionLauncher.launch(
                    permission
                )
                LoggerGenratter.getInstance().showToast(context,"ask permission")

            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    permission
                )
//                    Toast.makeText(requireContext(), "launch", Toast.LENGTH_SHORT).show()
            }
        }
        return requestPermissionLauncher
    }

    fun changeStatusBarColor(color: Int, context: FragmentActivity?, dark: Boolean = false) {

        if (context != null) {
            val window: Window = context.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(context, color)

            if (dark)
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.decorView.systemUiVisibility =
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                }

            /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else
                    if (dark)
                        window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            window.decorView.systemUiVisibility =
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        }*/
        }


        /*val window: Window = context.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(
                context,
                color
            )
        }
        context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR*/
    }

    fun hideKeyboard(activity: Activity?, view: View?) {
        try {
            val inputManager = activity
                ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            } else {
                val imm =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun statusBarTheme(activity: FragmentActivity?) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                activity?.window?.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                activity?.window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            else -> {

            }
        }
    }

    /*
    fun navigateTo(activity: FragmentActivity?, navDirections: NavDirections) {
         hideKeyboard(activity)
         activity?.findNavController(R.id.nav_host)?.navigate(navDirections)
     }

     fun popBackStack(activity: FragmentActivity?) {
         hideKeyboard(activity)
         activity?.findNavController(R.id.nav_host)?.popBackStack()
     }

     fun hideDropDown(powerSpinnerView: PowerSpinnerView?) {
         powerSpinnerView?.dismiss()
     }*/


    /*fun hasInternetConnection(): Boolean {
        if (MyApplication.get()?.activity == null) {
            return true
        }
        val connectivityManager = MyApplication.get()?.activity?.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val netInfo: NetworkInfo = connectivityManager.activeNetworkInfo!!
            return netInfo.isConnectedOrConnecting
        }

    }*/

    /*fun authNavigateTo(activity: FragmentActivity?, navDirections: NavDirections, view: View?) {
        hideKeyboard(activity, view)
        activity?.findNavController(R.id.main_content)?.navigate(navDirections)
    }*/

    /*fun authBackpressed(activity: FragmentActivity?, view: View?) {
        hideKeyboard(activity, view = view)
        activity?.findNavController(R.id.main_content)?.popBackStack()
    }*/

    /*fun mainBackpressed(activity: FragmentActivity?, view: View?) {
        hideKeyboard(activity, view = view)
        activity?.findNavController(R.id.nav_host)?.popBackStack()
    }*/

    @ExperimentalCoroutinesApi
    fun getMain(activity: FragmentActivity?): MainActivity {
        return activity as MainActivity
    }

    /*fun mainNavigateTo(
        activity: FragmentActivity?,
        navDirections: NavDirections,
        view: View?,
        currentDestination: Int? = null
    ) {

        if (currentDestination == null || activity?.findNavController(R.id.nav_host)?.currentDestination?.id == currentDestination) {
            hideKeyboard(activity, view = view)
            activity?.findNavController(R.id.nav_host)?.navigate(navDirections)
        }
    }*/


    /*fun showSnackBar(context: Context, view: View, message: String?, isSuccess: Boolean = true) {
        try {
            val snackbar: Snackbar =
                Snackbar.make(
                    view,
                    message.toString(),
                    Snackbar.LENGTH_SHORT
                )
            val snackbarLayout = snackbar.view

            snackbarLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (isSuccess) R.color.slightly_green else R.color.slightly_green
                )
            )

            snackbarLayout.backgroundTintList = ContextCompat.getColorStateList(
                context,
                if (isSuccess) R.color.light_greenish_background_color else R.color.light_red_background_color
            )

            val textView =
                snackbarLayout.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                if (isSuccess) R.drawable.ic_tick_circle_green else R.drawable.ic_cross_circle_red,
                0
            )
            textView.setTextAppearance(R.style.simpleBlueTextview)
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))
            textView.compoundDrawablePadding =
                context.resources.getDimensionPixelOffset(R.dimen.margin_top_10)
            snackbar.show()
        } catch (e: Exception) {
            // logout
        }
    }*/

    /*fun getAddMediumFragment(fragment: Fragment?): AddMediumFragment {
        return fragment as AddMediumFragment
    }*/

    fun twoDigitCode(numberText: Int): String {
        return if (numberText > 9)
            numberText.toString()
        else
            "0$numberText"
    }

    /*  fun saveUUID(context: Context, uuid: String) {
          val tinydb = TinyDB(context)
          tinydb.putString(Constants.UUID_NAME, uuid)
      }

      fun getUUID(context: Context): String {
          val tinydb = TinyDB(context)
          return tinydb.getString(Constants.UUID_NAME)
      }
  */


/*
    suspend fun savePhoneNo(context: Context, phoneNo: String) {
        val preferenceManager: PreferenceManager by lazy { PreferenceManager(context) }
        preferenceManager.saveBookmark(phoneNo)
    }

    fun getPhoneNo(context: Context, viewLifecycleOwner: LifecycleOwner) {
        val preferenceManager: PreferenceManager by lazy { PreferenceManager(context) }
        preferenceManager.bookmark.asLiveData().observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }
*/


    /*fun showToast(message: String?, context: Activity? = AgentMate.get()?.activity) {
        if (message.toString().contains(UNABLE_TO_RESOLVE_HOST))
            Toast.makeText(
                context,
                Constants.NO_INTERNET_CONNECTION_FOUND,
                Toast.LENGTH_SHORT
            ).show()
        else
            Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT)
                .show()
    }*/

    fun clickOnCrossIcon(activity: Activity?, callformHome: Boolean?) {
        if (callformHome != true) {
            activity?.startActivity(
                Intent(
                    activity,
                    MainActivity::class.java
                )
            )
        }

        activity?.finish()
    }

    /*suspend fun refreshToken(networkApi: NetworkApi) {

        if (isTokenExpire()) {
            try {
                val response = networkApi.refreshToken(
                    RefreshTokenPost(
                        refreshToken = SharedPreferences.getRefreshToken(),
                        type = Constants.PATIENT_TYPE,
                        uuid = SharedPreferences.getUUID(),
                        deviceId = SharedPreferences.getDeviceID()
                    )
                )
                if (response.statusCode == 200) {
                    SharedPreferences.saveIdToken(response.response.idToken)
                    SharedPreferences.saveAccessToken(response.response.accessToken)
                    SharedPreferences.saveIdTokenTime()
                    generateFirebaseToken()
                    sendDeviceToken(networkApi)

//                    loginFirebase(response.response.idToken, networkApi)
                } else {
                    SharedPreferences.clearSharedPreferences()
                    MyApplication.get()?.activity?.startActivity(
                        Intent(
                            MyApplication.get()?.activity!!,
                            AuthActivity::class.java
                        ).putExtra(Constants.IS_LOGOUT, true)
                    )

                    MyApplication.get()?.activity?.finish()
                }

            } catch (e: Exception) {
                // logout
                SharedPreferences.clearSharedPreferences()
                MyApplication.get()?.activity?.startActivity(
                    Intent(
                        MyApplication.get()?.activity!!,
                        AuthActivity::class.java
                    ).putExtra(Constants.IS_LOGOUT, true)
                )

                MyApplication.get()?.activity?.finish()
            }
        }
    }*/


    /*private fun isTokenExpire(): Boolean {
        return SharedPreferences.getRefreshToken()
            .isNotEmpty() && checkDifference(SharedPreferences.getIdTokenTime()) >= 1435

    }*/

    /*private suspend fun generateFirebaseToken() {
        SharedPreferences.saveFirebaseToken(FirebaseMessaging.getInstance().token.await())
    }*/

    /*private suspend fun sendDeviceToken(networkApi: NetworkApi) {
        try {
            val response = networkApi.deviceToken(
                DeviceTokenPost(
                    uuid = SharedPreferences.getUUID(),
                    token = SharedPreferences.getFirebaseToken()
                )
            )
            if (response.statusCode == 200) {


//                    loginFirebase(response.response.idToken, networkApi)
            } else {
                //logout
            }

        } catch (e: Exception) {
            // logout
        }
    }*/

    /*suspend fun sendTokenToServer(repository: Repository, token: String) {
        if (SharedPreferences.getUUID().isNotEmpty())
            try {
                val response = repository.remote.sendDeviceToken(
                    DeviceTokenPost(
                        uuid = SharedPreferences.getUUID(),
                        token = token
                    )
                )

                if (response.statusCode == 200) {

                    SharedPreferences.saveFirebaseToken(token)
//                    loginFirebase(response.response.idToken, networkApi)
                } else {
                    //logout
                }
            } catch (e: Exception) {
                // logout
            }
    }*/

    /*suspend fun joinMeeting(networkApi: NetworkApi): MeetingResponse {
        return try {
            val response = networkApi.joinMeeting(
                url = "https://xl5xizs8fc.execute-api.us-east-1.amazonaws.com/default/join_meeting",
                JoinMeetingParams("d6532b63-6702-491e-ba2a-871ec4b10706")
            )
            if (response.responseCode == "200") {
                response
            } else {
                MeetingResponse()
            }

        } catch (e: Exception) {
            // logout
            MeetingResponse()
        }
    }*/


    private fun checkDifference(start: Long): Long {
        val stardate = Date()
        stardate.time = start
        val endate = Date()
        endate.time = System.currentTimeMillis()
        val difference = endate.time - stardate.time
        return (difference / (1000 * 60))

    }


    fun isEmailValid(email: CharSequence?): Boolean {
        if (email == null)
            return false
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

/*    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl) {
            crossfade(600)
            error(R.drawable.ic_profile)
        }
    }*/

    /*fun loadImageFromUrlWithCoil(imageView: CircleImageView?, imageUrl: String?) {

        if (imageUrl.isNullOrEmpty())
            imageView?.setImageResource(R.drawable.ic_doctor_place_holder)
        else
            imageView?.load(imageUrl) {
                placeholder(R.drawable.ic_doctor_place_holder)
                error(R.drawable.ic_doctor_place_holder)
            }
    }
*/
    /*fun loadImageFromUrlWithCoil(
        imageView: ImageView?,
        imageUrl: String?,
        placeholder: Int = R.drawable.ic_doctor_place_holder
    ) {

        if (imageUrl.isNullOrEmpty())
            imageView?.setImageResource(placeholder)
        else
            imageView?.load(imageUrl) {
                placeholder(placeholder)
                error(placeholder)
            }
    }*/

    /*fun loadImageFromUrlWithCoilWithoutCache(imageView: CircleImageView?, imageUrl: String?) {


        if (imageUrl.isNullOrEmpty())
            imageView?.setImageResource(R.drawable.ic_patient_place_holder)
        else
            imageView?.load(imageUrl) {
                placeholder(R.drawable.ic_patient_place_holder)
//                memoryCachePolicy(CachePolicy.DISABLED)
//                diskCachePolicy(CachePolicy.WRITE_ONLY)
                error(R.drawable.ic_patient_place_holder)
            }
    }*/

/*
    fun loadImageFromUrlWithGlide(imageView: ImageView?, imageUrl: String?) {


        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_doctor_place_holder)
            .error(R.drawable.ic_doctor_place_holder)



        if (imageUrl.isNullOrEmpty())
            imageView?.setImageResource(R.drawable.ic_doctor_place_holder)
        else
            Glide.with(MyApplication.get()?.activity!!).load(imageUrl).thumbnail(0.03f)
                .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView!!)
    }

    fun loadImageFromUrlWithGlideWithoutCache(imageView: ImageView?, imageUrl: String?) {

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_doctor_place_holder)
            .error(R.drawable.ic_doctor_place_holder)


        if (imageUrl.isNullOrEmpty())
            imageView?.setImageResource(R.drawable.ic_doctor_place_holder)
        else
            Glide.with(MyApplication.get()?.activity!!).load(imageUrl).thumbnail(0.03f)
                .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView!!)
    }*/


    fun removeZeroFromFirstIndex(value: String): String {
        return if (value.isNotEmpty() && value.first() == '0')
            value.drop(1)
        else
            value
    }


    fun isPhoneNoValid(value: String): Boolean {
        val phoneNo = removeZeroFromFirstIndex(value)
        if (phoneNo.isEmpty())
            return true
        else {
            if (phoneNo.startsWith("30") ||
                phoneNo.startsWith("31") ||
                phoneNo.startsWith("32") ||
                phoneNo.startsWith("33") ||
                phoneNo.startsWith("34") ||
                phoneNo.startsWith("35")
            )
                return true
            return phoneNo.first() == '3' && phoneNo.length == 1

        }
    }

    fun isPhoneNoValidWithZero(phoneNo: String): Boolean {
        if (phoneNo.isEmpty())
            return false
        if (phoneNo.length < 11)
            return false
        else {
            if (phoneNo.startsWith("030") ||
                phoneNo.startsWith("031") ||
                phoneNo.startsWith("032") ||
                phoneNo.startsWith("033") ||
                phoneNo.startsWith("034") ||
                phoneNo.startsWith("035")
            )
                return true
            if (phoneNo.startsWith("03") && phoneNo.length == 2)
                return true
            return phoneNo.first() == '0' && phoneNo.length == 1
        }
    }

    fun validAmount(amount: String): Boolean {
        if (amount.isEmpty())
            return false
        if (amount.startsWith("0"))
            return false
        return amount.length <= 5
    }

    fun hideDay(datePicker: DatePicker) {
        val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
        if (daySpinnerId != 0) {
            val daySpinner = datePicker.findViewById<View>(daySpinnerId)
            if (daySpinner != null) {
                daySpinner.visibility = View.GONE
            }
        }
        val monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android")
        if (monthSpinnerId != 0) {
            val monthSpinner = datePicker.findViewById<View>(monthSpinnerId)
            if (monthSpinner != null) {
                monthSpinner.visibility = View.VISIBLE
            }
        }
        val yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android")
        if (yearSpinnerId != 0) {
            val yearSpinner = datePicker.findViewById<View>(yearSpinnerId)
            if (yearSpinner != null) {
                yearSpinner.visibility = View.VISIBLE
            }
        }
    }


    fun makeSectionOfTextBold(
        text: String,
        searchKeywords: Array<String>
    ): SpannableStringBuilder? {
        // searching in the lower case text to make sure we catch all cases
        val loweredMasterText = text.lowercase(Locale.ENGLISH)
        val span = SpannableStringBuilder(text)

        // for each keyword
        for (keyword in searchKeywords) {
            // lower the keyword to catch both lower and upper case chars
            val loweredKeyword = keyword.lowercase(Locale.ENGLISH)

            // start at the beginning of the master text
            var offset = 0
            var start: Int
            val len = keyword.length // let's calculate this outside the 'while'
            while (loweredMasterText.indexOf(loweredKeyword, offset).also { start = it } >= 0) {
                // make it bold
                span.setSpan(
                    StyleSpan(Typeface.BOLD), start, start + len,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                // move your offset pointer
                offset = start + len
            }
        }

        // put it in your TextView and smoke it!
        return span
    }

    fun isMyServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("isMyServiceRunning?", true.toString() + "")
                return true
            }
        }
        Log.i("isMyServiceRunning?", false.toString() + "")
        return false
    }

    fun convertSecondsToHMmSs(seconds: Long): String? {
        val s = seconds % 60
        val m = seconds / 60 % 60
        val h = seconds / (60 * 60) % 24
//        return String.format("%d:%02d:%02d", h, m, s)
        return String.format("%02d:%02d", m, s)
    }

    fun drawableToBitmap(context: Context?, drawable: Drawable): Bitmap? {

        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val roundedBitmapDrawable =
            context?.resources?.let { RoundedBitmapDrawableFactory.create(it, bitmap) }
        val roundPx = bitmap?.width?.toFloat()?.times(0.6f)
        if (roundPx != null) {
            roundedBitmapDrawable?.cornerRadius = roundPx
        }

        return roundedBitmapDrawable?.toBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, roundPixelSize: Int = 1000): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = roundPixelSize.toFloat()
        paint.isAntiAlias = true
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }


    /*  fun getRoundedBitmapFromURL(context: Context,url:String): Bitmap? {


          val futureTarget = Glide.with(context)
              .asBitmap()
              .load(url)
              .submit()
              .get()

          return getRoundedCornerBitmap(bitmap = futureTarget, roundPixelSize = 1000)
      }*/

    /*fun scheduleNotification(context: Context, delay: Long, msg: String) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(
                Data.Builder().putString(
                    "msg", msg
                ).build()
            ).build()

        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.beginUniqueWork(
            msg,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }*/


    private fun getExiryDateTime(): String? {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, +4)
        val dNow = calendar.time
        val ft = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)
        val datetime = ft.format(dNow)
        println(datetime)
        return datetime
    }

    private fun getTransDateTime(): String? {
        val dNow = Date()
        val ft = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)
        val datetime = ft.format(dNow)
        println(datetime)
        return datetime
    }

    private fun getUniqueId(appointmentId: String?): String? {
        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs", Locale.ENGLISH)
        val datetime = ft.format(dNow)
        println(datetime)
        return "T$datetime$appointmentId"
    }

    private fun hmacDigest(msg: String, keyString: String, algo: String?): String? {
        var digest: String? = null
        try {
            val key = SecretKeySpec(keyString.toByteArray(charset("UTF-8")), algo)
            val mac = Mac.getInstance(algo)
            mac.init(key)
            val bytes = mac.doFinal(msg.toByteArray(charset("ASCII")))
            val hash = StringBuffer()
            for (i in bytes.indices) {
                val hex = Integer.toHexString(0xFF and bytes[i].toInt())
                if (hex.length == 1) {
                    hash.append('0')
                }
                hash.append(hex)
            }
            digest = hash.toString()
        } catch (e: UnsupportedEncodingException) {
        } catch (e: InvalidKeyException) {
        } catch (e: NoSuchAlgorithmException) {
        }
        return digest
    }

    /*fun getJazzCashParams(
        fee: Int?,
        cnic: String?,
        mobileNo: String?,
        appointmentId: String?
    ): JazzCashPaymentPost {
        val str: String = (
                (fee?.times(100)).toString() //pp_Amount
                        + "&" + "billRef" //pp_BillReference
                        + "&" + cnic //pp_CNIC
                        + "&" + "Description" //pp_Description
                        + "&EN" //"pp_Language="
                        + "&" + BuildConfig.pp_MerchantID //pp_MerchantID
                        + "&" + mobileNo //pp_MobileNumber
                        + "&" + BuildConfig.pp_Password //pp_Password
//                        + "&" + doctorDetails?.id?.get(0).toString() //pp_ProductID
                        + "&PKR" //pp_TxnCurrency
                        + "&" + AppUtils.getTransDateTime() //pp_TxnDateTime
                        + "&" + getExiryDateTime() //pp_TxnExpiryDateTime
                        + "&" + AppUtils.getUniqueId(
                    appointmentId?.substring(
                        0,
                        3
                    )
                )) //pp_TxnRefNo


        return JazzCashPaymentPost(
            ppTxnDateTime = getTransDateTime(),
            ppTxnExpiryDateTime = getExiryDateTime(),
            ppCNIC = cnic,
            ppMobileNumber = mobileNo.toString(),
            ppAmount = (fee?.times(100)).toString(),
//            ppProductID = getUniqueId(doctorDetails?.id?.get(0).toString()),
            ppTxnRefNo = getUniqueId(appointmentId?.substring(0, 3)),
            ppSecureHash = hmacDigest(
                BuildConfig.secure_key + "&$str",
                BuildConfig.secure_key,
                "HmacSHA256"
            )


        )
    }*/


    /**
     * Dates related all methods here
     */

    fun convertDateyyyymmddTOShortEnglishDate(date: String): String {
        return try {
            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertServerDateTimeTOShortEnglishDate(date: String): String {
        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertDateyyyymmddTOEnglishDate(date: String): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("dd, MMM", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertDateyyyymmddTOEnglishDatewithYear(date: String?): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertDateyyyymmddTOEnglishDatewithoutcomma(date: String?): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("dd\nMMM", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertDateyyyymmddTOYYYY(date: String?): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("yyyy", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }


    fun convertDateddmmyyyyTOEnglishDay(date: String): String {

        return try {


            val initDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("EEE", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertDateddmmyyyyTOyyyymmdd(date: String): String {

        return try {


            val initDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }


    fun convertDateyyyymmddTOddmmyyyy(date: String): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun convertDateyyyymmddTOEnglishDay(date: String): String {

        return try {


            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("EEE", Locale.ENGLISH)

            return when {
                DateUtils.isToday(initDate.time) -> "Today"
//                DateUtils.isToday(initDate.time - DateUtils.DAY_IN_MILLIS) -> "Tomorrow"
                else -> formatter.format(initDate)
            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }


    fun convertDateyyyymmddTOFullEnglishdate(date: String?): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("EEEE, MMMM dd", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }


    fun convertDateyyyymmddTOFullDate(date: String?): String {

        return try {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.ENGLISH)
            return formatter.format(initDate)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }


    }

    fun yearToYYYYMMDD(date: String?): String {
        if (!date.isNullOrEmpty()) {

            val initDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            val formatter = SimpleDateFormat("yyyy", Locale.ENGLISH)
            return formatter.format(initDate)
        }
        return ""
    }

    fun convert24to12Hours(time: String?): String {
        return try {

            val _24HourSDF = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val _12HourSDF = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val _24HourDt = _24HourSDF.parse(time)
            _12HourSDF.format(_24HourDt).toString()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun convertCreatedAtTimeTo24hoursFormat(time: String?, minutes: Int = 0): String {
        return try {

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val output = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

            val d = sdf.parse(time)
            val cal = Calendar.getInstance()
            cal.time = d
            cal.add(Calendar.MINUTE, minutes)


            val formattedTime = output.format(cal.time)

            formattedTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun calculateCurrentTimeDifferceIn24Hours(time: String?): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val appointmentTime = sdf.parse(time)

            if (appointmentTime?.time == null)
                return 0


            if (appointmentTime.time - System.currentTimeMillis() <= 600000)
                return (appointmentTime.time - System.currentTimeMillis()) / 60000

            return 11

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            0
        }
    }


    fun convertWebto12Hours(time: String?): String {
        return try {
/*
            val inputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("EEE, MMMM dd HH:mm a", Locale.ENGLISH)
            val date: LocalDate = LocalDate.parse("2018-04-10T04:00:00.000Z", inputFormatter)
            val formattedDate: String = outputFormatter.format(date)
            formattedDate*/


            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val output = SimpleDateFormat("EEE, MMM dd hh:mm a", Locale.ENGLISH)
            val d = sdf.parse(time)
            val formattedTime = output.format(d)

            formattedTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun convertServerDateTimeToYYYYMMDD(time: String?): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val output = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val d = sdf.parse(time)
            val formattedTime = output.format(d)

            formattedTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertServerTimeToTimeStamp(time: String?): String {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val past = sdf.parse(time)
            val now = Date()
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
            //
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");
            return when {
                seconds < 60 -> {
                    "$seconds seconds ago"
                }
                minutes < 60 -> {
                    "$minutes minutes ago"
                }
                hours < 24 -> {
                    "$hours hours ago"
                }
                else -> {
                    "$days days ago"
                }
            }
        } catch (j: java.lang.Exception) {
            j.printStackTrace()
        }
        return ""
    }


    fun convertServerDateTimeToDDMMYY(time: String?): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val output = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val d = sdf.parse(time)
            val formattedTime = output.format(d)

            formattedTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertServerDateTimeTo12Hours(time: String?): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val output = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val d = sdf.parse(time)
            val formattedTime = output.format(d)

            formattedTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun convertServerDateTimeToEnglishDate(time: String?): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val output = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
            val d = sdf.parse(time)
            val formattedTime = output.format(d)

            formattedTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun differenceServerDateTimeToCurrent(time: String?): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val appointmentTime = sdf.parse(time)


            val remainingTime = appointmentTime.time - System.currentTimeMillis()

            remainingTime

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getFormatMonthYear(date: Date?): String? {
        return try {
            val dateFormat: DateFormat = SimpleDateFormat("MM / yy", Locale.ENGLISH)
            dateFormat.format(date)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getAgeFromYYYYMMDD(dob: String): String {
        return try {
            var date: Date? = null
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                date = sdf.parse(dob)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date == null) return "N/A"
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob.time = date
            val year = dob[Calendar.YEAR]
            val month = dob[Calendar.MONTH]
            val day = dob[Calendar.DAY_OF_MONTH]
            dob[year, month + 1] = day
            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
                age--
            }
            return age.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "N/A"
        }


    }


    /*fun handleErrorResponse(e: Exception): String {
        return try {
            if (e is HttpException) {
                val error = e.response()?.errorBody()?.string()?.toObject<ErrorResponse>()
                when (error?.statusCode) {
                    405 -> {
                        SharedPreferences.clearSharedPreferences()
                        MyApplication.get()?.activity?.startActivity(
                            Intent(
                                MyApplication.get()?.activity!!,
                                AuthActivity::class.java
                            ).putExtra(Constants.IS_LOGOUT, true)
                        )

                        MyApplication.get()?.activity?.finish()
                        error.message
                    }

                    else -> error?.message.toString()
                }
            } else {
                e.message.toString()
            }
        } catch (e: Exception) {
            e.message.toString()
        }


    }*/
}
