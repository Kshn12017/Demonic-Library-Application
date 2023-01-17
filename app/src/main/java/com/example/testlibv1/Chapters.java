package com.example.testlibv1;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.*;

public class Chapters {
    // Function to extract all the URL
    // from the string
    public static void extractURL(String str, String theUrl)
    {

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
        while (m.find())
        {
            // Find the substring from the
            // first index of match result
            // to the last index of match
            // result and add in the list
            list.add(str.substring(m.start(0), m.end(0)));
        }

        // IF there no URL present
        if (list.size() == 0)
        {
            System.out.println("-1");
            return;
        }

        // Print all the URLs stored
        for (String finUrl : list)
        {
            if (finUrl.contains("chapter"))
            {
                System.out.println(finUrl);
            }
        }
    }
    private static String getUrlContents(String theUrl)
    {
        StringBuilder content = new StringBuilder();
        // Use try and catch to avoid the exceptions
        try
        {
            URL url = new URL(theUrl); // creating a url object
            URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

            // wrapping the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // reading from the urlconnection using the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();

            String html = content.toString();

            getString(html, theUrl);
            //System.out.println(html);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // Driver Code
    public static void getString(String str, String theUrl)
    {

        // Given String str
        //String str = "I am Kshitiz and I run https://www.demonictl.com";

        // Function Call
        extractURL(str, theUrl);
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the full url for the novel");
        String allurl = sc.nextLine();
        String output = getUrlContents(allurl);
        System.out.println(output);
    }
}

//Use checkbox