package com.example.room_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler handler;
    private LayoutInflater inflater;
    private View layout;
    private AlertDialog.Builder builder;
    private TextView titleview;
    private ListView showmsg;
    private EditText sendmsgtext;
    private Button startserver;
    private Button continueserver;
    private Button sendmsgbt;
    private Button sendimg;
    private int StartPort;
    private boolean isContinue = true,isServer = false;
    private String message = "",userSendMsg = "",titletext = "";
    private String[] ContinueServerData = new String[2];// 0.ipv4 1.port
    private Long mID = 0L;
    private List<MessageInfor> datas = new ArrayList<MessageInfor>();
    private SimpleDateFormat simpleDateFormat;
    private MessageAdapte messageAdapte;
    private static Socket socket = null;//use with servers Socket
    private static ServerSocket server;
    private static List<PrintWriter> allOut; //A collection of output streams that store all clients for broadcasting

    private static final int IMAGE = 1;//Call system album - select picture
    private static String[] PERMISSIONS_STORAGE = {
            //Apply for permission in sequence
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide title bar
        applypermission();
        InitView();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 1){
                    titleview.setText(titletext);
                }else if(msg.what == 2){
                    titleview.setText("Number of people currently online["+(allOut.size()+1)+"]");
                }
                super.handleMessage(msg);
            }
        };
    }
    private void InitView() {
        titleview = (TextView) findViewById(R.id.titleview);
        showmsg = (ListView) findViewById(R.id.showmsg);
        sendmsgtext = (EditText) findViewById(R.id.sendmsgtext);
        startserver = (Button) findViewById(R.id.startserver);
        continueserver = (Button) findViewById(R.id.continueserver);
        sendmsgbt = (Button) findViewById(R.id.sendmsgbt);
        sendimg = (Button) findViewById(R.id.sendimg);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        messageAdapte = new MessageAdapte();
        showmsg.setAdapter(messageAdapte);

        startserver.setOnClickListener(this);
        continueserver.setOnClickListener(this);
        sendmsgbt.setOnClickListener(this);
        sendimg.setOnClickListener(this);

    }

    //Define the function for judging permission application, just call it in onCreat
    public void applypermission(){
        if(Build.VERSION.SDK_INT>=23){
            boolean needapply=false;
            for(int i=0;i<PERMISSIONS_STORAGE.length;i++){
                int chechpermission= ContextCompat.checkSelfPermission(getApplicationContext(),
                        PERMISSIONS_STORAGE[i]);
                if(chechpermission!= PackageManager.PERMISSION_GRANTED){
                    needapply=true;
                }
            }
            if(needapply){
                ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE,1);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startserver:
                //load layout
                inflater = LayoutInflater.from(this);
                layout = inflater.inflate(R.layout.start_server,null);
                //through to AlertDialog.Builder 對象調用 setView()
                builder =  new AlertDialog.Builder(MainActivity.this);
                builder.setView(R.layout.start_server);
                builder.setCancelable(false);//Cancellable

                EditText editprot = (EditText) layout.findViewById(R.id.editprot);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(layout)  //setting what to view
                        .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StartPort = Integer.valueOf(editprot.getText().toString());
                                mID = System.currentTimeMillis();
                                ServerInit();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)  //Pressing the back key does not cancel the dialog
                        .show();
                break;
            case R.id.continueserver:
                //load layout
                inflater = LayoutInflater.from(this);
                layout = inflater.inflate(R.layout.continue_server,null);

                builder =  new AlertDialog.Builder(MainActivity.this);
                builder.setView(R.layout.continue_server);
                builder.setCancelable(false);

                EditText editipv4text = (EditText) layout.findViewById(R.id.editipv4text);
                EditText editprottext = (EditText) layout.findViewById(R.id.editprottext);

                new AlertDialog.Builder(MainActivity.this)
                        .setView(layout)
                        .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContinueServerData[0] = editipv4text.getText().toString();
                                ContinueServerData[1] = editprottext.getText().toString();
                                ContinueSever();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)  //Pressing the back key does not cancel the dialog
                        .show();

                break;
            case R.id.sendmsgbt://send message
                if(isServer){//here is server
                    message = sendmsgtext.getText().toString();
                    if(message==null||"".equals(message)){
                        Toast.makeText(MainActivity.this,"Send message cannot be empty",Toast.LENGTH_LONG).show();
                        return ;
                    }
                    long Ltimes = System.currentTimeMillis();
                    message = sendmsgtext.getText().toString();
                    datas.add(new MessageInfor(message,Ltimes,mID,"1"));
                    sendMessage("{\"isimg\":\"1\",\"msg\":\""+message+"\",\"times\":\""+Ltimes+"\",\"id\":\""+mID+"\",\"peoplen\":\""+"Number of people currently online["+(allOut.size()+1)+"]"+"\"}");
                    sendmsgtext.setText("");



                }else {//Client
                    sendMsgText();
                }


                break;
            case R.id.sendimg:
                //get photo
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
        }
    }


    //Put the given output stream into the collection
    private synchronized void addOut(PrintWriter out){
        allOut.add(out);
    }

    //Put the given output stream into the collection
    private synchronized void removeOut(PrintWriter out){
        allOut.remove(out);
    }


    //Send the given message to the client
    private void sendMessage(String message) {
        Thread sendmsg = new Thread(new Runnable() {
            @Override
            public void run() {
                for(PrintWriter out:allOut) {
                    out.println(message);
                }
            }
        });
        sendmsg.start();
    }





    public void ServerInit() {
        try {
            server = new ServerSocket(StartPort);
            allOut = new ArrayList<PrintWriter>();
            isServer = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            Socket socket1 = null;
                            try {
                                socket1 = server.accept();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            ClientHandler hander = new ClientHandler(socket1);
                            Thread t = new Thread(hander);
                            t.start();
                        }
                    }
                }).start();
    }


    //The thread class is to interact with the specified client
    class ClientHandler implements Runnable{
        //Socket of the current thread client
        private Socket socket;

        //the client's address
        private String host;

        public ClientHandler(Socket socket) {
            this.socket=socket;
            InetAddress address = socket.getInetAddress();
            //get ip address
            host = address.getHostAddress();
        }

        @Override
        public void run() {
            PrintWriter pw = null;
            try {
                //there is user entry
                sendMessage("["+host+"]加入聊天!");

                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
                pw = new PrintWriter(osw,true);

                //Store the client's output stream into a shared collection so that messages can be broadcast to the client
                addOut(pw);

                handler.sendEmptyMessage(2);

                //Process data from the client
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String message = null;
                while((message = br.readLine())!=null) {

                    try {
                        JSONObject json = new  JSONObject(message);
                        if(json.getString("isimg").equals("1")){//not photo
                            datas.add(new MessageInfor(json.getString("msg"),Long.valueOf(json.getString("times")),Long.valueOf(json.getString("id")),"1"));
                        }else if(json.getString("isimg").equals("0")){//is photo
                            datas.add(new MessageInfor(json.getString("msg"),Long.valueOf(json.getString("times")),Long.valueOf(json.getString("id")),"0"));
                        }
                        titletext = json.getString("peoplen");
                        handler.sendEmptyMessage(1);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    sendMessage(message);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally {
                //removes this client's output stream from the shared collection
                removeOut(pw);

                //user out
                sendMessage("["+host+"] quit chat!");

                handler.sendEmptyMessage(2);

                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean ContinueSever(){

        Thread continuethread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //localhost 127.0.0.1
                            socket = new Socket(ContinueServerData[0],Integer.valueOf(ContinueServerData[1]));
                            mID = System.currentTimeMillis();
                        } catch (Exception e) {
                            isContinue = false;
                            isServer = false;
                            e.printStackTrace();
                        }
                    }
                }
        );
        continuethread.start();

        while(isContinue){
            if(socket != null){
                break;
            }
        }


        if(isContinue) {
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                           //server working manner
                            try {

                                ServerHandler handler = new ServerHandler();
                                Thread t = new Thread(handler);
                                t.start();

                                //data send to server
                                OutputStream out = socket.getOutputStream();
                                OutputStreamWriter osw = new OutputStreamWriter(out,"utf-8");
                                PrintWriter pw = new PrintWriter(osw,true);
                                while(true) {
                                    if(userSendMsg != "" && userSendMsg!=null){
                                        pw.println(userSendMsg);//output information to the server
                                        userSendMsg = "";
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
        }
        return isContinue;
    }

    class ServerHandler implements Runnable{
        //read the msg from server
        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in,"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String message1=br.readLine();
                while(message1!=null) {
                    Log.i("測試4",message1);
                    try {
                        JSONObject json = new  JSONObject(message1);
                        if(json.getLong("id") != mID){
                            if(json.getString("isimg").equals("1")){
                                datas.add(new MessageInfor(json.getString("msg"),Long.valueOf(json.getString("times")),Long.valueOf(json.getString("id")),"1"));
                            }else if(json.getString("isimg").equals("0")){
                                datas.add(new MessageInfor(json.getString("msg"),Long.valueOf(json.getString("times")),Long.valueOf(json.getString("id")),"0"));
                            }
                        }
                        titletext = json.getString("peoplen");
                        handler.sendEmptyMessage(1);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    message1=br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //send msg
    private void sendMsgText(){
        message = sendmsgtext.getText().toString();
        if(message==null||"".equals(message)){
            Toast.makeText(MainActivity.this,"Send message cannot be empty",Toast.LENGTH_LONG).show();
            return ;
        }
        long Ltimes = System.currentTimeMillis();
        MessageInfor m = new MessageInfor(message,Ltimes,mID,"1");
        userSendMsg = "{\"isimg\":\"1\",\"msg\":\""+sendmsgtext.getText().toString()+"\",\"times\":\""+Ltimes+"\",\"id\":\""+mID+"\"}";
        datas.add(m);
        messageAdapte.notifyDataSetChanged();//Notify data source changes

        sendmsgtext.setText("");
    }

    class MessageAdapte extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public MessageInfor getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            Long id = datas.get(i).getUserID();
            return id==null?0:id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MessageHolder holder = null;
            if(view == null){
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.chart_item,null);
                holder = new MessageHolder();
                holder.left = (TextView) view.findViewById(R.id.itemleft);
                holder.right = (TextView) view.findViewById(R.id.itemright);
                holder.lefttime = (TextView) view.findViewById(R.id.itemtimeleft);
                holder.righttime = (TextView) view.findViewById(R.id.itemtimeright);

                holder.rightimgtime = (TextView) view.findViewById(R.id.rightimgtime);
                holder.leftimgtime = (TextView) view.findViewById(R.id.leftimgtime);
                holder.rightimg = (ImageView) view.findViewById(R.id.rightimg);
                holder.leftimg = (ImageView) view.findViewById(R.id.leftimg);

                view.setTag(holder);
            }else {
                holder = (MessageHolder) view.getTag();
            }
            MessageInfor mi = getItem(i);
            //show
            if (mi.getUserID() == mID){//id equal
                if(mi.getType().equals("0")){//photo
                    holder.leftimg.setVisibility(View.GONE);
                    holder.leftimgtime.setVisibility(View.GONE);
                    holder.rightimg.setVisibility(View.VISIBLE);
                    holder.rightimgtime.setVisibility(View.VISIBLE);
                    holder.rightimg.setImageBitmap(convertStringToIcon(mi.getMsg()));
                    holder.rightimgtime.setText(simpleDateFormat.format(new Date(mi.getTime())));

                    holder.left.setVisibility(View.GONE);
                    holder.lefttime.setVisibility(View.GONE);
                    holder.right.setVisibility(View.GONE);
                    holder.righttime.setVisibility(View.GONE);

                }else if(mi.getType().equals("1")){//消息
                    holder.leftimg.setVisibility(View.GONE);
                    holder.leftimgtime.setVisibility(View.GONE);
                    holder.rightimg.setVisibility(View.GONE);
                    holder.rightimgtime.setVisibility(View.GONE);

                    holder.left.setVisibility(View.GONE);
                    holder.lefttime.setVisibility(View.GONE);
                    holder.right.setVisibility(View.VISIBLE);
                    holder.righttime.setVisibility(View.VISIBLE);
                    holder.right.setText(mi.getMsg());
                    holder.righttime.setText(simpleDateFormat.format(new Date(mi.getTime())));
                }


            }else {
                if(mi.getType().equals("0")){//图片
                    holder.leftimg.setVisibility(View.VISIBLE);
                    holder.leftimgtime.setVisibility(View.VISIBLE);
                    holder.rightimg.setVisibility(View.GONE);
                    holder.rightimgtime.setVisibility(View.GONE);
                    holder.leftimg.setImageBitmap(convertStringToIcon(mi.getMsg()));
                    holder.leftimgtime.setText(simpleDateFormat.format(new Date(mi.getTime())));

                    holder.left.setVisibility(View.GONE);
                    holder.lefttime.setVisibility(View.GONE);
                    holder.right.setVisibility(View.GONE);
                    holder.righttime.setVisibility(View.GONE);

                }else if(mi.getType().equals("1")){//消息
                    holder.leftimg.setVisibility(View.GONE);
                    holder.leftimgtime.setVisibility(View.GONE);
                    holder.rightimg.setVisibility(View.GONE);
                    holder.rightimgtime.setVisibility(View.GONE);

                    holder.left.setVisibility(View.VISIBLE);
                    holder.lefttime.setVisibility(View.VISIBLE);
                    holder.right.setVisibility(View.GONE);
                    holder.righttime.setVisibility(View.GONE);
                    holder.left.setText(mi.getMsg());
                    holder.lefttime.setText(simpleDateFormat.format(new Date(mi.getTime())));
                }
            }
            return view;
        }
    }

    class MessageHolder{
        public TextView left;
        public TextView right;
        public TextView lefttime;
        public TextView righttime;
        private TextView rightimgtime;
        private TextView leftimgtime;
        private ImageView rightimg;
        private ImageView leftimg;

    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        //got the photo address

        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);

            String imagePath = c.getString(columnIndex);

            activityImage(imagePath);
            c.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void activityImage(String imaePath){

        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        bm =  resizeBitmap(bm,400,400,true);
        long Ltimes = System.currentTimeMillis();
        String imgString = convertIconToString(bm);
        imgString = imgString.replace("\n","");
        datas.add(new MessageInfor(imgString,Ltimes,mID,"0"));

        if(isServer){//服务器
            sendMessage("{\"isimg\":\"0\",\"msg\":\""+imgString+"\",\"times\":\""+Ltimes+"\",\"id\":\""+mID+"\"}");
        }else {//客户端
            userSendMsg = "{\"isimg\":\"0\",\"msg\":\""+imgString+"\",\"times\":\""+Ltimes+"\",\"id\":\""+mID+"\"}";
        }


    }

    //photo to string
    private String convertIconToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// to byte
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }
    //string to bitmap
    private Bitmap convertStringToIcon(String st){
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //deal with photo
    private Bitmap resizeBitmap(Bitmap bitmap,int MaxWidth,int MaxHeight,boolean filter){
        Float ScalingNumber;
        Bitmap reBitmap;
        Matrix matrix = new Matrix();
        ScalingNumber = Float.valueOf(scalingNumber(bitmap.getWidth(),bitmap.getHeight(),MaxWidth,MaxHeight));
        matrix.setScale(1/ScalingNumber, 1/ScalingNumber);
        reBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, filter);

        return reBitmap;
    }


    private int scalingNumber(int oldWidth,int oldHeight,int MaxWidth,int MaxHeight){
        int scalingN = 1;
        if(oldWidth > MaxWidth || oldHeight > MaxHeight){
            scalingN = 2;
            while((oldWidth/scalingN > MaxWidth) || (oldHeight/scalingN > MaxHeight)){
                scalingN*=2;
            }
        }

        return scalingN;
    }
}