package kr.co.skills.skill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.skills.R
import kr.co.skills.databinding.ActivityLayoutAnimBinding
import kr.co.skills.databinding.ItemLinearAnimBinding
import kr.co.skills.model.AnimItem

class LayoutAnimActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityLayoutAnimBinding? = null
    private lateinit var linearAnimAdapter: LinearAnimAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutAnimBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        init()

        setRecyclerView()
    }

    private fun init(){
        binding!!.btnBack.setOnClickListener(this)
        binding!!.btnLinearAnim.setOnClickListener(this)
    }

    private fun setRecyclerView(){
        val list = ArrayList<AnimItem>()
        list.add(AnimItem(id = 0, text = "0번 입니다."))
        list.add(AnimItem(id = 1, text = "1번 입니다."))
        list.add(AnimItem(id = 2, text = "2번 입니다."))
        list.add(AnimItem(id = 3, text = "3번 입니다."))
        list.add(AnimItem(id = 4, text = "4번 입니다."))
        linearAnimAdapter = LinearAnimAdapter()
        linearAnimAdapter.submitList(list.toList())
        binding!!.linearRecyclerView.adapter = linearAnimAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }


    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnBack -> onBackPressed()
            R.id.btnLinearAnim -> binding!!.linearRecyclerView.startLayoutAnimation()
        }
    }
}

class LinearAnimAdapter: ListAdapter<AnimItem, RecyclerView.ViewHolder>(
    object: DiffUtil.ItemCallback<AnimItem>(){
        override fun areItemsTheSame(oldItem: AnimItem, newItem: AnimItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AnimItem, newItem: AnimItem): Boolean {
            return oldItem == newItem
        }
    }
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LinearAnimViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_linear_anim,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LinearAnimViewHolder).bind(currentList[position])
    }

    class LinearAnimViewHolder(
        private val binding: ItemLinearAnimBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: AnimItem){
            val context = binding.root.context
            when(item.id){
                0 -> binding.topLayout.setBackgroundColor(context.getColor(R.color.blue))
                1 -> binding.topLayout.setBackgroundColor(context.getColor(R.color.bg_gray))
                2 -> binding.topLayout.setBackgroundColor(context.getColor(R.color.warn_red))
                3 -> binding.topLayout.setBackgroundColor(context.getColor(R.color.yellow))
                else -> binding.topLayout.setBackgroundColor(context.getColor(R.color.teal_200))
            }

            binding.txtPosition.text = item.id.toString()
            binding.txtContent.text = item.text
        }
    }

}