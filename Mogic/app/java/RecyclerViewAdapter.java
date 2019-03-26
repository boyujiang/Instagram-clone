package com.cas.jiamin.mogic.Utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cas.jiamin.mogic.R;

import java.util.ArrayList;

/**
 * RecyclerViewAdapter class
 * RecyclerViewAdapter is the holder that kept the url of the resources which can be shown
 * in the image view
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
   // private ArrayList<String> Image = new ArrayList<>();
    private Context mcontex;
    private CardView cv;
    //private ArrayList<String> Username;
    //private ArrayList<String> Content;
    private ArrayList<uploads> Ups;

    /**
     * RecyclerViewAdapter
     * constructor that create the holder
     * @param mcontex the context that the holder wirk on
     * @param ups the object array that take the content in the recycler view
     */
    public RecyclerViewAdapter(Context mcontex, ArrayList<uploads> ups) {
        //Image = image;
        this.mcontex = mcontex;
        Ups = ups;
        //Username = username;
        //Content = content;
    }

    /**
     * onCreateViewHolder
     * create the independent card view that worked in the recycler view
     * @param viewGroup the parent recycler view
     * @param viewType the order number of the child view
     * @return the child view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cycler_imageview, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * onBindViewHolder
     * extract the data from the holder to the given position
     * @param holder the cardview that contain the structure
     * @param position the position of the cardview
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mcontex)  //extract image by the export library
                .asBitmap()
                .load(Ups.get(position).getUrl())
                .into(holder.mIamge);
        holder.musername.setText(Ups.get(position).getUsername());
        holder.mcontent.setText(Ups.get(position).getContents());
    }

    /**
     * getItemCount
     * get the count of the item in the array list
     * @return the size of the item
     */
    @Override
    public int getItemCount() {
        return Ups.size();
    }

    /**
     * ViewHolder
     * the extension of the View holder in RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mIamge;
        RelativeLayout parentLayout;
        TextView musername;
        TextView mcontent;

        /**
         * ViewHolder
         * The extended constructor of the original ViewHolder
         * @param itemView the item of matched with the view on the front end
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardview);
            mIamge = itemView.findViewById(R.id.RecyclerImage);
            musername =itemView.findViewById(R.id.username);
            mcontent =itemView.findViewById(R.id.context);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

}
