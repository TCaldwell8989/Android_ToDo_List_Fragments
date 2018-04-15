package com.clara.simple_todo_list_with_fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
		AddToDoItemFragment.NewItemCreatedListener,
		ToDoItemDetailFragment.MarkItemAsDoneListener,
		ToDoListFragment.ListItemSelectedListener {


	private static final String TODO_ITEMS_KEY = "TODO ITEMS ARRAY LIST";
	private static final String ADD_NEW_FRAG_TAG = "ADD NEW FRAGMENT";
	private static final String LIST_FRAG_TAG = "LIST FRAGMENT";
	private static final String DETAIL_FRAG_TAG = "DETAIL FRAGMENT";

	private ArrayList<ToDoItem> mTodoItems;

	private static final String TAG = "MAIN ACTIVITY";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			// no savedInstanceState - app starting
			// Create new ArrayList, and create Add and List fragments
			// Add to this activity

			Log.d(TAG, "onCreate has no instance state");

			mTodoItems = new ArrayList<>();

			AddToDoItemFragment addNewFragment = AddToDoItemFragment.newInstance();
			ToDoListFragment listFragment = ToDoListFragment.newInstance(mTodoItems);
			ToDoItemDetailFragment detailFragment = ToDoItemDetailFragment.newInstance(new ToDoItem("", false));

			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();

			ft.add(R.id.add_todo_view_container, addNewFragment, ADD_NEW_FRAG_TAG);
			ft.add(R.id.todo_list_view_container, listFragment, LIST_FRAG_TAG);
			ft.add(R.id.todo_detail_view_container, detailFragment);

			ft.commit();

		} else {
			// There is savedInstanceState, so app is resuming
			// Activity should already have fragments
			// Restore savedInstanceState, the Arraylist

			mTodoItems = savedInstanceState.getParcelableArrayList(TODO_ITEMS_KEY);
			Log.d(TAG, "onCreate has savedInstanceState ArrayList = " + mTodoItems);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outBundle) {
		super.onSaveInstanceState(outBundle);
		outBundle.putParcelableArrayList(TODO_ITEMS_KEY, mTodoItems);
	}

	@Override
	public void newItemCreated(ToDoItem newItem) {
		//Add new item to ArrayList
		mTodoItems.add(newItem);

		Log.d(TAG, "newItemCreated = " + mTodoItems);

		// Get a reference to list fragment from the FragmentManager
		// notify ToDoListFragment that it needs to update
		FragmentManager fm = getSupportFragmentManager();
		ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
		listFragment.notifyItemsChanged();;

	}

	@Override
	public void itemSelected(ToDoItem selected) {

		// Replace the previous detail fragment with a new detail fragment
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.todo_detail_view_container, ToDoItemDetailFragment.newInstance(selected));
		ft.commit();
	}

	@Override
	public void todoItemDone(ToDoItem doneItem) {

		mTodoItems.remove(doneItem);
		// Find list fragment and tell it that the data has changed
		FragmentManager fm = getSupportFragmentManager();
		ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
		listFragment.notifyItemsChanged();

	}
}


