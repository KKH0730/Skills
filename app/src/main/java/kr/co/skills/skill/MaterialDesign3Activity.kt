package kr.co.skills.skill

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import kr.co.skills.R
import kr.co.skills.databinding.ActivityMaterialDesign3Binding
import kr.co.skills.databinding.LayoutRailHeaderBinding

class MaterialDesign3Activity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityMaterialDesign3Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialDesign3Binding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.btnDropDown.setOnClickListener(this)
        setNavigationRailViewEvent()

        showExposedDropDownMenu()
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


    @SuppressLint("RestrictedApi")
    private fun showDropDownMenu(v: View){
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(R.menu.menu_drop_down, popup.menu)

        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            menuBuilder.visibleItems.forEach { item ->
                val iconMarginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()

                item.icon?.let {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(it, iconMarginPx, 0, iconMarginPx, 0)
                    } else {
                        item.icon = object: InsetDrawable(it, iconMarginPx, 0, iconMarginPx, 0) {
                            override fun getIntrinsicWidth(): Int {
                                return it.intrinsicWidth + iconMarginPx + iconMarginPx
                            }
                        }
                    }
                }
            }
        }
        popup.show()


        popup.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.dropDown1 -> {
                    Toast.makeText(this, "DropDown1 Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.dropDown2 -> {
                    Toast.makeText(this, "DropDown2 Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.dropDown3 -> {
                    Toast.makeText(this, "DropDown3 Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.dropDown4 -> {
                    Toast.makeText(this, "DropDown4 Clicked",Toast.LENGTH_SHORT).show()
                }
            }
            false
        }

        popup.setOnDismissListener {
            Snackbar.make(binding!!.btnDropDown, "DropDown Dismissed!!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showExposedDropDownMenu() {
        val items = listOf("items 1","items 2","items 3","items 4")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding!!.textInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding!!.textInputLayout.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            when(position) {
                0 -> Toast.makeText(this, "ExposedDropDown 1 Clicked",Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "ExposedDropDown 2 Clicked",Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this, "ExposedDropDown 3 Clicked",Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(this, "ExposedDropDown 4 Clicked",Toast.LENGTH_SHORT).show()
            }
        }
        (binding!!.textInputLayout.editText as? AutoCompleteTextView)?.setOnDismissListener {
            Snackbar.make(binding!!.btnDropDown, "ExposedDropDownMenu Dismissed!!", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnReturn -> onBackPressed()
            R.id.layoutDraw -> Toast.makeText(this, "Draw Clicked", Toast.LENGTH_SHORT).show()
            R.id.btnDraw -> Toast.makeText(this, "Draw Clicked", Toast.LENGTH_SHORT).show()
            R.id.btnDropDown ->  showDropDownMenu(v)
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



