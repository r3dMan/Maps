package com.example.test_utility_library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private ViewGroup mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (ViewGroup) findViewById(R.id.menu);
		addDemo("Scale & Cluster",ScaleCluster.class);
		addDemo("MapTypeRouteAnimation", MapTypeRouteAnimation.class);
	}

	private void addDemo(String demoName,
			Class<? extends Activity> activityClass) {
		Button b = new Button(this);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        b.setLayoutParams(layoutParams);
        b.setText(demoName);
        b.setTag(activityClass);
        b.setOnClickListener(this);
        mListView.addView(b);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Class activityClass = (Class) v.getTag();
		startActivity(new Intent(this, activityClass));
	}

}
