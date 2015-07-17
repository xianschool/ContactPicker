package com.wanglin.contactpicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ContactPickerTesterActivity extends ActionBarActivity {


    public static final int PICK_CONTACT = 1;       //  定义执行码！

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker_tester);

        Button button = (Button) findViewById(R.id.pick_contact_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                * intent构造函数的两个参数分别为：定义intent的动作，以及动作执行数据的uri
                * 由于intent是全局使用的，所以动作的定义需要在同一个命名空间内
                * 如android.intent.action.VIEW
                * 或者自定义动作为：com.wanglin.app.myapp.CUSTOM_ACTION
                * */

                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/"));

                //第二个参数为执行码，是后面返回结果的子Activity的唯一标识。
                // 若执行码大于0，则程序会在onActivityResult函数中返回相应结果
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

    }

    @Override
    public  void onActivityResult(int reqCode, int resCode, Intent data){

        /*
        * 关键字super指的是父类，而关键字this是指当前对象
        * 此处在重写的函数onActivityResult中调用了父类的onActivityResult函数
        * */

        super.onActivityResult(reqCode, resCode,data);


        /*
        * reqCode即是上文提到的执行码（requestCode），作为处理返回数据的唯一标识
        * resCode即是在ContactPickerActivity中setResult(Activity.RESULT_OK, outData)的resultCode参数，用来标识运行结果
        * */

        switch(reqCode){
            case (PICK_CONTACT) :{
                if(resCode == Activity.RESULT_OK){

                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    c.moveToFirst();
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    c.close();

                    TextView tv = (TextView) findViewById(R.id.selected_contact_textview);
                    tv.setText(name);
                }
                break;
            }
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_picker_tester, menu);
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
