package me.deiters.palindrome;

public class Palindrome {

    public static boolean check(String _s) {
        return check(_s, 0, _s.length()-1);
    }

    private static boolean check(String _s, int _start, int _end) {

        // base case
        if (_start >= _end) return true;

        // do some work
        while(!Character.isLetterOrDigit(_s.charAt(_start)))
            (_start)++;
        while(!Character.isLetterOrDigit(_s.charAt(_end)))
            _end--;
        char downcaseStart = Character.toLowerCase(_s.charAt((_start)));
        char downcaseEnd = Character.toLowerCase(_s.charAt(_end));
        if (downcaseStart != downcaseEnd) return false;

        // call simpler version
        return check(_s, (_start)+1, _end-1);

    }
}