package com.example.testlibv1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageFragment extends Fragment {

    Button addNovel, addChap, imgBtn;
    CheckBox action_check, adventure_check, comedy_check, drama_check, fantasy_check, isekai_check, romance_check, wuxia_check;
    TextInputLayout textInputLayout;
    TextInputEditText noveltitle, description, author;
    AutoCompleteTextView autoCompleteTextView2, autoCompleteTextView3, autoCompleteTextView4, autoCompleteTextView5;
    View view1, view2;

    FirebaseFirestore db;
    DatabaseReference dbRef;
    FirebaseStorage fb;
    StorageReference storageRef;
    FirebaseDatabase fdb;

    ScrollView scroll1, scroll2;
    LinearLayout layout;

    List<String> chapLink = new ArrayList<>();
    List<String> chapNum = new ArrayList<>();
    List<String> genreList = new ArrayList<>();
    List<String> galleryList = new ArrayList<>();
    String checked, chapNumber, noveltitlestr, descriptionstr, authorstr, statusstr, typestr, uristr;
    String novel;
    String actionstr, adventurestr, comedystr, dramastr, fantasystr, isekaistr, romancestr, wuxiastr;

    ActivityResultLauncher<String> novelcover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNovel = view.findViewById(R.id.addNovel);
        addChap = view.findViewById(R.id.addChap);
        imgBtn = view.findViewById(R.id.imgBtn);
        noveltitle = view.findViewById(R.id.noveltitle);
        description = view.findViewById(R.id.description);
        author = view.findViewById(R.id.author);
        action_check = view.findViewById(R.id.action_check);
        adventure_check = view.findViewById(R.id.adventure_check);
        comedy_check = view.findViewById(R.id.comedy_check);
        drama_check = view.findViewById(R.id.drama_check);
        fantasy_check = view.findViewById(R.id.fantasy_check);
        isekai_check = view.findViewById(R.id.isekai_check);
        romance_check = view.findViewById(R.id.romance_check);
        wuxia_check = view.findViewById(R.id.wuxia_check);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView3 = view.findViewById(R.id.autoCompleteTextView3);
        autoCompleteTextView4 = view.findViewById(R.id.autoCompleteTextView4);
        autoCompleteTextView5 = view.findViewById(R.id.autoCompleteTextView5);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        layout = view.findViewById(R.id.layout);
        scroll1 = view.findViewById(R.id.scroll1);
        scroll2 = view.findViewById(R.id.scroll2);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        dbRef = fdb.getReference();
        fb = FirebaseStorage.getInstance();
        storageRef = fb.getReference();

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String todo = autoCompleteTextView2.getText().toString();
                if (todo.equals("Add Novel")) {
                    textInputLayout.setVisibility(View.GONE);
                    addChap.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    scroll1.setVisibility(View.GONE);
                    addNovel.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    scroll2.setVisibility(View.VISIBLE);
                }
                if (todo.equals("Add Chapter")) {
                    addNovel.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    scroll2.setVisibility(View.GONE);
                    textInputLayout.setVisibility(View.VISIBLE);
                    addChap.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                }
            }
        });

        autoCompleteTextView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                novel = autoCompleteTextView3.getText().toString();
                if (novel.equals("I am the Fated Villain")) {
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_iatfv();
                }
                if(novel.equals("I Became the Rich Second Generation Villain")){
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_sgv();
                }
                if(novel.equals("Return of the Shattered Constellation")){
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_rsc();
                }
                if(novel.equals("Duck Emperor Chronicles")){
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_dec();
                }
                if(novel.equals("I'm Not That Kind of Talent")){
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_intkt();
                }
                if(novel.equals("Logging 10,000 Years Into Future")){
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_login();
                }
                if(novel.equals("My School Life Pretending to be a Worthless Person")){
                    layout.removeAllViews();
                    scroll1.setVisibility(View.VISIBLE);
                    Chapterlist_slpw();
                }

            }
        });

        description.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        autoCompleteTextView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                statusstr = autoCompleteTextView4.getText().toString();
            }
        });

        autoCompleteTextView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typestr = autoCompleteTextView5.getText().toString();
            }
        });

        addChap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String chapNo : chapNum) {
                    for (String chapLi : chapLink) {
                        if (chapLi.contains(chapNo)) {
                            Map<String, Object> chapter = new HashMap<>();
                            chapter.put(chapNo, chapLi);
                            dbRef.child("Novels").child(novel).child("Chapter").updateChildren(chapter);
                        }
                    }
                }
            }
        });

        addNovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addnovel();
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noveltitlestr = noveltitle.getText().toString();
                if (noveltitlestr.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Novel Title first.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Recommended image ratio - 3:4", Toast.LENGTH_SHORT).show();
                    novelcover.launch("image/*");
                }
            }
        });

        novelcover = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                storageRef.child("novelimages/"+noveltitlestr).putFile(result)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child("novelimages/"+noveltitlestr).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(getActivity(), "Add all the details to make sure image is stored.", Toast.LENGTH_SHORT).show();
                                        uristr = uri.toString();
                                        galleryList.add(uristr);
                                        dbRef.child("Novels").child(noveltitlestr).child("Cover").setValue(uristr)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Intent change = new Intent(getActivity(), ManageFragment.class);
                                                        startActivity(change);
                                                        Toast.makeText(getActivity(), "Image upload failed.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Intent change = new Intent(getActivity(), ManageFragment.class);
                                        startActivity(change);
                                        Toast.makeText(getActivity(), "Image upload failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent change = new Intent(getActivity(), ManageFragment.class);
                                startActivity(change);
                                Toast.makeText(getActivity(), "Image upload failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void addnovel() {
        noveltitlestr = noveltitle.getText().toString();
        descriptionstr = description.getText().toString();
        authorstr = author.getText().toString();
        checks();
        System.out.println(noveltitlestr);
        if (noveltitlestr != null && !noveltitlestr.isEmpty()) {
            dbRef.child("Novels").child(noveltitlestr).child("Title").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String novelname = snapshot.getValue(String.class);
                    System.out.println(novelname);
                    if (novelname != null && !novelname.isEmpty()) {
                        Toast.makeText(getActivity(), "Novel already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(typestr);
                        if (typestr != null && !typestr.isEmpty()) {
                            System.out.println(descriptionstr);
                            if (descriptionstr != null && !descriptionstr.isEmpty()) {
                                System.out.println(authorstr);
                                if (authorstr != null && !authorstr.isEmpty()) {
                                    System.out.println(statusstr);
                                    if (statusstr != null && !statusstr.isEmpty()) {
                                        dbRef.child("Novels").child(noveltitlestr).child("Cover").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String coverstr = snapshot.getValue(String.class);
                                                System.out.println(coverstr);
                                                if (coverstr != null && !coverstr.isEmpty()) {
                                                    System.out.println(genreList);
                                                    if (genreList != null && !genreList.isEmpty()) {
                                                        dbRef.child("Novels").child(noveltitlestr).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Map<String, Object> values = new HashMap<>();
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    values.put(snapshot.getKey(), snapshot.getValue());
                                                                }
                                                                values.put("Title", noveltitlestr);
                                                                values.put("Type",typestr);
                                                                values.put("Description", descriptionstr);
                                                                values.put("Genre", genreList);
                                                                values.put("Author", authorstr);
                                                                values.put("Status", statusstr);
                                                                values.put("Gallery",galleryList);
                                                                db.collection("Novels").document(noveltitlestr)
                                                                                .set(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(getActivity(), "Novel Added", Toast.LENGTH_SHORT).show();
                                                                                Intent change = new Intent(getActivity(), NavigationActivity.class);
                                                                                startActivity(change);
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getActivity(), "Adding failed.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(getActivity(), "Select atleast 1 genre.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity(), "Cover not found.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "Select status", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Enter Author Name", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "No description found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Select Type", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(getActivity(), "Enter Novel Title", Toast.LENGTH_SHORT).show();
        }

    }

    private void checks() {
        if (action_check.isChecked()) {
            actionstr = action_check.getText().toString();
            addgenre(actionstr);
        } else {
            actionstr = action_check.getText().toString();
            removegenre(actionstr);
        }
        if (adventure_check.isChecked()) {
            adventurestr = adventure_check.getText().toString();
            addgenre(adventurestr);
        } else {
            adventurestr = adventure_check.getText().toString();
            removegenre(adventurestr);
        }
        if (comedy_check.isChecked()) {
            comedystr = comedy_check.getText().toString();
            addgenre(comedystr);
        } else {
            comedystr = comedy_check.getText().toString();
            removegenre(comedystr);
        }
        if (drama_check.isChecked()) {
            dramastr = drama_check.getText().toString();
            addgenre(dramastr);
        } else {
            dramastr = drama_check.getText().toString();
            removegenre(dramastr);
        }
        if (fantasy_check.isChecked()) {
            fantasystr = fantasy_check.getText().toString();
            addgenre(fantasystr);
        } else {
            fantasystr = fantasy_check.getText().toString();
            removegenre(fantasystr);
        }
        if (isekai_check.isChecked()) {
            isekaistr = isekai_check.getText().toString();
            addgenre(isekaistr);
        } else {
            isekaistr = isekai_check.getText().toString();
            removegenre(isekaistr);
        }
        if (romance_check.isChecked()) {
            romancestr = romance_check.getText().toString();
            addgenre(romancestr);
        } else {
            romancestr = romance_check.getText().toString();
            removegenre(romancestr);
        }
        if (wuxia_check.isChecked()) {
            wuxiastr = wuxia_check.getText().toString();
            addgenre(wuxiastr);
        } else {
            wuxiastr = wuxia_check.getText().toString();
            removegenre(wuxiastr);
        }
    }

    private void addgenre(String genreadd) {
        if (!genreList.contains(genreadd)) {
            genreList.add(genreadd);
        }
    }

    private void removegenre(String genreremove) {
        if (genreList.contains(genreremove)) {
            genreList.remove(genreremove);
        }
    }

    private void Chapterlist_iatfv() {
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novels/i-am-the-fated-villain/i-am-the-fated-villain-chapter-104";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/i-am-the-fated-villain-chapter-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_iatfv(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Chapterlist_sgv(){
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novel/i-became-the-rich-second-generation-villain/i-became-the-rich-second-generation-villain-chapter-35/";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/i-became-the-rich-second-generation-villain-chapter-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_sgv(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Chapterlist_login(){
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novel/logging-10000-years-into-the-future/chapter-1-nightmares/";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/logging-10000-years-into-the-future/chapter-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_login(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Chapterlist_rsc(){
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novel/return-of-the-shattered-constellation/return-of-the-shattered-constellation-chapter-1/";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/return-of-the-shattered-constellation-chapter-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_rsc(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Chapterlist_slpw(){
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novel/my-school-life-pretending-to-be-a-worthless-person/my-school-life-pretending-to-be-a-worthless-person-prologue/";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/my-school-life-pretending-to-be-a-worthless-person/my-school-life-pretending-to-be-a-worthless-person-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_slpw(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Chapterlist_dec(){
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novel/duck-emperor-chronicles/duck-emperor-chronicles-prologue-1/";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/duck-emperor-chronicles/duck-emperor-chronicles-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_dec(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Chapterlist_intkt(){
        Toast.makeText(getActivity(), "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novel/im-not-that-kind-of-talent/chapter-1-the-demon-kings-strongest-hand-1/";
        StringBuilder content = new StringBuilder();
        try {
            String pageUrl = theUrl;
            URL url = new URL(pageUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            String str = content.toString();

            List<String> list = new ArrayList<>();

            String regex = "\\b((?:https?|ftp|file):" + "//[-a-zA-Z0-9+&@#/%?=" + "~_|!:, .;]*[-a-zA-Z0-9+" + "&@#/%=~_|])";

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            Matcher m = p.matcher(str);

            while (m.find()) {
                String str2 = str.substring(m.start(0), m.end(0));
                if (!list.contains(str2)) {
                    list.add(str2);
                }
            }

            if (list.size() == 0) {
                System.out.println("-1");
            }

            for (String finUrl : list) {
                if (finUrl.contains("/im-not-that-kind-of-talent/chapter-")) {
                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText(finUrl);
                    checkBox.setTextSize(25);
                    checkBox.setPadding(1, 20, 1, 20);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    checkBox.setLayoutParams(params);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                geturl_intkt(finUrl);
                            } else {
                                removeurl(finUrl);
                            }
                        }
                    });
                    layout.addView(checkBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void geturl_iatfv(String finUrl) {
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/i-am-the-fated-villain/i-am-the-fated-villain-", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void geturl_sgv(String finUrl){
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/i-became-the-rich-second-generation-villain/i-became-the-rich-second-generation-villain-", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void geturl_login(String finUrl) {
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/logging-10000-years-into-the-future/", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void geturl_rsc(String finUrl) {
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/return-of-the-shattered-constellation/return-of-the-shattered-constellation-", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void geturl_slpw(String finUrl) {
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/my-school-life-pretending-to-be-a-worthless-person/my-school-life-pretending-to-be-a-worthless-person-", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void geturl_dec(String finUrl) {
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/duck-emperor-chronicles/duck-emperor-chronicles-", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void geturl_intkt(String finUrl) {
        checked = finUrl;
        chapNumber = checked.replace("https://demonictl.com/novel/im-not-that-kind-of-talent/", "");
        chapNumber = chapNumber.replace("/", "");
        if (!chapLink.contains(checked)) {
            chapLink.add(checked);
        }
        if (!chapNum.contains(chapNumber)) {
            chapNum.add(chapNumber);
        }
    }

    private void removeurl(String finUrl) {
        checked = finUrl;
        if (chapLink.contains(checked)) {
            chapLink.remove(checked);
        }
        if (chapNum.contains(chapNumber)) {
            chapNum.remove(chapNumber);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.adds, R.layout.dropdown);
        autoCompleteTextView2.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.novels, R.layout.dropdown);
        autoCompleteTextView3.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.status, R.layout.dropdown);
        autoCompleteTextView4.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getActivity(), R.array.type, R.layout.dropdown);
        autoCompleteTextView5.setAdapter(adapter4);
    }

}