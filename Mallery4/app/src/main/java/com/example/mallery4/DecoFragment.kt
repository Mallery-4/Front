package com.example.mallery4

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_decorate.*

class DecoFragment : Fragment() {
    private val PERMISSION_Album = 101 // Album permission request code
    private val REQUEST_STORAGE = 102 // Request code for image selection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_decorate, container, false)
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
            val drawFragment = DrawFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, drawFragment) // Replace with your actual container ID
            fragmentTransaction.addToBackStack(null) // Optional: Add the transaction to the back stack
            fragmentTransaction.commit()
        }

    }

    private fun requestAlbumPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_Album
            )
        }
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
                requireContext(),
                "You need to grant storage permission to access images from the album.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val textView = requireView().findViewById<TextView>(R.id.deco_done)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    data?.data?.let { uri ->
                        imagePreview.setImageURI(uri)
                        textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.highlightcolor)
                        )


                        textView?.setOnClickListener {
                            val intent = Intent(requireContext(), DrawFragment::class.java)
                            intent.apply {
                                putExtra("uri", data?.data.toString())
                                putExtra("Uri", uri)
                            }

// 프래그먼트로 이동
                            val fragmentManager = requireActivity().supportFragmentManager
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            val drawFragment = DrawFragment()
                            drawFragment.arguments = intent.extras // 인텐트의 인자를 프래그먼트의 인자로 설정
                            fragmentTransaction.replace(R.id.fragmentContainer, drawFragment)
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()

                        }

                    }
                }
            }
        }
    }
}
