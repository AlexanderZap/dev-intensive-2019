package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.databinding.ActivityProfileBinding
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object{
        const val IS_EDIT_MODE = "IS_EDIT_MODE "
    }

    private lateinit var binding: ActivityProfileBinding

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews(savedInstanceState)
        initViewModels()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)

    }

    private fun initViews(savedInstanceState: Bundle?) {

        viewFields = mapOf(
            "nickname" to binding.tvNickName,
            "rank" to binding.tvRank,
            "firstName" to binding.etFirstName,
            "lastName" to binding.etLastName,
            "about" to binding.etAbout,
            "repository" to binding.etRepository,
            "rating" to binding.tvRating,
            "respect" to binding.tvRespect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        binding.btnEdit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        binding.btSwitchTheme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit : Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_,v) in info){
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if(isEdit) 255 else 0
        }

        binding.icEye.visibility = if(isEdit) View.GONE else View.VISIBLE
        binding.wrAbout.isCounterEnabled = isEdit

        with(binding.btnEdit) {
            val filter: ColorFilter? = if (isEdit){
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val  icon = if (isEdit){
                resources.getDrawable(R.drawable.ic_baseline_save_24, theme)
            } else {
                resources.getDrawable(R.drawable.ic_baseline_edit_24, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun initViewModels() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileDate().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for((k,v) in viewFields){
                v.text = it[k].toString()
            }
        }
    }

    private fun saveProfileInfo() {
        Profile (
            firstName = binding.etFirstName.text.toString(),
            lastName = binding.etLastName.text.toString(),
            about = binding.etAbout.text.toString(),
            repository = binding.etRepository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }
}