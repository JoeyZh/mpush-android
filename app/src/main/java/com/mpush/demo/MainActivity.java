package com.mpush.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mpush.android.MPushApiHelper;
import com.mpush.android.MPushConfig;
import com.mpush.api.Constants;
import com.mpush.api.http.HttpCallback;
import com.mpush.api.http.HttpResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MPushConfig config = MyConfig.build(this);
        MPushApiHelper.getInstance().initSDK(this, config)
                .startPush(MyConfig.allotServer);
        EditText et = findViewById(R.id.alloc);
        et.setText(config.getAllotServer());
        EditText from = findViewById(R.id.from);
        from.setText(config.getUserId());
        EditText log = findViewById(R.id.log);
        ((MyLog) config.getLogger()).setLogView(log);
    }


    public void bindUser(View btn) {
        EditText et = findViewById(R.id.from);
        EditText tags = findViewById(R.id.tags);
        String userId = et.getText().toString().trim();
        if (!TextUtils.isEmpty(userId)) {
            MPushApiHelper.getInstance().bindAccount(userId, tags.toString());
        }
    }

    public void startPush(View btn) {
        EditText et = findViewById(R.id.alloc);
        String allocServer = et.getText().toString().trim();
        MPushApiHelper.getInstance().startPush(allocServer);
        Toast.makeText(this, "start push", Toast.LENGTH_SHORT).show();
    }

    public void sendPush(View btn) throws Exception {
        EditText et1 = findViewById(R.id.alloc);
        String allocServer = et1.getText().toString().trim();

        if (TextUtils.isEmpty(allocServer)) {
            Toast.makeText(this, "请填写正确的alloc地址", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText toET = findViewById(R.id.to);
        String to = toET.getText().toString().trim();

        EditText helloET = findViewById(R.id.httpProxy);
        String hello = helloET.getText().toString().trim();

        MPushApiHelper.getInstance().sendMessageTo(to, hello, new HttpCallback() {
            @Override
            public void onResponse(final HttpResponse httpResponse) {
                String response;
                if (httpResponse.statusCode == 200) {
                    response = new String(httpResponse.body, Constants.UTF_8);
                } else {
                    response = httpResponse.reasonPhrase;
                }
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    public void stopPush(View btn) {
        MPushApiHelper.getInstance().stopPush();
        Toast.makeText(this, "stop push", Toast.LENGTH_SHORT).show();
    }

    public void pausePush(View btn) {
        MPushApiHelper.getInstance().pausePush();
        Toast.makeText(this, "pause push", Toast.LENGTH_SHORT).show();
    }

    public void resumePush(View btn) {
        MPushApiHelper.getInstance().resumePush();
        Toast.makeText(this, "resume push", Toast.LENGTH_SHORT).show();
    }

    public void unbindUser(View btn) {
        MPushApiHelper.getInstance().unbindAccount();
        Toast.makeText(this, "unbind user", Toast.LENGTH_SHORT).show();
    }
}
