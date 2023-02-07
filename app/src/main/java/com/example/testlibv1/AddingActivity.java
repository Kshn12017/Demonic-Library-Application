package com.example.testlibv1;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddingActivity extends AppCompatActivity {

    Button linkbutton;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView2, autoCompleteTextView3;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    LinearLayout layout;

    ArrayList<String> list;

    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        linkbutton = findViewById(R.id.linkbutton);
        textInputLayout = findViewById(R.id.textInputLayout);
        autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView3 = findViewById(R.id.autoCompleteTextView3);
        layout = findViewById(R.id.layout);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = autoCompleteTextView2.getText().toString();
                Toast.makeText(AddingActivity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
                if (item.equals("Add Chapter")) {
                    textInputLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(AddingActivity.this, "If not ran", Toast.LENGTH_SHORT).show();
                }
            }
        });

        autoCompleteTextView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String novel = autoCompleteTextView3.getText().toString();
                if (novel.equals("I am the Fated Villain")) {
                    String checked = Chapterlist_iatfv();
                    if (checked != null) {

                        Toast.makeText(AddingActivity.this, "Checked: " + checked, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        linkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chapters chap = new Chapters(links);
                Chapterlist_iatfv();
            }
        });
    }

    private String Chapterlist_iatfv() {

        //String links = link.getText().toString();
        //System.out.println("Hello" + links);
        Toast.makeText(AddingActivity.this, "Please wait before selecting the chapters to add.", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }

        String theUrl = "https://demonictl.com/novels/i-am-the-fated-villain/i-am-the-fated-villain-chapter-104";
        //String[] content;
        StringBuilder content = new StringBuilder();
        // Use try and catch to avoid the exceptions
        try {
                String pageUrl = theUrl;
                URL url = new URL(pageUrl); // creating a url object
                URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

                // wrapping the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                // reading from the urlconnection using the bufferedreader
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                bufferedReader.close();

                String str = content.toString();

                //getString(html, theUrl);
                //System.out.println(html);

                // Creating an empty ArrayList
                List<String> list
                        = new ArrayList<>();

                // Regular Expression to extract
                // URL from the string
                String regex
                        = "\\b((?:https?|ftp|file):"
                        + "//[-a-zA-Z0-9+&@#/%?="
                        + "~_|!:, .;]*[-a-zA-Z0-9+"
                        + "&@#/%=~_|])";

                // Compile the Regular Expression
                Pattern p = Pattern.compile(
                        regex,
                        Pattern.CASE_INSENSITIVE);

                // Find the match between string
                // and the regular expression
                Matcher m = p.matcher(str);

                // Find the next subsequence of
                // the input subsequence that
                // find the pattern
                while (m.find()) {
                    // Find the substring from the
                    // first index of match result
                    // to the last index of match
                    // result and add in the list
                    String str2 = str.substring(m.start(0), m.end(0));
                    if (!list.contains(str2)) {
                        list.add(str2);
                    }
                }

                // IF there no URL present
                if (list.size() == 0) {
                    System.out.println("-1");
                    return null;
                }

                // Print all the URLs stored
                //List<String> unique = new ArrayList<>();
                for (String finUrl : list) {
                    if (finUrl.contains("/i-am-the-fated-villain-chapter-")) {
                        //System.out.println(finUrl);
                        CheckBox checkBox = new CheckBox(AddingActivity.this);
                        checkBox.setText(finUrl);
                        checkBox.setTextSize(25);
                        checkBox.setPadding(1,20,1,20);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER_HORIZONTAL;
                        checkBox.setLayoutParams(params);
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (checkBox.isChecked()) {
                                    String msg = "You have checked " + finUrl + "checkbox";
                                    Toast.makeText(AddingActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    geturl(finUrl);
                                }
                            }
                        });




                        layout.addView(checkBox);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String geturl(String url) {
        return url;
    }


    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.adds, R.layout.dropdown);
        autoCompleteTextView2.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.novels, R.layout.dropdown);
        autoCompleteTextView3.setAdapter(adapter2);
    }
}