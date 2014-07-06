package vssnake.intervaltraining.menuView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vssnake.intervaltraining.R;

/**
 * Created by unai on 24/06/2014.
 */
public class MenuAdapter extends BaseAdapter{


    static class ViewHolder{
        ImageView photo;
        TextView name;
    }
    private List<MenuModel> menuList;
    private final Context fContext;
    private final LayoutInflater fli;

    public MenuAdapter(Context context,List<MenuModel> menuList){
        this.fContext = context;
        this.menuList = menuList;
        fli = LayoutInflater.from(fContext);

    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuList.get(position).getMenuID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){

            convertView = fli.inflate(R.layout.view_menu,null);




        }
        TextView name = (TextView) convertView.findViewById(R.id.menu_name_textView);
        ImageView photo = (ImageView) convertView.findViewById(R.id.menu_photo_ImageView);

        MenuModel menuModel = menuList.get(position);

        name.setText(menuModel.getNameMenu());

        photo.setImageDrawable(fContext.getResources().getDrawable(menuModel.getMenuPhoto()));

        return convertView;
    }
}
