package com.appinsnap.aishrm.util

import android.content.Context
import android.os.AsyncTask
import android.widget.ProgressBar
import com.appinsnap.aishrm.common.hide
import com.appinsnap.aishrm.common.show
import com.appinsnap.aishrm.util.dialogs.ProgressBarDialog
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RetrievePDFFromURL(val progressBar: ProgressBar,pdfView: PDFView) :
    AsyncTask<String, Void, InputStream>() {

    // on below line we are creating a variable for our pdf view.
    val mypdfView: PDFView = pdfView
    // on below line we are calling our do in background method.

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.show()
        // Show the progress bar before starting the background task.
    }
    override fun doInBackground(vararg params: String?): InputStream? {
        // on below line we are creating a variable for our input stream.
        var inputStream: InputStream? = null
        try {
            // on below line we are creating an url
            // for our url which we are passing as a string.
            val url = URL(params.get(0))

            // on below line we are creating our http url connection.
            val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

            // on below line we are checking if the response
            // is successful with the help of response code
            // 200 response code means response is successful
            if (urlConnection.responseCode == 200) {
                // on below line we are initializing our input stream
                // if the response is successful.
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        }
        // on below line we are adding catch block to handle exception
        catch (e: Exception) {
            // on below line we are simply printing
            // our exception and returning null
            e.printStackTrace()
            return null;
        }
        // on below line we are returning input stream.
        return inputStream;
    }

    // on below line we are calling on post execute
    // method to load the url in our pdf view.
    override fun onPostExecute(result: InputStream?) {
        // on below line we are loading url within our
        // pdf view on below line using input stream.
        mypdfView.fromStream(result).load()
        progressBar.hide()
    }

    }

