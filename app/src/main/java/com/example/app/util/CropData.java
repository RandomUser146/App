package com.example.app.util;

public class CropData {
    public static enum Crop{rice,chickpea,kidneybeans,pigeonpeas,mothbeans,mungbean,
        blackgram,lentil,maize,watermelon,muskmelon,
        cotton,jute
    };

    public static final String[] croplist = {"Rice", "Chickpea", "Kidneybeans", "Pigeonpeas","Mothbeans", "Mungbean", "Blackgram",
            "Lentil", "Maize","Watermelon", "Muskmelon", "Cotton", "Jute"};

    public static double[] ph = {6.4254709,7.336956624,5.749410586,5.79417488,6.831174083,6.72395694,7.133951629,
            6.927931572,6.245189722,6.495778302,6.358805452,6.912675496,6.732777568};

    public static double[] wa = {236.1811136,80.05897726,105.9197775,149.4575638,51.19848705,48.4036009,67.88415118,
            45.6804542,84.76698766,50.78621894,24.68995207,80.39804312,174.7927975};
}


