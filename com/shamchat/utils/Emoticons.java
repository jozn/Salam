package com.shamchat.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Emoticons {
    public static final Map<Pattern, Integer> ANDROID_EMOTICONS;
    public static final Map<Pattern, Integer> NONE_EMOTICONS;
    private static final Factory spannableFactory;

    static {
        ANDROID_EMOTICONS = new HashMap();
        NONE_EMOTICONS = new HashMap();
        spannableFactory = Factory.getInstance();
        addPattern(ANDROID_EMOTICONS, "(airplane)", 2130837765);
        addPattern(ANDROID_EMOTICONS, "(angel)", 2130837766);
        addPattern(ANDROID_EMOTICONS, "(angry)", 2130837767);
        addPattern(ANDROID_EMOTICONS, "(baseball)", 2130837768);
        addPattern(ANDROID_EMOTICONS, "(basketball)", 2130837769);
        addPattern(ANDROID_EMOTICONS, "(bicycle)", 2130837771);
        addPattern(ANDROID_EMOTICONS, "(burger)", 2130837772);
        addPattern(ANDROID_EMOTICONS, "(car)", 2130837773);
        addPattern(ANDROID_EMOTICONS, "(cat)", 2130837775);
        addPattern(ANDROID_EMOTICONS, "(chick)", 2130837776);
        addPattern(ANDROID_EMOTICONS, "(christmas_tree)", 2130837777);
        addPattern(ANDROID_EMOTICONS, "(cigarette)", 2130837778);
        addPattern(ANDROID_EMOTICONS, "(clap)", 2130837779);
        addPattern(ANDROID_EMOTICONS, "(cloud)", 2130837780);
        addPattern(ANDROID_EMOTICONS, "(coffee)", 2130837781);
        addPattern(ANDROID_EMOTICONS, "(confused)", 2130837782);
        addPattern(ANDROID_EMOTICONS, "(console)", 2130837783);
        addPattern(ANDROID_EMOTICONS, "(cool)", 2130837784);
        addPattern(ANDROID_EMOTICONS, "(crazy)", 2130837785);
        addPattern(ANDROID_EMOTICONS, "(cry)", 2130837786);
        addPattern(ANDROID_EMOTICONS, "(cupcake)", 2130837787);
        addPattern(ANDROID_EMOTICONS, "(depressed)", 2130837788);
        addPattern(ANDROID_EMOTICONS, "(devil)", 2130837789);
        addPattern(ANDROID_EMOTICONS, "(dog)", 2130837790);
        addPattern(ANDROID_EMOTICONS, "(dollar)", 2130837791);
        addPattern(ANDROID_EMOTICONS, "(exclamation)", 2130837792);
        addPattern(ANDROID_EMOTICONS, "(facepalm)", 2130837793);
        addPattern(ANDROID_EMOTICONS, "(fire)", 2130837794);
        addPattern(ANDROID_EMOTICONS, "(flirt)", 2130837795);
        addPattern(ANDROID_EMOTICONS, "(flower)", 2130837796);
        addPattern(ANDROID_EMOTICONS, "(football)", 2130837797);
        addPattern(ANDROID_EMOTICONS, "(heart)", 2130837799);
        addPattern(ANDROID_EMOTICONS, "(heart_break)", 2130837800);
        addPattern(ANDROID_EMOTICONS, "(ice_cream)", 2130837801);
        addPattern(ANDROID_EMOTICONS, "(inlove)", 2130837802);
        addPattern(ANDROID_EMOTICONS, "(kangaroo)", 2130837803);
        addPattern(ANDROID_EMOTICONS, "(kiss)", 2130837804);
        addPattern(ANDROID_EMOTICONS, "(koala)", 2130837805);
        addPattern(ANDROID_EMOTICONS, "(ladybug)", 2130837806);
        addPattern(ANDROID_EMOTICONS, "(laugh)", 2130837807);
        addPattern(ANDROID_EMOTICONS, "(light_bulb)", 2130837808);
        addPattern(ANDROID_EMOTICONS, "(like)", 2130837809);
        addPattern(ANDROID_EMOTICONS, "(mad)", 2130837810);
        addPattern(ANDROID_EMOTICONS, "(moa)", 2130837811);
        addPattern(ANDROID_EMOTICONS, "(money)", 2130837812);
        addPattern(ANDROID_EMOTICONS, "(monkey)", 2130837813);
        addPattern(ANDROID_EMOTICONS, "(moon)", 2130837814);
        addPattern(ANDROID_EMOTICONS, "(music)", 2130837815);
        addPattern(ANDROID_EMOTICONS, "(nerd)", 2130837816);
        addPattern(ANDROID_EMOTICONS, "(not_sure)", 2130837817);
        addPattern(ANDROID_EMOTICONS, "(panda)", 2130837818);
        addPattern(ANDROID_EMOTICONS, "(phone)", 2130837819);
        addPattern(ANDROID_EMOTICONS, "(pizza)", 2130837821);
        addPattern(ANDROID_EMOTICONS, "(poo)", 2130837822);
        addPattern(ANDROID_EMOTICONS, "(popcorn)", 2130837823);
        addPattern(ANDROID_EMOTICONS, "(purple_heart)", 2130837824);
        addPattern(ANDROID_EMOTICONS, "(Q)", 2130837825);
        addPattern(ANDROID_EMOTICONS, "(rain)", 2130837826);
        addPattern(ANDROID_EMOTICONS, "(run)", 2130837828);
        addPattern(ANDROID_EMOTICONS, "(relax)", 2130837827);
        addPattern(ANDROID_EMOTICONS, "(sad)", 2130837829);
        addPattern(ANDROID_EMOTICONS, "(scream)", 2130837830);
        addPattern(ANDROID_EMOTICONS, "(sheep)", 2130837831);
        addPattern(ANDROID_EMOTICONS, "(shy)", 2130837832);
        addPattern(ANDROID_EMOTICONS, "(sick)", 2130837833);
        addPattern(ANDROID_EMOTICONS, "(skull)", 2130837834);
        addPattern(ANDROID_EMOTICONS, "(sleeping)", 2130837835);
        addPattern(ANDROID_EMOTICONS, "(smiley)", 2130837836);
        addPattern(ANDROID_EMOTICONS, "(soccer)", 2130837837);
        addPattern(ANDROID_EMOTICONS, "(sun)", 2130837839);
        addPattern(ANDROID_EMOTICONS, "(soda)", 2130837838);
        addPattern(ANDROID_EMOTICONS, "(surprised)", 2130837840);
        addPattern(ANDROID_EMOTICONS, "(tape)", 2130837841);
        addPattern(ANDROID_EMOTICONS, "(teeth)", 2130837842);
        addPattern(ANDROID_EMOTICONS, "(time)", 2130837843);
        addPattern(ANDROID_EMOTICONS, "(tongue)", 2130837844);
        addPattern(ANDROID_EMOTICONS, "(TV)", 2130837845);
        addPattern(ANDROID_EMOTICONS, "(unlike)", 2130837846);
        addPattern(ANDROID_EMOTICONS, "(V)", 2130837847);
        addPattern(ANDROID_EMOTICONS, "(wink)", 2130837849);
        addPattern(ANDROID_EMOTICONS, "(yummi)", 2130837850);
        addPattern(ANDROID_EMOTICONS, "(zzz)", 2130837851);
    }

    private static void addPattern(Map<Pattern, Integer> map, String smile, int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), Integer.valueOf(resource));
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        for (Entry entry : ANDROID_EMOTICONS.entrySet()) {
            Matcher matcher = ((Pattern) entry.getKey()).matcher(spannable);
            while (matcher.find()) {
                for (Object obj : (ImageSpan[]) spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                    if (spannable.getSpanStart(obj) < matcher.start() || spannable.getSpanEnd(obj) > matcher.end()) {
                        Object obj2 = null;
                        break;
                    }
                    spannable.removeSpan(obj);
                }
                int i = 1;
                if (obj2 != null) {
                    spannable.setSpan(new ImageSpan(context, ((Integer) entry.getValue()).intValue()), matcher.start(), matcher.end(), 33);
                }
            }
        }
        return spannable;
    }
}
