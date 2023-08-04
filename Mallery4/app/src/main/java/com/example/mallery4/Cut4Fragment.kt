package com.example.mallery4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_cut4.*
import kotlinx.android.synthetic.main.activity_decorate.*
import kotlinx.android.synthetic.main.activity_decorate.imagePreview

class Cut4Fragment : Fragment() {
    private val PERMISSION_Album = 101 // Album permission handling
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private lateinit var imagePreview: ImageView
    private lateinit var cut4_1: ImageView
    private lateinit var cut4_2: ImageView
    private lateinit var cut4_3: ImageView
    private lateinit var cut4_4: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cut4, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val cameraBtn = view.findViewById<Button>(R.id.camera_btn)
        cameraBtn.setOnClickListener {
            // Check if the permission is already granted
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is already granted, open the gallery
                openGallery()
            } else {
                // Request the permission
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_Album
                )
            }
        }


        val textdone = view.findViewById<TextView>(R.id.deco_done)
        textdone.setOnClickListener {
            val make4Fragment = Make4Fragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, make4Fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Add the transaction to the back stack
            fragmentTransaction.commit()
        }


        // Initialize the galleryLauncher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    handleGalleryResult(data)
                }
            }
        }


        // Find UI elements after the view is created
        imagePreview = view.findViewById(R.id.imagePreview)
        cut4_1 = view.findViewById(R.id.cut4_1)
        cut4_2 = view.findViewById(R.id.cut4_2)
        cut4_3 = view.findViewById(R.id.cut4_3)
        cut4_4 = view.findViewById(R.id.cut4_4)
        // ... Other code ...
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_Album -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted(requestCode)
                } else {
                    permissionDenied(requestCode)
                }
            }
        }
    }

    private fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERMISSION_Album -> openGallery()
        }
    }

    private fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERMISSION_Album -> Toast.makeText(
                activity,
                "You need to grant storage permission to load images from the album.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openGallery() {
       /* val intent = Intent(Intent.ACTION_PICK)
        MediaStore.Images.Media.CONTENT_TYPE.also { intent.type = it }
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)*/
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
    }



    private fun handleGalleryResult(data: Intent) {
        resetSelectedImages()
        val clipData = data.clipData
        if (clipData != null) {
            for (i in 0 until clipData.itemCount) {
                val uri = clipData.getItemAt(i).uri
                if (selectedImages.size < 4) {
                    selectedImages.add(uri)
                } else {
                    break
                }
            }
        } else {
            val uri = data.data
            if (uri != null) {
                selectedImages.add(uri)
            }
        }

        if (selectedImages.isNotEmpty()) {
            val firstImageUri = selectedImages[0]
            val secondImageUri = selectedImages.getOrNull(1)
            val thirdImageUri = selectedImages.getOrNull(2)
            val fourthImageUri = selectedImages.getOrNull(3)

            val textView = view?.findViewById<TextView>(R.id.deco_done)
            textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.highlightcolor))

            val image4 = view?.findViewById<LinearLayout>(R.id.image4)
            imagePreview.visibility = View.GONE
            image4?.visibility = View.VISIBLE

            cut4_1.setImageURI(firstImageUri)
            cut4_1.visibility = View.VISIBLE

            cut4_2.setImageURI(secondImageUri)
            cut4_2.visibility = View.VISIBLE

            cut4_3.setImageURI(thirdImageUri)
            cut4_3.visibility = View.VISIBLE

            cut4_4.setImageURI(fourthImageUri)
            cut4_4.visibility = View.VISIBLE
        }
        else {
            val image4 = view?.findViewById<LinearLayout>(R.id.image4)
            image4?.visibility = View.VISIBLE
            imagePreview.visibility = View.GONE // Hide the image preview if no image is selected

            val textView = view?.findViewById<TextView>(R.id.deco_done)
            textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.background)) // Use requireContext()
        }

        val textView = view?.findViewById<TextView>(R.id.deco_done)
       /* textView?.setOnClickListener {
            if (selectedImages.size == 4) {
                val make4Fragment = Make4Fragment() // Instantiate your Make4Fragment
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, make4Fragment)
                fragmentTransaction.addToBackStack(null) // Optional: Add the transaction to the back stack
                fragmentTransaction.commit()
            } else {
                Toast.makeText(requireContext(), "사진을 4개 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }*/

        textView?.setOnClickListener {
            if (selectedImages != null  && selectedImages.size == 4) {
                val fragment = Make4Fragment()
                val args = Bundle()
                args.putParcelableArrayList("selectedImages", ArrayList(selectedImages))
                fragment.arguments = args

                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            } else {
                Toast.makeText(requireContext(), "사진을 4개 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun resetSelectedImages() {
        selectedImages.clear()

        val image4 = view?.findViewById<LinearLayout>(R.id.image4) // Use rootView.findViewById
        imagePreview.visibility = View.GONE
        image4?.visibility = View.GONE

        val textView = view?.findViewById<TextView>(R.id.deco_done) // Use rootView.findViewById
        textView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.background)) // Use requireContext()
    }

}
