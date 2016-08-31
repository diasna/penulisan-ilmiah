package id.ac.gunadarma.tugasku;

import id.ac.gunadarma.tugasku.ui.PrefsFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class PreferenceActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefsFragment()).commit();
	}

	@Override
	public void finish() {
		setResult(RESULT_OK, null);
		super.finish();
	}
}
