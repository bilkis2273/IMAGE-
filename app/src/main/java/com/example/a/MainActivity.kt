package com.example.a

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.a.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.FilterOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    //GALLERY
    var getgalleryTop = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback { binding.Top.setImageURI(it) })
    var getgallerypaint = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback { binding.Paint.setImageURI(it) })
    var getimagepaint =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val bitmap = it.data?.extras?.get("data") as Bitmap
            binding.Paint.setImageBitmap(bitmap)
        }
    var getimagetop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bitmap = it.data?.extras?.get("data") as Bitmap
        binding.Top.setImageBitmap(bitmap)
    }


    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        //ONE CAMERA AND GALLERY TOP
        binding.addOne.setOnClickListener {
            binding.cameraone.visibility = View.VISIBLE
            binding.GalleryOne.visibility = View.VISIBLE
            binding.Uploadtop.visibility = View.VISIBLE
            binding.cameraone.setOnClickListener {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    getimagetop.launch(intent)
                }
            }

            binding.GalleryOne.setOnClickListener {
                getgalleryTop.launch("image/*")
            }
        }

        //ONE CAMERA AND GALLERY PAINT
        binding.addTwo.setOnClickListener {
            binding.cameraTwo.visibility = View.VISIBLE
            binding.GalleryTwo.visibility = View.VISIBLE
            binding.uploadpaint.visibility = View.VISIBLE

            binding.cameraTwo.setOnClickListener {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    getimagepaint.launch(intent)
                }
            }

            binding.GalleryTwo.setOnClickListener {
                getgallerypaint.launch("image/*")
            }
        }

        //FAV BUTTON CHANG
        binding.fav.setOnClickListener {
            binding.fav.setImageResource(R.drawable.kk)
        }

        binding.uploadpaint.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        100)


                } else {
                    savImageToStorage()
                }
            } else {
                savImageToStorage()

            }
        }
        binding.Uploadtop.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        100)


                } else {
                    savImageToStorage()
                }
            } else {
                savImageToStorage()

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savImageToStorage()
            } else {
                Toast.makeText(this, "PERMISSION NOT GRANTED ", Toast.LENGTH_LONG).show()
            }

        } else {

        }
    }

    fun savImageToStorage() {
        val externalStroarageState = Environment.getExternalStorageState()
        if (externalStroarageState.equals(Environment.MEDIA_MOUNTED)) {
            val storage = Environment.getExternalStorageDirectory().toString()
            val file = File(storage, "test_image.jpg")
            try {
                val stream: OutputStream = FileOutputStream(file)
                var drawer = ContextCompat.getDrawable(applicationContext, R.drawable.he);
                var bitmap = (drawer as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                Toast.makeText(this,
                    "Image save ${Uri.parse(file.absolutePath)}",
                    Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "UNABLE TO ACCESS STORAGE", Toast.LENGTH_LONG).show()

        }
    }


}