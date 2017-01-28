package com.swetupuru.fragment;


import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.swetupuru.Adapter.AdapterBoxOffice;
import com.swetupuru.extras.Constants;
import com.swetupuru.extras.MovieSorter;
import com.swetupuru.extras.SortListener;
import com.swetupuru.extras.UrlEndpoints;
import com.swetupuru.logging.L;
import com.swetupuru.materialtest.MyApplication;
import com.swetupuru.materialtest.R;
import static com.swetupuru.extras.UrlEndpoints.*;
import static com.swetupuru.extras.Keys.EndpointBoxOffice.*;
import com.swetupuru.network.VolleySingleton;
import com.swetupuru.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.android.volley.Request.*;
import static com.android.volley.Response.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends Fragment implements SortListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIES = "state_movies";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private ArrayList<Movie> mListMovies = new ArrayList<>();
    private DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView listMovieHits;
    private AdapterBoxOffice adapterBoxOffice;
    private TextView textVolleyError;
    //the sorter responsible for sorting our movie results based on choice made by the user in the FAB
    private MovieSorter mSorter = new MovieSorter();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBoxOffice.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBoxOffice newInstance(String param1, String param2) {
        FragmentBoxOffice fragment = new FragmentBoxOffice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static String getRequestUrl(int limit){
        return URL_BOX_OFFICE+
                URL_CHAR_QUESTION+
                URL_PARAM_API_KEY+
                MyApplication.API_KEY_ROTTEN_TOMATOES+
                URL_CHAR_AMEPERSAND+
                URL_PARAM_LIMIT+
                limit;
    }

    public FragmentBoxOffice() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES,mListMovies);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=volleySingleton.getRequestQueue();

    }

    private void sendJsonRequest() {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(30),
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textVolleyError.setVisibility(View.GONE);
                        mListMovies=parseJSONResponse(response);
                        adapterBoxOffice.setMovieList(mListMovies);
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // L.t(getActivity(), error.getMessage()+"");
                       handleVolleyError(error);
                    }
                }
        );
        requestQueue.add(request);
    }

    private void handleVolleyError(VolleyError error) {

        textVolleyError.setVisibility(View.VISIBLE);
        if(error instanceof TimeoutError || error instanceof NoConnectionError){
            textVolleyError.setText(R.string.error_timeout);

        } else if(error instanceof AuthFailureError){
            textVolleyError.setText(R.string.error_auth_failure);

        }else if(error instanceof ServerError){
            textVolleyError.setText(R.string.error_auth_failure);

        }else if(error instanceof NetworkError){
            textVolleyError.setText(R.string.error_network);

        }else if(error instanceof ParseError){
            textVolleyError.setText(R.string.error_parser);

        }
    }

    private ArrayList<Movie> parseJSONResponse(JSONObject response) {
        ArrayList<Movie> listMovie= new ArrayList<>();
        if (response != null || response.length() > 0)
        {
            try {
                if (response.has(KEY_MOVIES)) {
                    JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);
                    for (int i = 0; i < arrayMovies.length(); i++) {

                        long id=-1;
                        String title= Constants.NA;
                        String releaseDate=Constants.NA;
                        int audienceScore = -1;
                        String synopsis=Constants.NA;
                        String urlThumbnail=Constants.NA;

                        JSONObject currentMovie = arrayMovies.getJSONObject(i);

                        if(currentMovie.has(KEY_ID)
                                && !currentMovie.isNull(KEY_ID)) {
                            id = currentMovie.getLong(KEY_ID);
                        }
                        if(currentMovie.has(KEY_TITLE)
                                && !currentMovie.isNull(KEY_TITLE)) {
                            title = currentMovie.getString(KEY_TITLE);
                        }

                        if(currentMovie.has(KEY_RELEASE_DATES)
                                && !currentMovie.isNull(KEY_RELEASE_DATES)) {
                            JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);

                            if (objectReleaseDates.has(KEY_THEATER)) {
                                releaseDate = objectReleaseDates.getString(KEY_THEATER);
                            }
                        }
                        // get Audience score for current movie

                        JSONObject objectRating = currentMovie.getJSONObject(KEY_RATINGS);

                        if (objectRating.has(KEY_AUDIENCE_SCORE)) {
                            audienceScore = objectRating.getInt(KEY_AUDIENCE_SCORE);
                        }

                        synopsis = currentMovie.getString(KEY_SYNOPSIS);

                        // get movie poster
                        JSONObject objectPoster = currentMovie.getJSONObject(KEY_POSTERS);

                        if (objectPoster.has(KEY_THUMBNAIL)) {
                            urlThumbnail = objectPoster.getString(KEY_THUMBNAIL);
                        }

                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);
                        Date date=null;
                        try {
                                date = dateFormat.parse(releaseDate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                        movie.setReleaseDateTheater(date);
                        movie.setAudienceScore(audienceScore);
                        movie.setSynopsis(synopsis);
                        movie.setUrlThumbnail(urlThumbnail);

                        if(id!=-1 && !title.equals(Constants.NA)){
                            listMovie.add(movie);
                        }

                    }
                   // L.T(getActivity(), mListMovies.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listMovie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_box_office, container, false);
        textVolleyError=(TextView)view.findViewById(R.id.textVolleyError);
        listMovieHits=(RecyclerView)view.findViewById(R.id.listMovieHits);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice=new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);
        if(savedInstanceState!=null){
            mListMovies=savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            adapterBoxOffice.setMovieList(mListMovies);
        }
        else{
        sendJsonRequest();
        }
        return view;
    }


    @Override
    public void onSortByName() {

        mSorter.sortMoviesByName(mListMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByDate() {

        mSorter.sortMoviesByDate(mListMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {

        mSorter.sortMoviesByRatings(mListMovies);
        adapterBoxOffice.notifyDataSetChanged();

    }
}
