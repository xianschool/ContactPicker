package com.wanglin.contactpicker;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ContactPickerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);

        /*
        * 以下代码过程为:
        * 1. 创建Cursor，遍历联系人
        * 2. 将Cursor绑定到ListView
        * 3. 对ListView建立onItemClickListener，监视点击事件
        * 4. 将选中项的URI传递给Intent
        * 5. Intent作为数据返回给Activity
        * */


        /*
        * 创建一个新的Cursor来遍历储存在联系人列表中的联系人
        * Cursor是所查询数据中每一行的集合，为一个随机的数据源
        * 通过query()方法可以得到Cursor对象
        * 安卓程序之间的数据传输是通过Provider/Resolver完成的，其中Provider是数据提供方，Resolver是数据接收方
        * 此处的Provider是系统的联系人应用，即ContactsContract
        *
        * <def>
        *   public final Cursor query (Uri uri, String[] projection,String selection,String[] selectionArgs, String sortOrder)
        *    uri参数用来对Provider进行身份标识
        *   projection参数表示Provider所要返回的内容（列），值为null时标识返回所有内容
        *   selection参数设置返回数据的筛选条件，值为null时表示不筛选
        *   selectionArgs参数配合参数selection使用，selection参数中存在“？”操作符，会使用selectionArgs参数的值将其替换
        *   sortOrder参数定义返回数据的排序方式
        * </def>
        *
        * */
        final Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        String[] from = new String[] {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        int[] to = new int[]{R.id.itemTextView};

        /*
        * 使用SimpleCursorAdapte将Cursor绑定到ListView上
        * 注意，本段程序是将查询在主UI线程上执行的，更好的解决方案是使用Cursor Loader
        *
        * <def>
        *     public SimpleCursorAdapter (Context context, int layout, Cursor c, String[] from, int[] to, int flags)
        *     context参数标明ListView所在的Activity
        *     layout参数要绑定的layout的id
        *     c参数是指数据源的Cursor，可以为null
        *     from参数是指需要绑定的数据列表,当c参数值为null时，该参数值可以为null
        *     to参数是需要包含from参数中所描述的数据源的数据列表，所有值都应该为TextView对象，当c参数值为null时，该参数值可以为null
        *     flags参数为用来决定adapter行为的参数
        * </def>
        * */
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitemlayout, c, from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView lv = (ListView) findViewById(R.id.contactListView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {

            /*
            * public abstract void onItemClick (AdapterView<?> parent, View view, int position, long id)
            * parent参数是指发生点击事件的AdapterView
            * view参数是AdapterView内被点击的控件
            * position参数view在adapter中的位置
            * id参数被点击控件的row-id
            * */

            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                //将cursor移动至选中项
                c.moveToPosition(pos);

                //获取行id
                int rowID = c.getInt(c.getColumnIndexOrThrow("_id"));

                //构建Result URI
                /*
                 * withAppendedId (Uri contentUri, long id)
                 *把id和contentUri连接成一个新的Uri
                 * ContactsContract.Contacts.CONTENT_URI为联系人的uri
                 * rowID为具体id，定位uri
                 *  */
                Uri outURI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowID);
                Intent outData = new Intent();
                outData.setData(outURI);

                /*
                *以Intent的形式向调用的Activity返回数据，在finish()之前使用
                * setResult (int resultCode, Intent data)
                * resultCode参数为结果码，表示子Activity运行的结果，一般为Activity.RESULT_OK或Activity.RESULT_CANCELED
                * data参数表示要返回的数据结果
                * */

                setResult(Activity.RESULT_OK, outData);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_picker, menu);
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
