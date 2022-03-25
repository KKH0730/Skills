package kr.co.skills.skill

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.co.skills.R
import kr.co.skills.databinding.ActivityPhotoSaveBinding
import kr.co.skills.databinding.ItemPhotoBinding
import kr.co.skills.model.Photo
import kr.co.skills.utils.GridSpacingItemDecoration
import kr.co.skills.utils.MediaUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class PhotoSaveActivity : AppCompatActivity(), View.OnClickListener, PhotoAdapter.OnPhotoListener {
    private var binding : ActivityPhotoSaveBinding? = null
    private val adapter : PhotoAdapter by lazy { PhotoAdapter(this) }
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_save)

        init()
        setRecyclerView()
    }

    private fun init(){
        binding!!.btnBack.setOnClickListener(this)
        binding!!.btnSelectPhoto.setOnClickListener(this)
    }

    private fun setRecyclerView(){
        binding!!.photoRecyclerView.addItemDecoration(GridSpacingItemDecoration(4, 10, false))
        binding!!.photoRecyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnSelectPhoto -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    requestReadStoragePermission(this, storagePermissionListener)
                else
                    requestAllStoragePermission(this, storagePermissionListener)
            }
            R.id.btnBack -> onBackPressed()
        }
    }

    private val storagePermissionListener = object : PermissionListener{
        override fun onPermissionGranted() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            photoSelectLauncher.launch(intent)
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { }
    }

    private fun requestReadStoragePermission(context: Context, listener : PermissionListener){
        TedPermission.with(context)
            .setPermissionListener(listener)
            .setDeniedMessage("해당 기능을 사용하기 위해서 저장공간에 대한 권한이 필요합니다.")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).check()
    }

    private fun requestAllStoragePermission(context: Context, listener : PermissionListener){
        TedPermission.with(context)
            .setPermissionListener(listener)
            .setDeniedMessage("해당 기능을 사용하기 위해서 저장공간에 대한 권한이 필요합니다.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).check()
    }

    private val photoSelectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK) {
            it.data.let { intent ->
                val currentList = adapter.currentList.toMutableList()
                val clipData = intent!!.clipData

                if(clipData?.itemCount!! > 0){
                    for(i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        currentList.add(Photo(id = id++, uri = uri))
                    }
                    adapter.submitList(currentList.toList())
                }
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }

    override fun onDeleteClicked(item: Photo) {
        val currentList = adapter.currentList.toMutableList()
        currentList.remove(item)

        adapter.submitList(currentList.toList())
    }

    override fun onDownLoadClicked(position: Int, uri : Uri) {
        val byteArray = getBytes(uri = uri)
        if(byteArray != null) {
            if(MediaUtil.saveImageToGallery(byteArray, this)){
                Toast.makeText(this, "이미지가 저장되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @Throws(IOException::class)
    fun getBytes(uri: Uri): ByteArray? {
        val inputStream = contentResolver.openInputStream(uri)

        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream?.read(buffer).also {
                if (it != null) { len = it }
            } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}

class PhotoAdapter(
    private val deleteListener: OnPhotoListener
) : ListAdapter<Photo, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
            
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
){
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPhotoBinding>(LayoutInflater.from(parent.context), R.layout.item_photo, parent , false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoViewHolder).bind(currentList[position], deleteListener)
    }

    class PhotoViewHolder(
        private val binding : ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Photo, deleteListener: OnPhotoListener){
            binding.imgPhoto.setImageURI(item.uri)
            binding.btnDelete.setOnClickListener { deleteListener.onDeleteClicked(item) }
            binding.btnDownload.setOnClickListener { deleteListener.onDownLoadClicked(adapterPosition, item.uri) }
        }
    }
    
    interface OnPhotoListener {
        fun onDeleteClicked(item: Photo)
        fun onDownLoadClicked(position: Int, uri : Uri)
    }
}