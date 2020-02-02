package com.ger.garage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ger.garage.R;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ListViewAdapter extends ArrayAdapter<String> {

    private List<String> bookings;
    private List<Boolean> checkBoxes;
    private Context context;
    private CheckBoxCheckedListener checkedListener;

    public ListViewAdapter(List<String> bookings, List<Boolean> checkBoxes,Context context) {
        super(context, R.layout.item_list_view, bookings);

        this.context = context;
        this.bookings = bookings;
        this.checkBoxes = checkBoxes;
    }

    class MyViewHolder {

        TextView booking;
        CheckBox checkBox;

        MyViewHolder(View view) {

            booking = view.findViewById(R.id.booking);
            checkBox = view.findViewById(R.id.checkbox);

        }

    }

    @Override
    public View getView(final int position, @Nullable View convertView, @Nonnull ViewGroup parent) {

        View item = convertView;
        MyViewHolder holder = null;

        if (item == null) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.item_list_view, parent, false);
            holder =  new MyViewHolder(item);
            item.setTag(holder);

        } else {
            holder = (MyViewHolder)item.getTag();
        }

        holder.booking.setText(bookings.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkedListener != null) {

                    checkedListener.getCheckBoxCheckedListener(position, isChecked);

                }


            }

        });
        holder.checkBox.setChecked(checkBoxes.get(position));
        return item;

    }

    public interface CheckBoxCheckedListener {

        void getCheckBoxCheckedListener(int position, Boolean isChecked);

    }

    public void setCheckedListener(CheckBoxCheckedListener checkedListener) {

        this.checkedListener = checkedListener;

    }



}
