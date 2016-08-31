package id.ac.gunadarma.tugasku.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPreference extends DialogPreference {

	private int lastValue = 0;
	private NumberPicker picker = null;

	public NumberPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setPositiveButtonText("Set");
		setNegativeButtonText("Cancel");
	}
	
	@Override
	protected View onCreateDialogView() {
		picker = new NumberPicker(getContext());
		picker.setMinValue(1);
		picker.setMaxValue(60);
		return (picker);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		picker.setValue(lastValue);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			lastValue = picker.getValue();
			if (callChangeListener(lastValue)) {
				persistInt(lastValue);
			}
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		int val = 0;
		if (restoreValue) {
			if (defaultValue == null) {
				val = getPersistedInt(0);
			} else {
				val = getPersistedInt(Integer.parseInt((String) defaultValue));
			}
		} else {
			val = Integer.parseInt((String)defaultValue);
		}
		lastValue = val;
	}
}
