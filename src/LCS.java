
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Creates a MyString object that behaves similar to a Java String, however
 * the MyString's substring behaves like older version of the substring method
 * by using pointers rather than creating a new String object.
 * 
 * @author David Romero PID: 3624439 COP 3530U01
 */
class MyString implements Comparable<MyString>
{

    private String source;      // The original string
    private int startIndex;     // Starting place in the string
    private int length;         // Length of the string

    /**
     * Creates a MyString object from a string with a specified start index
     *
     * @param str String that is to become the MyString
     * @param startIdx Starting index in the string (str)
     */
    public MyString(String str, int startIdx)
    {
        startIndex = startIdx;
        source = str;
        length = str.length() - startIdx;
    }

    /**
     * Creating a substring using a pointer rather than creating a new string
     *
     * @param start desired starting index
     * @return substring of the MyString object
     */
    public String substring(int start)
    {
        return source.substring(start + startIndex);
    }

    /**
     * Creates a substring using a pointer rather than creating a new string.
     * Accepts a starting and an ending index.
     *
     * @param start desired starting index
     * @param end desired ending index
     * @return substring of the MyString object
     */
    public String substring(int start, int end)
    {
        return source.substring(start + startIndex, end + startIndex);
    }

    @Override
    /**
     * Compares two MyString objects and determines whether they are equal or if
     * one comes before the other
     *
     * @param other the MyString that is compared
     * @return 0 if they are equal, -1 if it is smaller, 1 if it is bigger
     */
    public int compareTo(MyString other)
    {
        int rightIndex = startIndex;        // Index of the right MyString
        int leftIndex = other.startIndex;   // Index of the left MyString

        int rightEnd = startIndex + length;            // End of the right side
        int leftEnd = other.startIndex + other.length; // End of the left side

        // If we haven't reached the end of the character yet and if characters 
        // are matching then we'll move on to the next and compare the following
        // ones
        while (leftEnd > leftIndex && rightEnd > rightIndex
                && source.charAt(rightIndex)
                == source.charAt(leftIndex))
        {
            ++rightIndex;
            ++leftIndex;
        }
        //The index made it all the way the end
        if (rightIndex == rightEnd)
        {
            //If the left index made it to the end as well then they both had to
            //be equal.
            if (leftIndex == leftEnd)
            {
                return 0;
            }
            // The left index didn't make it all the way, therefore, it is 
            // smaller than the right.
            else
            {
                return -1;
            }
        }
        // The right index didn't make it all the way to the end, however the 
        // left index did, meaning it is bigger than the right.
        if (leftIndex == leftEnd)
        {
            return 1;
        }
        // Taking the ASCII values of the characters and taking the difference
        // in order to determine order. This is if the whole string was not
        // travesed
        return source.charAt(rightIndex) - other.source.charAt(leftIndex);
    }

    /**
     * Returns character desired in the MyString object
     *
     * @param idx position of the character
     * @return character at the position (idx)
     */
    public char charAt(int idx)
    {
        return source.charAt(startIndex + idx);
    }

    /**
     * Returns the length of the MyString object
     *
     * @return length
     */
    public int length()
    {
        return length;
    }

    /**
     * Returns the startIndex variable
     *
     * @return startIndex
     */
    public int getStartIndex()
    {
        return startIndex;
    }
}
/**
 * The LCS class compares two text files and finds the Longest Common Substring 
 * (LCS). It accomplishes this by creating an array of suffixes and then 
 * sorting it by prefix. In the array, the prefixes are compared against the 
 * previous one, with the one with the most common substring being kept track 
 * of.
 * 
 * @author David Romero PID: 3624439 COP 3530U01
 */
public class LCS
{

    static String str = "";        // The string who's LCS will be determined
    static int symbIndex = 0;      // Location of dividor sepreating both files

    /**
     * Finds the longest common prefix between two MyStrings
     * @param left the left hand MyString
     * @param right the right hand MyString
     * @param pivot the divider used to distinguish the two text files
     * @return the number of characters in the prefix
     */
    public static int longestPrefix(MyString left, MyString right, int pivot)
    {
        int LPLen = 0;             // The length of the prefix
        
        // If the start indexes for both the arrays are not at the pivot
        if (left.getStartIndex() < pivot && right.getStartIndex() > pivot
                || left.getStartIndex() > pivot && right.getStartIndex() < pivot)
        {
            // If the characters are matching at the current index, move on to 
            // the next and update the length
            while (LPLen < left.length() && LPLen < right.length()
                    && left.charAt(LPLen)
                    == right.charAt(LPLen))
            {
                ++LPLen;
            }
        }
        return LPLen;
    }
    /**
     * Reads a text file and places the contents of the file into a 
     * StringBuilder.
     * @param input the text file
     * @return StringBuilder containing the contents of the file
     * @throws FileNotFoundException 
     */
    public static StringBuilder readFile(File input) throws FileNotFoundException
    {
        Scanner scan = new Scanner(input);
        StringBuilder sb = new StringBuilder();
        while (scan.hasNext())
        {
            sb.append(scan.next());
            if (scan.hasNext())
            {
                sb.append(" ");     // Placing spaces between tokens
            }
        }
        return sb;
    }
    /**
     * Finds the LCS by creating an array containing suffixes of the MyString 
     * and an array keeping track of the Longest Common Prefix (LCP) in 
     * an array. It then prints out the number of characters in common 
     * (achieved by the LCP array) and the prefix in common
     * @throws FileNotFoundException 
     */
    public static void findLCS() throws FileNotFoundException
    {
        MyString[] suffixes = new MyString[str.length()];
        int[] LCP = new int[str.length()];

        // Populating suffixes with suffixes of the MyString objects. 
        for (int i = 0; i < str.length(); ++i)
        {
            MyString stringy = new MyString(str, i);
            suffixes[i] = stringy;
        }
        Arrays.sort(suffixes);
        // Finding the LPLen of the MyStrings and placing the values in the
        // LCP array
        
        for (int i = 1; i < str.length(); ++i)
        {
            LCP[i] = longestPrefix(suffixes[i], suffixes[i - 1], symbIndex);
        }
        
        int maxLCPIndex = 0;       // The position of the LCP
        
        for (int i = 1; i < LCP.length; ++i)
        {
            if (LCP[i] > LCP[maxLCPIndex])
            {
                maxLCPIndex = i;
            }
        }
        
        // If the longest common substring is more than 30 characters, add '...'
        // after the 30th character
        if (suffixes[maxLCPIndex].substring(0, LCP[maxLCPIndex]).length() > 30)
        {
            System.out.println("The longest common substring is "
                    + LCP[maxLCPIndex] + " characters '"
                    + suffixes[maxLCPIndex].substring(0, 31) + "...'");
        }
        else
        {
            System.out.println("The longest common substring is "
                    + LCP[maxLCPIndex] + " characters '"
                    + suffixes[maxLCPIndex].substring(0, LCP[maxLCPIndex]) + "'");
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        if (args.length == 2)
        {
            StringBuilder sb = readFile(new File(args[0]));
            sb.append("☺");
            symbIndex = sb.length() - 1;
            sb.append(readFile(new File(args[1])));
            str = new String(sb);
            long start = System.currentTimeMillis();
            findLCS();
            long finish = System.currentTimeMillis();
            System.out.println("Time elapsed finding LCS: " + 
                              (finish - start) + "ms");
        }
        else
        {
            System.exit(0);
        }
    }
}
