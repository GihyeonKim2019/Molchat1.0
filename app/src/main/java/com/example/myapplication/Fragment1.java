package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

public class Fragment1 extends Fragment {
    public static Fragment1 newInstance(int number) {
        Fragment1 fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        fragment1.setArguments(bundle);
        return fragment1;
    }
    private Socket mSocket;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        Button startasjack = (Button)view.findViewById(R.id.startasjack);
        Button startaskate = (Button)view.findViewById(R.id.startaskate);
        startasjack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WaitlistActivity.class);
                startActivity(intent);
            }
        });

        startaskate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GlobalId)getActivity().getApplication()).setGlobalId("kate");
                Intent intent = new Intent(getActivity(),WaitlistActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
