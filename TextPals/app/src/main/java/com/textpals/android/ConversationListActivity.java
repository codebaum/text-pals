package com.textpals.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


/**
 * An activity representing a list of Conversations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ConversationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ConversationListFragment} and the item details
 * (if present) is a {@link ConversationDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ConversationListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ConversationListActivity extends Activity
        implements ConversationListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        if (findViewById(R.id.conversation_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ConversationListFragment) getFragmentManager()
                    .findFragmentById(R.id.conversation_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.

        Translate.setClientId("text-pals");
        Translate.setClientSecret("DVx9fWsXLJdcbvOpL+DwBN0sL8C6g+KSU579OoJ4IvI=");

        try {
            class bgStuff extends AsyncTask<Void, Void, String> {

                String translatedText = "";
                @Override
                protected String doInBackground(Void... params) {
                    // TODO Auto-generated method stub
                    try {
                        String translatedText = Translate.execute("Hello, my name is Brandon", Language.ENGLISH, Language.SPANISH);
                        return translatedText;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        translatedText = e.toString();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    if (result == null) {
                        result = "ERROR";
                    }
                    Toast.makeText(ConversationListActivity.this, result, Toast.LENGTH_SHORT).show();
                }

            }

            new bgStuff().execute();
        }
        catch (Exception e) {
            Log.e("TRANSLATE ERROR", e.toString());
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback method from {@link ConversationListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ConversationDetailFragment.ARG_ITEM_ID, id);
            ConversationDetailFragment fragment = new ConversationDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.conversation_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ConversationDetailActivity.class);
            detailIntent.putExtra(ConversationDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
