package com.knziha.plod.settings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.GlobalOptions;
import androidx.preference.PreferenceFragmentCompat;

import com.knziha.filepicker.settings.FileChooser;
import com.knziha.filepicker.settings.FilePickerOptions;
import com.knziha.plod.PlainDict.AgentApplication;
import com.knziha.plod.PlainDict.CMN;
import com.knziha.plod.PlainDict.CrashHandler;
import com.knziha.plod.PlainDict.PDICMainAppOptions;
import com.knziha.plod.PlainDict.R;
import com.knziha.plod.PlainDict.Toastable_Activity;

import java.io.File;

public class SettingsActivity extends Toastable_Activity {
	private int realm_id;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				checkBack();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void checkBack() {
		if(realm_id==3){
			PDICMainAppOptions.SecondFlag(FilePickerOptions.SecondFlag);
		}
		else if(realm_id==7){
			if(opt.CetUseRegex3(SFStamp)
					|opt.CetPageCaseSensitive(SFStamp)
					|opt.CetPageWildcardMatchNoSpace(SFStamp)
					|opt.CetPageWildcardSplitKeywords(SFStamp)
					|opt.CetInPageSearchUseWildcard(TFStamp)
			)
			CMN.setCheckRcsp();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		if(opt.isFullScreen()){
			win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		win.getDecorView().setBackgroundColor(GlobalOptions.isDark? Color.BLACK:Color.WHITE);
		root=win.getDecorView().findViewById(android.R.id.content);
		checkMargin(this);
		if(Build.VERSION.SDK_INT>=21) {
			win.setStatusBarColor(CMN.MainBackground);
			win.setNavigationBarColor(CMN.MainBackground);
		}

		File log=new File(CrashHandler.getInstance(this, opt).getLogFile());
		File lock=new File(log.getParentFile(),"lock");
		if(lock.exists()) lock.delete();

		PreferenceFragmentCompat fragment;
		Bundle args = new Bundle();
		switch (realm_id = getIntent().getIntExtra("realm", 0)){
			default:
			case 0:
				fragment = new MainProgram();
			break;
			case 3:
				fragment = new FileChooser();
			break;
			case 4:
				fragment = new DevoloperOptions();
			break;
			case 6:
				fragment = new Licences();
			break;
			case 7:
				fragment = new SearchSpecification();
			break;
			case 8:
				fragment = new ViewSpecification();
				args.putInt("title", R.string.view_spec);
			break;
			case 9:
				fragment = new ClickSearch();
			break;
			case 10:
				fragment = new HistoryPreference();
			break;
		}
		fragment.setArguments(args);
		this.getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content, fragment)
				.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		checkFlags();
	}
}