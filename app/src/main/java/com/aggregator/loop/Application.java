package com.aggregator.loop;

import com.aggregator.Configuration.FontsOverride;

/**
 * Created by Pranav Mittal on 8/20/2015.
 */


public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT","fonts/DroidSans.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE","fonts/DroidSans.ttf");
        FontsOverride.setDefaultFont(this, "SERIF","fonts/DroidSans.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF","fonts/DroidSans.ttf");
    }

}
