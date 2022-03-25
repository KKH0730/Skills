package kr.co.skills.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

object MediaUtil {

    fun saveImageToGallery(array: ByteArray, context: Context) : Boolean{
        val values = ContentValues()
        val currentTime = System.currentTimeMillis()


        val contentResolver: ContentResolver = context.contentResolver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val relativeLocation = Environment.DIRECTORY_DCIM + File.separator + "Skills";

            values.put(MediaStore.Images.Media.DISPLAY_NAME, "${currentTime}.jpg")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            values.put(MediaStore.Images.Media.IS_PENDING, 1)  // IS_PENDING을 1로 설정 > 현재 파일을 업데이트 전까지 외부에서 접근하지 못하도록 함
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)

            val item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            try {
                val pdf = contentResolver.openFileDescriptor(item!!, "w", null)
                val fos = FileOutputStream(pdf?.fileDescriptor)
                fos.write(array)
                fos.close()

                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)  // 파일 저장이 완료되면 IS_PENDING 0으로 변경한다.
                contentResolver.update(item, values, null, null)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.d("test", "FileNotFoundException : ${e.message}")
                return false
            } catch (e: java.lang.Exception) {
                Log.d("test", "FileOutputStream : ${e.message}")
                return false
            }
            return true
        } else {
            val rootFolder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Skills") //루트 폴더 생성
            if (!rootFolder.exists()) { rootFolder.mkdirs() }
            val imageRoot = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/Skills", "${currentTime}.jpg") //이미지가 저장될 경로를 지정

            try {
                val outputStream = FileOutputStream(imageRoot)
                outputStream.write(array)
                outputStream.close()

                values.put(MediaStore.Images.Media.DISPLAY_NAME, "${currentTime}.jpg")
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                values.put(MediaStore.Images.Media.DATA, "${rootFolder.path}/${currentTime}.jpg")
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
            } catch (e: Exception) {
                Log.d("test", "error : ${e.message}")
                e.printStackTrace()
                return false
            }
            return true
        }
    }
}