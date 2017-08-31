package mb.wordslide.src.Vocabulary;

import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import mb.wordslide.R;
import mb.wordslide.src.L;

/**
 * Created by mbolg on 11.08.2017.
 */

public class Vocabulary {
    private Resources resources;

    public Vocabulary(Resources resources) {
        this.resources = resources;
    }

    public boolean exist(String word) {
        Character firstChar = word.charAt(0);

        boolean exist = parseXmlVocAndCheck(word);

        return exist;
    }

    public boolean parseXmlVocAndCheck(String word) {
        XmlPullParser parser = null;
        word = word.toUpperCase();
        char ch = word.charAt(0);

        switch (ch) {
            case 'А':
            case 'а':
                parser = resources.getXml(R.xml.voc_a);
                break;
            case 'Б':
            case 'б':
                parser = resources.getXml(R.xml.voc_b);
                break;
            case 'В':
            case 'в':
                parser = resources.getXml(R.xml.voc_v);
                break;
            case 'Г':
            case 'г':
                parser = resources.getXml(R.xml.voc_g);
                break;
            case 'Д':
            case 'д':
                parser = resources.getXml(R.xml.voc_d);
                break;
            case 'Е':
            case 'е':
            case 'Ё':
            case 'ё':
                parser = resources.getXml(R.xml.voc_e);
                break;
            case 'Ж':
            case 'ж':
                parser = resources.getXml(R.xml.voc_zh);
                break;
            case 'З':
            case 'з':
                parser = resources.getXml(R.xml.voc_z);
                break;
            case 'И':
            case 'и':
                parser = resources.getXml(R.xml.voc_i);
                break;
            case 'Й':
            case 'й':
                parser = resources.getXml(R.xml.voc_j);
                break;
            case 'К':
            case 'к':
                parser = resources.getXml(R.xml.voc_k);
                break;
            case 'Л':
            case 'л':
                parser = resources.getXml(R.xml.voc_l);
                break;
            case 'М':
            case 'м':
                parser = resources.getXml(R.xml.voc_m);
                break;
            case 'Н':
            case 'н':
                parser = resources.getXml(R.xml.voc_n);
                break;
            case 'О':
            case 'о':
                parser = resources.getXml(R.xml.voc_o);
                break;
            case 'П':
            case 'п':
                parser = resources.getXml(R.xml.voc_p);
                break;
            case 'Р':
            case 'р':
                parser = resources.getXml(R.xml.voc_r);
                break;
            case 'С':
            case 'с':
                parser = resources.getXml(R.xml.voc_s);
                break;
            case 'Т':
            case 'т':
                parser = resources.getXml(R.xml.voc_t);
                break;
            case 'У':
            case 'у':
                parser = resources.getXml(R.xml.voc_u);
                break;
            case 'Ф':
            case 'ф':
                parser = resources.getXml(R.xml.voc_f);
                break;
            case 'Х':
            case 'х':
                parser = resources.getXml(R.xml.voc_kh);
                break;
            case 'Ц':
            case 'ц':
                parser = resources.getXml(R.xml.voc_ts);
                break;
            case 'Ч':
            case 'ч':
                parser = resources.getXml(R.xml.voc_ch);
                break;
            case 'Ш':
            case 'ш':
                parser = resources.getXml(R.xml.voc_sh);
                break;
            case 'Щ':
            case 'щ':
                parser = resources.getXml(R.xml.voc_shch);
                break;
            case 'Ь':
            case 'ь':
                parser = resources.getXml(R.xml.voc_ss);
                break;
            case 'Ы':
            case 'ы':
                parser = resources.getXml(R.xml.voc_y);
                break;
            case 'Ъ':
            case 'ъ':
                parser = resources.getXml(R.xml.voc_hs);
                break;
            case 'Э':
            case 'э':
                parser = resources.getXml(R.xml.voc_re);
                break;
            case 'Ю':
            case 'ю':
                parser = resources.getXml(R.xml.voc_yu);
                break;
            case 'Я':
            case 'я':
                parser = resources.getXml(R.xml.voc_ya);
                break;
        }
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("word")) {
                    if (parser.getAttributeValue(0).equals(word))
                        return true;
                }
                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
