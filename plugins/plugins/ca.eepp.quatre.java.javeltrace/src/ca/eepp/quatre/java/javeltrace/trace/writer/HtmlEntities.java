package ca.eepp.quatre.java.javeltrace.trace.writer;

import java.util.*;
import java.util.regex.*;

/**
 * This class offers methods to decode and encode html entities.
 * @author Michael Yagudaev
 * @version 1.2 April 9, 2011
 */
public class HtmlEntities 
{
    private static Map<String, Character> map = new LinkedHashMap<String, Character>();

    static 
    {
        map.put("&quot;", (char) 34); //$NON-NLS-1$
        map.put("&amp;", (char) 38); //$NON-NLS-1$
        map.put("&lt;", (char) 60); //$NON-NLS-1$
        map.put("&gt;", (char) 62); //$NON-NLS-1$
        map.put("&nbsp;", (char) 160); //$NON-NLS-1$
        map.put("&iexcl;", (char) 161); //$NON-NLS-1$
        map.put("&cent;", (char) 162); //$NON-NLS-1$
        map.put("&pound;", (char) 163); //$NON-NLS-1$
        map.put("&curren;", (char) 164); //$NON-NLS-1$
        map.put("&yen;", (char) 165); //$NON-NLS-1$
        map.put("&brvbar;", (char) 166); //$NON-NLS-1$
        map.put("&sect;", (char) 167); //$NON-NLS-1$
        map.put("&uml;", (char) 168); //$NON-NLS-1$
        map.put("&copy;", (char) 169); //$NON-NLS-1$
        map.put("&ordf;", (char) 170); //$NON-NLS-1$
        map.put("&laquo;", (char) 171); //$NON-NLS-1$
        map.put("&not;", (char) 172); //$NON-NLS-1$
        map.put("&shy;", (char) 173); //$NON-NLS-1$
        map.put("&reg;", (char) 174); //$NON-NLS-1$
        map.put("&macr;", (char) 175); //$NON-NLS-1$
        map.put("&deg;", (char) 176); //$NON-NLS-1$
        map.put("&plusmn;", (char) 177); //$NON-NLS-1$
        map.put("&sup2;", (char) 178); //$NON-NLS-1$
        map.put("&sup3;", (char) 179); //$NON-NLS-1$
        map.put("&acute;", (char) 180); //$NON-NLS-1$
        map.put("&micro;", (char) 181); //$NON-NLS-1$
        map.put("&para;", (char) 182); //$NON-NLS-1$
        map.put("&middot;", (char) 183); //$NON-NLS-1$
        map.put("&cedil;", (char) 184); //$NON-NLS-1$
        map.put("&sup1;", (char) 185); //$NON-NLS-1$
        map.put("&ordm;", (char) 186); //$NON-NLS-1$
        map.put("&raquo;", (char) 187); //$NON-NLS-1$
        map.put("&frac14;", (char) 188); //$NON-NLS-1$
        map.put("&frac12;", (char) 189); //$NON-NLS-1$
        map.put("&frac34;", (char) 190); //$NON-NLS-1$
        map.put("&iquest;", (char) 191); //$NON-NLS-1$
        map.put("&times;", (char) 215); //$NON-NLS-1$
        map.put("&divide;", (char) 247); //$NON-NLS-1$
        map.put("&Agrave;", (char) 192); //$NON-NLS-1$
        map.put("&Aacute;", (char) 193); //$NON-NLS-1$
        map.put("&Acirc;", (char) 194); //$NON-NLS-1$
        map.put("&Atilde;", (char) 195); //$NON-NLS-1$
        map.put("&Auml;", (char) 196); //$NON-NLS-1$
        map.put("&Aring;", (char) 197); //$NON-NLS-1$
        map.put("&AElig;", (char) 198); //$NON-NLS-1$
        map.put("&Ccedil;", (char) 199); //$NON-NLS-1$
        map.put("&Egrave;", (char) 200); //$NON-NLS-1$
        map.put("&Eacute;", (char) 201); //$NON-NLS-1$
        map.put("&Ecirc;", (char) 202); //$NON-NLS-1$
        map.put("&Euml;", (char) 203); //$NON-NLS-1$
        map.put("&Igrave;", (char) 204); //$NON-NLS-1$
        map.put("&Iacute;", (char) 205); //$NON-NLS-1$
        map.put("&Icirc;", (char) 206); //$NON-NLS-1$
        map.put("&Iuml;", (char) 207); //$NON-NLS-1$
        map.put("&ETH;", (char) 208); //$NON-NLS-1$
        map.put("&Ntilde;", (char) 209); //$NON-NLS-1$
        map.put("&Ograve;", (char) 210); //$NON-NLS-1$
        map.put("&Oacute;", (char) 211); //$NON-NLS-1$
        map.put("&Ocirc;", (char) 212); //$NON-NLS-1$
        map.put("&Otilde;", (char) 213); //$NON-NLS-1$
        map.put("&Ouml;", (char) 214); //$NON-NLS-1$
        map.put("&Oslash;", (char) 216); //$NON-NLS-1$
        map.put("&Ugrave;", (char) 217); //$NON-NLS-1$
        map.put("&Uacute;", (char) 218); //$NON-NLS-1$
        map.put("&Ucirc;", (char) 219); //$NON-NLS-1$
        map.put("&Uuml;", (char) 220); //$NON-NLS-1$
        map.put("&Yacute;", (char) 221); //$NON-NLS-1$
        map.put("&THORN;", (char) 222); //$NON-NLS-1$
        map.put("&szlig;", (char) 223); //$NON-NLS-1$
        map.put("&agrave;", (char) 224); //$NON-NLS-1$
        map.put("&aacute;", (char) 225); //$NON-NLS-1$
        map.put("&acirc;", (char) 226); //$NON-NLS-1$
        map.put("&atilde;", (char) 227); //$NON-NLS-1$
        map.put("&auml;", (char) 228); //$NON-NLS-1$
        map.put("&aring;", (char) 229); //$NON-NLS-1$
        map.put("&aelig;", (char) 230); //$NON-NLS-1$
        map.put("&ccedil;", (char) 231); //$NON-NLS-1$
        map.put("&egrave;", (char) 232); //$NON-NLS-1$
        map.put("&eacute;", (char) 233); //$NON-NLS-1$
        map.put("&ecirc;", (char) 234); //$NON-NLS-1$
        map.put("&euml;", (char) 235); //$NON-NLS-1$
        map.put("&igrave;", (char) 236); //$NON-NLS-1$
        map.put("&iacute;", (char) 237); //$NON-NLS-1$
        map.put("&icirc;", (char) 238); //$NON-NLS-1$
        map.put("&iuml;", (char) 239); //$NON-NLS-1$
        map.put("&eth;", (char) 240); //$NON-NLS-1$
        map.put("&ntilde;", (char) 241); //$NON-NLS-1$
        map.put("&ograve;", (char) 242); //$NON-NLS-1$
        map.put("&oacute;", (char) 243); //$NON-NLS-1$
        map.put("&ocirc;", (char) 244); //$NON-NLS-1$
        map.put("&otilde;", (char) 245); //$NON-NLS-1$
        map.put("&ouml;", (char) 246); //$NON-NLS-1$
        map.put("&oslash;", (char) 248); //$NON-NLS-1$
        map.put("&ugrave;", (char) 249); //$NON-NLS-1$
        map.put("&uacute;", (char) 250); //$NON-NLS-1$
        map.put("&ucirc;", (char) 251); //$NON-NLS-1$
        map.put("&uuml;", (char) 252); //$NON-NLS-1$
        map.put("&yacute;", (char) 253); //$NON-NLS-1$
        map.put("&thorn;", (char) 254); //$NON-NLS-1$
        map.put("&yuml;", (char) 255); //$NON-NLS-1$
    }

