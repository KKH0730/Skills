package kr.co.skills.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.co.skills.R
import kr.co.skills.databinding.ActivityMaterialDesignBinding

class MaterialDesign2Activity : AppCompatActivity() {
    private var binding : ActivityMaterialDesignBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_material_design)

        setMaterialAppBar()

    }

    private fun setMaterialAppBar(){
        binding!!.toolBar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
        }

        binding!!.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    Toast.makeText(applicationContext, "search 아이콘 클릭", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.more -> {
                    Toast.makeText(applicationContext, "more 아이콘 클릭", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
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