package com.aena.mantuca;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * @author fjrodriguez
 *
 */
public class MaquinaEdit extends Activity {

	private MaquinasDbAdapter mDbHelper;
	private EditText mIdAenaText;
	private EditText mSerialText;
	private EditText mModeloText;
	private EditText mTipoText;
	private EditText mUbicacionText;
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new MaquinasDbAdapter(this);
		mDbHelper.open();
		setContentView(R.layout.maquina_edit);
		setTitle(R.string.edit_maquina);
		mIdAenaText = (EditText) findViewById(R.id.idAena);
		mSerialText = (EditText) findViewById(R.id.serial);
		mModeloText = (EditText) findViewById(R.id.modelo);
		mTipoText = (EditText) findViewById(R.id.tipo);
		mUbicacionText = (EditText) findViewById(R.id.ubicacion);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		
		mRowId = (savedInstanceState == null) ? null :
			(Long) savedInstanceState.getSerializable(MaquinasDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(MaquinasDbAdapter.KEY_ROWID) : null;
		}
		
		populateFields();
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor maquina = mDbHelper.fetchMaquina(mRowId);
			startManagingCursor(maquina);
			mIdAenaText.setText(maquina.getString(
                    maquina.getColumnIndexOrThrow(MaquinasDbAdapter.KEY_IDAENA)));
			mSerialText.setText(maquina.getString(
                    maquina.getColumnIndexOrThrow(MaquinasDbAdapter.KEY_SERIAL)));
			mTipoText.setText(maquina.getString(
                    maquina.getColumnIndexOrThrow(MaquinasDbAdapter.KEY_TIPO)));
			mModeloText.setText(maquina.getString(
                    maquina.getColumnIndexOrThrow(MaquinasDbAdapter.KEY_MODELO)));
			mUbicacionText.setText(maquina.getString(
                    maquina.getColumnIndexOrThrow(MaquinasDbAdapter.KEY_UBICACION)));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(MaquinasDbAdapter.KEY_ROWID, mRowId);
	}

	private void saveState() {
		String idAena = mIdAenaText.getText().toString();
        String serial = mSerialText.getText().toString();
        String tipo = mTipoText.getText().toString();
        String modelo = mModeloText.getText().toString();
        String ubicacion = mUbicacionText.getText().toString();
        
        if (mRowId == null) {
            long id = mDbHelper.createMaquina(idAena, serial, tipo, modelo, ubicacion);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateMaquina(mRowId, idAena, serial, tipo, modelo, ubicacion);
        }

		
	}

}
