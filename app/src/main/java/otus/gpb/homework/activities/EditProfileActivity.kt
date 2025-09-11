package otus.gpb.homework.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Полезная статья по разрешениям [shouldShowRequestPermissionRationale] и
 * Result API: [Habr Article](https://habr.com/ru/companies/e-legion/articles/545934/)
 */
class EditProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private val permissionCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::handleCameraPermission
    )

    private val launcherSettings = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { _ ->
        if (isCameraForbidden())
            requirementDialog.show()
    }


    private var currentItemsWhichChooseImageMethodDialog = -1

    private val chooseImageMethodDialogItems by lazy {
        arrayOf(
            resources.getString(R.string.choose_dialog_create_photo),
            resources.getString(R.string.choose_dialog_choose_photo)
        )
    }
    val chooseImageMethodDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.choose_dialog_title)
            .setSingleChoiceItems(chooseImageMethodDialogItems, -1) { _, which ->
                currentItemsWhichChooseImageMethodDialog = which
            }
            .setPositiveButton(
                R.string.choose_dialog_ok,
                ::onPositiveChooseImageMethodDialogListenerClick
            )
        .setNegativeButton(R.string.choose_dialog_canceled) { dialog, _ ->
            currentItemsWhichChooseImageMethodDialog = -1
            dialog.dismiss()
        }
                .setOnDismissListener {
            currentItemsWhichChooseImageMethodDialog = -1
        }
                 .create()
    }

    val rationaleDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.rationale_dialog_title))
            .setMessage(getString(R.string.rationale_dialog_message))
            .setPositiveButton(R.string.rationale_dialog_button_ok, ::startSettingsActivity)
            .setNegativeButton(R.string.rationale_dialog_button_canceled) { _, _ -> }
            .create()
    }
    val requirementDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.rationale_dialog_title))
            .setMessage(getString(R.string.rationale_dialog_message))
            .setPositiveButton(R.string.rationale_dialog_button_ok, ::startSettingsActivity)
            .create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)
        imageView.setOnClickListener { chooseImageMethodDialog.show() }

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
                imageView.setImageDrawable(getDrawableById(R.drawable.cat))
                Toast.makeText(this, "Доступ к камере предоставлен", Toast.LENGTH_SHORT).show()
            }
            isCameraForbidden() -> {
                // доступ к камере запрещен, пользователь поставил галочку Don't ask again.
                rationaleDialog.show()
            }
            else -> {
                // доступ к камере запрещен, пользователь отклонил запрос
                Toast.makeText(this, "Потом попробуешь еще раз", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun isCameraForbidden(): Boolean =
        !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

    /**
     * Получает ресурс drawable по его идентификатору.
     *
     * @param imageId идентификатор ресурса drawable для получения
     * @return ресурс drawable, связанный с данным идентификатором, или null, если не найден
     */
    private fun getDrawableById(imageId: Int) =
        AppCompatResources.getDrawable(this, imageId)

    fun startSettingsActivity(dialog: DialogInterface?, which: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            // если в новом потоке, то ResultApi не дожидается результат
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        launcherSettings.launch(intent)
    }

    fun onPositiveChooseImageMethodDialogListenerClick(dialog: DialogInterface?, which: Int) {
        when (currentItemsWhichChooseImageMethodDialog) {
            0 -> permissionCamera.launch(Manifest.permission.CAMERA)
            1 -> print("todo") //todo
        }
        dialog?.dismiss()
    }

}
