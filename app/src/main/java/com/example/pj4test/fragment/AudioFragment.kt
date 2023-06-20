package com.example.pj4test.fragment

import SharedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pj4test.ProjectConfiguration
import com.example.pj4test.audioInference.KnockClassifier
import com.example.pj4test.databinding.FragmentAudioBinding

class AudioFragment: Fragment(), KnockClassifier.DetectorListener {
    private val TAG = "AudioFragment"

    private var _fragmentAudioBinding: FragmentAudioBinding? = null

    private val fragmentAudioBinding
        get() = _fragmentAudioBinding!!

    // classifiers
    lateinit var knockClassifier: KnockClassifier

    // views
    lateinit var snapView: TextView

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentAudioBinding = FragmentAudioBinding.inflate(inflater, container, false)

        return fragmentAudioBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snapView = fragmentAudioBinding.SnapView

        knockClassifier = KnockClassifier()
        knockClassifier.initialize(requireContext())
        knockClassifier.setDetectorListener(this)
    }

    override fun onPause() {
        super.onPause()
        knockClassifier.stopInferencing()
    }

    override fun onResume() {
        super.onResume()
        knockClassifier.startInferencing()
    }

    override fun onResults(score: Float) {
        activity?.runOnUiThread {
            if (score > KnockClassifier.THRESHOLD) {
                snapView.text = "Knocking!"
                snapView.setBackgroundColor(ProjectConfiguration.activeBackgroundColor)
                snapView.setTextColor(ProjectConfiguration.activeTextColor)

                sharedViewModel.updateKnockDetected(true)
                onPause()
            } else {
                snapView.text = "Quiet"
                snapView.setBackgroundColor(ProjectConfiguration.idleBackgroundColor)
                snapView.setTextColor(ProjectConfiguration.idleTextColor)

                sharedViewModel.updateKnockDetected(false)
            }
        }
    }
}