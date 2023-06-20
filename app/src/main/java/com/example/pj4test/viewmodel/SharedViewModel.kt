import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _knockDetected = MutableLiveData<Boolean>()
    val knockDetected: LiveData<Boolean> get() = _knockDetected

    fun setKnockDetected(detected: Boolean) {
        _knockDetected.value = detected
    }

    fun updateKnockDetected(detected: Boolean) {
        _knockDetected.postValue(detected)
    }
}