package com.sungkyul.imagesearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.sungkyul.imagesearch.Fragment.CrawlFragment;
import com.sungkyul.imagesearch.Fragment.DescriptionFragment;
import com.sungkyul.imagesearch.Fragment.NoResultFragment;
import com.sungkyul.imagesearch.Fragment.SuccessFragment;
import com.sungkyul.imagesearch.es.Description;
import com.sungkyul.imagesearch.es.ESDescriptionManager;
import com.sungkyul.imagesearch.es.ESFoodManager;
import com.sungkyul.imagesearch.es.ESTouristManager;
import com.sungkyul.imagesearch.es.Food;
import com.sungkyul.imagesearch.es.IDescriptionManager;
import com.sungkyul.imagesearch.es.IFoodManager;
import com.sungkyul.imagesearch.es.ITouristManager;
import com.sungkyul.imagesearch.es.Tourist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyCw4TXyc9Z9HxiThagHAnnKjbJELM4hvTQ";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10; //최대 결과 수
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

//    private TextView mImageDetails;
//    private ImageView mMainImage;
    private EditText edit_keyword;
    private LinearLayout recommendkeyword;

    //fragment
    private FragmentManager fragmentManager;
    private SuccessFragment fragment_suceess;
    private CrawlFragment fragment_crawl;
    private NoResultFragment fragment_noresult;
    private DescriptionFragment fragment_description;
    private FragmentTransaction transaction;

    //ES
    private IFoodManager foodManager;
    private List<Food> foods;
    private IDescriptionManager descriptionManager;
    private List<Description> descriptions;
    private ITouristManager touristManager;
    private List<Tourist> tourists;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton search = findViewById(R.id.search);
        ImageButton camera = findViewById(R.id.camera);
        edit_keyword = findViewById(R.id.edit_keword);
        recommendkeyword = findViewById(R.id.recommendkeyword);

        //fragment
        fragmentManager = getSupportFragmentManager();

        fragment_suceess = new SuccessFragment();
        fragment_crawl = new CrawlFragment();
        fragment_noresult = new NoResultFragment();
        fragment_description = new DescriptionFragment();

        transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.frameLayout, fragment_suceess).commitAllowingStateLoss();

        //ES
        //***s는 데이터를 담아놓는 리스트, ***manager는 함수호출용
        foodManager = new ESFoodManager();
        foods = new ArrayList<>();
        descriptionManager = new ESDescriptionManager();
        descriptions = new ArrayList<>();
        touristManager = new ESTouristManager();
        tourists = new ArrayList<>();

        //카메라 버튼 클릭시
        camera.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder
                    .setMessage("사진 촬영과 갤러리 선택중 고르시오.")
                    .setPositiveButton("갤러리", (dialog, which) -> startGalleryChooser())
                    .setNegativeButton("사진 촬영", (dialog, which) -> startCamera());
            builder.create().show();
//            recommendkeyword.setVisibility(View.INVISIBLE);
        });

        //검색 버튼 클릭시
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ES
                String searchText = edit_keyword.getText().toString();
                Thread thread = new SearchThread(searchText);
                thread.start();
                try {
                    thread.join();
                }catch (InterruptedException e ){
                    e.printStackTrace();
                }

                if(!descriptions.isEmpty() && !foods.isEmpty() && !tourists.isEmpty()) {
                    Log.i(TAG, "description[0] ==> " + descriptions.get(0).toString());
                    Log.i(TAG, "food[0] ==> " + foods.get(0).toString());
                    Log.i(TAG, "tourist[0] ==> " + tourists.get(0).toString());
                }

                //키워드 검색시에는 2가지경우
                if(!descriptions.isEmpty() && !foods.isEmpty() && !tourists.isEmpty()){
                    // 1. Elasticsearch에 결과가 있는경우 --> ! des,food,tourist.isEmpty()
                    Log.i(TAG, "키워드 검색 -> 엘라스틱 서치에 결과가 있다.");

                    //검색이 성공한경우 bundle에 담아서 프래그먼트로 전송
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("des_list" ,(ArrayList<? extends Parcelable>) descriptions);
                    bundle.putParcelableArrayList("food_list" ,(ArrayList<? extends Parcelable>) foods);
                    bundle.putParcelableArrayList("tourist_list" ,(ArrayList<? extends Parcelable>) tourists);

                    fragment_description.setArguments(bundle);
                    onFragmentChange(0); //successFragment로 변경

                }else{
                    // 2. Elasticsearch에 결과가 없는경우 --> des,food,tourist.isEmpty()
                    Log.i(TAG, "키워드 검색 -> 결과가 없다.");
                    onFragmentChange(2); //noResultFragment로 변경
                }

            }
        });

