package otus.gpb.homework.activities

import android.Manifest
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private val permissionCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::handleCameraPermission
    )

    private val chooseImageMethodAlertDialogItems = arrayOf(
        resources.getString(R.string.aler_dialog_create_foto),
        resources.getString(R.string.aler_dialog_choose_foto)
    )
    private var currentItemsWhichInChooseImageMethodAlertDialog = -1

    val chooseImageMethodAlertDialog: AlertDialog =
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.aler_dialog_title)
            .setSingleChoiceItems(chooseImageMethodAlertDialogItems, -1) { _, which ->
                currentItemsWhichInChooseImageMethodAlertDialog = which
            }
            .setPositiveButton(
                R.string.alert_dialog_ok,
                ::onPositiveChooseImageMethodAlertDialogListenerClick
            )
            .setNegativeButton(R.string.aler_dialog_canceled) { _, _ -> }
            .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)
        imageView.setOnClickListener { chooseImageMethodAlertDialog.show() }

        findViewById<Toolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.send_item -> {
                        openSenderApp()
                        true
                    }

                    else -> false
                }
            }
        }
    }


    /**
     * Используйте этот метод чтобы отобразить картинку полученную из медиатеки в ImageView
     */
    private fun populateImage(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        imageView.setImageBitmap(bitmap)
    }

    private fun openSenderApp() {
        TODO("В качестве реализации метода отправьте неявный Intent чтобы поделиться профилем. В качестве extras передайте заполненные строки и картинку")
    }

    private fun handleCameraPermission(granted: Boolean) {
        when {
            granted -> {
                Toast.makeText(this, "Получили доступ к камере", Toast.LENGTH_SHORT).show()
                imageView.setImageDrawable(getDrawableById(R.drawable.cat))
            }

            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Пользователь еще раз запросил разрешение на использование камеры после отмены
                // → покажите Rationale Dialog, и объясните зачем вам камера

                Toast.makeText(
                    this,
                    "Нам нужен доступ к камере чтобы следить за тобой ночью",
                    Toast.LENGTH_LONG
                ).show()

            }

            else -> {
                // Пользователь нажал не разрешать (однократно)
                Toast.makeText(this, "Потом попробуешь еще раз", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Получает ресурс drawable по его идентификатору.
     *
     * @param imageId идентификатор ресурса drawable для получения
     * @return ресурс drawable, связанный с данным идентификатором, или null, если не найден
     */
    private fun getDrawableById(imageId: Int) =
        AppCompatResources.getDrawable(this, imageId)

    fun onPositiveChooseImageMethodAlertDialogListenerClick(dialog: DialogInterface?, which: Int) {
        //create foto
        if (currentItemsWhichInChooseImageMethodAlertDialog == 0) permissionCamera.launch(
            Manifest.permission.CAMERA
        )
        //choose_foto
        if (currentItemsWhichInChooseImageMethodAlertDialog == 1) {
            //todo
        }

    }

}
