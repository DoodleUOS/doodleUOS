package team.udacity.uos.doodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import team.udacity.uos.doodle.R;
import team.udacity.uos.doodle.activity.DetailViewActivity;
import team.udacity.uos.doodle.model.Doodle;
import team.udacity.uos.doodle.network.VolleyHelper;
import team.udacity.uos.doodle.network.request.DoodleUploadRequest;
import team.udacity.uos.doodle.util.Constants;
import team.udacity.uos.doodle.util.DBHelper;
import team.udacity.uos.doodle.util.GpsInfo;

/**
 * Created by include on 2015. 1. 21..
 */
public class DoodleFragment extends Fragment {

    @InjectView(R.id.editTextLocation)
    EditText mEditTextLocation;
    @InjectView(R.id.editTextContext)
    EditText mEditTextContext;
    @InjectView(R.id.buttonDoodleUpload)
    ImageView mButtonDoodleUpload;
    @InjectView(R.id.load_picture)
    ImageButton mLoadPicture;
    @InjectView(R.id.tag_user)
    ImageButton mTagUser;
    @InjectView(R.id.imageView)
    ImageView mImageView;

    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_TAG_FRIEND = 2;
    private String mImagePath = "";
    private int mTagNo = 0;
    private String mTagFbNo = "";

    private GpsInfo gps;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_doodle, container, false);

        ButterKnife.inject(this, layout);

        // 사진 불러오기 버튼 이벤트
        mLoadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        // 친구 태그 버튼 이벤트
        mTagUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 이름 넣으면 완성
//                Intent i = new Intent(getActivity(), xxx.class);
//                startActivityForResult(i, RESULT_TAG_FRIEND);
            }
        });

        // 메모 업로드 버튼 이벤트
        mButtonDoodleUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mLocation = String.valueOf(mEditTextLocation.getText());
                String mContext = String.valueOf(mEditTextContext.getText());

                if (mLocation.length() == 0 || mContext.length() == 0) {
                    Toast.makeText(getActivity(), "장소와 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

                gps = new GpsInfo(getActivity());
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

                SharedPreferences prefs = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                Doodle mDoodle = new Doodle();
                mDoodle.setDooMemNo(prefs.getInt(Constants.USER_NO, -1));
                mDoodle.setDooLoca(mLocation);
                mDoodle.setDooCon(mContext);
                mDoodle.setDooLat(latitude);
                mDoodle.setDooLong(longitude);

                // 메모 하나 받아오는 통신
                Response.Listener<Doodle> listener = new Response.Listener<Doodle>() {
                    @Override
                    public void onResponse(Doodle response) {
                        if (response.getDooNo() == 0) {
                            Toast.makeText(getActivity(), "[실패]", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(getActivity(), "[성공] 글번호 : " + response.getDooNo(), Toast.LENGTH_SHORT).show();


                            new AsyncTask<Void, Void, List<Doodle>>() {
                                @Override
                                protected List<Doodle> doInBackground(Void... params) {
                                    DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(), DBHelper.class);
                                    List<Doodle> items = null;
                                    try {
                                        final Dao<Doodle, Long> dao = dbHelper.getDao(Doodle.class);
                                        items = dao.queryForAll();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    OpenHelperManager.releaseHelper();
                                    return items;
                                }

                                @Override
                                protected void onPostExecute(List<Doodle> doodles) {
                                    // 여기서 하고싶은거 하면 됨
                                    Log.i("Help222", "Database size : " + doodles.size());
                                }
                            }.execute();

                            Toast.makeText(getActivity(), "메모를 업로드하였습니다.", Toast.LENGTH_SHORT).show();

                            Intent mDetailIntent = new Intent(getActivity(), DetailViewActivity.class);
                            mDetailIntent.putExtra("doodleNo", response.getDooNo());
                            startActivity(mDetailIntent);
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "통신상태가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                };

                DoodleUploadRequest doodleRequest = new DoodleUploadRequest(getActivity(), listener, errorListener);
                if(mImagePath.length() == 0){       // 이미지 X
                    if(mTagNo == 0){    // 친구 태그 X
                        doodleRequest.setParameter(mDoodle, "empty", "empty");
                    }
                    else{       // 친구 태그 O
                        doodleRequest.setParameter(mDoodle, "empty", "tag", mTagNo);
                    }
                }
                else{       // 이미지 O
                    if(mTagNo == 0){    // 친구 태그 X
                        doodleRequest.setParameter(mDoodle, "image", "empty", imageToString(mImagePath));
                    }
                    else{       // 친구 태그 O
                        doodleRequest.setParameter(mDoodle, "image", "tag", imageToString(mImagePath), mTagNo);
                    }
                }

                VolleyHelper.getRequestQueue().add(doodleRequest);
            }
        });

        return layout;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 친구 태그
        if(requestCode == RESULT_TAG_FRIEND){
            // 태그할 친구의 회원번호를 저장
            // 태그할 친구의 페북 프로필을 띄어주셈
            // 임시임시
//          mTagNo = Integer.parseInt(data);
//          mTagFbNo = data.getDataString();
        }
        // 그림 불러오기
        else if(requestCode == RESULT_LOAD_IMAGE){
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Cursor c = getActivity().getContentResolver().query(Uri.parse(data.getDataString()), null, null, null, null);
                c.moveToNext();
                String tempPath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                Uri uri = Uri.fromFile(new File(tempPath));
                c.close();

                final Bitmap b = BitmapFactory.decodeFile(tempPath, options);

                // temp_img.setImageURI(data.getData());

                //img[0].setImageBitmap(b);
                //ImgPath[0] = tempPath;

                mImagePath = tempPath;
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageBitmap(b);
            } catch (Exception e) {

            }
        }

    }

    // 이미지를 String으로 변환
    private String imageToString(String uploadFilePath) {
        if (uploadFilePath != null) {
            byte[] buffer = new byte[1024];
            try {
                final File file = new File(uploadFilePath);
                BufferedInputStream inputStream = new BufferedInputStream(new BufferedInputStream(new FileInputStream(file)));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Base64OutputStream outputStream = new Base64OutputStream(new BufferedOutputStream(baos), Base64.DEFAULT);
                int n;
                while ((n = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, n);
                }
                outputStream.flush();
                outputStream.close();
                String s = new String(baos.toByteArray());
                return s;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
        else{
            Toast.makeText(getActivity(), "파일 경로가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();

            return null;
        }
    }
}