package com.aena.mantuca;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Mantuca extends ListActivity {
	private MaquinasDbAdapter mDbHelper;
	public static final int INSERT_ID = Menu.FIRST;
	public static final int DELETE_ID = Menu.FIRST + 1;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maquinas_lista);
        mDbHelper = new MaquinasDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
		// Get all of the notes from the database and create the item list
    	Cursor maquinasCursor = mDbHelper.fetchAllMaquinas();
    	startManagingCursor(maquinasCursor);
    	
    	String[] from = new String[] { MaquinasDbAdapter.KEY_UBICACION, 
    			MaquinasDbAdapter.KEY_TIPO, MaquinasDbAdapter.KEY_IDAENA };
    	int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3 };
    	
    	// Now create an array adapter and set it to display using our row
    	SimpleCursorAdapter maquinas = new SimpleCursorAdapter(this, R.layout.maquinas_fila, maquinasCursor, from, to);
    	setListAdapter(maquinas);
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
        case INSERT_ID:
        	createMaquina();
        	return true;
        }
    	return super.onOptionsItemSelected(item);
    }

	private void createMaquina() {
		Intent i = new Intent(this,MaquinaEdit.class);
    	startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, MaquinaEdit.class);
		i.putExtra(MaquinasDbAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteMaquina(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}
}