package com.swetupuru.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.swetupuru.anim.AnimationUtils;
import com.swetupuru.extras.Constants;
import com.swetupuru.materialtest.R;
import com.swetupuru.network.VolleySingleton;
import com.swetupuru.pojo.Movie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SWETABH on 12/20/2015.
 */
public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolderBoxOffice> {

    private LayoutInflater inflater;
    private ArrayList<Movie> listMovie= new ArrayList<>();
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private DateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd");
    int previousPosition=0;

    public AdapterBoxOffice(Context context){
        inflater=LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getsInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
    }

    public void setMovieList(ArrayList<Movie> listMovie){
        this.listMovie=listMovie;
        notifyItemRangeChanged(0,listMovie.size());
    }
    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.custom_movie_box_office, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {
        Movie currentMovie=listMovie.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        Date movieReleaseDate=currentMovie.getReleaseDateTheater();
        if(movieReleaseDate!=null){
            String formattedDate= dateFormatter.format(movieReleaseDate);
            holder.movieReleaseDate.setText(formattedDate);
        }
        else {
            holder.movieReleaseDate.setText(Constants.NA);
        }

        int audienceScore=currentMovie.getAudienceScore();
        if(audienceScore==-1){
            holder.movieAudienceScore.setRating(0.0F);
            holder.movieAudienceScore.setAlpha(0.5F);
        }
        else {
            holder.movieAudienceScore.setRating(currentMovie.getAudienceScore() / 20.0F);
            holder.movieAudienceScore.setAlpha(1.0F);
        }
        if(position>previousPosition){
            AnimationUtils.animate(holder,true);
        }
        else {
            AnimationUtils.animate(holder,false);
        }
        previousPosition=position;


        String urlThumbnail=currentMovie.getUrlThumbnail();
        loadImages(urlThumbnail, holder);


    }

    private void loadImages(String urlThumbnail,final ViewHolderBoxOffice holder) {
        if(!urlThumbnail.equals( Constants.NA))
        {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.movieThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    static class ViewHolderBoxOffice extends RecyclerView.ViewHolder{

        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieReleaseDate;
        RatingBar movieAudienceScore;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
        }
    }
}