    /**
     * Find the Html Entity and convert it back to a regular character if the
     * entity exists, otherwise return the same string.
     * @param str
     * @return Character represented by HTML Entity or the same string if unknown entity.
     */
    private static String fromHtmlEntity(String str) 
    {
        Character ch = map.get(str);
        return ( ch != null ) ? ch.toString() : str;
    }
    
    /**
     * Finds the value and returns the key that corresponds to that value. If value not found
     * returns null.
     * @param value The value to be found.
     * @return The key corresponding to the value that was found or null if value not found.
     */
    private static String findValue(char value)
    {
        Set<String> keySet = map.keySet();
        Iterator<String> i = keySet.iterator();
        String key = i.next(); // key
        boolean found = false;
        String result = null;
        
        while(i.hasNext() && !found)
        {
            if(map.get(key).charValue() == value)
            {
                found = true;
                result = key;
            }
            
            key = i.next();
        }
        
        return result;
    }
    
    /**
     * Converts special characters in ASCII into html entities (e.g. & -> &amp;)
     * @param encode The string to be encoded.
     * @return The encoded string with HTML entities.
     */
    public static String encode(String encode)
    {
        StringBuilder str = new StringBuilder(encode);
        String key;
        int i = 0;
        
        // loop over all the characters in the string
        while(i < str.length())
        {
            // try matching a character to an entity
            key = findValue(str.charAt(i));
            if(key != null)
            {
                str.replace(i, i + 1, key);
                i += key.length();
            }
            else
            {
                i++;
            }
        }
        
        return str.toString();
    }
    
    /**
     * Converts html entities (e.g. &amp;) into real characters (ASCII characters, e.g. &amp; -> &)
     * @param decode A string to be decoded.
     * @return The string decoded with no HTML entities.
     */
    public static String decode(String decode)
    {
        StringBuilder str = new StringBuilder(decode);
        Matcher m = Pattern.compile("&[A-Za-z]+;").matcher(str); //$NON-NLS-1$
        String replaceStr = null;
        
        int matchPointer = 0;
        while (m.find(matchPointer))
        {
            // check if we have a corresponding key in our map
            replaceStr = fromHtmlEntity(m.group());
            str.replace(m.start(), m.end(), replaceStr);
            matchPointer = m.start() + replaceStr.length();
        }
        
        return str.toString();
    }
}