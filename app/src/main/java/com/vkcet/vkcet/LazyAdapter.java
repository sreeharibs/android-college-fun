package com.vkcet.vkcet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] fn,fe,fd;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Context c, String[] n, String[] e, String[] d) {
        super();
//        activity = a;
        fn=n;fe=e;fd=d;
//        inflater = (LayoutInflater)activity.getSystemSer vice(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(c);

        imageLoader = new ImageLoader(c);
   /*     imageLoader.setMemoryCacheEnabled(true);
        //imageLoader.setTimeToCleanUnusedFiles(60000);
        imageLoader.setTimeToCheckChanges(60);
        imageLoader.setOriginalImageDensity(0);//don't care about densities*/
    }

    public int getCount() {
        return fn.length;

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.colleagues_list_item, parent,false);

        TextView name=(TextView)vi.findViewById(R.id.tvName);;
        TextView email=(TextView)vi.findViewById(R.id.tvEmail);;
        ImageView image=(ImageView)vi.findViewById(R.id.proPic);

        name.setText(fn[position]);
        name.setTag(fn[position]);
        email.setText(fe[position]);
        email.setTag(fe[position ]);
        imageLoader.DisplayImage(fd[position], image);
        image.setTag(fd[position]);
        return vi;
    }
}