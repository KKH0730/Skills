package kr.co.skills

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.skills.databinding.ActivityMainBinding
import kr.co.skills.databinding.ItemSkillBinding
import kr.co.skills.model.Skill
import kr.co.skills.skill.*

class MainActivity : AppCompatActivity(), SkillsAdapter.OnSkillItemClickListener {
    private var binding: ActivityMainBinding? = null
    private lateinit var skillAdapter : SkillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSkillRecyclerView()

    }

    private fun setSkillRecyclerView(){
        skillAdapter = SkillsAdapter(this)
        binding!!.skillRecycler.adapter = skillAdapter

        val skillArray = ArrayList<Skill>()
        skillArray.add(Skill(skillName = "Material Design2", orderNumber = 1, isHeroAnim = false))
        skillArray.add(Skill(skillName = "Material Design3", orderNumber = 2, isHeroAnim = false))
        skillArray.add(Skill(skillName = "갤러리 사진 저장", orderNumber = 3, isHeroAnim = false))
        skillArray.add(Skill(skillName = "Hero Animation", orderNumber = 4, isHeroAnim = true))
        skillArray.add(Skill(skillName = "Layout Animation", orderNumber = 5, isHeroAnim = false))
        skillAdapter.submitList(skillArray.toList())
    }

    override fun onSkillClicked(item: Skill, imageView: ImageView, button: AppCompatButton) {
        var intent : Intent? = null

        when(item.orderNumber) {
            1 -> intent = Intent(applicationContext, MaterialDesign2Activity::class.java)
            2 -> intent = Intent(applicationContext, MaterialDesign3Activity::class.java)
            3 -> intent = Intent(applicationContext, PhotoSaveActivity::class.java)
            4 -> intent = Intent(applicationContext, HeroAnimActivity::class.java)
            5 -> intent = Intent(applicationContext, LayoutAnimActivity::class.java)
        }
        if (item.orderNumber != 4) {
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        } else {
            val imagePair: Pair<View, String> = Pair.create(imageView, imageView.transitionName)
            val buttonPair: Pair<View, String> = Pair.create(button, button.transitionName)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, imagePair, buttonPair)
            startActivity(intent, options.toBundle())
        }

    }


    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}

class SkillsAdapter(
    private val onSkillItemClickListener : OnSkillItemClickListener
) : ListAdapter<Skill, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Skill>(){
        override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean {
            return oldItem.orderNumber == newItem.orderNumber
        }

        override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean {
            return oldItem == newItem
        }
    } 
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemSkillBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_skill,
            parent,false
        )
        return SkillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SkillViewHolder).bind(getItem(position), onSkillItemClickListener)
    }

    class SkillViewHolder(
        private val binding : ItemSkillBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Skill, onSkillItemClickListener : OnSkillItemClickListener){
            binding.btnSkill.text = item.skillName
            if (item.isHeroAnim) {
                binding.img.visibility = View.VISIBLE
            } else {
                binding.img.visibility = View.GONE
            }
            binding.btnSkill.setOnClickListener { onSkillItemClickListener.onSkillClicked(item, binding.img, binding.btnSkill) }
        }
    }

    interface OnSkillItemClickListener {
        fun onSkillClicked(item : Skill, imageView: ImageView, button: AppCompatButton)
    }

}