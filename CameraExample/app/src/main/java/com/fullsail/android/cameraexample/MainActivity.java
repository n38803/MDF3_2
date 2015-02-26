package com.fullsail.android.cameraexample;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private static final int REQUEST_TAKE_PICTURE = 0x01001;

	ImageView mImageView;
	Uri mImageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mImageView = (ImageView)findViewById(R.id.image);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if(requestCode == REQUEST_TAKE_PICTURE && resultCode != RESULT_CANCELED) {
			if(mImageUri != null) {
				mImageView.setImageBitmap(BitmapFactory.decodeFile(mImageUri.getPath()));
				addImageToGallery(mImageUri);
			} else {
				mImageView.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_take_picture) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mImageUri = getImageUri();
			if(mImageUri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			}
			startActivityForResult(intent, REQUEST_TAKE_PICTURE);
		}
		return true;
	}
	
	private Uri getImageUri() {
		
		String imageName = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date(System.currentTimeMillis()));
		
		File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File appDir = new File(imageDir, "CameraExample");
		appDir.mkdirs();
		
		File image = new File(appDir, imageName + ".jpg");
		try {
			image.createNewFile();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return Uri.fromFile(image);
	}
	
	private void addImageToGallery(Uri imageUri) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    scanIntent.setData(imageUri);
	    sendBroadcast(scanIntent);
	}
}
