package com.pac.scramble;

/**
 * Created with IntelliJ IDEA.
 * User: paulc
 * Date: 4/17/13
 * Time: 10:09 AM
 */
public interface PrefixDictionary {

    /**
     * Initialize here
     */
    public void init() throws Exception;

    /**
     * Does this prefix exist in the word list
     *
     *
     * @returns true if any of the words in the list starts with the prefix string
     */
    public boolean hasPrefix(String prefix);

    /**
     *
     *
     * @return
     */
    public boolean hasWord(String word);
}
