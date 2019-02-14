package com.ycbjie.ycapt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ycbjie.annotation.router.Extra;
import com.ycbjie.annotation.router.Router;
import com.ycbjie.api.router.EasyRouter;


@Router(path = Path.five)
public class FiveActivity extends AppCompatActivity {

    @Extra
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        EasyRouter.getsInstance().inject(this);

        //Intent intent = getIntent();
        //String title = intent.getStringExtra("title");
        Toast.makeText(this, "title=" + title, Toast.LENGTH_SHORT).show();
    }

}
