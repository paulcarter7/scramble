package com.pac.scramble;

/**
 * Created with IntelliJ IDEA.
 * User: paucarter
 * Date: 1/11/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ScrambleDictionary {
    public boolean hasPrefix(String prefix);

    public boolean hasWord(String word);
}
