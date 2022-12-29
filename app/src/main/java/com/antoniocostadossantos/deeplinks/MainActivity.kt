package com.antoniocostadossantos.deeplinks

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antoniocostadossantos.deeplinks.databinding.ActivityMainBinding

const val productNameParameter = "product_name"
const val productPriceParameter = "product_price"
const val productDescriptionParameter = "product_description"
const val productBarcodeParameter = "product_barcode"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        portraitOnly()
        getDeepLinks()

        binding.shareProduct.setOnClickListener {
            checkBarcodeSize()
        }
    }

    private fun checkBarcodeSize() {
        val barcode = binding.inputBarcode.text.toString()

        when {
            barcode.length < 13 || barcode.length > 13 -> {
                binding.productBarcode.error = "O código de barras deve conter 13 números"
            }
            else -> {
                binding.productBarcode.error = null
                shareProduct()
            }
        }
    }

    private fun shareProduct() {
        val productName = binding.inputName.text.toString().replace(" ", "_")
        val productPrice = binding.inputPrice.text.toString()
        val productDescription = binding.inputDescription.text.toString().replace(" ", "_")
        val productBarcode = binding.inputBarcode.text.toString()

        val newUrl =
            "https://toninhomercados.com.br/?" +
                    "product_name=$productName&" +
                    "product_price=$productPrice&" +
                    "product_description=$productDescription&" +
                    "product_barcode=$productBarcode"

        shareDataIntent(newUrl)
    }

    private fun shareDataIntent(message: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun getDeepLinks() {
        val linkIntent = intent
        val linkData = linkIntent.data
        val linkUrl = linkData?.host

        val paramsNames = linkData?.queryParameterNames ?: listOf()

        val productName = linkData?.getQueryParameter(productNameParameter) ?: ""
        val productPrice = linkData?.getQueryParameter(productPriceParameter) ?: ""
        val productDescription = linkData?.getQueryParameter(productDescriptionParameter) ?: ""
        val productBarcode = linkData?.getQueryParameter(productBarcodeParameter) ?: ""

        binding.inputName.setText(productName.replace("_", " "))
        binding.inputPrice.setText(productPrice)
        binding.inputDescription.setText(productDescription.replace("_", " "))
        binding.inputBarcode.setText(productBarcode)
    }

    private fun portraitOnly() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}