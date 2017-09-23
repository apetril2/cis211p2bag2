
public final class Artist<T> implements ListInterface<T>
{

    private final T[] artist;
    private int numberOfEntries;
    private int artistID;
    private String artistName;
    private boolean initialized = false;

    public Artist(T[] artist)
    {
        this.artist = artist;
    }



    public Artist(T artistID, T artistName)
    {
        // The cast is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        T[] tempArtist = (T[]) new Object[2]; // Unchecked cast
        tempArtist[0] = artistID;
        tempArtist[1] = artistName;
        artist = tempArtist;
        numberOfEntries = 0;
        initialized = true;
    }

    public Artist(T artistID, T artistName, T extraField)
    {
        // The cast is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        T[] tempArtist = (T[]) new Object[3]; // Unchecked cast
        tempArtist[0] = artistID;
        tempArtist[1] = artistName;
        tempArtist[2] = extraField;
        artist = tempArtist;
        numberOfEntries = 0;
        initialized = true;
    }

    public int getID()
    {
        return this.artistID;
    }

    public int getID(T anEntry)
    {
        String[] piece;
        String artistString = anEntry.toString();
        piece = artistString.split("\t"); //piece is a "piece" of the line, made into an array of each split
        return Integer.valueOf(piece[0]);
    }


    public String getName()
    {
        return this.artistName;
    }

    public String getName(T anEntry)
    {
        String[] piece;
        String artistString = anEntry.toString();
        piece = artistString.split("\t"); //piece is a "piece" of the line, made into an array of each split
        return piece[1];
    }

    public String getThirdField(T anEntry)
    {
        String[] piece;
        String artistString = anEntry.toString();
        piece = artistString.split("\t"); //piece is a "piece" of the line, made into an array of each split
        return piece[2];
    }

    public String toString()
    {
        checkInitialization();
        try //checks if Artist has three fields (for "delete" or "next") or just two
        {
            String str = (this.artist[0] + "\t" + this.artist[1] + "\t"  + this.artist[2]);
            return str;
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            String str = (this.artist[0] + "\t" + this.artist[1]);
            return str;
        }
    }
    private void checkInitialization()
    {
        if (!initialized)
            throw new SecurityException("ArrayArtist object is not initialized properly.");
    }
}
