/* Author: Patrick Maley

 * Date: 31 May 2015
 *
 * Description: The DrudgeReporter is an easy to use app that brings the Drudge Report to the user
 * in an easy to use way. The listview of the information is generated into one list that the user
 * easily able to scroll through. Any image source is loaded directly above its associated story.
 * Click on the titles to be transferred to each webpage.
 *
 * Version: TDR v.1.0.0
 */

package com.androidbegin.jsouptutorial;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/*
 * Provides for app creation, pre-execution, asynchonous retrieval of information, and provides
 * onclickmethod for each row.
 */
public class MainActivity extends Activity {
    private static final String TAG = "MyActivity";
    private ProgressDialog mProgressDialog;
    private listViewAdapter mAdapter;
    private ListView mListView;
    private ArrayList<Item> dataForTheAdapter = new ArrayList<Item>();

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new backgroundTasks().execute();
        mListView = (ListView) findViewById(R.id.listView1);
    }

	public class backgroundTasks extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Drudge Report");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

		@Override
		protected String doInBackground(String... arg) {
            parseDrudgeReport();
            mAdapter =  new listViewAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            dataForTheAdapter);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TextView textcontent = (TextView) findViewById(R.id.article_name);
                   // textcontent.setMovementMethod(LinkMovementMethod.getInstance());

                    String text = dataForTheAdapter.get(position).getLink();
                    if(dataForTheAdapter.get(position).getImg() == null) {
                        Intent mIntent = new Intent(Intent.ACTION_VIEW);
                        mIntent.setData(Uri.parse(text));
                        startActivity(mIntent);
                    }
                }
            });
        }

    }

    private void parseDrudgeReport() {
        Document elementsDrudge = Jsoup.parse(getDrudgeReport());
        Elements links = elementsDrudge.select("a[href], img");

        for(Element link : links){
            if(link.attr("src").contains("http")) {
                dataForTheAdapter.add(new Item(link.text(), link.attr("href"), link.attr("src")));

            }
            else
                dataForTheAdapter.add(new Item(link.text(),link.attr("href")));

        }
    }

    private static String getDrudgeReport(){
        String stringDrudge = "";
        try {
            //Gets HTTP protocol for Drudge Report
            stringDrudge= getHtml("http://drudgereport.com");

        } catch (IOException e) {
            Log.e(TAG, "Android was attempting to retrieve the HTML code from the web page.", e);
        }

        //Pull indexes from Drudge Report webpage for all columns
        int topLeftColumnStart = stringDrudge.indexOf("<! TOP LEFT STARTS HERE>");
        int topLeftColumnEnd = stringDrudge.indexOf("<! MAIN HEADLINE>");
        int mainHeadlineEnd = stringDrudge.indexOf("<!-- Main headlines links END --->");
        int firstColumnStart = stringDrudge.indexOf("<! FIRST COLUMN STARTS HERE>");
        int firstColumnEnd = stringDrudge.indexOf("<!--JavaScript Tag  // Website: DrudgeReport");
        int secondColumnStart = stringDrudge.indexOf("<! SECOND COLUMN BEGINS HERE>");
        int secondColumnEnd = stringDrudge.indexOf("<! L I N K S      S E C O N D     C O L U M N>");
        int thirdColumnStart = stringDrudge.indexOf("<! THIRD COLUMN STARTS HERE>");
        int thirdColumnEnd = stringDrudge.indexOf("DrudgeReport_Home_Right_dynamic (1131611)");

        //Pull item strings from each column
        String topLeftColumn = stringDrudge.substring(topLeftColumnStart,topLeftColumnEnd);
        String mainHeadline = stringDrudge.substring(topLeftColumnEnd, mainHeadlineEnd);
        String mainHeadlineBanner = "<img src=\"http://www.drudgereport.com/i/logo9.gif\" border=\"0\" WIDTH=\"610\" HEIGHT=\"85\">";
        String firstColumn = stringDrudge.substring(firstColumnStart, firstColumnEnd);
        String secondColumn = stringDrudge.substring(secondColumnStart, secondColumnEnd);
        String thirdColumn = stringDrudge.substring(thirdColumnStart, thirdColumnEnd);

        //This section of code will be used to place section headers after each section of the
        //Drudge Report. These sections include the top left column of the page, the main headline,
        //and the three news story columns.

        /*Document topLeftElementsDrudge = Jsoup.parse(topLeftColumn);
        Elements topLeftLinks = topLeftElementsDrudge.select("a[href], img");
        Document mainHeadlineElementsDrudge = Jsoup.parse(mainHeadline);
        Elements mainHeadlineLinks = mainHeadlineElementsDrudge.select("a[href], img");
        Document firstColumnElementsDrudge = Jsoup.parse(firstColumn);
        Elements firstColumnLinks = firstColumnElementsDrudge.select("a[href], img");
        Document secondColumnElementsDrudge = Jsoup.parse(secondColumn);
        Elements secondColumnLinks = secondColumnElementsDrudge.select("a[href], img");
        Document thirdColumnElementsDrudge = Jsoup.parse(mainHeadline);
        Elements thirdColumnLinks = thirdColumnElementsDrudge.select("a[href], img");
        int topLeftSize = topLeftLinks.size();
        int mainHeadlineSize = mainHeadlineLinks.size();
        int firstColumnSize = firstColumnLinks.size();
        int secondColumnSize = secondColumnLinks.size();
        int thirdColumnSize = thirdColumnLinks.size();
        Log.d(TAG, String.format("This is the size of the five columns: %d, %d, %d, %d, %d",
                topLeftSize,mainHeadlineSize, firstColumnSize, secondColumnSize, thirdColumnSize));*/

        String allDrudge = (topLeftColumn + mainHeadline+ mainHeadlineBanner + firstColumn + secondColumn + thirdColumn);
        return allDrudge;
    }

    private static String getHtml(String location) throws IOException {
        URL url = new URL(location);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String input;
        StringBuilder builder = new StringBuilder();
        while((input = in.readLine()) != null)
        {
            builder.append(input);
        }

        return builder.toString();
    }
}