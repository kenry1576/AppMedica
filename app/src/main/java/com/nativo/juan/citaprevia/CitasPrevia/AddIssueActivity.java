package com.nativo.juan.citaprevia.CitasPrevia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nativo.juan.citaprevia.model.Issue;
import com.nativo.juan.citaprevia.model.IssueType;
import com.nativo.juan.citaprevia.Adapters.IssueTypeAdapter;
import com.nativo.juan.citaprevia.R;
import com.nativo.juan.citaprevia.Res.RestClient;
import com.nativo.juan.citaprevia.Utils.DateTimeUtils;
import com.nativo.juan.citaprevia.Utils.SessionPrefs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddIssueActivity extends AppCompatActivity {

    public static final String TAG = AddIssueActivity.class.getSimpleName();

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Spinner mIssueTypesMenu;
    private IssueTypeAdapter mIssueTypeAdapter;
    private Button mAddPhotoButton;
    private ImageView mPhotoImage;

    private String mCurrentPhotoPath;
    private ImageButton mRemoveButton;
    private TextInputLayout mDescriptionField;
    private Uri mPhotoURI;
    private Toolbar mToolbar;
    private MenuItem mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        mIssueTypesMenu = findViewById(R.id.issue_types_menu);
        mIssueTypeAdapter = new IssueTypeAdapter(this, new ArrayList<IssueType>(0));
        mIssueTypesMenu.setAdapter(mIssueTypeAdapter);
        loadIssueTypes();

        mAddPhotoButton = findViewById(R.id.add_photo_button);
        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCameraApp();
            }
        });

        mRemoveButton = findViewById(R.id.remove_photo_button);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoImage.setVisibility(View.GONE);
                mRemoveButton.setVisibility(View.GONE);
            }
        });

        mPhotoImage = findViewById(R.id.issue_photo);
    }

    private void loadIssueTypes() {
        String userToken = SessionPrefs.get(this).getToken();
        RestClient.getClient().getIssueTypes(userToken).enqueue(new Callback<List<IssueType>>() {
            @Override
            public void onResponse(Call<List<IssueType>> call, Response<List<IssueType>> response) {
                if (response.isSuccessful()) {
                    mIssueTypeAdapter.replaceData(response.body());
                    Log.d(TAG, "Motivos de PQRS cargados desde la API");
                } else {
                    // TODO: Manejar posibles errores
                    Log.d(TAG, "Error Api:" + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<IssueType>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }



    private void showCameraApp() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, "Error ocurrido cuando se estaba creando el archivo de la imagen. Detalle: "+ex.toString());
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.nativo.juan.citaprevia",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = DateTimeUtils.formatDateForFileName(new Date());

        String prefix = "JPEG_" + timeStamp + "_";
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                prefix,
                ".jpg",
                directory
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_issue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.action_save_issue){
            saveIssue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveIssue() {
        // Obtenemos los datos planos
        String userToken = SessionPrefs.get(this).getToken();
        String issueType = ((IssueType) mIssueTypesMenu.getSelectedItem()).getId();
        String description = mDescriptionField.getEditText().getText().toString();
        File image = getPicture();

        // Creamos payloads por cada dato
        RequestBody issueTypeBody = RequestBody.create(MultipartBody.FORM, issueType);
        RequestBody descriptionBody = RequestBody.create(MultipartBody.FORM, description);
        MultipartBody.Part imagePart = null;

        if (image != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), image);

            imagePart = MultipartBody.Part.createFormData("picture", image.getName(), imageBody);
        }

        Call<Issue> createIssue = RestClient.getClient().createIssue(
                userToken,
                issueTypeBody,
                descriptionBody,
                imagePart);

        showProgressIndicator(true);
        showSaveButton(false);
        createIssue.enqueue(new Callback<Issue>() {
            @Override
            public void onResponse(Call<Issue> call, Response<Issue> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Creación de PQR correcta. Datos=" + response.body().toString());
                    showAppointmentsUi();
                } else {
                    // TODO: Manejar errores
                    Log.d(TAG, "Error al crear PQR. Detalles= " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Issue> call, Throwable t) {
                showProgressIndicator(false);
                showSaveButton(true);
                showSavingError();
                Log.d(TAG, "Error al crear PQR. Detalles= " + t.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                handleCameraPhoto();
            }
        }
    }

    private void handleCameraPhoto() {
        if (mCurrentPhotoPath != null) {
            setPic();
            mCurrentPhotoPath = null;
        }
    }

    private void setPic() {
        Glide.with(this).
                load(mCurrentPhotoPath).
                apply(RequestOptions.centerCropTransform()).
                into(mPhotoImage);
        mPhotoImage.setVisibility(View.VISIBLE);
        mRemoveButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showSaveButton(boolean show) {
        if (mSaveButton == null) {
            mSaveButton = mToolbar.getMenu().findItem(R.id.action_save_issue);
        }
        mSaveButton.setVisible(show);
    }

    private void showAppointmentsUi() {
        setResult(RESULT_OK);
        finish();
    }

    private void showSavingError() {
        Snackbar.make(
                findViewById(R.id.add_issue_content),
                "No se pudo guardar el PQR",
                Snackbar.LENGTH_LONG).show();
    }

    private void showProgressIndicator(boolean show) {
        findViewById(R.id.upload_progress_content).setVisibility(show ? View.VISIBLE : View.GONE);
        findViewById(R.id.add_issue_content).setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private File getPicture() {
        File file = null;

        if (mCurrentPhotoPath != null) {
            file = new File(mCurrentPhotoPath);
        }
        return file;
    }

}
