package otus.gpb.homework.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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

    private val permissionCameraFromChooseImageDialog = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::handleCameraPermissionFromChooseImageDialog
    )

    private val permissionCameraAfterSettingsActivity = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::handleCameraPermissionAfterSettingsActivity
    )

    private val launcherSettings = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { _ ->
        Log.d("debug_granted", "Callback launcherSettings.")
        permissionCameraAfterSettingsActivity.launch(Manifest.permission.CAMERA)
    }

    private var currentItemsWhichChooseImageDialog = -1

    private val chooseImageDialogItems by lazy {
        arrayOf(
            resources.getString(R.string.choose_dialog_create_photo),
            resources.getString(R.string.choose_dialog_choose_photo)
        )
    }

    //region dialogs
    val chooseImageDialog: AlertDialog
        get() {
            Log.d("debug_granted", "chooseImageDialog getter.")
            return MaterialAlertDialogBuilder(this)
                .setTitle(R.string.choose_dialog_title)
                .setSingleChoiceItems(chooseImageDialogItems, -1) { _, which ->
                    currentItemsWhichChooseImageDialog = which
                }
                .setPositiveButton(
                    R.string.choose_dialog_ok,
                    ::onPositiveChooseImageDialogListenerClick
                )
                .setNegativeButton(R.string.choose_dialog_canceled) { dialog, _ ->
                    Log.d("debug_granted", "chooseImageDialog onNegativeButtonClickListener.")
                    dialog.dismiss()
                }
                .setOnDismissListener {
                    Log.d("debug_granted", "chooseImageDialog onDismissListener.")
                    currentItemsWhichChooseImageDialog = -1
                }
                .create()
        }

    val rationaleDialog: AlertDialog
        get() {
            Log.d("debug_granted", "rationaleDialog getter.")
            return MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.rationale_dialog_title))
                .setMessage(getString(R.string.rationale_dialog_message))
                .setPositiveButton(
                    R.string.rationale_dialog_button_ok,
                    ::rationaleDialogInClickListener
                )
                .setNegativeButton(R.string.rationale_dialog_button_canceled) { _, _ ->
                    Log.d("debug_granted", "rationaleDialog onNegativeButtonClickListener.")
                }
                .setOnDismissListener {
                    Log.d("debug_granted", "rationaleDialog onDismissListener.")
                }
                .create()
        }

    val requirementDialog: AlertDialog
        get() {
            Log.d("debug_granted", "requirementDialog getter.")
            return MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.rationale_dialog_title))
                .setMessage(getString(R.string.rationale_dialog_message))
                .setPositiveButton(
                    R.string.rationale_dialog_button_ok,
                    ::requirementDialogInClickListener
                )
                .setOnDismissListener {
                    Log.d("debug_granted", "requirementDialog onDismissListener.")
                }
                .create()
        }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)
        imageView.setOnClickListener { chooseImageDialog.show() }

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

    private fun handleCameraPermissionFromChooseImageDialog(granted: Boolean) {
        when {
            granted -> {
                imageView.setImageDrawable(getDrawableById(R.drawable.cat))
                Log.d("debug_granted", "handleCameraPermissionFromChooseImageDialog: granted")
                Toast.makeText(this, "Доступ к камере предоставлен", Toast.LENGTH_SHORT).show()
            }
            // доступ к камере запрещен, пользователь поставил галочку Don't ask again.
            isCameraForbidden() -> {
                Log.d("debug_granted", "camera forbidden")
                rationaleDialog.show()
            }
            // доступ к камере запрещен, пользователь отклонил запрос
            else -> {
                Log.d("debug_granted", "camera denied")
                Toast.makeText(this, "Потом попробуешь еще раз", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleCameraPermissionAfterSettingsActivity(granted: Boolean) {
        Log.d("debug_granted", "handleCameraPermissionAfterSettingsActivity: granted: $granted")
        if (!granted) requirementDialog.show()
        else imageView.setImageDrawable(getDrawableById(R.drawable.cat))

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

    //region listeners
    private fun rationaleDialogInClickListener(dialog: DialogInterface?, which: Int) {
        Log.d("debug_granted", "rationaleDialogInClickListener")
        startSettingsActivity()
        dialog?.dismiss()
    }

    private fun requirementDialogInClickListener(dialog: DialogInterface?, which: Int) {
        Log.d("debug_granted", "requirementDialogInClickListener")
        startSettingsActivity()
        dialog?.dismiss()
    }

    fun onPositiveChooseImageDialogListenerClick(dialog: DialogInterface?, which: Int) {
        Log.d("debug_granted", "onPositiveChooseImageDialogListenerClick")
        when (currentItemsWhichChooseImageDialog) {
            0 -> permissionCameraFromChooseImageDialog.launch(Manifest.permission.CAMERA)
            1 -> print("todo") //todo
        }
        dialog?.dismiss()
    }
    //endregion

    fun startSettingsActivity() {
        Log.d("debug_granted", "startSettingsActivity")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            // если в новом потоке, то ResultApi не дожидается результат
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        launcherSettings.launch(intent)
    }
}
