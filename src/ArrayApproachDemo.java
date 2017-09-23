import java.io.*;

/**
 * Created by Drew on 2/6/2016.
 */
public class ArrayApproachDemo {
    public static void main(String[] args) throws IOException
    {
        int countArtists = getCount("p1artists.txt");
        int countChanges = getCount("p2changes.txt");
        Artist[] artists = new Artist[countArtists];
        artists = populateArtistArray(artists);
        String[][] changeArray;
        changeArray = processChanges(artists, countChanges);

        //testing withoutGap time
        long startTime = System.nanoTime();
        withoutGap(artists, changeArray);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("withoutGap time: " +duration);

        //testing useDeleteField time
        startTime = System.nanoTime();
        useDeleteField(artists, changeArray);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("useDeleteField time: " +duration);

        //testing useNextField time
        startTime = System.nanoTime();
        useNextField(artists, changeArray);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.println("useNextField time: " +duration);
    }

    public static int getCount(String file) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String str;
        int count = 0;
        while ((str = bufferedReader.readLine()) != null)
            count++;
        bufferedReader.close();
        return count;
    }

    public static Artist[] populateArtistArray(Artist[] artists) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("p1artists.txt"));
        String str;
        int fileIndex = 0;
        while((str=bufferedReader.readLine())!=null)
        {
            String[] piece = new String[2];
            for (int i = 0; i < 2; i++)
                piece = str.split("\t"); //piece is a "piece" of the line, made into an array of each split
            artists[fileIndex] = new Artist(piece[0], piece[1]);
            fileIndex++;
        }
        bufferedReader.close();
        return artists;
    }

    public static String[][] processChanges(Artist[] artists, int lineCount) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("p2changes.txt"));
        String str;
        String[][] changeArray = new String[lineCount][2];
        int fileIndex = 0;
        while((str=bufferedReader.readLine())!=null)
        {
            String[] piece = new String[2];
            for (int i = 0; i < 2; i++)
                piece = str.split("\t"); //piece is a "piece" of the line, made into an array of each split
            changeArray[fileIndex][0] = piece[0];
            changeArray[fileIndex][1] = piece[1];
            fileIndex++;
        }
        bufferedReader.close();
        return changeArray;
    }
    /*PART A WITHOUT GAP
     *
     */
    public static void withoutGap(Artist[] artists, String[][] changes) throws IOException
    {
        Artist[] changedArtists = new Artist[artists.length + newEntryCount(changes)];
        System.arraycopy(artists, 0, changedArtists, 0, artists.length);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("p2artists2a.txt"));
        boolean write;
        int idCounter = 1;
        bufferedWriter.write("ArtistID\tArtistName");
        bufferedWriter.newLine();
        for (int i = 0; i < changedArtists.length; i++)
        {
            write = true;
            for(int j = 0; j < changes.length; j++)
            {
                if (changes[j][0].equals("A") && i >= changedArtists.length - 1)
                {
                    bufferedWriter.write(idCounter +"\t" + changes[j][1]+"");
                    bufferedWriter.newLine();
                    idCounter++;
                }
                else if (changes[j][0].equals("D")
                         && i <= artists.length -1
                         && artists[i].getID(artists[i]) == Integer.valueOf(changes[j][1]))
                {
                    write = false;
                    idCounter++;
                }
                else if (changedArtists[i] == null && !changes[j][0].equals("A"))
                {
                    write = false;
                }
            }
            if (write)
            {
                bufferedWriter.write(changedArtists[i]+"");
                bufferedWriter.newLine();
                idCounter++;
            }
        }
        bufferedWriter.close();
    }

    /*PART B DELETE FIELD
     *
     */
    public static void useDeleteField(Artist[] artists, String[][] changes) throws IOException
    {
        Artist[] changedArtists = new Artist[artists.length + newEntryCount(changes)];

        //copies artists into changedArtists, with an extra field for delete
        String deleteStatus;
        for (int i = 0; i < artists.length; i++)
        {
                if (getDeleteStatus(changes, i+1))
                    changedArtists[i] = new Artist(artists[i].getID(artists[i]), artists[i].getName(artists[i]), "T");
                if (!getDeleteStatus(changes, i+1))
                    changedArtists[i] = new Artist(artists[i].getID(artists[i]), artists[i].getName(artists[i]), "F");
        }
        //adds new artist entries
        int nextID = artists.length;
        for (int i = 0; i < changes.length; i++)
        {
            if (changes[i][0].equals("A")) {
                changedArtists[nextID] = new Artist(nextID + 1, changes[i][1], "F");
                nextID++;
            }
        }

        //writes to file!!!
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("p2artists2b.txt"));
        String temp;
        bufferedWriter.write("ArtistID\tArtistName");
        bufferedWriter.newLine();
        for (int i = 0; i < changedArtists.length; i++)
        {
            temp = changedArtists[i].getThirdField(changedArtists[i]);
            if (temp.equals("F"))
            {
                bufferedWriter.write(changedArtists[i].getID(changedArtists[i]) + "\t" +
                                     changedArtists[i].getName(changedArtists[i]));
                bufferedWriter.newLine();
            }

        }
        bufferedWriter.close();

    }

    /*PART C USING NEXT FIELD
     *
     */
    public static void useNextField(Artist[] artists, String[][] changes) throws IOException
    {
        Artist[] changedArtists = new Artist[artists.length + newEntryCount(changes)];

        //copies artists into changedArtists, with an extra field for next
        for (int i = 0; i < artists.length; i++)
        {
            if (getDeleteStatus(changes, i+2) && !getDeleteStatus(changes, i+1))
                changedArtists[i] = new Artist(artists[i].getID(artists[i]), artists[i].getName(artists[i]), artists[i].getID(artists[i]) +2);
            else if (getDeleteStatus(changes, i+1))
                changedArtists[i] = new Artist(artists[i].getID(artists[i]), artists[i].getName(artists[i]), 0);
            else if (!getDeleteStatus(changes, i+1))
                changedArtists[i] = new Artist(artists[i].getID(artists[i]), artists[i].getName(artists[i]), artists[i].getID(artists[i]) +1);
        }
        //adds new artist entries
        int nextID = artists.length;
        for (int i = 0; i < changes.length; i++)
        {
            if (changes[i][0].equals("A")) {
                changedArtists[nextID] = new Artist(nextID + 1, changes[i][1], nextID + 2);
                nextID++;
            }
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("p2artists2c.txt"));
        nextID = 0;
        bufferedWriter.write("ArtistID\tArtistName");
        bufferedWriter.newLine();
        for(int i = 0; i < changedArtists.length ; i++)
        {
            if (nextID < changedArtists.length) //prevents out of bounds exception
            {
                bufferedWriter.write(changedArtists[nextID].getID(changedArtists[nextID]) + "\t");
                bufferedWriter.write(changedArtists[nextID].getName(changedArtists[nextID]));
                bufferedWriter.newLine();
                nextID = Integer.valueOf(changedArtists[nextID].getThirdField(changedArtists[nextID])) - 1;
            }
        }
        bufferedWriter.close();
    }

    //counts how many entries are being added and returns as an integer
    public static int newEntryCount(String[][] changes)
    {
        int count = 0;
        for(int i = 0; i < changes.length; i++)
        {
            if (changes[i][0].equals("A"))
                count++;
        }
        return count;
    }

    public static boolean getDeleteStatus(String[][] changes, int idNumber)
    {
        boolean result = false;
        for (int i = 0; i < changes.length; i++)
            if (changes[i][0].equals("D") && idNumber == Integer.valueOf(changes[i][1]))
                result = true;
        return result;
    }
}
