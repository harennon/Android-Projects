package neilj.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ItemsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ItemsActivity.class.getSimpleName();
    public static final String EXTRA_LIST_ITEM = "neilj.shoppingList.extra.ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
    }

    public void updateList(View view) {
        String item = ((Button) view).getText().toString() + "\n";
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_LIST_ITEM, item);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
