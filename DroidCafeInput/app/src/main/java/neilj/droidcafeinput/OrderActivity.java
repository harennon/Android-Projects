package neilj.droidcafeinput;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = OrderActivity.class.getSimpleName();
    private EditText mPhoneEditText;
    private ArrayList<String> mToppings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //initialize variables
        mToppings = new ArrayList<>();

        //get intent data
        Intent orderIntent = getIntent();
        String message = orderIntent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.order_textview);
        textView.setText(message);

        //setting up listener for phone
        mPhoneEditText = findViewById(R.id.phone_text);
        if(mPhoneEditText != null) {
            mPhoneEditText.setOnEditorActionListener
                    (new TextView.OnEditorActionListener(){
                        @Override
                        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                            boolean handled = false;
                            if (actionId == EditorInfo.IME_ACTION_SEND) {
                                dialNumber();
                                handled = true;
                            }
                            return handled;
                        }
                    });
        }

        //create spinner
        Spinner spinner = findViewById(R.id.label_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }
        //create ArrayAdaptor using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerLabel = parent.getItemAtPosition(position).toString();
        //displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onRadioButtonClicked(View view) {
        //Is the button now checked?
        boolean checked = ((RadioButton)view).isChecked();
        //Check which button clicked
        switch (view.getId()){
            case R.id.sameday:
                if(checked)
                    displayToast(getString(R.string.same_day_messenger_service));
                break;
            case R.id.nextday:
                if(checked)
                    displayToast(getString(R.string.next_day_ground_delivery));
                break;
            case R.id.pickup:
                if(checked)
                    displayToast(getString(R.string.pick_up));
                break;
            default:
                //do nothing
                break;
        }
    }

    private void dialNumber(){
        //find the phoneText
        String phoneNum = null;
        //if EditText field is not null, concatenate "tel :" to number
        if (mPhoneEditText != null) {
            phoneNum = "tel:" + mPhoneEditText.getText().toString();
        }
        //log concatenated number
        Log.d(LOG_TAG, "dialNumber: " + phoneNum);

        //Specify Intent
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        //Set data for the intent as the phone number
        callIntent.setData(Uri.parse(phoneNum));
        //If intent resolves to a package, run it
        if(callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this!");
        }
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToppingsToast(View view) {
        String concatenate = "Toppings:";
        if(mToppings.size() == 0){
            concatenate += " None!?";
        }
        for(String s : mToppings){
            concatenate += " " + s;
        }
        displayToast(concatenate);
    }

    public void onToppingSelected(View view) {
        //find current topping selected
        String toppingToAdd;
        switch (view.getId()){
            case R.id.topping_chocolate_syrup:
                toppingToAdd = getString(R.string.chocolate_syrup);
                break;
            case R.id.topping_crushed_nuts:
                toppingToAdd = getString(R.string.crushed_nuts);
                break;
            case R.id.topping_sprinkles:
                toppingToAdd = getString(R.string.sprinkles);
                break;
            default:
                toppingToAdd = "";
                break;
        }
        //if checked
        if(((CheckBox)view).isChecked()){
            //if topping identified
            if(toppingToAdd.length() != 0){
                mToppings.add(toppingToAdd);
            }
        } else {
            if(mToppings != null){
                mToppings.remove(toppingToAdd);
            }
        }
    }
}
