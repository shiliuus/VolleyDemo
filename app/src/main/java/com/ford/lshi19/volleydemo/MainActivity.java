package com.ford.lshi19.volleydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    private Button mStringBtn;
    private Button mObjectBtn;
    private Button mImgBtn;
    private Button mImgLoaderBtn;
    private Button mNetworkImgLoaderBtn;
    private Button mXmlParserBtn;
    private ImageView mImgView;
    private NetworkImageView networkImageView;
    private Context mContext;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        mQueue = Volley.newRequestQueue(mContext);

        mStringBtn = (Button) findViewById(R.id.btn_string);
        mObjectBtn = (Button) findViewById(R.id.btn_json_object);
        mImgBtn = (Button) findViewById(R.id.btn_img);
        mImgLoaderBtn = (Button) findViewById(R.id.btn_imgloader);
        mNetworkImgLoaderBtn = (Button) findViewById(R.id.btn_network_imgloader);
        mXmlParserBtn = (Button) findViewById(R.id.btn_xml_parser);
        mImgView = (ImageView) findViewById(R.id.img_view);
        networkImageView = (NetworkImageView) findViewById(R.id.network_img_view);
        mStringBtn.setOnClickListener(this);
        mObjectBtn.setOnClickListener(this);
        mImgBtn.setOnClickListener(this);
        mImgLoaderBtn.setOnClickListener(this);
        mNetworkImgLoaderBtn.setOnClickListener(this);
        mXmlParserBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_string:
                Log.d("TAG", "hehe");
                StringRequest stringRequest = new StringRequest("http://www.google.com", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                });
                mQueue.add(stringRequest);
                return;
            case R.id.btn_json_object:
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d("TAG", jsonObject.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                });
                mQueue.add(jsonObjectRequest);
                return;
            case R.id.btn_img:
                ImageRequest imageRequest = new ImageRequest("http://developer.android.com/images/home/aw_dac.png",
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                mImgView.setImageBitmap(bitmap);
                            }
                        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                mImgView.setImageResource(R.drawable.ic_launcher);
                            }
                });
                mQueue.add(imageRequest);
                return;
            case R.id.btn_imgloader:
                ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImgView, R.drawable.ic_launcher, R.drawable.ic_launcher);
                imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener);
                return;
            case R.id.btn_network_imgloader:
                Log.d("TAG", "HERE");
                networkImageView.setDefaultImageResId(R.drawable.ic_launcher);
                networkImageView.setErrorImageResId(R.drawable.ic_launcher);
                ImageLoader networkImageLoader = new ImageLoader(mQueue, new BitmapCache());
                networkImageView.setImageUrl("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", networkImageLoader);
                return;
            case R.id.btn_xml_parser:
                XMLRequest xmlRequest = new XMLRequest("http://flash.wather.com.cn/wmaps/xml/china.xml",
                        new Response.Listener<XmlPullParser>() {
                            @Override
                            public void onResponse(XmlPullParser response) {
                                try {
                                    int eventType = response.getEventType();
                                    while (eventType != XmlPullParser.END_DOCUMENT) {
                                        switch (eventType) {
                                            case XmlPullParser.START_TAG:
                                                String nodeName = response.getName();
                                                if ("city".equals(nodeName)) {
                                                    String pName = response.getAttributeValue(0);
                                                    Log.d("TAG", "pName is " + pName);
                                                }
                                                break;
                                        }
                                        eventType = response.next();
                                    }
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                });
                mQueue.add(xmlRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
