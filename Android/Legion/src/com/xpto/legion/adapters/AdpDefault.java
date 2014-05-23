package com.xpto.legion.adapters;

import java.util.ArrayList;

import com.xpto.legion.models.Default;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdpDefault<T extends Default> extends BaseAdapter {
	private ArrayList<T> items;

	protected ArrayList<T> getItems() {
		return items;
	}

	private LayoutInflater inflater;

	protected LayoutInflater getInflater() {
		return inflater;
	}

	protected ArrayList<T> createItems() {
		return new ArrayList<T>();
	}

	public AdpDefault(Context context) {
		clear();
		inflater = LayoutInflater.from(context);
	}

	public void clear() {
		items = createItems();
	}

	public void addItem(T[] _items) {
		for (int i = 0; i < _items.length; i++)
			addItem(_items[i]);
	}

	public void addItem(ArrayList<T> _items) {
		for (int i = 0; i < _items.size(); i++)
			addItem(_items.get(i));
	}

	public boolean addItem(T _item) {
		for (int i = 0; i < items.size(); i++)
			if (items.get(i).getId() == _item.getId())
				return false;

		items.add(_item);
		return true;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public T getItem(int _position) {
		if (_position >= 0 && _position < items.size())
			return items.get(_position);
		else
			return null;
	}

	@Override
	public long getItemId(int _position) {
		T t = getItem(_position);
		if (t != null)
			return t.getId();
		else
			return 0;
	}

	@Override
	public View getView(int _position, View _convertView, ViewGroup _parent) {
		return getView(_position, _convertView);
	}

	public abstract View getView(int _position, View _convertView);
}
