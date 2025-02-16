package ge.designs.design_system.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import ge.designs.design_system.R
import ge.designs.design_system.databinding.ViewButtonBinding

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private val binding = ViewButtonBinding.inflate(LayoutInflater.from(context), this)

    var buttonType: ButtonType = ButtonType.PRIMARY
        set(value) {
            field = value
            setButtonColors()
        }


    var isLoading: Boolean = false
        set(value) {
            setLoader(value)
            isEnabled = isEnabled
            field = value
        }

    var text: String = ""
        set(value) {
            binding.tvTitle.text = value
            field = value
        }

    var btnEnabled: Boolean = true
        set(value) {
            isEnabled = value
            field = value
        }

    init {
        isClickable = true
        isFocusable = true
        context.withStyledAttributes(attrs, R.styleable.ButtonView) {
            buttonType = ButtonType.getEnumForValue(getInt(R.styleable.ButtonView_btnType, 0))
            text = getString(R.styleable.ButtonView_text) ?: ""
            btnEnabled = getBoolean(R.styleable.ButtonView_btnEnabled, true)
            isLoading = getBoolean(R.styleable.ButtonView_loading, false)


        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.container.isEnabled = enabled
    }

    private fun setLoader(loading: Boolean) {
        with(binding) {
            loader.setAnimation(buttonType.loader)
            loader.playAnimation()
            tvTitle.visibility = if (loading) View.INVISIBLE else View.VISIBLE
            loader.isVisible = loading
        }
        isPressed = false
        isClickable = !loading

    }

    private fun setButtonColors() {
        binding.tvTitle.setTextColor(ContextCompat.getColor(context, buttonType.contentColor))
        binding.container.setBackgroundResource(buttonType.backgroundDrawable)

    }


}