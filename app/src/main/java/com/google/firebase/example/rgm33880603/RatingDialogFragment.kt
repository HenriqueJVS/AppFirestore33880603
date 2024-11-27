package com.google.firebase.example.rgm33880603

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.edu.up.rgm33880603.databinding.DialogRatingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.example.rgm33880603.model.Rating
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Dialog Fragment containing rating form.
 */
class RatingDialogFragment : DialogFragment() {

    private var _binding: DialogRatingBinding? = null
    private val binding get() = _binding!!
    private var ratingListener: RatingListener? = null

    internal interface RatingListener {
        fun onRating(rating: Rating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRatingBinding.inflate(inflater, container, false)

        binding.restaurantFormButton.setOnClickListener { onSubmitClicked() }
        binding.restaurantFormCancel.setOnClickListener { onCancelClicked() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (parentFragment is RatingListener) {
            ratingListener = parentFragment as RatingListener
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun onSubmitClicked() {
        val user = Firebase.auth.currentUser
        user?.let {
            val rating = Rating(
                it,
                binding.restaurantFormRating.rating.toDouble(),
                binding.restaurantFormText.text.toString()
            )

            // Add the rating to the Firestore subcollection
            val restaurantId = "abc123" // Replace with the actual restaurant ID
            val firestore = Firebase.firestore
            val subRef = firestore.collection("restaurants")
                .document(restaurantId)
                .collection("ratings")

            subRef.add(rating)
                .addOnSuccessListener {
                    Log.d(TAG, "Rating successfully added!")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding rating", e)
                }

            ratingListener?.onRating(rating)
        }

        dismiss()
    }

    private fun onCancelClicked() {
        dismiss()
    }

    companion object {
        const val TAG = "RatingDialog"
    }
}
