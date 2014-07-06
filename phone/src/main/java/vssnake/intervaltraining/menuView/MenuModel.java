package vssnake.intervaltraining.menuView;

import java.util.jar.Attributes;

import vssnake.intervaltraining.R;

/**
 * Created by unai on 24/06/2014.
 */
public class MenuModel {

    private Long menuID;

    private String mNameMenu;

    private int menuPhoto;

    public MenuModel(Long menuID,String nameMenu,int menuPhoto){
        this.menuID = menuID;
        this.mNameMenu = nameMenu;
        this.menuPhoto = menuPhoto;
    }

    public Long getMenuID() {
        return menuID;
    }

    public String getNameMenu() {
        return mNameMenu;
    }

    public int getMenuPhoto() {
        return menuPhoto;
    }
}
