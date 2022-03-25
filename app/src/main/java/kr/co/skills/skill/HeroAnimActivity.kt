package kr.co.skills.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.skills.R
import kr.co.skills.databinding.ActivityHeroAnimBinding

class HeroAnimActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityHeroAnimBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroAnimBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        init()
    }

    private fun init(){
        binding!!.btnBack.setOnClickListener(this)
        binding!!.img.setImageResource(R.mipmap.ic_launcher)
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnBack -> {
                onBackPressed()
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