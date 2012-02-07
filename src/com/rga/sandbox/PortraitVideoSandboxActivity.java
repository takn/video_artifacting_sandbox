package com.rga.sandbox;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.VideoView;

public class PortraitVideoSandboxActivity extends FragmentActivity {
	private static final int ONE_SECOND = 1000;
	private VideoView videoView;
	private View contentView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		videoView = (VideoView) findViewById(R.id.videoView1);
		contentView = findViewById(R.id.fragment_container);

		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		Fragment fragment = new OverlayFragment();

		transaction.replace(R.id.fragment_container, fragment);
		// TODO ask Adam about how to deal with the artifacting this hack fixes
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				transaction.commit();
				showVideo(R.raw.vid_test);
			}
		};

		handler.postDelayed(runnable, 700);
	}

	public void showVideo(int videoId) {
		if (videoView != null) {
			Uri videoUri = parseVideoURI(videoId);
			videoView.setVideoURI(videoUri);

			videoView.setOnCompletionListener(completionListener);
			videoView.setOnPreparedListener(prepareListener);
			videoView.requestFocus();
			videoView.start();

		}
		
	}

	private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO enable touch at the end of the video so it can finish.

		}
	};

	private MediaPlayer.OnPreparedListener prepareListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			int delay = mp.getDuration() - ONE_SECOND;
			showShareButton(delay);
			
				showContent(delay);

		}
	};

	protected Uri parseVideoURI(int whichVideo) {
		return Uri.parse("android.resource://" + getPackageName() + "/"
				+ whichVideo);

	}

	public void showContent(int delay) {
		AlphaAnimation alphaAnim = (AlphaAnimation) AnimationUtils
				.loadAnimation(PortraitVideoSandboxActivity.this, R.anim.fade_in);
		alphaAnim.setStartOffset(delay);

		if (contentView != null) {
			contentView.setVisibility(View.VISIBLE);
			contentView.startAnimation(alphaAnim);
		}
	}

	public void showShareButton(int delay) {
		// animate the content that should be shown prior to the end of the
		// video
		AlphaAnimation alphaAnim = (AlphaAnimation) AnimationUtils
				.loadAnimation(this, R.anim.fade_in);
		alphaAnim.setStartOffset(delay);

		View btn = findViewById(R.id.share_button);

		if (btn != null) {
			btn.bringToFront();
			btn.setVisibility(View.VISIBLE);
			btn.startAnimation(alphaAnim);
		}

	}
	
	public void clickHandler(View v){
		
	}
}