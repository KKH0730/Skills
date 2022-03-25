package kr.co.skills.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import kr.co.skills.R
import kr.co.skills.databinding.ActivityMaterialDesign3Binding
import kr.co.skills.databinding.LayoutRailHeaderBinding

class MaterialDesign3Activity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityMaterialDesign3Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialDesign3Binding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setNavigationRailViewEvent()
    }

    private fun setNavigationRailViewEvent(){
        binding!!.navigationRail.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.folder -> {
                    Toast.makeText(this, "Folder Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.recent -> {
                    binding!!.navigationRail.getBadge(R.id.recent)?.let {
                        if (binding!!.navigationRail.selectedItemId != R.id.recent) { binding!!.navigationRail.removeBadge(R.id.recent) }
                    }
                    Toast.makeText(this, "Recent Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.images -> {
                    Toast.makeText(this, "Images Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.library -> {
                    binding!!.navigationRail.getBadge(R.id.library)?.let {
                        if (binding!!.navigationRail.selectedItemId != R.id.library) { binding!!.navigationRail.removeBadge(R.id.library) }
                    }
                    Toast.makeText(this, "Library Clicked",Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        binding!!.navigationRail.headerView?.let {
            (it.findViewById(R.id.btnReturn) as AppCompatImageButton).setOnClickListener(this)
            (it.findViewById(R.id.layoutDraw) as ConstraintLayout).setOnClickListener(this)
            (it.findViewById(R.id.btnDraw) as AppCompatImageButton).setOnClickListener(this)
        }

        binding!!.navigationRail.getOrCreateBadge(R.id.library).apply {
            this.backgroundColor = getColor(R.color.warn_red)
            this.badgeTextColor = getColor(R.color.white)
            this.maxCharacterCount = 3
            this.number = 100
            this.isVisible = true
        }

        binding!!.navigationRail.getOrCreateBadge(R.id.recent).apply {
            this.backgroundColor = getColor(R.color.warn_red)
            this.isVisible = true
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnReturn -> onBackPressed()
            R.id.layoutDraw -> Toast.makeText(this, "Draw Clicked", Toast.LENGTH_SHORT).show()
            R.id.btnDraw -> Toast.makeText(this, "Draw Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}

