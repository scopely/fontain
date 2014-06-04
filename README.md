#Fontain

Fontain is a lightweight library for displaying text in custom fonts in your Android applications

##Features
Fontain allows you to include a number of custom fonts within your app, and then access them by their Font Family, Weight, Width and Slope. It simplifies the process of selecting the proper typeface for any given usage.

##Usage

###Setup

In order for Fontain to be able to make use of your custom fonts, they must be placed in the Assets folder, with the following directory structure:

<pre>
assets  
  `-->&lt;font folder&gt;  
    `-->&lt;font family name&gt;
      `-->&lt;font&gt;
</pre>

Example:
<pre>
-->assets  
 `--> fonts  
    |--> Helvetica
    | |--> Helvetica.otf
    | |--> Helvetica-UltraBold.otf
    | `--> Helvetica-Italic.otf
    `--> Garamond
      |--> Garamond.otf
      |--> Garamond-UltraBold.otf
      `--> Garamond-Italic.otf
</pre>

The weight, width and slope information of a particular font will be parsed from its name, so any relevant descriptors must remain in the font name. "Helvetica.otf" will be assumed to have Normal weight, Normal width and not italic. "Helvetica-UltraBold-Italic.otf" will be treated as having UltraBold weight, Normal width and italic.


###Initializing
You must initialize Fontain with a Context and the name of whatever font family you want to be the default.

Fontain must be initialized before it is used (including the inflation of any of Fontain's custom Font Views), so that it may create fonts from the assets folder, but it need only be initialized once.

Fontain can be initialized with any one of the overloaded init methods:  
```java
Fontain.init(Context context, String fontsFolder, String defaultFontName)
Fontain.init(Context context, String defaultFontName)
Fontain.init(Context context, String fontsFolder, int defaultFontResId)
Fontain.init(Context context, int defaultFontResId)
```

Your application's onCreate() method is the recommended place to initialize Fontain.

###Usage

Fontain can be used primarily in one of two ways:

####Font Views

Fontain provides a number of Font Views that are extended from Android's basic text views (TextView => FontTextView, Button => FontButton, etc). You can use these views exactly as you would use the regular android version, and on creation they will seek out the default Typeface from Fontain and apply it to themselves. Additionally these views come with custom XML attributes that allow you to specify the Font Family, Weight, Width and/or Caps Mode directly in layout XML (Slope is taken from Android's native TextStyle attribute). Example usage:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">
  <com.scopely.fontain.views.FontTextView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:text="Text"
      android:textStyle="italic"
      app:font_family="Helvetica"
      app:font_weight="ULTRA_BOLD"
      app:font_width="NARROW"
      app:caps_mode="words"/>
</LinearLayout>
```

#####Caps Mode
All of the Font Views include the ability to set a Caps Mode. Doing so will initialize the view with a ```TransformationMethod``` that will display the text of the view with certain letters capitalized. Caps Mode ```characters``` will capitalize all letters, Caps Mode ```words``` will capitalize the first letter of each word, and Caps Mode ```sentences``` will capitalize the first letter of each sentence. It is analagous to, and is implemented with, Android's ```TextUtils.CAP_MODE_XXXXX```, but with more granular control in xml than merely ```android:textAllCaps```.

####Apply to View Hierarchy
Fontain also contains methods for walking a view hierarchy and applying a given typeface to any TextView contained therein. Fontain provides several overloaded methods that achieve the same thing:

```java
Fontain.applyFontToViewHierarchy(View view, int weight, int width, boolean italic)
Fontain.applyFontToViewHierarchy(View view, FontFamily fontFamily, int weight, int width, boolean italic)
Fontain.applyFontToViewHierarchy(View view, Font font)
```
Fontain also has a method that will apply a font family across a view hierarchy. Whereas the above methods will apply a single font to all views in the hierarchy, the below method will select the font that best matches the view's pre-existing weight, width and slope attributes.

```java
Fontain.applyFontFamilyToViewHierarchy(View view, FontFamily family)
```
The use case for these methods is generally when the layout in question is provided by the system (eg: AlertDialog) or a third party library (eg: https://github.com/JakeWharton/Android-ViewPagerIndicator)

##Spans
Fontain also provides the following spans that allow you to change font within a single TextView:

	FontSpan: applies a specified typeface to the spanned text
    WidthSpan: does a reverse lookup of the text's current typeface and then finds a font in the same font family with the specified width
    WeightSpan: does a reverse lookup of the text's current typeface and then finds a font in the same font family with the specified weight
    SlopeSpan: does a reverse lookup of the text's current typeface and then finds a font in the same font family with the specified slope.
    
These spans all extend ```MetricAffectingSpan``` are applied in the same manner as sibling classes such as ```RelativeSizeSpan``` and ```SubscriptSpan```.


##Definitions
###Weight
The weight of a font is its thickness. Bold is the classic descriptor related to weight, but there are many more, and all correspond to a numerical value on a scale that ranges from 100 to 900. The names and numerical values used by Fontain are listed below:

    HAIRLINE: 100
    THIN: 100
    ULTRA_LIGHT: 100 
    EXTRA_LIGHT: 100
    LIGHT: 200
    BOOK: 300
    NORMAL: 400
    MEDIUM: 500
    DEMI_BOLD: 600
    SEMI_BOLD: 600
    BOLD: 700
    EXTRA_BOLD: 800
    HEAVY: 800
    BLACK: 800
    ULTRA_HEAVY: 900
    EXTRA_BLACK: 900
    ULTRA_BLACK: 900
    ULTRA_BOLD: 900
    FAT: 900
    POSTER: 900

###Width
The width of a font is the the horizontal space taken up by each letter relative to the letter's height. There are commonly accepted descriptors, but no standard numerical value the way there is with weight. Fontain simply maps the descriptors to a 1-5 scale. The names used by Fontain are listed below:

    ULTRA_COMPRESSED: 1
    EXTRA_COMPRESSED: 1
    ULTRA_CONDENSED: 1
    EXTRA_CONDENSED: 1
    ULTRA_NARROW: 1
    EXTRA_NARROW: 1
    COMPRESSED: 2
    CONDENSED: 2
    NARROW: 2
    NORMAL: 3
    WIDE: 4
    EXTENDED: 4
    EXPANDED: 4
    EXTRA_WIDE: 5
    ULTRA_WIDE: 5
    EXTRA_EXTENDED: 5
    ULTRA_EXTENDED: 5
    EXTRA_EXPANDED: 5
    ULTRA_EXPANDED: 5
    
###Slope
The slope of a font is the angle at which the presumably vertical elements of the font rest. In Fontain this is a simple boolean: the font is either Normal or it is Italic.