package at.alex_s168.imageeditor.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Translator {

    public static String LangFolder;

    public static final Gson GSON = new Gson();

    public static HashMap<String, String> translations = new HashMap<>();

    public static String translate(String key, String... args) {
        String translationPattern = translations.get(key);
        if (translationPattern == null) return key;
        for (int i = 0; i < args.length; i++) {
            translationPattern = translationPattern.replace("%" + i, args[i]);
        }
        return translationPattern;
    }

    public static void setLangFolder(String s) {
        LangFolder = s;
    }

    public static void changeLanguage(String lang) {
        translations.clear();
        String file = LangFolder + "/" + lang + ".json";
        InputStream stream = Translator.class.getResourceAsStream(file);
        if (stream == null) {
            System.err.println("Language file '" + file + "' not found!");
        }
        assert stream != null;
        JsonObject json = GSON.fromJson(new InputStreamReader(stream), JsonObject.class);
        json.entrySet().forEach(entry -> translations.put(entry.getKey(), entry.getValue().getAsString()));
    }
}
