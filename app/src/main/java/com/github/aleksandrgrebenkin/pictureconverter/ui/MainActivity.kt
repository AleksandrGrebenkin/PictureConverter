package com.github.aleksandrgrebenkin.pictureconverter.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.github.aleksandrgrebenkin.pictureconverter.R
import com.github.aleksandrgrebenkin.pictureconverter.databinding.ActivityMainBinding
import com.github.aleksandrgrebenkin.pictureconverter.mvp.presenter.MainPresenter
import com.github.aleksandrgrebenkin.pictureconverter.mvp.view.MainView
import com.github.aleksandrgrebenkin.pictureconverter.ui.converter.AndroidConverter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    companion object {
        private const val PICK_IMAGE = 1456
    }

    private lateinit var binding: ActivityMainBinding

    private val presenter by moxyPresenter {
        MainPresenter(
            AndroidSchedulers.mainThread(),
            AndroidConverter(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnOpenImage.setOnClickListener { presenter.openImageClicked() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            data?.data?.let { uri ->
                val bytes =
                    this.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }
                bytes?.let { presenter.convertImage(bytes) }

            }
        }
    }

    override fun openImage() {
        val intent = Intent().apply {
            type = "image/jpeg"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(
                intent,
                getString(R.string.open_image_intent_title)
            ), PICK_IMAGE
        )
    }

    private var conversionDialog: Dialog? = null
    override fun showConversionDialog() {
        this.let {
            conversionDialog = AlertDialog.Builder(it)
                .setMessage(getString(R.string.conversion_dialog_message))
                .setNegativeButton(getString(R.string.conversion_dialog_negative)) { _, _ ->
                    presenter.conversionCancel()
                }
                .create()
            conversionDialog?.show()
        }
    }

    override fun hideConversionDialog() {
        conversionDialog?.dismiss()
    }

    override fun showSuccessResult() {
        binding.tvStatus.text = getString(R.string.result_success)
    }

    override fun showCancelResult() {
        binding.tvStatus.text = getString(R.string.result_cancel)
    }

    override fun showErrorResult(text: String) {
        (getString(R.string.result_error) + ": " + text).also { binding.tvStatus.text = it }
    }
}