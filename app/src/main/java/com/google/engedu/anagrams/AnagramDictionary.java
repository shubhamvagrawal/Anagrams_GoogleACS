/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<Integer,ArrayList<String>> sizeToWords;
    HashMap<String,ArrayList<String>> lettersToWord;
    private Integer wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        wordList= new ArrayList<String>();
        wordSet = new HashSet<String>();
        lettersToWord = new HashMap<String,ArrayList<String>>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null)
        {
            String word = line.trim();
            wordSet.add(word);
            if(!sizeToWords.containsKey(word.length())){
                sizeToWords.put(word.length(),new ArrayList<String>());
                sizeToWords.get(word.length()).add(word);
            }
            else {
                sizeToWords.get(word.length()).add(word);
            }
            String key = sortLetters(word);
            if(!lettersToWord.containsKey(key)) {
                lettersToWord.put(key, new ArrayList<String>());
                lettersToWord.get(key).add(word);
            }
        }
        wordList.addAll(wordSet);
        wordLength = DEFAULT_WORD_LENGTH;
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && (word.indexOf(base) < 0)){
            //TODO: CHECK WHETHER THE GIVEN WORD CONATINS BASE WORD LETTERS
                return true;
        }
        return false;
    }

    public HashSet<String> getAnagrams(String targetWord) {
        Iterator<String> iterator = null;
        if(lettersToWord.containsKey(targetWord.length()))
            iterator = lettersToWord.get(targetWord.length()).iterator();
        else
            iterator = wordSet.iterator();
        String word = new String();
        String targetKey = sortLetters(targetWord);
        if(!lettersToWord.containsKey(targetKey)){
            lettersToWord.put(targetKey,new ArrayList<String>());
            lettersToWord.get(targetKey).add(targetWord);
        }
        while (iterator.hasNext()){
            word = iterator.next();
            if(word.length()==targetWord.length()){
                String key = sortLetters(word);
                if(lettersToWord.containsKey(key)){
                    if(!(lettersToWord.get(key).contains(word))){
                        lettersToWord.get(key).add(word);
                    }
                }
                else {
                    lettersToWord.put(key,new ArrayList<String>());
                    lettersToWord.get(key).add(word);
                }
            }

        }
        return new HashSet<String>(lettersToWord.get(targetKey));
    }

    public HashSet<String> getAnagramsWithOneMoreLetter(String word) {
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> anagrams = new ArrayList<>();
        for (char i = 'a' ; i <= 'z'; i++){
            String key = sortLetters(i+word);
            if(lettersToWord.containsKey(key)) {
                anagrams.addAll(lettersToWord.get(key));
            }
        }
        Iterator iterator = anagrams.iterator();
        while (iterator.hasNext())
        {
            String anagram = (String)iterator.next();
            result.addAll(getAnagrams(anagram));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int flag=1;
        //static int wordLength=DEFAULT_WORD_LENGTH;
        String s=new String();

        while(flag==1) {
            ArrayList<String> words= sizeToWords.get(wordLength);
            int n = random.nextInt(words.size());
            s = words.get(n);

            if(s!=null) {
                if(wordLength<=MAX_WORD_LENGTH)
                    wordLength++;
                flag = 0;
            }
        }
        return s;
    }

    public String sortLetters(String word){
        char [] letters = word.toCharArray();
        Arrays.sort(letters);
        String alphabeticalOrder = new String(letters);
        return alphabeticalOrder;
    }


}