//        mImageDetails = findViewById(R.id.image_details);
//        mMainImage = findViewById(R.id.main_image);
    }

    public void onFragmentChange(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_description).commit();
        }else if(index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_crawl).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_noresult).commit();
        }
    }

    //사진 입력시 갤러리 선택
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    //사진 입력시 카메라 시작
    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
            // content://com.google.android.apps.docs.storage/document/acc%3D1%3Bdoc%3Dencoded%3Dgss2Sqjm2NIucVg93PJvTJSr%2FlGe8iq85aF1v1Nb9W3CQZXtckqc
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);
                Log.i("uri ==> ",  uri.getPath());
//                String str = uri.getPath();
//                edit_keyword.setText(uri.getPath());

                callCloudVision(bitmap);
//                mMainImage.setImageBitmap(bitmap);

                if(uri.getPath().equals("/document/acc=1;doc=encoded=gss2Sqjm2NIucVg93PJvTJSr/lGe8iq85aF1v1Nb9W3CQZXtckqc")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            recommendkeyword.setVisibility(View.VISIBLE);
                        }
                    },3000);

                }



            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "Image picking failed", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "Image picker gave us a null image.", Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
//                labelDetection.setType("LABEL_DETECTION");

                //랜드마크로 설정
                labelDetection.setType("LANDMARK_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<MainActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(MainActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            MainActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
//                TextView imageDetail = activity.findViewById(R.id.image_details);
//                imageDetail.setText(result);
            }
        }
    }

    //vision ai call
    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
//        mImageDetails.setText("이미지 업로드 중입니다.\n잠시만 기다려주세요.");

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;

        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    //번역 부분
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("검색된 결과 : ");

//        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLandmarkAnnotations();

        //labels != null는 비전 ai가 검색이 잘 되었을때
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                Log.i("label.getDescription ", "번역하기전 결과 label.getDescription ==> " + label.getDescription());

                //번역하기
                String keyword = "No";
                try {
                    keyword = new NaverTranslateTask().doInBackground(label.getDescription());
                }catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("keyword ", "번역한 결과 keyword ==> " + keyword);

                //vision ai => o, Elasticsearch => o
                if(keyword.equals("경복궁")){
                    onFragmentChange(0);
                    Log.i("onFragmentChange => 0 ", "0번으로 변경 => 성공");
//                    message.append("경복궁");
//                    Intent intent = new Intent(MainActivity.this, GyeongbokgungActivity.class);
//                    startActivity(intent);
                }

                //vision ai => o, Elasticsearch => x
                else{
                    onFragmentChange(2);
                    Log.i("onFragmentChange => 2 ", "2번으로 변경 => noresult");
                    message.append(keyword);
                }
                message.append("\n");
                break;
            }
        } else {
            //vision ai => x --> 크롤로 가야함
            message.append("키워드 추출 실패");
            onFragmentChange(1);
            Log.i("onFragmentChange => 1 ", "1번으로 변경 => 크롤");
        }

        return message.toString();
    }

    //검색하는 스레드
    class SearchThread extends Thread{

        private String search;

        public SearchThread(String s) {
            search = s;
        }

        @Override
        public void run() {

            descriptions.clear();
            descriptions.addAll(descriptionManager.searchDescription(search,null));

            foods.clear();
            foods.addAll(foodManager.searchFoods(search, null));

            tourists.clear();
            tourists.addAll(touristManager.searchTourists(search,null));

        }
    }

}