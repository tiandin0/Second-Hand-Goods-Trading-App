package com.example.backend.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.backend.Activity.ProfileActivity;
import com.example.backend.Activity.ChatActivity;
import com.example.backend.Activity.MainPageActivity;
import com.example.backend.Activity.PostActivity;
import com.example.backend.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link buttom_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link buttom_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class buttom_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageButton home;
    private ImageButton post;
    private ImageButton chat;
    private ImageButton profile;

    public buttom_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment buttom_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static buttom_fragment newInstance(String param1, String param2) {
        buttom_fragment fragment = new buttom_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("fragment", "oncreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("fragment", "oncreateview");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.buttom_fragment, container, false);

        home = (ImageButton) view.findViewById(R.id.Home);
        post = (ImageButton) view.findViewById(R.id.Post);
        chat = (ImageButton) view.findViewById(R.id.Chat);
        profile = (ImageButton) view.findViewById(R.id.Profile);

        home.setOnClickListener(new homeListener());
        post.setOnClickListener(new postListenr());
        chat.setOnClickListener(new chatListener());
        profile.setOnClickListener(new profileListener());


        return view;
    }


    private class homeListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), MainPageActivity.class);
            startActivity(intent);            //to do, transfer to main page
        }
    }

    private class postListenr implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
            //to do, transfer to post page

        }
    }

    private class chatListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        }
    }

    private class profileListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
            //to do, transfer to profile page
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Log.d("fragment", "onattach");
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d("fragment", "onactivitycreate");
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("fragment", "onstart");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("fragment", "onresume");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("fragment", "onpause");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("fragment", "onstop");
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.d("fragment", "ondestroyview");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("fragment", "ondestroy");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.d("fragment", "dettach");
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
