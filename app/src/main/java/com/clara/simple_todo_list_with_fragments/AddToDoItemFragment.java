package com.clara.simple_todo_list_with_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A Fragment for adding a new To Do item to the list
 */

public class AddToDoItemFragment extends Fragment {

	private static final String TAG = "Add To Do Item Fragment";

	private NewItemCreatedListener mNewItemListener;

	// Nothing happens here except creating a new Fragment
	// This is nice in case we did need to send data to this fragment
	public static AddToDoItemFragment newInstance() {
		return new AddToDoItemFragment();
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		Log.d(TAG, "onAttach");

		if (context instanceof NewItemCreatedListener) {
			mNewItemListener = (NewItemCreatedListener) context;
			Log.d(TAG, "set listener");

		} else {
			throw new RuntimeException(context.toString() + " must implement NewItemCreatedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_to_do_item, container, false);

		Log.d(TAG, "onCreateView");

		Button addItem = (Button) view.findViewById(R.id.add_todo_item_button);
		final EditText newItemText = (EditText) view.findViewById(R.id.new_todo_item_edittext);
		final CheckBox urgentCheckBox = (CheckBox) view.findViewById(R.id.urgent_checkbox);

		addItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// Validate user entered text
				if (newItemText.getText().length() > 0) {
					String text = newItemText.getText().toString();
					boolean urgent = urgentCheckBox.isChecked();

					// Clear input form
					newItemText.getText().clear();
					urgentCheckBox.setChecked(false);

					// Create a new to do item
					ToDoItem newItem = new ToDoItem(text, urgent);

					Log.d(TAG, "New item is " + newItem);

					// Return newItem back to calling Activity
					mNewItemListener.newItemCreated(newItem);

				} else {
					Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_LONG).show();
				}
			}
		});

		return view;

	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach");

		mNewItemListener = null;
	}

	// Use to notify hosting Activity that a new To Do item has been created
	public interface NewItemCreatedListener {
		void newItemCreated(ToDoItem newItem);
	}

}

