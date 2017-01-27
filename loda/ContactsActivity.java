package com.locatedamini.loda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactsActivity extends ActionBarActivity {
List<Contact> Contacts= new ArrayList<Contact>();
    public static List<String> Phones = new ArrayList<String>();
    ListView contactListView;
	//public final static String NEW_NUMBER = "com.locatedamini.loda.NUMBER";
	//public final static String NEW_CONTACT = "com.locatedamini.loda.CONTACT";
    private static final int EDIT = 0, DELETE = 1;
Phone phone;
    EditText phonetext, nametext;
    DatabaseHandler dbHandler;
    ArrayAdapter<Contact> contactAdapter;
    int longClickedItemIndex;

    public void openHome(View view) {
        // Do something in response to button

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);


    }
    public void openAlertPro(View view) {
        // Do something in response to button

        Intent i = new Intent(this, AlertProActivity.class);
        startActivity(i);


    }
    public void openLocationActivity(View view) {
        //Do something in response to the button
        Intent i = new Intent(this, LocationActivity.class);
        startActivity(i);
    }
    public void openContacts(View view) {
        // Do something in response to button
        Intent i = new Intent(this, ContactsActivity.class);
        startActivity(i);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        nametext = (EditText) findViewById(R.id.Name);
        phonetext = (EditText) findViewById(R.id.Phone);
        contactListView = (ListView) findViewById(R.id.listView);
        dbHandler = new DatabaseHandler(getApplicationContext());
        registerForContextMenu(contactListView);
contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        longClickedItemIndex = position;
        return false;

    }
});

        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Add Contact");
        tabSpec.setContent(R.id.addContact);
        tabSpec.setIndicator("Add Contact");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Contact List");
        tabSpec.setContent(R.id.contactList);
        tabSpec.setIndicator("Contact List");
        tabHost.addTab(tabSpec);


        final Button addBtn = (Button) findViewById(R.id.Add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adding the user-inputed phone number into the Phones string array (converting the numbers to strings) and passing them to the dbHandler/DatabaseHandler class in order to save the new contact
                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(nametext.getText()), String.valueOf(phonetext.getText()));
                Phone phones = new Phone(dbHandler.getPhoneCount(), String.valueOf(phonetext.getText()));
                if (!contactExists(contact)){
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    Phones.add(phonetext.getText().toString());
                    Toast.makeText(getApplicationContext(),
                            Arrays.toString(Phones.toArray()).replaceAll("\\[|\\]", ""),
                            Toast.LENGTH_LONG).show();//Shows the number that was just added.
                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(nametext.getText()) + " has been added to your Contacts!", Toast.LENGTH_SHORT).show();
                    tabHost.setCurrentTab(1);
                    return;
            }
                Toast.makeText(getApplicationContext(), String.valueOf(nametext.getText()) + " already exists. Please use a different name.", Toast.LENGTH_SHORT).show();
            }
        });

        /**if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }**/

        nametext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(String.valueOf(nametext.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
if (dbHandler.getContactsCount() != 0)
       Contacts.addAll(dbHandler.getAllContacts());

//populating the ContactsList with the new contacts
            populateList();
    }


public String getPhones(){
     String numbers = Arrays.toString(Phones.toArray()).replaceAll("\\[|\\]", "");
    return numbers;
}

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        //when the user long clicks one of the contacts in the list, they will see this menu
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderIcon(R.drawable.pencil);
        menu.setHeaderTitle("Contact Options");
      //  menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Contact Phone" );
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Contact" );
    }

    public boolean onContextItemSelected(MenuItem item){
    switch (item.getItemId()){
        //*IMPORTANT TO NOTE* The editing a contact feature has not yet been implemented into the app
        case EDIT://TODO: Implement editing a contact
            AlertDialog.Builder editContactBox = new AlertDialog.Builder(ContactsActivity.this);
            editContactBox.setTitle("Edit");
            editContactBox.setMessage("Edit your contact's phone number:");
            editContactBox.setIcon(R.drawable.pencil);
            final EditText phone = new EditText(ContactsActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            phone.setLayoutParams(lp);
            editContactBox.setView(phone);

            editContactBox.setPositiveButton("Save",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String.valueOf(phone.getText());
                            dbHandler.updateContact(Contacts.get(longClickedItemIndex));

                            //Contacts.remove(longClickedItemIndex);
                            contactAdapter.notifyDataSetChanged();

                        }
                    }
            );
            // Setting Negative "NO" Button
            editContactBox.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                        }
                    }
            );
            editContactBox.show();
            break;
        case DELETE://if the user has selected this option, the selected contact will be deleted
            AlertDialog.Builder confirmDelete = new AlertDialog.Builder(ContactsActivity.this);
            confirmDelete.setTitle("Confirm");
            confirmDelete.setMessage("Are you sure you want to delete this contact?");
            confirmDelete.setIcon(R.drawable.delete);
            confirmDelete.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbHandler.deleteContact(Contacts.get(longClickedItemIndex));
                            Contacts.remove(longClickedItemIndex);
                            contactAdapter.notifyDataSetChanged();

                        }
                    }
            );
            // Setting Negative "NO" Button
            confirmDelete.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                        }
                    }
            );

            // closed

            // Showing Alert Message
            confirmDelete.show();
            /**dbHandler.deleteContact(Contacts.get(longClickedItemIndex));
            Contacts.remove(longClickedItemIndex);
            contactAdapter.notifyDataSetChanged();
            break;**/

            break;

    }
        return super.onContextItemSelected(item);
    }

    private boolean contactExists(Contact contact){
        String name = contact.getName();
        int contactCount = Contacts.size();

        for (int i = 0; i <contactCount; i++){
            if (name.compareToIgnoreCase(Contacts.get(i).getName()) == 0)
                return true;
        }
        return false;
    }

    private void populateList(){//populating the contacts list
        contactAdapter = new ContactListAdapter();
        contactListView.setAdapter(contactAdapter);

    }

	public void addNewContact(View view) {//ignore this
	    // Do something in response to button
		 Intent intent = new Intent(this, ContactsListActivity.class);
	        EditText text = (EditText) findViewById(R.id.Phone);
	        String Phone = PhoneNumberUtils.formatNumber(text.getText().toString());
	        //intent.putExtra(NEW_NUMBER, Phone);
	        EditText editText = (EditText) findViewById(R.id.Name);
	        String Name = editText.getText().toString();
	        //intent.putExtra(NEW_CONTACT, Name);



	        //startActivity(intent);
        

	}
	public void openContactList(View view) {
	    // Do something in response to button
		Intent i = new Intent(ContactsActivity.this, ContactsListActivity.class);
        startActivity(i);

	}

   private void addContact(Integer id, String name, String phone){
        Contacts.add(new Contact(id, name, phone));
    }





private class ContactListAdapter extends ArrayAdapter<Contact>{
    //contact values of each item (contact) displayed in the ContactList
    public ContactListAdapter(){
        super (ContactsActivity.this, R.layout.listview_item, Contacts);
    }
@Override
    public View getView(int position, View view, ViewGroup parent) {
    if(view == null)
        view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
    Contact currentContact = Contacts.get(position);
            TextView name = (TextView) view.findViewById(R.id.contactName);
    name.setText(currentContact.getName());
    TextView phone = (TextView) view.findViewById(R.id.phone);
    phone.setText(currentContact.getPhone());


    return view;
}}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_contacts,
					container, false);
			return rootView;
		}
	}

}
